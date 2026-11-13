package br.clinica.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import br.clinica.dto.AgendamentoResp;
import br.clinica.dto.PagamentoReq;
import br.clinica.model.Pacote;
import br.clinica.model.Pagamento;
import br.clinica.model.StatusAgendamento;
import br.clinica.model.StatusPagamento;
import br.clinica.repository.PagamentoRepository;

@Service
public class PagamentoServiceImpl implements PagamentoService {

	@Autowired
	private PagamentoRepository pagamentoRepository;

	@Autowired
	private AgendamentoService agendamentoService;

	@Autowired
	private ServicoService servicoService;

	@Autowired
	private PacoteService pacoteService;

	@Autowired
	private ClienteService clienteService;

	@Autowired
	private CreditoService creditoService;

	@Override
	@Transactional
	public List<Pagamento> pagar(String login, boolean equipe, PagamentoReq dados) {
		List<Long> ids = dados.agendamentoIds();
		if (ids == null || ids.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Informe ao menos um agendamento.");
		}
		if (ids.contains(null) || new HashSet<>(ids).size() != ids.size()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Lista de agendamentos inválida ou repetida.");
		}

		List<AgendamentoResp> agendamentos = new ArrayList<>();
		for (Long id : ids) {
			agendamentos.add(agendamentoService.buscarPorId(id));
		}

		Long clienteId = agendamentos.get(0).getClienteId();
		for (AgendamentoResp agendamento : agendamentos) {
			if (!clienteId.equals(agendamento.getClienteId())) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
						"Todos os agendamentos devem ser do mesmo cliente.");
			}
		}

		if (!equipe) {
			Long logado = clienteService.buscarPorLogin(login).pegarId();
			if (!logado.equals(clienteId)) {
				throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Você só pode pagar os seus agendamentos.");
			}
		}

		for (AgendamentoResp agendamento : agendamentos) {
			validarPagavel(agendamento);
		}

		List<BigDecimal> valores = calcularValores(agendamentos, dados.pacoteId());
		BigDecimal total = BigDecimal.ZERO;
		for (BigDecimal valor : valores) {
			total = total.add(valor);
		}

		boolean usarCredito = Boolean.TRUE.equals(dados.usarCredito());
		BigDecimal saldo = usarCredito ? creditoService.buscarPorClienteId(clienteId).pegarSaldo() : BigDecimal.ZERO;
		boolean creditoCobreTudo = usarCredito && saldo.compareTo(total) >= 0;

		// O cartão só é exigido (e validado) se o crédito não cobrir tudo.
		String bandeira = null;
		String ultimosDigitos = null;
		if (!creditoCobreTudo) {
			String numero = validarCartao(dados);
			bandeira = descobrirBandeira(numero);
			ultimosDigitos = numero.substring(numero.length() - 4);
		}

		BigDecimal creditoRestante = usarCredito ? creditoService.usar(clienteId, total) : BigDecimal.ZERO;

		List<Pagamento> pagamentos = new ArrayList<>();
		for (int i = 0; i < agendamentos.size(); i++) {
			BigDecimal valor = valores.get(i);
			BigDecimal doCredito = creditoRestante.min(valor);
			creditoRestante = creditoRestante.subtract(doCredito);

			Pagamento pagamento = new Pagamento();
			pagamento.modificarAgendamentoId(agendamentos.get(i).getId());
			pagamento.modificarClienteId(clienteId);
			pagamento.modificarValorTotal(valor);
			pagamento.modificarValorCredito(doCredito);
			pagamento.modificarValorCartao(valor.subtract(doCredito));
			pagamento.modificarBandeira(doCredito.compareTo(valor) == 0 ? null : bandeira);
			pagamento.modificarUltimosDigitos(doCredito.compareTo(valor) == 0 ? null : ultimosDigitos);
			pagamento.modificarStatus(StatusPagamento.PAGO);
			pagamento.modificarDataHora(LocalDateTime.now());
			pagamentos.add(pagamentoRepository.save(pagamento));

			// Pagamento recebido: o agendamento é confirmado.
			agendamentoService.confirmar(agendamentos.get(i).getId());
		}
		return pagamentos;
	}

	@Override
	@Transactional
	public Pagamento registrarFalta(Long agendamentoId) {
		Pagamento pagamento = pagamentoRepository.acharPorAgendamentoId(agendamentoId).orElseThrow(
				() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Este agendamento não tem pagamento."));

		if (pagamento.pegarStatus() == StatusPagamento.FALTA) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "A falta deste agendamento já foi registrada.");
		}

		// Confirma que o agendamento está AGENDADO e libera o horário (status FALTOU).
		agendamentoService.registrarFalta(agendamentoId);

		// O salão fica com metade; a outra metade vira crédito do cliente.
		BigDecimal retido = pagamento.pegarValorTotal().divide(BigDecimal.valueOf(2), 2, RoundingMode.HALF_UP);
		BigDecimal credito = pagamento.pegarValorTotal().subtract(retido);
		if (credito.signum() > 0) {
			creditoService.adicionar(pagamento.pegarClienteId(), credito);
		}

		pagamento.modificarStatus(StatusPagamento.FALTA);
		return pagamentoRepository.save(pagamento);
	}

	@Override
	public List<Pagamento> listarDoCliente(Long clienteId) {
		return pagamentoRepository.acharPorClienteIdPorOrdemPorIdDesc(clienteId);
	}

	@Override
	public List<Pagamento> listarTodos() {
		return pagamentoRepository.acharTodosPorOrdemPorIdDesc();
	}

	private void validarPagavel(AgendamentoResp agendamento) {
		if (agendamento.getStatus() == StatusAgendamento.AGENDADO
				|| pagamentoRepository.acharPorAgendamentoId(agendamento.getId()).isPresent()) {
			throw new ResponseStatusException(HttpStatus.CONFLICT,
					"O agendamento " + agendamento.getId() + " já foi pago.");
		}
		if (agendamento.getStatus() != StatusAgendamento.PENDENTE_PAGAMENTO) {
			throw new ResponseStatusException(HttpStatus.CONFLICT,
					"O agendamento " + agendamento.getId() + " não aceita pagamento (situação: "
							+ agendamento.getStatus() + ").");
		}
	}

	// Valor de cada agendamento: preço do serviço, ou a parcela do pacote.
	private List<BigDecimal> calcularValores(List<AgendamentoResp> agendamentos, Long pacoteId) {
		List<BigDecimal> valores = new ArrayList<>();

		if (pacoteId == null) {
			for (AgendamentoResp agendamento : agendamentos) {
				valores.add(servicoService.buscarPorId(agendamento.getServicoId()).pegarPreco());
			}
			return valores;
		}

		Pacote pacote = pacoteService.buscarPorId(pacoteId);
		int sessoes = pacote.pegarQuantidadeSessoes();

		if (agendamentos.size() != sessoes) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Este pacote tem " + sessoes + " sessões: informe exatamente " + sessoes + " agendamentos.");
		}
		for (AgendamentoResp agendamento : agendamentos) {
			if (!agendamento.getServicoId().equals(pacote.pegarServico().pegarId())) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
						"Os agendamentos devem ser do serviço do pacote.");
			}
		}

		BigDecimal parcela = pacote.pegarPreco().divide(BigDecimal.valueOf(sessoes), 2, RoundingMode.HALF_UP);
		for (int i = 0; i < sessoes - 1; i++) {
			valores.add(parcela);
		}
		// A última parcela ajusta os centavos para a soma bater com o preço do pacote.
		valores.add(pacote.pegarPreco().subtract(parcela.multiply(BigDecimal.valueOf(sessoes - 1L))));
		return valores;
	}

	// Valida os dados do cartão e devolve o número só com dígitos.
	private String validarCartao(PagamentoReq dados) {
		if (vazio(dados.numeroCartao()) || vazio(dados.nomeTitular()) || vazio(dados.validade())
				|| vazio(dados.cvv())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Informe número, titular, validade e CVV do cartão.");
		}

		String numero = dados.numeroCartao().replaceAll("[\\s-]", "");
		if (!numero.matches("\\d{13,19}") || !passaNoLuhn(numero)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Número de cartão inválido.");
		}
		if (!dados.cvv().trim().matches("\\d{3,4}")) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CVV inválido.");
		}
		String validade = dados.validade().trim();
		if (!validade.matches("(0[1-9]|1[0-2])/\\d{2}")) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Validade inválida. Use o formato MM/AA.");
		}

		int mes = Integer.parseInt(validade.substring(0, 2));
		int ano = 2000 + Integer.parseInt(validade.substring(3, 5));
		if (YearMonth.of(ano, mes).isBefore(YearMonth.now())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cartão vencido.");
		}
		return numero;
	}

	private boolean passaNoLuhn(String numero) {
		int soma = 0;
		boolean dobrar = false;
		for (int i = numero.length() - 1; i >= 0; i--) {
			int digito = numero.charAt(i) - '0';
			if (dobrar) {
				digito *= 2;
				if (digito > 9) {
					digito -= 9;
				}
			}
			soma += digito;
			dobrar = !dobrar;
		}
		return soma % 10 == 0;
	}

	private String descobrirBandeira(String numero) {
		if (numero.startsWith("4")) {
			return "VISA";
		}
		if (numero.matches("^(5[1-5]|2[2-7]).*")) {
			return "MASTERCARD";
		}
		if (numero.startsWith("34") || numero.startsWith("37")) {
			return "AMEX";
		}
		return "OUTRA";
	}

	private boolean vazio(String texto) {
		return texto == null || texto.isBlank();
	}
}

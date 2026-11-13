package br.clinica.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import br.clinica.dto.AgendamentoReq;
import br.clinica.dto.AgendamentoResp;
import br.clinica.dto.FilaReq;
import br.clinica.model.Cliente;
import br.clinica.model.FilaEspera;
import br.clinica.model.Profissional;
import br.clinica.model.Servico;
import br.clinica.model.StatusAgendamento;
import br.clinica.model.StatusFila;
import br.clinica.repository.FilaEsperaRepository;

@Service
public class FilaEsperaServiceImpl implements FilaEsperaService {

	@Autowired
	private FilaEsperaRepository filaEsperaRepository;

	@Autowired
	private ClienteService clienteService;

	@Autowired
	private ServicoService servicoService;

	@Autowired
	private ProfissionalService profissionalService;

	@Autowired
	private AgendamentoService agendamentoService;

	@Override
	@Transactional
	public FilaEspera entrar(String login, boolean equipe, FilaReq dados) {
		if (dados.servicoId() == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Informe o serviço.");
		}

		Cliente cliente;
		if (equipe) {
			if (dados.clienteId() == null) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Informe o cliente.");
			}
			cliente = acharCliente(dados.clienteId());
		} else {
			cliente = clienteService.buscarPorLogin(login);
		}

		Servico servico = servicoService.buscarPorId(dados.servicoId());

		Profissional profissional = null;
		if (dados.profissionalId() != null) {
			profissional = profissionalService.buscarPorId(dados.profissionalId());
			if (!profissional.pegarEspecialidade().pegarId().equals(servico.pegarEspecialidade().pegarId())) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
						"O profissional não está habilitado para esse serviço.");
			}
		}

		if (filaEsperaRepository.acharAguardandoPorClienteIdEServicoId(cliente.pegarId(), servico.pegarId())
				.isPresent()) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "O cliente já está na fila deste serviço.");
		}

		FilaEspera item = new FilaEspera();
		item.modificarCliente(cliente);
		item.modificarServico(servico);
		item.modificarProfissional(profissional);
		item.modificarDataHoraEntrada(LocalDateTime.now());
		item.modificarStatus(StatusFila.AGUARDANDO);

		return filaEsperaRepository.save(item);
	}

	@Override
	public List<FilaEspera> listarAguardando() {
		return filaEsperaRepository.acharAguardandoPorOrdemPorIdAsc();
	}

	@Override
	@Transactional
	public FilaEspera chamarProximo(Long agendamentoVagoId) {
		if (agendamentoVagoId == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Informe o agendamento que ficou vago.");
		}

		AgendamentoResp vago = agendamentoService.buscarPorId(agendamentoVagoId);
		if (vago.getStatus() != StatusAgendamento.FALTOU && vago.getStatus() != StatusAgendamento.CANCELADO) {
			throw new ResponseStatusException(HttpStatus.CONFLICT,
					"Este horário ainda está ocupado: registre a falta ou cancele o agendamento antes.");
		}

		Profissional profissional = profissionalService.buscarPorId(vago.getProfissionalId());
		List<FilaEspera> candidatos = filaEsperaRepository.acharAguardandoParaHorario(
				profissional.pegarEspecialidade().pegarId(), profissional.pegarId());
		if (candidatos.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Ninguém na fila de espera para este horário.");
		}
		FilaEspera proximo = candidatos.get(0);

		// Se o horário original já passou, o encaixe começa daqui a 1 minuto.
		LocalDateTime inicio = vago.getHorarioInicio();
		if (!inicio.isAfter(LocalDateTime.now())) {
			inicio = LocalDateTime.now().plusMinutes(1).withSecond(0).withNano(0);
		}

		AgendamentoReq novo = new AgendamentoReq();
		novo.setClienteId(proximo.pegarCliente().pegarId());
		novo.setProfissionalId(profissional.pegarId());
		novo.setServicoId(proximo.pegarServico().pegarId());
		novo.setHorarioInicio(inicio);

		// Nasce PENDENTE_PAGAMENTO: o cliente chamado confirma pagando.
		AgendamentoResp criado = agendamentoService.agendar(novo);

		proximo.modificarStatus(StatusFila.CHAMADO);
		proximo.modificarAgendamentoId(criado.getId());
		return filaEsperaRepository.save(proximo);
	}

	@Override
	@Transactional
	public void sair(String login, boolean equipe, Long id) {
		FilaEspera item = filaEsperaRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Item da fila não encontrado."));

		if (!equipe) {
			Long logado = clienteService.buscarPorLogin(login).pegarId();
			if (!logado.equals(item.pegarCliente().pegarId())) {
				throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Sem permissão.");
			}
		}
		if (item.pegarStatus() != StatusFila.AGUARDANDO) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Este cliente não está mais aguardando na fila.");
		}

		item.modificarStatus(StatusFila.CANCELADO);
		filaEsperaRepository.save(item);
	}

	// O ClienteService só busca por login, então a recepção localiza o cliente pelo id na lista.
	private Cliente acharCliente(Long id) {
		for (Cliente cliente : clienteService.listarTodos()) {
			if (cliente.pegarId().equals(id)) {
				return cliente;
			}
		}
		throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado.");
	}
}

package br.clinica.configuracao;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import br.clinica.dto.CadastroReq;
import br.clinica.dto.FilaReq;
import br.clinica.model.Cliente;
import br.clinica.model.Servico;
import br.clinica.service.ClienteService;
import br.clinica.service.CreditoService;
import br.clinica.service.FilaEsperaService;
import br.clinica.service.ServicoService;

// Dados de teste da Parte 4: um cliente com R$ 50,00 de crédito e um item na fila de espera.
@Component
@DependsOn({ "dadosIniciais", "dadosCatalogo" })
public class DadosPagamento implements CommandLineRunner {

	@Autowired
	private ClienteService clienteService;

	@Autowired
	private CreditoService creditoService;

	@Autowired
	private ServicoService servicoService;

	@Autowired
	private FilaEsperaService filaEsperaService;

	@Override
	public void run(String... args) {
		Cliente cliente = null;
		for (Cliente existente : clienteService.listarTodos()) {
			if ("cliente@teste.com".equals(existente.pegarEmail())) {
				cliente = existente;
			}
		}
		if (cliente != null) {
			return;
		}

		cliente = clienteService.cadastrar(
				new CadastroReq("cliente", "123456", "Cliente Teste", "cliente@teste.com", "21999990000"));
		creditoService.adicionar(cliente.pegarId(), new BigDecimal("50.00"));

		List<Servico> servicos = servicoService.listarTodos();
		if (!servicos.isEmpty()) {
			filaEsperaService.entrar("cliente", false, new FilaReq(null, servicos.get(0).pegarId(), null));
		}
	}
}

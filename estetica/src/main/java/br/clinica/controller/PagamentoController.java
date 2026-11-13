package br.clinica.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.clinica.dto.PagamentoReq;
import br.clinica.dto.PagamentoResp;
import br.clinica.model.Pagamento;
import br.clinica.model.Perfil;
import br.clinica.security.permissao;
import br.clinica.service.ClienteService;
import br.clinica.service.PagamentoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/pagamentos")
@Tag(name = "Pagamentos")
public class PagamentoController {

	@Autowired
	private PagamentoService pagamentoService;

	@Autowired
	private ClienteService clienteService;

	@PostMapping
	@Operation(summary = "Pagar agendamentos com cartão de crédito e confirmá-los (cliente, recepção e admin)")
	public List<PagamentoResp> pagar(HttpServletRequest req, @RequestBody PagamentoReq dados) {
		permissao.exigirPerfil(req, Perfil.CLIENTE, Perfil.RECEPCAO, Perfil.ADMIN);

		List<Pagamento> pagamentos = pagamentoService.pagar(permissao.loginLogado(req), permissao.ehEquipe(req),
				dados);
		return converter(pagamentos);
	}

	@PostMapping("/falta/{agendamentoId}")
	@Operation(summary = "Registrar falta: o salão fica com metade e a outra metade vira crédito (recepção e admin)")
	public PagamentoResp registrarFalta(HttpServletRequest req, @PathVariable Long agendamentoId) {
		permissao.exigirEquipe(req);

		return PagamentoResp.de(pagamentoService.registrarFalta(agendamentoId));
	}

	@GetMapping("/me")
	@Operation(summary = "Ver meus pagamentos")
	public List<PagamentoResp> listarMeus(HttpServletRequest req) {
		permissao.exigirPerfil(req, Perfil.CLIENTE);

		Long clienteId = clienteService.buscarPorLogin(permissao.loginLogado(req)).pegarId();
		return converter(pagamentoService.listarDoCliente(clienteId));
	}

	@GetMapping
	@Operation(summary = "Listar todos os pagamentos (recepção e admin)")
	public List<PagamentoResp> listarTodos(HttpServletRequest req) {
		permissao.exigirEquipe(req);

		return converter(pagamentoService.listarTodos());
	}

	private List<PagamentoResp> converter(List<Pagamento> pagamentos) {
		List<PagamentoResp> lista = new ArrayList<>();
		for (Pagamento pagamento : pagamentos) {
			lista.add(PagamentoResp.de(pagamento));
		}
		return lista;
	}
}

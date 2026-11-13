package br.clinica.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.clinica.dto.CreditoResp;
import br.clinica.model.Perfil;
import br.clinica.security.permissao;
import br.clinica.service.ClienteService;
import br.clinica.service.CreditoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/creditos")
@Tag(name = "Créditos")
public class CreditoController {

	@Autowired
	private CreditoService creditoService;

	@Autowired
	private ClienteService clienteService;

	@GetMapping("/me")
	@Operation(summary = "Ver meu saldo de crédito")
	public CreditoResp verMeuSaldo(HttpServletRequest req) {
		permissao.exigirPerfil(req, Perfil.CLIENTE);

		Long clienteId = clienteService.buscarPorLogin(permissao.loginLogado(req)).pegarId();
		return CreditoResp.de(creditoService.buscarPorClienteId(clienteId));
	}

	@GetMapping("/cliente/{clienteId}")
	@Operation(summary = "Ver o saldo de crédito de um cliente (recepção e admin)")
	public CreditoResp verSaldoDoCliente(HttpServletRequest req, @PathVariable Long clienteId) {
		permissao.exigirEquipe(req);

		return CreditoResp.de(creditoService.buscarPorClienteId(clienteId));
	}
}

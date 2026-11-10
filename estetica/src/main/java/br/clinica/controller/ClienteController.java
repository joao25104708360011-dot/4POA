package br.clinica.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.clinica.dto.ClienteReq;
import br.clinica.dto.ClienteResp;
import br.clinica.dto.MensagemResp;
import br.clinica.model.Cliente;
import br.clinica.security.permissao;
import br.clinica.service.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/clientes")
@Tag(name = "Clientes")
public class ClienteController {

	@Autowired
	private ClienteService clienteService;

	@GetMapping
	@Operation(summary = "Listar todos os clientes (recepção e admin)")
	public List<ClienteResp> listar(HttpServletRequest req) {
		permissao.exigirEquipe(req);

		List<ClienteResp> lista = new ArrayList<>();
		for (Cliente cliente : clienteService.listarTodos()) {
			lista.add(ClienteResp.de(cliente));
		}
		return lista;
	}

	@GetMapping("/me")
	@Operation(summary = "Ver meus dados")
	public ClienteResp verMeusDados(HttpServletRequest req) {
		Cliente cliente = clienteService.buscarPorLogin(permissao.loginLogado(req));
		return ClienteResp.de(cliente);
	}

	@PutMapping("/me")
	@Operation(summary = "Atualizar meus dados")
	public ClienteResp atualizarMeusDados(HttpServletRequest req, @RequestBody ClienteReq dados) {
		Cliente cliente = clienteService.atualizar(permissao.loginLogado(req), dados);
		return ClienteResp.de(cliente);
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Excluir um cliente (recepção e admin)")
	public MensagemResp deletar(HttpServletRequest req, @PathVariable Long id) {
		permissao.exigirEquipe(req);

		clienteService.deletar(id);
		return new MensagemResp("Cliente removido!");
	}
}
package br.clinica.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.clinica.dto.MensagemResp;
import br.clinica.dto.ServicoReq;
import br.clinica.dto.ServicoResp;
import br.clinica.model.Servico;
import br.clinica.security.permissao;
import br.clinica.service.ServicoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/servicos")
@Tag(name = "Serviços")
public class ServicoController {

	@Autowired
	private ServicoService servicoService;

	@GetMapping
	@Operation(summary = "Listar serviços (todos os usuários)")
	public List<ServicoResp> listar() {
		List<ServicoResp> lista = new ArrayList<>();
		for (Servico servico : servicoService.listarTodos()) {
			lista.add(ServicoResp.de(servico));
		}
		return lista;
	}

	@GetMapping("/{id}")
	@Operation(summary = "Ver um serviço pelo id (todos os usuários)")
	public ServicoResp ver(@PathVariable Long id) {
		return ServicoResp.de(servicoService.buscarPorId(id));
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(summary = "Cadastrar um serviço (recepção e admin)")
	public ServicoResp cadastrar(HttpServletRequest req, @RequestBody ServicoReq dados) {
		permissao.exigirEquipe(req);
		return ServicoResp.de(servicoService.cadastrar(dados));
	}

	@PutMapping("/{id}")
	@Operation(summary = "Atualizar um serviço (recepção e admin)")
	public ServicoResp atualizar(HttpServletRequest req, @PathVariable Long id, @RequestBody ServicoReq dados) {
		permissao.exigirEquipe(req);
		return ServicoResp.de(servicoService.atualizar(id, dados));
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Excluir um serviço (recepção e admin)")
	public MensagemResp deletar(HttpServletRequest req, @PathVariable Long id) {
		permissao.exigirEquipe(req);
		servicoService.deletar(id);
		return new MensagemResp("Serviço removido!");
	}
}

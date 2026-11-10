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
import br.clinica.dto.PacoteReq;
import br.clinica.dto.PacoteResp;
import br.clinica.model.Pacote;
import br.clinica.security.permissao;
import br.clinica.service.PacoteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/pacotes")
@Tag(name = "Pacotes")
public class PacoteController {

	@Autowired
	private PacoteService pacoteService;

	@GetMapping
	@Operation(summary = "Listar pacotes (todos os usuários)")
	public List<PacoteResp> listar() {
		List<PacoteResp> lista = new ArrayList<>();
		for (Pacote pacote : pacoteService.listarTodos()) {
			lista.add(PacoteResp.de(pacote));
		}
		return lista;
	}

	@GetMapping("/{id}")
	@Operation(summary = "Ver um pacote pelo id (todos os usuários)")
	public PacoteResp ver(@PathVariable Long id) {
		return PacoteResp.de(pacoteService.buscarPorId(id));
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(summary = "Cadastrar um pacote (recepção e admin)")
	public PacoteResp cadastrar(HttpServletRequest req, @RequestBody PacoteReq dados) {
		permissao.exigirEquipe(req);
		return PacoteResp.de(pacoteService.cadastrar(dados));
	}

	@PutMapping("/{id}")
	@Operation(summary = "Atualizar um pacote (recepção e admin)")
	public PacoteResp atualizar(HttpServletRequest req, @PathVariable Long id, @RequestBody PacoteReq dados) {
		permissao.exigirEquipe(req);
		return PacoteResp.de(pacoteService.atualizar(id, dados));
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Excluir um pacote (recepção e admin)")
	public MensagemResp deletar(HttpServletRequest req, @PathVariable Long id) {
		permissao.exigirEquipe(req);
		pacoteService.deletar(id);
		return new MensagemResp("Pacote removido!");
	}
}

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
import br.clinica.dto.ProfissionalReq;
import br.clinica.dto.ProfissionalResp;
import br.clinica.model.Profissional;
import br.clinica.security.permissao;
import br.clinica.service.ProfissionalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/profissionais")
@Tag(name = "Profissionais")
public class ProfissionalController {

	@Autowired
	private ProfissionalService profissionalService;

	@GetMapping
	@Operation(summary = "Listar profissionais (todos os usuários)")
	public List<ProfissionalResp> listar() {
		List<ProfissionalResp> lista = new ArrayList<>();
		for (Profissional profissional : profissionalService.listarTodos()) {
			lista.add(ProfissionalResp.de(profissional));
		}
		return lista;
	}

	@GetMapping("/{id}")
	@Operation(summary = "Ver um profissional pelo id (todos os usuários)")
	public ProfissionalResp ver(@PathVariable Long id) {
		return ProfissionalResp.de(profissionalService.buscarPorId(id));
	}

	@GetMapping("/especialidade/{especialidadeId}")
	@Operation(summary = "Listar profissionais de uma especialidade (todos os usuários)")
	public List<ProfissionalResp> listarPorEspecialidade(@PathVariable Long especialidadeId) {
		List<ProfissionalResp> lista = new ArrayList<>();
		for (Profissional profissional : profissionalService.listarPorEspecialidade(especialidadeId)) {
			lista.add(ProfissionalResp.de(profissional));
		}
		return lista;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(summary = "Cadastrar um profissional (recepção e admin)")
	public ProfissionalResp cadastrar(HttpServletRequest req, @RequestBody ProfissionalReq dados) {
		permissao.exigirEquipe(req);
		return ProfissionalResp.de(profissionalService.cadastrar(dados));
	}

	@PutMapping("/{id}")
	@Operation(summary = "Atualizar um profissional (recepção e admin)")
	public ProfissionalResp atualizar(HttpServletRequest req, @PathVariable Long id, @RequestBody ProfissionalReq dados) {
		permissao.exigirEquipe(req);
		return ProfissionalResp.de(profissionalService.atualizar(id, dados));
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Excluir um profissional (recepção e admin)")
	public MensagemResp deletar(HttpServletRequest req, @PathVariable Long id) {
		permissao.exigirEquipe(req);
		profissionalService.deletar(id);
		return new MensagemResp("Profissional removido!");
	}
}

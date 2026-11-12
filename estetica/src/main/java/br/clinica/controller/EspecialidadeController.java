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
import br.clinica.dto.EspecialidadeReq;
import br.clinica.dto.EspecialidadeResp;
import br.clinica.model.Especialidade;
import br.clinica.security.permissao;
import br.clinica.service.EspecialidadeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/especialidades")
@Tag(name = "Especialidades")
public class EspecialidadeController {

	@Autowired
	private EspecialidadeService especialidadeService;

	@GetMapping
	@Operation(summary = "Listar especialidades (todos os usuários)")
	public List<EspecialidadeResp> listar() {
		List<EspecialidadeResp> lista = new ArrayList<>();
		for (Especialidade especialidade : especialidadeService.listarTodos()) {
			lista.add(EspecialidadeResp.de(especialidade));
		}
		return lista;
	}

	@GetMapping("/{id}")
	@Operation(summary = "Ver uma especialidade pelo id (todos os usuários)")
	public EspecialidadeResp ver(@PathVariable Long id) {
		return EspecialidadeResp.de(especialidadeService.buscarPorId(id));
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(summary = "Cadastrar uma especialidade (recepção e admin)")
	public EspecialidadeResp cadastrar(HttpServletRequest req, @RequestBody EspecialidadeReq dados) {
		permissao.exigirEquipe(req);
		return EspecialidadeResp.de(especialidadeService.cadastrar(dados));
	}

	@PutMapping("/{id}")
	@Operation(summary = "Atualizar uma especialidade (recepção e admin)")
	public EspecialidadeResp atualizar(HttpServletRequest req, @PathVariable Long id, @RequestBody EspecialidadeReq dados) {
		permissao.exigirEquipe(req);
		return EspecialidadeResp.de(especialidadeService.atualizar(id, dados));
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Excluir uma especialidade (recepção e admin)")
	public MensagemResp deletar(HttpServletRequest req, @PathVariable Long id) {
		permissao.exigirEquipe(req);
		especialidadeService.deletar(id);
		return new MensagemResp("Especialidade removida!");
	}
}

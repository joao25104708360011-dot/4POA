package br.clinica.service;

import java.util.List;

import br.clinica.dto.EspecialidadeReq;
import br.clinica.model.Especialidade;

public interface EspecialidadeService {

	Especialidade cadastrar(EspecialidadeReq dados);

	Especialidade atualizar(Long id, EspecialidadeReq dados);

	void deletar(Long id);

	Especialidade buscarPorId(Long id);

	List<Especialidade> listarTodos();
}

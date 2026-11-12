package br.clinica.service;

import java.util.List;

import br.clinica.dto.ProfissionalReq;
import br.clinica.model.Profissional;

public interface ProfissionalService {

	Profissional cadastrar(ProfissionalReq dados);

	Profissional atualizar(Long id, ProfissionalReq dados);

	void deletar(Long id);

	Profissional buscarPorId(Long id);

	List<Profissional> listarTodos();

	List<Profissional> listarPorEspecialidade(Long especialidadeId);
}

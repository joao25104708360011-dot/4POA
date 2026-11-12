package br.clinica.service;

import java.util.List;

import br.clinica.dto.PacoteReq;
import br.clinica.model.Pacote;

public interface PacoteService {

	Pacote cadastrar(PacoteReq dados);

	Pacote atualizar(Long id, PacoteReq dados);

	void deletar(Long id);

	Pacote buscarPorId(Long id);

	List<Pacote> listarTodos();
}

package br.clinica.service;

import java.util.List;

import br.clinica.dto.ServicoReq;
import br.clinica.model.Servico;

public interface ServicoService {

	Servico cadastrar(ServicoReq dados);

	Servico atualizar(Long id, ServicoReq dados);

	void deletar(Long id);

	Servico buscarPorId(Long id);

	List<Servico> listarTodos();
}

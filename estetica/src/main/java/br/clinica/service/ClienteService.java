package br.clinica.service;

import java.util.List;

import br.clinica.dto.CadastroReq;
import br.clinica.dto.ClienteReq;
import br.clinica.model.Cliente;

public interface ClienteService {

	Cliente cadastrar(CadastroReq dados);

	Cliente atualizar(String login, ClienteReq dados);

	void deletar(Long id);

	Cliente buscarPorLogin(String login);

	List<Cliente> listarTodos();
}
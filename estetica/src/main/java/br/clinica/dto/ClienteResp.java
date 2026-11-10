package br.clinica.dto;

import br.clinica.model.Cliente;

public record ClienteResp(Long id, String nome, String email, String telefone) {

	public static ClienteResp de(Cliente cliente) {
		return new ClienteResp(cliente.pegarId(), cliente.pegarNome(), cliente.pegarEmail(),
				cliente.pegarTelefone());
	}
}
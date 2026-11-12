package br.clinica.dto;

import br.clinica.model.Especialidade;

public record EspecialidadeResp(Long id, String nome, String descricao) {

	public static EspecialidadeResp de(Especialidade especialidade) {
		return new EspecialidadeResp(especialidade.pegarId(), especialidade.pegarNome(),
				especialidade.pegarDescricao());
	}
}

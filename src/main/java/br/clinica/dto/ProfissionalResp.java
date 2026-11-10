package br.clinica.dto;

import br.clinica.model.Profissional;

public record ProfissionalResp(Long id, String nome, String telefone, Long especialidadeId,
		String especialidadeNome) {

	public static ProfissionalResp de(Profissional profissional) {
		return new ProfissionalResp(profissional.pegarId(), profissional.pegarNome(),
				profissional.pegarTelefone(), profissional.pegarEspecialidade().pegarId(),
				profissional.pegarEspecialidade().pegarNome());
	}
}

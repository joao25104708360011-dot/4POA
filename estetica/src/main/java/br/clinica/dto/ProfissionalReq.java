package br.clinica.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record ProfissionalReq(
		@Schema(example = "Ana Souza") String nome,
		@Schema(example = "21988887777") String telefone,
		@Schema(example = "1") Long especialidadeId) {
}

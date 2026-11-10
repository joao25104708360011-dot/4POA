package br.clinica.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record ClienteReq(
		@Schema(example = "Maria Silva") String nome,
		@Schema(example = "maria@email.com") String email,
		@Schema(example = "21999999999") String telefone) {
}
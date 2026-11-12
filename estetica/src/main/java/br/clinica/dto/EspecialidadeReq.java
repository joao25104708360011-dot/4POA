package br.clinica.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record EspecialidadeReq(
		@Schema(example = "Estética Facial") String nome,
		@Schema(example = "Cuidados com a pele do rosto") String descricao) {
}

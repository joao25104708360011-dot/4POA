package br.clinica.dto;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;

public record PacoteReq(
		@Schema(example = "Pacote 5 limpezas") String nome,
		@Schema(example = "1") Long servicoId,
		@Schema(example = "5") Integer quantidadeSessoes,
		@Schema(example = "650.00") BigDecimal preco) {
}

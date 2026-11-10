package br.clinica.dto;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;

public record ServicoReq(
		@Schema(example = "Limpeza de pele") String nome,
		@Schema(example = "Limpeza profunda com extração") String descricao,
		@Schema(example = "150.00") BigDecimal preco,
		@Schema(example = "60") Integer duracaoMinutos,
		@Schema(example = "1") Long especialidadeId) {
}

package br.clinica.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record FilaReq(
		@Schema(description = "Só a recepção/admin informa o cliente; o cliente logado entra por conta própria") Long clienteId,
		@Schema(example = "1") Long servicoId,
		@Schema(description = "Opcional: profissional de preferência") Long profissionalId) {
}

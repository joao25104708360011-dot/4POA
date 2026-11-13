package br.clinica.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

public record PagamentoReq(
		@Schema(description = "Agendamentos a pagar (um ou vários, do mesmo cliente)", example = "[1]") List<Long> agendamentoIds,
		@Schema(description = "Opcional: pagar como pacote (informe todas as sessões do pacote)") Long pacoteId,
		@Schema(example = "4111 1111 1111 1111") String numeroCartao,
		@Schema(example = "MARIA SILVA") String nomeTitular,
		@Schema(example = "12/30") String validade,
		@Schema(example = "123") String cvv,
		@Schema(description = "Usar o saldo de crédito para abater o valor", example = "false") Boolean usarCredito) {
}

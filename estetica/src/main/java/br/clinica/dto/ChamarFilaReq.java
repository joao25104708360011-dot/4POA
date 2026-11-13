package br.clinica.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record ChamarFilaReq(
		@Schema(description = "Agendamento que ficou vago (com falta registrada ou cancelado)", example = "1") Long agendamentoVagoId) {
}

package br.clinica.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record LoginReq(
		@Schema(example = "admin") String login,
		@Schema(example = "123456") String senha) {
}
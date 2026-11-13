package br.clinica.dto;

import java.math.BigDecimal;

import br.clinica.model.Credito;

public record CreditoResp(Long clienteId, BigDecimal saldo) {

	public static CreditoResp de(Credito credito) {
		return new CreditoResp(credito.pegarClienteId(), credito.pegarSaldo());
	}
}

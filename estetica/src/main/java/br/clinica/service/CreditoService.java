package br.clinica.service;

import java.math.BigDecimal;

import br.clinica.model.Credito;

public interface CreditoService {

	// Devolve o crédito do cliente; se ainda não existe, cria com saldo zero.
	Credito buscarPorClienteId(Long clienteId);

	void adicionar(Long clienteId, BigDecimal valor);

	// Gasta até "maximo" do saldo e devolve quanto foi gasto de fato.
	BigDecimal usar(Long clienteId, BigDecimal maximo);
}

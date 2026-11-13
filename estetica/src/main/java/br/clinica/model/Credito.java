package br.clinica.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Credito {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true)
	private Long clienteId;

	@Column(nullable = false, precision = 10, scale = 2)
	private BigDecimal saldo = BigDecimal.ZERO;

	public Long pegarId() {
		return id;
	}

	public Long pegarClienteId() {
		return clienteId;
	}

	public void modificarClienteId(Long clienteId) {
		this.clienteId = clienteId;
	}

	public BigDecimal pegarSaldo() {
		return saldo;
	}

	public void modificarSaldo(BigDecimal saldo) {
		this.saldo = saldo;
	}
}

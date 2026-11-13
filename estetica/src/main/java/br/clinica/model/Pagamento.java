package br.clinica.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Pagamento {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true)
	private Long agendamentoId;

	@Column(nullable = false)
	private Long clienteId;

	@Column(nullable = false, precision = 10, scale = 2)
	private BigDecimal valorTotal;

	@Column(nullable = false, precision = 10, scale = 2)
	private BigDecimal valorCartao;

	@Column(nullable = false, precision = 10, scale = 2)
	private BigDecimal valorCredito;

	// Só guardamos a bandeira e os 4 últimos dígitos. Nunca o número completo.
	private String bandeira;

	private String ultimosDigitos;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private StatusPagamento status;

	@Column(nullable = false)
	private LocalDateTime dataHora;

	public Long pegarId() {
		return id;
	}

	public Long pegarAgendamentoId() {
		return agendamentoId;
	}

	public void modificarAgendamentoId(Long agendamentoId) {
		this.agendamentoId = agendamentoId;
	}

	public Long pegarClienteId() {
		return clienteId;
	}

	public void modificarClienteId(Long clienteId) {
		this.clienteId = clienteId;
	}

	public BigDecimal pegarValorTotal() {
		return valorTotal;
	}

	public void modificarValorTotal(BigDecimal valorTotal) {
		this.valorTotal = valorTotal;
	}

	public BigDecimal pegarValorCartao() {
		return valorCartao;
	}

	public void modificarValorCartao(BigDecimal valorCartao) {
		this.valorCartao = valorCartao;
	}

	public BigDecimal pegarValorCredito() {
		return valorCredito;
	}

	public void modificarValorCredito(BigDecimal valorCredito) {
		this.valorCredito = valorCredito;
	}

	public String pegarBandeira() {
		return bandeira;
	}

	public void modificarBandeira(String bandeira) {
		this.bandeira = bandeira;
	}

	public String pegarUltimosDigitos() {
		return ultimosDigitos;
	}

	public void modificarUltimosDigitos(String ultimosDigitos) {
		this.ultimosDigitos = ultimosDigitos;
	}

	public StatusPagamento pegarStatus() {
		return status;
	}

	public void modificarStatus(StatusPagamento status) {
		this.status = status;
	}

	public LocalDateTime pegarDataHora() {
		return dataHora;
	}

	public void modificarDataHora(LocalDateTime dataHora) {
		this.dataHora = dataHora;
	}
}

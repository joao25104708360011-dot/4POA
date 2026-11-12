package br.clinica.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Pacote {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true)
	private String nome;

	@ManyToOne
	@JoinColumn(nullable = false)
	private Servico servico;

	@Column(nullable = false)
	private Integer quantidadeSessoes;

	@Column(nullable = false)
	private BigDecimal preco;

	public Long pegarId() {
		return id;
	}

	public String pegarNome() {
		return nome;
	}

	public void modificarNome(String nome) {
		this.nome = nome;
	}

	public Servico pegarServico() {
		return servico;
	}

	public void modificarServico(Servico servico) {
		this.servico = servico;
	}

	public Integer pegarQuantidadeSessoes() {
		return quantidadeSessoes;
	}

	public void modificarQuantidadeSessoes(Integer quantidadeSessoes) {
		this.quantidadeSessoes = quantidadeSessoes;
	}

	public BigDecimal pegarPreco() {
		return preco;
	}

	public void modificarPreco(BigDecimal preco) {
		this.preco = preco;
	}
}

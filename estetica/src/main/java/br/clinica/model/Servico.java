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
public class Servico {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true)
	private String nome;

	private String descricao;

	@Column(nullable = false)
	private BigDecimal preco;

	@Column(nullable = false)
	private Integer duracaoMinutos;

	@ManyToOne
	@JoinColumn(nullable = false)
	private Especialidade especialidade;

	public Long pegarId() {
		return id;
	}

	public String pegarNome() {
		return nome;
	}

	public void modificarNome(String nome) {
		this.nome = nome;
	}

	public String pegarDescricao() {
		return descricao;
	}

	public void modificarDescricao(String descricao) {
		this.descricao = descricao;
	}

	public BigDecimal pegarPreco() {
		return preco;
	}

	public void modificarPreco(BigDecimal preco) {
		this.preco = preco;
	}

	public Integer pegarDuracaoMinutos() {
		return duracaoMinutos;
	}

	public void modificarDuracaoMinutos(Integer duracaoMinutos) {
		this.duracaoMinutos = duracaoMinutos;
	}

	public Especialidade pegarEspecialidade() {
		return especialidade;
	}

	public void modificarEspecialidade(Especialidade especialidade) {
		this.especialidade = especialidade;
	}
}

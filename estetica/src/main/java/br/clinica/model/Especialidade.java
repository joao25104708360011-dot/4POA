package br.clinica.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Especialidade {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true)
	private String nome;

	private String descricao;

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
}

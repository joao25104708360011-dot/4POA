package br.clinica.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Profissional {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String nome;

	private String telefone;

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

	public String pegarTelefone() {
		return telefone;
	}

	public void modificarTelefone(String telefone) {
		this.telefone = telefone;
	}

	public Especialidade pegarEspecialidade() {
		return especialidade;
	}

	public void modificarEspecialidade(Especialidade especialidade) {
		this.especialidade = especialidade;
	}

	public Long getId() {
		return null;
	}
}

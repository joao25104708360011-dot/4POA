package br.clinica.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

@Entity
public class Cliente {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String nome;

	private String telefone;

	@Column(nullable = false, unique = true)
	private String email;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(nullable = false, unique = true)
	private Usuario usuario;

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

	public String pegarEmail() {
		return email;
	}

	public void modificarEmail(String email) {
		this.email = email;
	}

	public Usuario pegarUsuario() {
		return usuario;
	}

	public void modificarUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
}
package br.clinica.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Usuario {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true)
	private String login;

	@Column(nullable = false)
	private String senha;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Perfil perfil;

	public Long pegarId() {
		return id;
	}

	public String pegarLogin() {
		return login;
	}

	public void modificarLogin(String login) {
		this.login = login;
	}

	public String pegarSenha() {
		return senha;
	}

	public void modificarSenha(String senha) {
		this.senha = senha;
	}

	public Perfil pegarPerfil() {
		return perfil;
	}

	public void modificarPerfil(Perfil perfil) {
		this.perfil = perfil;
	}
}
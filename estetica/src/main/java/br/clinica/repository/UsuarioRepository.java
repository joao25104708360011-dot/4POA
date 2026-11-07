package br.clinica.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.clinica.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

	Optional<Usuario> findByLogin(String login);

	default Optional<Usuario> acharPorLogin(String login) {
		return findByLogin(login);
	}
}
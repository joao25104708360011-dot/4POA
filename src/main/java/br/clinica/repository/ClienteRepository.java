package br.clinica.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.clinica.model.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

	Optional<Cliente> findByEmail(String email);

	Optional<Cliente> findByUsuarioLogin(String login);

	List<Cliente> findAllByOrderByIdAsc();

	default Optional<Cliente> acharPorEmail(String email) {
		return findByEmail(email);
	}

	default Optional<Cliente> acharPorUsuarioLogin(String login) {
		return findByUsuarioLogin(login);
	}

	default List<Cliente> acharTodosPorOrdemPorIdAsc() {
		return findAllByOrderByIdAsc();
	}
}
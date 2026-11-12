package br.clinica.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.clinica.model.Cliente;
import br.clinica.model.Profissional;

public interface ProfissionalRepository extends JpaRepository<Profissional, Long> {

	List<Profissional> findAllByOrderByIdAsc();

	List<Profissional> findByEspecialidadeIdOrderByIdAsc(Long especialidadeId);

	boolean existsByEspecialidadeId(Long especialidadeId);

	default List<Profissional> acharTodosPorOrdemPorIdAsc() {
		return findAllByOrderByIdAsc();
	}

	default List<Profissional> acharPorEspecialidade(Long especialidadeId) {
		return findByEspecialidadeIdOrderByIdAsc(especialidadeId);
	}

	default boolean existePorEspecialidade(Long especialidadeId) {
		return existsByEspecialidadeId(especialidadeId);
	}

	Optional<Cliente> buscarPorIdComBloqueio(Long profissionalId);
}

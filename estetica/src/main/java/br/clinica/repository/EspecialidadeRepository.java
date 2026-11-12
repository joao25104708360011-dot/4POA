package br.clinica.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.clinica.model.Especialidade;

public interface EspecialidadeRepository extends JpaRepository<Especialidade, Long> {

	Optional<Especialidade> findByNomeIgnoreCase(String nome);

	List<Especialidade> findAllByOrderByIdAsc();

	default Optional<Especialidade> acharPorNome(String nome) {
		return findByNomeIgnoreCase(nome);
	}

	default List<Especialidade> acharTodosPorOrdemPorIdAsc() {
		return findAllByOrderByIdAsc();
	}
}

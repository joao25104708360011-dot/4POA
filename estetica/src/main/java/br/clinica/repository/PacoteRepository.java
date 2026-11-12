package br.clinica.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.clinica.model.Pacote;

public interface PacoteRepository extends JpaRepository<Pacote, Long> {

	Optional<Pacote> findByNomeIgnoreCase(String nome);

	List<Pacote> findAllByOrderByIdAsc();

	boolean existsByServicoId(Long servicoId);

	default Optional<Pacote> acharPorNome(String nome) {
		return findByNomeIgnoreCase(nome);
	}

	default List<Pacote> acharTodosPorOrdemPorIdAsc() {
		return findAllByOrderByIdAsc();
	}

	default boolean existePorServico(Long servicoId) {
		return existsByServicoId(servicoId);
	}
}

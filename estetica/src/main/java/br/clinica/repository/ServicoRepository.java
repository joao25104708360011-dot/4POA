package br.clinica.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.clinica.model.Servico;

public interface ServicoRepository extends JpaRepository<Servico, Long> {

	Optional<Servico> findByNomeIgnoreCase(String nome);

	List<Servico> findAllByOrderByIdAsc();

	boolean existsByEspecialidadeId(Long especialidadeId);

	default Optional<Servico> acharPorNome(String nome) {
		return findByNomeIgnoreCase(nome);
	}

	default List<Servico> acharTodosPorOrdemPorIdAsc() {
		return findAllByOrderByIdAsc();
	}

	default boolean existePorEspecialidade(Long especialidadeId) {
		return existsByEspecialidadeId(especialidadeId);
	}
}

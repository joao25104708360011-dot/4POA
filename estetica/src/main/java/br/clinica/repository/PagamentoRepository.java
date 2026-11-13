package br.clinica.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.clinica.model.Pagamento;

public interface PagamentoRepository extends JpaRepository<Pagamento, Long> {

	Optional<Pagamento> findByAgendamentoId(Long agendamentoId);

	List<Pagamento> findByClienteIdOrderByIdDesc(Long clienteId);

	List<Pagamento> findAllByOrderByIdDesc();

	default Optional<Pagamento> acharPorAgendamentoId(Long agendamentoId) {
		return findByAgendamentoId(agendamentoId);
	}

	default List<Pagamento> acharPorClienteIdPorOrdemPorIdDesc(Long clienteId) {
		return findByClienteIdOrderByIdDesc(clienteId);
	}

	default List<Pagamento> acharTodosPorOrdemPorIdDesc() {
		return findAllByOrderByIdDesc();
	}
}

package br.clinica.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.clinica.model.FilaEspera;
import br.clinica.model.StatusFila;

public interface FilaEsperaRepository extends JpaRepository<FilaEspera, Long> {

	List<FilaEspera> findByStatusOrderByIdAsc(StatusFila status);

	Optional<FilaEspera> findByStatusAndClienteIdAndServicoId(StatusFila status, Long clienteId, Long servicoId);

	// Quem aguarda e aceita este horário: mesma especialidade e (sem preferência ou o mesmo profissional).
	@Query("""
			SELECT f FROM FilaEspera f
			WHERE f.status = br.clinica.model.StatusFila.AGUARDANDO
			AND f.servico.especialidade.id = :especialidadeId
			AND (f.profissional IS NULL OR f.profissional.id = :profissionalId)
			ORDER BY f.id ASC
			""")
	List<FilaEspera> buscarAguardandoParaHorario(@Param("especialidadeId") Long especialidadeId,
			@Param("profissionalId") Long profissionalId);

	default List<FilaEspera> acharAguardandoPorOrdemPorIdAsc() {
		return findByStatusOrderByIdAsc(StatusFila.AGUARDANDO);
	}

	default Optional<FilaEspera> acharAguardandoPorClienteIdEServicoId(Long clienteId, Long servicoId) {
		return findByStatusAndClienteIdAndServicoId(StatusFila.AGUARDANDO, clienteId, servicoId);
	}

	default List<FilaEspera> acharAguardandoParaHorario(Long especialidadeId, Long profissionalId) {
		return buscarAguardandoParaHorario(especialidadeId, profissionalId);
	}
}

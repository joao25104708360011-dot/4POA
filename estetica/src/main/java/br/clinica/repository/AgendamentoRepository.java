
package br.clinica.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.clinica.model.Agendamento;
import br.clinica.model.StatusAgendamento;

public interface AgendamentoRepository
        extends JpaRepository<Agendamento, Long> {

    List<Agendamento> findByProfissional_Id(Long profissionalId);

    List<Agendamento> findByCliente_Id(Long clienteId);

    @Query("""
        SELECT a FROM Agendamento a
        WHERE a.profissional.id = :profissionalId
        AND a.horarioInicio < :fim
        AND a.horarioFim > :inicio
        AND a.status <> :statusCancelado
        AND a.status <> br.clinica.model.StatusAgendamento.FALTOU
    """)
    List<Agendamento> buscarAgendamentosNoPeriodo(
            @Param("profissionalId") Long profissionalId,
            @Param("inicio") LocalDateTime inicio,
            @Param("fim") LocalDateTime fim,
            @Param("statusCancelado") StatusAgendamento statusCancelado
    );

    @Query("""
        SELECT COUNT(a) > 0 FROM Agendamento a
        WHERE a.profissional.id = :profissionalId
        AND a.horarioInicio < :fim
        AND a.horarioFim > :inicio
        AND a.status <> :statusCancelado
        AND a.status <> br.clinica.model.StatusAgendamento.FALTOU
    """)
    boolean existeConflito(
            @Param("profissionalId") Long profissionalId,
            @Param("inicio") LocalDateTime inicio,
            @Param("fim") LocalDateTime fim,
            @Param("statusCancelado") StatusAgendamento statusCancelado
    );
}

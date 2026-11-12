
package br.clinica.repository;

import java.time.DayOfWeek;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.clinica.model.Disponibilidade;

public interface DisponibilidadeRepository
        extends JpaRepository<Disponibilidade, Long> {

    List<Disponibilidade> findByProfissional_Id(Long profissionalId);

    List<Disponibilidade> findByProfissional_IdAndDiaSemanaOrderByHorarioInicioAsc(
            Long profissionalId,
            DayOfWeek diaSemana
    );
}

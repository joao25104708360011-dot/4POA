package br.clinica.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.clinica.model.ExcecaoAgenda;

public interface ExcecaoAgendaRepository
        extends JpaRepository<ExcecaoAgenda, Long> {

    List<ExcecaoAgenda> findByProfissional_Id(Long profissionalId);

    List<ExcecaoAgenda> findByProfissional_IdAndData(
            Long profissionalId,
            LocalDate data
    );

    List<ExcecaoAgenda> findByProfissional_IdAndDataBetween(
            Long profissionalId,
            LocalDate dataInicio,
            LocalDate dataFim
    );
}
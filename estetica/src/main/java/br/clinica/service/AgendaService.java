
package br.clinica.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import br.clinica.dto.DisponibilidadeReq;
import br.clinica.dto.ExcecaoAgendaReq;
import br.clinica.dto.HorarioLivreResp;
import br.clinica.model.Disponibilidade;
import br.clinica.model.ExcecaoAgenda;

public interface AgendaService {

    Disponibilidade cadastrarDisponibilidade(DisponibilidadeReq dados);

    List<Disponibilidade> listarDisponibilidades(Long profissionalId);

    ExcecaoAgenda cadastrarExcecao(ExcecaoAgendaReq dados);

    List<ExcecaoAgenda> listarExcecoes(Long profissionalId, LocalDate data);

    List<HorarioLivreResp> listarHorariosLivres(
            Long profissionalId,
            Long servicoId,
            LocalDate data
    );

    boolean horarioDisponivel(
            Long profissionalId,
            LocalDateTime inicio,
            LocalDateTime fim
    );
}

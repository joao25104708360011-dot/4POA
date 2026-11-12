
package br.clinica.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.clinica.dto.DisponibilidadeReq;
import br.clinica.dto.ExcecaoAgendaReq;
import br.clinica.dto.HorarioLivreResp;
import br.clinica.model.Disponibilidade;
import br.clinica.model.ExcecaoAgenda;
import br.clinica.security.permissao;
import br.clinica.service.AgendaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/agenda")
@Tag(name = "Agenda")
public class AgendaController {

    @Autowired
    private AgendaService agendaService;

    @GetMapping("/disponibilidades")
    @Operation(summary = "Listar disponibilidades de um profissional")
    public List<Disponibilidade> listarDisponibilidades(
            @RequestParam Long profissionalId) {

        return agendaService.listarDisponibilidades(profissionalId);
    }

    @PostMapping("/disponibilidades")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Cadastrar disponibilidade semanal (recepção e admin)")
    public Disponibilidade cadastrarDisponibilidade(
            HttpServletRequest req,
            @RequestBody DisponibilidadeReq dados) {

        permissao.exigirEquipe(req);

        return agendaService.cadastrarDisponibilidade(dados);
    }

    @GetMapping("/excecoes")
    @Operation(summary = "Listar exceções da agenda de um profissional")
    public List<ExcecaoAgenda> listarExcecoes(
            @RequestParam Long profissionalId,
            @RequestParam(required = false) LocalDate data) {

        return agendaService.listarExcecoes(profissionalId, data);
    }

    @PostMapping("/excecoes")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Cadastrar exceção na agenda (recepção e admin)")
    public ExcecaoAgenda cadastrarExcecao(
            HttpServletRequest req,
            @RequestBody ExcecaoAgendaReq dados) {

        permissao.exigirEquipe(req);

        return agendaService.cadastrarExcecao(dados);
    }

    @GetMapping("/horarios-livres")
    @Operation(summary = "Consultar horários livres de um profissional")
    public List<HorarioLivreResp> listarHorariosLivres(
            @RequestParam Long profissionalId,
            @RequestParam Long servicoId,
            @RequestParam LocalDate data) {

        return agendaService.listarHorariosLivres(
                profissionalId,
                servicoId,
                data
        );
    }
}

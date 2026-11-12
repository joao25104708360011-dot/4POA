
package br.clinica.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.clinica.dto.AgendamentoReq;
import br.clinica.dto.AgendamentoResp;
import br.clinica.security.permissao;
import br.clinica.service.AgendamentoService;
import br.clinica.service.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/agendamentos")
@Tag(name = "Agendamentos")
public class AgendamentoController {

    @Autowired
    private AgendamentoService agendamentoService;

    @Autowired
    private ClienteService clienteService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Criar agendamento")
    public AgendamentoResp agendar(
            HttpServletRequest req,
            @RequestBody AgendamentoReq dados) {

        if (!permissao.ehEquipe(req)) {

            if (!"CLIENTE".equals(permissao.perfilLogado(req))) {
                permissao.exigirEquipe(req);
            }

            Long clienteId = clienteService
                    .buscarPorLogin(permissao.loginLogado(req))
                    .pegarId();

            if (!clienteId.equals(dados.getClienteId())) {
                throw new org.springframework.web.server.ResponseStatusException(
                        HttpStatus.FORBIDDEN,
                        "Você só pode realizar agendamentos para sua própria conta."
                );
            }
        }

        return agendamentoService.agendar(dados);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consultar um agendamento")
    public AgendamentoResp buscarPorId(
            HttpServletRequest req,
            @PathVariable Long id) {

        AgendamentoResp agendamento = agendamentoService.buscarPorId(id);

        exigirAcessoAoAgendamento(req, agendamento.getClienteId());

        return agendamento;
    }

    @GetMapping("/profissional/{profissionalId}")
    @Operation(summary = "Listar agendamentos de um profissional (recepção e admin)")
    public List<AgendamentoResp> listarPorProfissional(
            HttpServletRequest req,
            @PathVariable Long profissionalId) {

        permissao.exigirEquipe(req);

        return agendamentoService.listarPorProfissional(profissionalId);
    }

    @GetMapping("/cliente/{clienteId}")
    @Operation(summary = "Listar agendamentos de um cliente")
    public List<AgendamentoResp> listarPorCliente(
            HttpServletRequest req,
            @PathVariable Long clienteId) {

        exigirAcessoAoAgendamento(req, clienteId);

        return agendamentoService.listarPorCliente(clienteId);
    }

    @PostMapping("/{id}/cancelar")
    @Operation(summary = "Cancelar um agendamento")
    public AgendamentoResp cancelar(
            HttpServletRequest req,
            @PathVariable Long id) {

        AgendamentoResp agendamento = agendamentoService.buscarPorId(id);

        exigirAcessoAoAgendamento(req, agendamento.getClienteId());

        return agendamentoService.cancelar(id);
    }

    private void exigirAcessoAoAgendamento(
            HttpServletRequest req,
            Long clienteId) {

        if (permissao.ehEquipe(req)) {
            return;
        }

        if (!"CLIENTE".equals(permissao.perfilLogado(req))) {
            permissao.exigirEquipe(req);
        }

        Long clienteLogado = clienteService
                .buscarPorLogin(permissao.loginLogado(req))
                .pegarId();

        if (!clienteLogado.equals(clienteId)) {
            throw new org.springframework.web.server.ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Você não tem permissão para acessar esse agendamento."
            );
        }
    }
}

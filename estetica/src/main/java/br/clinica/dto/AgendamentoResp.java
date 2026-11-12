
package br.clinica.dto;

import java.time.LocalDateTime;

import br.clinica.model.StatusAgendamento;

public class AgendamentoResp {

    private Long id;
    private Long clienteId;
    private Long profissionalId;
    private Long servicoId;
    private LocalDateTime horarioInicio;
    private LocalDateTime horarioFim;
    private StatusAgendamento status;
    private LocalDateTime criadoEm;
    private LocalDateTime canceladoEm;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public Long getProfissionalId() {
        return profissionalId;
    }

    public void setProfissionalId(Long profissionalId) {
        this.profissionalId = profissionalId;
    }

    public Long getServicoId() {
        return servicoId;
    }

    public void setServicoId(Long servicoId) {
        this.servicoId = servicoId;
    }

    public LocalDateTime getHorarioInicio() {
        return horarioInicio;
    }

    public void setHorarioInicio(LocalDateTime horarioInicio) {
        this.horarioInicio = horarioInicio;
    }

    public LocalDateTime getHorarioFim() {
        return horarioFim;
    }

    public void setHorarioFim(LocalDateTime horarioFim) {
        this.horarioFim = horarioFim;
    }

    public StatusAgendamento getStatus() {
        return status;
    }

    public void setStatus(StatusAgendamento status) {
        this.status = status;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public void setCriadoEm(LocalDateTime criadoEm) {
        this.criadoEm = criadoEm;
    }

    public LocalDateTime getCanceladoEm() {
        return canceladoEm;
    }

    public void setCanceladoEm(LocalDateTime canceladoEm) {
        this.canceladoEm = canceladoEm;
    }
}

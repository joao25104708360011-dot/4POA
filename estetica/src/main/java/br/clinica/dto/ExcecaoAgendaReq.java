
package br.clinica.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public class ExcecaoAgendaReq {

    private Long profissionalId;
    private LocalDate data;
    private LocalTime horarioInicio;
    private LocalTime horarioFim;
    private boolean diaFechado;

    public Long getProfissionalId() {
        return profissionalId;
    }

    public void setProfissionalId(Long profissionalId) {
        this.profissionalId = profissionalId;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public LocalTime getHorarioInicio() {
        return horarioInicio;
    }

    public void setHorarioInicio(LocalTime horarioInicio) {
        this.horarioInicio = horarioInicio;
    }

    public LocalTime getHorarioFim() {
        return horarioFim;
    }

    public void setHorarioFim(LocalTime horarioFim) {
        this.horarioFim = horarioFim;
    }

    public boolean isDiaFechado() {
        return diaFechado;
    }

    public void setDiaFechado(boolean diaFechado) {
        this.diaFechado = diaFechado;
    }
}

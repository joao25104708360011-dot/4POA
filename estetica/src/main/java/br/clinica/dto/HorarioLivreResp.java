
package br.clinica.dto;

import java.time.LocalDateTime;

public class HorarioLivreResp {

    private LocalDateTime horarioInicio;
    private LocalDateTime horarioFim;

    public HorarioLivreResp() {
    }

    public HorarioLivreResp(
            LocalDateTime horarioInicio,
            LocalDateTime horarioFim) {

        this.horarioInicio = horarioInicio;
        this.horarioFim = horarioFim;
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
}


package br.clinica.model;

import java.time.DayOfWeek;
import java.time.LocalTime;

import jakarta.persistence.*;

@Entity
public class Disponibilidade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "profissional_id")
    private Profissional profissional;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DayOfWeek diaSemana;

    @Column(nullable = false)
    private LocalTime horarioInicio;

    @Column(nullable = false)
    private LocalTime horarioFim;

    public Long getId() {
        return id;
    }

    public Profissional getProfissional() {
        return profissional;
    }

    public void setProfissional(Profissional profissional) {
        this.profissional = profissional;
    }

    public DayOfWeek getDiaSemana() {
        return diaSemana;
    }

    public void setDiaSemana(DayOfWeek diaSemana) {
        this.diaSemana = diaSemana;
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
}

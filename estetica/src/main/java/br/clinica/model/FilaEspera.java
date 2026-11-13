package br.clinica.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class FilaEspera {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(nullable = false)
	private Cliente cliente;

	@ManyToOne
	@JoinColumn(nullable = false)
	private Servico servico;

	// Opcional: se vazio, o cliente aceita qualquer profissional do serviço.
	@ManyToOne
	private Profissional profissional;

	@Column(nullable = false)
	private LocalDateTime dataHoraEntrada;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private StatusFila status;

	// Preenchido quando o cliente é chamado e o agendamento é criado.
	private Long agendamentoId;

	public Long pegarId() {
		return id;
	}

	public Cliente pegarCliente() {
		return cliente;
	}

	public void modificarCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Servico pegarServico() {
		return servico;
	}

	public void modificarServico(Servico servico) {
		this.servico = servico;
	}

	public Profissional pegarProfissional() {
		return profissional;
	}

	public void modificarProfissional(Profissional profissional) {
		this.profissional = profissional;
	}

	public LocalDateTime pegarDataHoraEntrada() {
		return dataHoraEntrada;
	}

	public void modificarDataHoraEntrada(LocalDateTime dataHoraEntrada) {
		this.dataHoraEntrada = dataHoraEntrada;
	}

	public StatusFila pegarStatus() {
		return status;
	}

	public void modificarStatus(StatusFila status) {
		this.status = status;
	}

	public Long pegarAgendamentoId() {
		return agendamentoId;
	}

	public void modificarAgendamentoId(Long agendamentoId) {
		this.agendamentoId = agendamentoId;
	}
}

package br.clinica.dto;

import java.time.LocalDateTime;

import br.clinica.model.FilaEspera;
import br.clinica.model.StatusFila;

public record FilaResp(Long id, Long clienteId, String clienteNome, String clienteTelefone, Long servicoId,
		String servicoNome, Long profissionalId, String profissionalNome, LocalDateTime dataHoraEntrada,
		StatusFila status, Long agendamentoId) {

	public static FilaResp de(FilaEspera f) {
		Long profissionalId = f.pegarProfissional() == null ? null : f.pegarProfissional().pegarId();
		String profissionalNome = f.pegarProfissional() == null ? null : f.pegarProfissional().pegarNome();

		return new FilaResp(f.pegarId(), f.pegarCliente().pegarId(), f.pegarCliente().pegarNome(),
				f.pegarCliente().pegarTelefone(), f.pegarServico().pegarId(), f.pegarServico().pegarNome(),
				profissionalId, profissionalNome, f.pegarDataHoraEntrada(), f.pegarStatus(), f.pegarAgendamentoId());
	}
}

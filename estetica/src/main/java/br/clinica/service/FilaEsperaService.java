package br.clinica.service;

import java.util.List;

import br.clinica.dto.FilaReq;
import br.clinica.model.FilaEspera;

public interface FilaEsperaService {

	FilaEspera entrar(String login, boolean equipe, FilaReq dados);

	List<FilaEspera> listarAguardando();

	// Dá o horário vago (falta ou cancelamento) ao primeiro da fila: cria o agendamento dele.
	FilaEspera chamarProximo(Long agendamentoVagoId);

	void sair(String login, boolean equipe, Long id);
}

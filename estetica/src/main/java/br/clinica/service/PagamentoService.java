package br.clinica.service;

import java.util.List;

import br.clinica.dto.PagamentoReq;
import br.clinica.model.Pagamento;

public interface PagamentoService {

	// Paga um ou mais agendamentos. Ao pagar, cada agendamento é confirmado.
	List<Pagamento> pagar(String login, boolean equipe, PagamentoReq dados);

	// Cliente reservou e não veio: o salão fica com metade e a outra metade vira crédito.
	Pagamento registrarFalta(Long agendamentoId);

	List<Pagamento> listarDoCliente(Long clienteId);

	List<Pagamento> listarTodos();
}

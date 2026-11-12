
package br.clinica.service;

import java.util.List;

import br.clinica.dto.AgendamentoReq;
import br.clinica.dto.AgendamentoResp;

public interface AgendamentoService {

    AgendamentoResp agendar(AgendamentoReq dados);

    AgendamentoResp buscarPorId(Long id);

    List<AgendamentoResp> listarPorProfissional(Long profissionalId);

    List<AgendamentoResp> listarPorCliente(Long clienteId);

    AgendamentoResp cancelar(Long id);
}

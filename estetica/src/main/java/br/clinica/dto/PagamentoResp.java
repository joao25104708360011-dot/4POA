package br.clinica.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import br.clinica.model.Pagamento;
import br.clinica.model.StatusPagamento;

public record PagamentoResp(Long id, Long agendamentoId, Long clienteId, BigDecimal valorTotal, BigDecimal valorCartao,
		BigDecimal valorCredito, String bandeira, String ultimosDigitos, StatusPagamento status,
		LocalDateTime dataHora) {

	public static PagamentoResp de(Pagamento p) {
		return new PagamentoResp(p.pegarId(), p.pegarAgendamentoId(), p.pegarClienteId(), p.pegarValorTotal(),
				p.pegarValorCartao(), p.pegarValorCredito(), p.pegarBandeira(), p.pegarUltimosDigitos(),
				p.pegarStatus(), p.pegarDataHora());
	}
}

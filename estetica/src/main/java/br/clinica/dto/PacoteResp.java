package br.clinica.dto;

import java.math.BigDecimal;

import br.clinica.model.Pacote;

public record PacoteResp(Long id, String nome, Long servicoId, String servicoNome, Integer quantidadeSessoes,
		BigDecimal preco) {

	public static PacoteResp de(Pacote pacote) {
		return new PacoteResp(pacote.pegarId(), pacote.pegarNome(), pacote.pegarServico().pegarId(),
				pacote.pegarServico().pegarNome(), pacote.pegarQuantidadeSessoes(), pacote.pegarPreco());
	}
}

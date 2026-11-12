package br.clinica.dto;

import java.math.BigDecimal;

import br.clinica.model.Servico;

public record ServicoResp(Long id, String nome, String descricao, BigDecimal preco, Integer duracaoMinutos,
		Long especialidadeId, String especialidadeNome) {

	public static ServicoResp de(Servico servico) {
		return new ServicoResp(servico.pegarId(), servico.pegarNome(), servico.pegarDescricao(),
				servico.pegarPreco(), servico.pegarDuracaoMinutos(), servico.pegarEspecialidade().pegarId(),
				servico.pegarEspecialidade().pegarNome());
	}
}

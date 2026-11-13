package br.clinica.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import br.clinica.model.Credito;
import br.clinica.repository.CreditoRepository;

@Service
public class CreditoServiceImpl implements CreditoService {

	@Autowired
	private CreditoRepository creditoRepository;

	@Override
	@Transactional
	public Credito buscarPorClienteId(Long clienteId) {
		if (clienteId == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Informe o cliente.");
		}
		return creditoRepository.acharPorClienteId(clienteId).orElseGet(() -> {
			Credito novo = new Credito();
			novo.modificarClienteId(clienteId);
			novo.modificarSaldo(BigDecimal.ZERO);
			return creditoRepository.save(novo);
		});
	}

	@Override
	@Transactional
	public void adicionar(Long clienteId, BigDecimal valor) {
		if (valor == null || valor.signum() <= 0) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O valor do crédito deve ser maior que zero.");
		}
		Credito credito = buscarPorClienteId(clienteId);
		credito.modificarSaldo(credito.pegarSaldo().add(valor));
		creditoRepository.save(credito);
	}

	@Override
	@Transactional
	public BigDecimal usar(Long clienteId, BigDecimal maximo) {
		if (maximo == null || maximo.signum() <= 0) {
			return BigDecimal.ZERO;
		}
		Credito credito = buscarPorClienteId(clienteId);
		BigDecimal usado = credito.pegarSaldo().min(maximo);
		credito.modificarSaldo(credito.pegarSaldo().subtract(usado));
		creditoRepository.save(credito);
		return usado;
	}
}

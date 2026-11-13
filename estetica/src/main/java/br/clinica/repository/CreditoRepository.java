package br.clinica.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.clinica.model.Credito;

public interface CreditoRepository extends JpaRepository<Credito, Long> {

	Optional<Credito> findByClienteId(Long clienteId);

	default Optional<Credito> acharPorClienteId(Long clienteId) {
		return findByClienteId(clienteId);
	}
}

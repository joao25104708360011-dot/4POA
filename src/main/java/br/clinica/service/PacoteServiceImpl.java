package br.clinica.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import br.clinica.dto.PacoteReq;
import br.clinica.model.Pacote;
import br.clinica.repository.PacoteRepository;

@Service
public class PacoteServiceImpl implements PacoteService {

	@Autowired
	private PacoteRepository pacoteRepository;

	@Autowired
	private ServicoService servicoService;

	@Override
	@Transactional
	public Pacote cadastrar(PacoteReq dados) {
		Pacote pacote = new Pacote();
		preencher(pacote, dados, null);
		return pacoteRepository.save(pacote);
	}

	@Override
	@Transactional
	public Pacote atualizar(Long id, PacoteReq dados) {
		Pacote pacote = buscarPorId(id);
		preencher(pacote, dados, id);
		return pacoteRepository.save(pacote);
	}

	@Override
	@Transactional
	public void deletar(Long id) {
		buscarPorId(id); 
		pacoteRepository.deleteById(id);
	}

	@Override
	public Pacote buscarPorId(Long id) {
		return pacoteRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pacote não encontrado."));
	}

	@Override
	public List<Pacote> listarTodos() {
		return pacoteRepository.acharTodosPorOrdemPorIdAsc();
	}

	private void preencher(Pacote pacote, PacoteReq dados, Long idAtual) {
		if (dados.nome() == null || dados.nome().isBlank()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Informe o nome.");
		}
		if (dados.servicoId() == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Informe o serviço.");
		}
		if (dados.quantidadeSessoes() == null || dados.quantidadeSessoes() < 2) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O pacote deve ter pelo menos 2 sessões.");
		}
		if (dados.preco() == null || dados.preco().compareTo(BigDecimal.ZERO) <= 0) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O preço deve ser maior que zero.");
		}

		String nome = dados.nome().trim();
		Optional<Pacote> outro = pacoteRepository.acharPorNome(nome);
		if (outro.isPresent() && !outro.get().pegarId().equals(idAtual)) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe um pacote com este nome.");
		}

		pacote.modificarNome(nome);
		pacote.modificarServico(servicoService.buscarPorId(dados.servicoId()));
		pacote.modificarQuantidadeSessoes(dados.quantidadeSessoes());
		pacote.modificarPreco(dados.preco());
	}
}

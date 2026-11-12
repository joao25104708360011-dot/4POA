package br.clinica.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import br.clinica.dto.ServicoReq;
import br.clinica.model.Servico;
import br.clinica.repository.PacoteRepository;
import br.clinica.repository.ServicoRepository;

@Service
public class ServicoServiceImpl implements ServicoService {

	@Autowired
	private ServicoRepository servicoRepository;

	@Autowired
	private PacoteRepository pacoteRepository;

	@Autowired
	private EspecialidadeService especialidadeService;

	@Override
	@Transactional
	public Servico cadastrar(ServicoReq dados) {
		Servico servico = new Servico();
		preencher(servico, dados, null);
		return servicoRepository.save(servico);
	}

	@Override
	@Transactional
	public Servico atualizar(Long id, ServicoReq dados) {
		Servico servico = buscarPorId(id);
		preencher(servico, dados, id);
		return servicoRepository.save(servico);
	}

	@Override
	@Transactional
	public void deletar(Long id) {
		buscarPorId(id); 
		if (pacoteRepository.existePorServico(id)) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Serviço em uso por algum pacote.");
		}
		servicoRepository.deleteById(id);
	}

	@Override
	public Servico buscarPorId(Long id) {
		return servicoRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Serviço não encontrado."));
	}

	@Override
	public List<Servico> listarTodos() {
		return servicoRepository.acharTodosPorOrdemPorIdAsc();
	}

	private void preencher(Servico servico, ServicoReq dados, Long idAtual) {
		if (dados.nome() == null || dados.nome().isBlank()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Informe o nome.");
		}
		if (dados.preco() == null || dados.preco().compareTo(BigDecimal.ZERO) <= 0) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O preço deve ser maior que zero.");
		}
		if (dados.duracaoMinutos() == null || dados.duracaoMinutos() <= 0) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A duração deve ser maior que zero.");
		}
		if (dados.especialidadeId() == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Informe a especialidade.");
		}

		String nome = dados.nome().trim();
		Optional<Servico> outro = servicoRepository.acharPorNome(nome);
		if (outro.isPresent() && !outro.get().pegarId().equals(idAtual)) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe um serviço com este nome.");
		}

		servico.modificarNome(nome);
		servico.modificarDescricao(dados.descricao());
		servico.modificarPreco(dados.preco());
		servico.modificarDuracaoMinutos(dados.duracaoMinutos());
		servico.modificarEspecialidade(especialidadeService.buscarPorId(dados.especialidadeId()));
	}
}

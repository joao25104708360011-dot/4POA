package br.clinica.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import br.clinica.dto.EspecialidadeReq;
import br.clinica.model.Especialidade;
import br.clinica.repository.EspecialidadeRepository;
import br.clinica.repository.ProfissionalRepository;
import br.clinica.repository.ServicoRepository;

@Service
public class EspecialidadeServiceImpl implements EspecialidadeService {

	@Autowired
	private EspecialidadeRepository especialidadeRepository;

	@Autowired
	private ProfissionalRepository profissionalRepository;

	@Autowired
	private ServicoRepository servicoRepository;

	@Override
	@Transactional
	public Especialidade cadastrar(EspecialidadeReq dados) {
		String nome = validarNome(dados.nome());

		if (especialidadeRepository.acharPorNome(nome).isPresent()) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe uma especialidade com este nome.");
		}

		Especialidade especialidade = new Especialidade();
		especialidade.modificarNome(nome);
		especialidade.modificarDescricao(dados.descricao());
		return especialidadeRepository.save(especialidade);
	}

	@Override
	@Transactional
	public Especialidade atualizar(Long id, EspecialidadeReq dados) {
		Especialidade especialidade = buscarPorId(id);
		String nome = validarNome(dados.nome());

		Optional<Especialidade> outra = especialidadeRepository.acharPorNome(nome);
		if (outra.isPresent() && !outra.get().pegarId().equals(id)) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe uma especialidade com este nome.");
		}

		especialidade.modificarNome(nome);
		especialidade.modificarDescricao(dados.descricao());
		return especialidadeRepository.save(especialidade);
	}

	@Override
	@Transactional
	public void deletar(Long id) {
		buscarPorId(id); 

		if (profissionalRepository.existePorEspecialidade(id) || servicoRepository.existePorEspecialidade(id)) {
			throw new ResponseStatusException(HttpStatus.CONFLICT,
					"Especialidade em uso por profissionais ou serviços.");
		}
		especialidadeRepository.deleteById(id);
	}

	@Override
	public Especialidade buscarPorId(Long id) {
		return especialidadeRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Especialidade não encontrada."));
	}

	@Override
	public List<Especialidade> listarTodos() {
		return especialidadeRepository.acharTodosPorOrdemPorIdAsc();
	}

	private String validarNome(String nome) {
		if (nome == null || nome.isBlank()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Informe o nome.");
		}
		return nome.trim();
	}
}

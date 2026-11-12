package br.clinica.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import br.clinica.dto.ProfissionalReq;
import br.clinica.model.Profissional;
import br.clinica.repository.ProfissionalRepository;

@Service
public class ProfissionalServiceImpl implements ProfissionalService {

	@Autowired
	private ProfissionalRepository profissionalRepository;

	@Autowired
	private EspecialidadeService especialidadeService;

	@Override
	@Transactional
	public Profissional cadastrar(ProfissionalReq dados) {
		Profissional profissional = new Profissional();
		preencher(profissional, dados);
		return profissionalRepository.save(profissional);
	}

	@Override
	@Transactional
	public Profissional atualizar(Long id, ProfissionalReq dados) {
		Profissional profissional = buscarPorId(id);
		preencher(profissional, dados);
		return profissionalRepository.save(profissional);
	}

	@Override
	@Transactional
	public void deletar(Long id) {
		buscarPorId(id); 
		profissionalRepository.deleteById(id);
	}

	@Override
	public Profissional buscarPorId(Long id) {
		return profissionalRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Profissional não encontrado."));
	}

	@Override
	public List<Profissional> listarTodos() {
		return profissionalRepository.acharTodosPorOrdemPorIdAsc();
	}

	@Override
	public List<Profissional> listarPorEspecialidade(Long especialidadeId) {
		especialidadeService.buscarPorId(especialidadeId); 
		return profissionalRepository.acharPorEspecialidade(especialidadeId);
	}

	private void preencher(Profissional profissional, ProfissionalReq dados) {
		if (dados.nome() == null || dados.nome().isBlank()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Informe o nome.");
		}
		if (dados.especialidadeId() == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Informe a especialidade.");
		}
		profissional.modificarNome(dados.nome().trim());
		profissional.modificarTelefone(dados.telefone());
		profissional.modificarEspecialidade(especialidadeService.buscarPorId(dados.especialidadeId()));
	}
}

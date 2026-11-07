package br.clinica.service;

import java.util.List;
import java.util.Optional;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import br.clinica.dto.CadastroReq;
import br.clinica.dto.ClienteReq;
import br.clinica.model.Cliente;
import br.clinica.model.Perfil;
import br.clinica.model.Usuario;
import br.clinica.repository.ClienteRepository;
import br.clinica.repository.UsuarioRepository;

@Service
public class ClienteServiceImpl implements ClienteService {

	@Autowired
	private ClienteRepository clienteRepository;

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Override
	@Transactional
	public Cliente cadastrar(CadastroReq dados) {
		if (vazio(dados.login()) || vazio(dados.senha())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Informe login e senha.");
		}
		if (dados.senha().length() < 6) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A senha deve ter pelo menos 6 caracteres.");
		}

		String login = dados.login().trim();
		String nome = validarNome(dados.nome());
		String email = validarEmail(dados.email());

		if (usuarioRepository.acharPorLogin(login).isPresent()) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Este login já está em uso.");
		}
		if (clienteRepository.acharPorEmail(email).isPresent()) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Este e-mail já está em uso.");
		}

		Usuario usuario = new Usuario();
		usuario.modificarLogin(login);
		usuario.modificarSenha(BCrypt.hashpw(dados.senha(), BCrypt.gensalt()));
		usuario.modificarPerfil(Perfil.CLIENTE);

		Cliente cliente = new Cliente();
		cliente.modificarNome(nome);
		cliente.modificarEmail(email);
		cliente.modificarTelefone(dados.telefone());
		cliente.modificarUsuario(usuario);

		return clienteRepository.save(cliente);
	}

	@Override
	@Transactional
	public Cliente atualizar(String login, ClienteReq dados) {
		Cliente cliente = buscarPorLogin(login);

		String nome = validarNome(dados.nome());
		String email = validarEmail(dados.email());

		Optional<Cliente> outro = clienteRepository.acharPorEmail(email);
		if (outro.isPresent() && !outro.get().pegarId().equals(cliente.pegarId())) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Este e-mail já está em uso.");
		}

		cliente.modificarNome(nome);
		cliente.modificarEmail(email);
		cliente.modificarTelefone(dados.telefone());

		return clienteRepository.save(cliente);
	}

	@Override
	@Transactional
	public void deletar(Long id) {
		if (!clienteRepository.existsById(id)) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado.");
		}
		clienteRepository.deleteById(id);
	}

	@Override
	public Cliente buscarPorLogin(String login) {
		return clienteRepository.acharPorUsuarioLogin(login)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado."));
	}

	@Override
	public List<Cliente> listarTodos() {
		return clienteRepository.acharTodosPorOrdemPorIdAsc();
	}

	private boolean vazio(String texto) {
		return texto == null || texto.isBlank();
	}

	private String validarNome(String nome) {
		if (vazio(nome)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Informe o nome.");
		}
		return nome.trim();
	}

	private String validarEmail(String email) {
		if (vazio(email) || !email.contains("@")) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Informe um e-mail válido.");
		}
		return email.trim().toLowerCase();
	}
}
package br.clinica.service;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import br.clinica.model.Usuario;
import br.clinica.repository.UsuarioRepository;
import br.clinica.security.JwtUtil;

@Service
public class AuthServiceImpl implements AuthService {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Override
	public String autenticar(String login, String senha) {
		if (login == null || login.isBlank() || senha == null || senha.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Informe login e senha.");
		}

		Usuario usuario = usuarioRepository.acharPorLogin(login.trim()).orElse(null);

		if (usuario == null || !BCrypt.checkpw(senha, usuario.pegarSenha())) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Login ou senha inválidos.");
		}

		return JwtUtil.gerarToken(usuario.pegarLogin(), usuario.pegarPerfil().name());
	}
}
package br.clinica.configuracao;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import br.clinica.model.Perfil;
import br.clinica.model.Usuario;
import br.clinica.repository.UsuarioRepository;

@Component
public class DadosIniciais implements CommandLineRunner {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Override
	public void run(String... args) {
		criar("admin", Perfil.ADMIN);
		criar("recepcao", Perfil.RECEPCAO);
		criar("profissional", Perfil.PROFISSIONAL);
	}

	private void criar(String login, Perfil perfil) {
		if (usuarioRepository.acharPorLogin(login).isPresent()) {
			return;
		}

		Usuario usuario = new Usuario();
		usuario.modificarLogin(login);
		usuario.modificarSenha(BCrypt.hashpw("123456", BCrypt.gensalt()));
		usuario.modificarPerfil(perfil);
		usuarioRepository.save(usuario);
	}
}
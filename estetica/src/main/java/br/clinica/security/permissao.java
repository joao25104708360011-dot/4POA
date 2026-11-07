package br.clinica.security;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import br.clinica.model.Perfil;
import jakarta.servlet.http.HttpServletRequest;

public class permissao {

	public static String loginLogado(HttpServletRequest req) {
		return (String) req.getAttribute("usuarioLogado");
	}

	public static String perfilLogado(HttpServletRequest req) {
		return (String) req.getAttribute("perfilUsuario");
	}

	public static boolean ehEquipe(HttpServletRequest req) {
		String perfil = perfilLogado(req);
		return Perfil.RECEPCAO.name().equals(perfil) || Perfil.ADMIN.name().equals(perfil);
	}

	public static void exigirEquipe(HttpServletRequest req) {
		if (!ehEquipe(req)) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Sem permissão.");
		}
	}

	public static void exigirPerfil(HttpServletRequest req, Perfil... aceitos) {
		String perfil = perfilLogado(req);
		for (Perfil aceito : aceitos) {
			if (aceito.name().equals(perfil)) {
				return;
			}
		}
		throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Sem permissão.");
	}
}
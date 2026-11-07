package br.clinica.security;

import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {
		return !request.getRequestURI().startsWith("/api/");
	}

	@Override
	protected void doFilterInternal(HttpServletRequest req, HttpServletResponse resp, FilterChain chain)
			throws ServletException, IOException {

		String cabecalho = req.getHeader("Authorization");

		if (cabecalho != null && cabecalho.startsWith("Bearer ")) {
			String token = cabecalho.substring(7);

			if (JwtUtil.validarToken(token)) {
				req.setAttribute("usuarioLogado", JwtUtil.obterLogin(token));
				req.setAttribute("perfilUsuario", JwtUtil.obterPerfil(token));
				chain.doFilter(req, resp);
				return;
			}
		}

		resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Faça login para continuar.");
	}
}
package br.clinica.security;

import java.security.Key;
import java.util.Date;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

public class JwtUtil {

	private static final Key CHAVE = Keys.secretKeyFor(SignatureAlgorithm.HS256);

	private static final long EXPIRACAO = 3600000;

	public static String gerarToken(String login, String perfil) {
		Date agora = new Date();

		return Jwts.builder()
				.setSubject(login)
				.claim("perfil", perfil)
				.setIssuedAt(agora)
				.setExpiration(new Date(agora.getTime() + EXPIRACAO))
				.signWith(CHAVE)
				.compact();
	}

	private static Claims lerToken(String token) {
		return Jwts.parserBuilder().setSigningKey(CHAVE).build().parseClaimsJws(token).getBody();
	}

	public static boolean validarToken(String token) {
		try {
			lerToken(token);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static String obterLogin(String token) {
		return lerToken(token).getSubject();
	}

	public static String obterPerfil(String token) {
		return lerToken(token).get("perfil", String.class);
	}
}
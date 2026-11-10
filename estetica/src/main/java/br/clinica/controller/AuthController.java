package br.clinica.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.clinica.dto.LoginReq;
import br.clinica.dto.TokenReq;
import br.clinica.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/auth")
@Tag(name = "Autenticação")
@SecurityRequirements
public class AuthController {

	@Autowired
	private AuthService authService;

	@PostMapping("/login")
	@Operation(summary = "Entrar e receber o token JWT")
	public TokenReq login(@RequestBody LoginReq dados) {
		return new TokenReq(authService.autenticar(dados.login(), dados.senha()));
	}
}
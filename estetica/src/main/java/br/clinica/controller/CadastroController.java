package br.clinica.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.clinica.dto.CadastroReq;
import br.clinica.dto.MensagemResp;
import br.clinica.service.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/auth")
@Tag(name = "Autenticação")
@SecurityRequirements
public class CadastroController {

	@Autowired
	private ClienteService clienteService;

	@PostMapping("/cadastro")
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(summary = "Criar conta de cliente")
	public MensagemResp cadastrar(@RequestBody CadastroReq dados) {
		clienteService.cadastrar(dados);
		return new MensagemResp("Cadastro realizado!");
	}
}
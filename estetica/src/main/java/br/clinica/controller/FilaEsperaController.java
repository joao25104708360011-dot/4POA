package br.clinica.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.clinica.dto.ChamarFilaReq;
import br.clinica.dto.FilaReq;
import br.clinica.dto.FilaResp;
import br.clinica.dto.MensagemResp;
import br.clinica.model.FilaEspera;
import br.clinica.model.Perfil;
import br.clinica.security.permissao;
import br.clinica.service.FilaEsperaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/fila-espera")
@Tag(name = "Fila de espera")
public class FilaEsperaController {

	@Autowired
	private FilaEsperaService filaEsperaService;

	@PostMapping
	@Operation(summary = "Entrar na fila de espera de um serviço (cliente; recepção e admin informam o cliente)")
	public FilaResp entrar(HttpServletRequest req, @RequestBody FilaReq dados) {
		permissao.exigirPerfil(req, Perfil.CLIENTE, Perfil.RECEPCAO, Perfil.ADMIN);

		FilaEspera item = filaEsperaService.entrar(permissao.loginLogado(req), permissao.ehEquipe(req), dados);
		return FilaResp.de(item);
	}

	@GetMapping
	@Operation(summary = "Listar quem está aguardando na fila (recepção e admin)")
	public List<FilaResp> listar(HttpServletRequest req) {
		permissao.exigirEquipe(req);

		List<FilaResp> lista = new ArrayList<>();
		for (FilaEspera item : filaEsperaService.listarAguardando()) {
			lista.add(FilaResp.de(item));
		}
		return lista;
	}

	@PostMapping("/chamar-proximo")
	@Operation(summary = "Dar o horário vago ao primeiro da fila: cria o agendamento dele, que fica aguardando pagamento (recepção e admin)")
	public FilaResp chamarProximo(HttpServletRequest req, @RequestBody ChamarFilaReq dados) {
		permissao.exigirEquipe(req);

		return FilaResp.de(filaEsperaService.chamarProximo(dados.agendamentoVagoId()));
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Sair da fila de espera (o próprio cliente, recepção e admin)")
	public MensagemResp sair(HttpServletRequest req, @PathVariable Long id) {
		permissao.exigirPerfil(req, Perfil.CLIENTE, Perfil.RECEPCAO, Perfil.ADMIN);

		filaEsperaService.sair(permissao.loginLogado(req), permissao.ehEquipe(req), id);
		return new MensagemResp("Saída da fila registrada!");
	}
}

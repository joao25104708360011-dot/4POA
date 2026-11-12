package br.clinica.configuracao;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import br.clinica.dto.EspecialidadeReq;
import br.clinica.dto.PacoteReq;
import br.clinica.dto.ProfissionalReq;
import br.clinica.dto.ServicoReq;
import br.clinica.model.Especialidade;
import br.clinica.model.Servico;
import br.clinica.service.EspecialidadeService;
import br.clinica.service.PacoteService;
import br.clinica.service.ProfissionalService;
import br.clinica.service.ServicoService;

@Component
public class DadosCatalogo implements CommandLineRunner {

	@Autowired
	private EspecialidadeService especialidadeService;

	@Autowired
	private ProfissionalService profissionalService;

	@Autowired
	private ServicoService servicoService;

	@Autowired
	private PacoteService pacoteService;

	@Override
	public void run(String... args) {
		if (!especialidadeService.listarTodos().isEmpty()) {
			return; 
		}

		Especialidade facial = especialidadeService
				.cadastrar(new EspecialidadeReq("Estética Facial", "Cuidados com a pele do rosto"));
		Especialidade corporal = especialidadeService
				.cadastrar(new EspecialidadeReq("Massoterapia", "Massagens e drenagem"));

		profissionalService.cadastrar(new ProfissionalReq("Ana Souza", "21988887777", facial.pegarId()));
		profissionalService.cadastrar(new ProfissionalReq("Bruno Lima", "21977776666", corporal.pegarId()));

		Servico limpeza = servicoService.cadastrar(new ServicoReq("Limpeza de pele", "Limpeza profunda",
				new BigDecimal("150.00"), 60, facial.pegarId()));
		Servico drenagem = servicoService.cadastrar(new ServicoReq("Drenagem linfática", "Drenagem corporal",
				new BigDecimal("120.00"), 50, corporal.pegarId()));

		pacoteService.cadastrar(new PacoteReq("Pacote 5 limpezas", limpeza.pegarId(), 5, new BigDecimal("650.00")));
		pacoteService.cadastrar(new PacoteReq("Pacote 10 drenagens", drenagem.pegarId(), 10, new BigDecimal("1000.00")));
	}
}

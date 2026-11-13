
package br.clinica.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import br.clinica.dto.AgendamentoReq;
import br.clinica.dto.AgendamentoResp;
import br.clinica.model.Agendamento;
import br.clinica.model.Cliente;
import br.clinica.model.Profissional;
import br.clinica.model.Servico;
import br.clinica.model.StatusAgendamento;
import br.clinica.repository.AgendamentoRepository;
import br.clinica.repository.ClienteRepository;
import br.clinica.repository.ProfissionalRepository;
import br.clinica.repository.ServicoRepository;

@Service
public class AgendamentoServiceImpl implements AgendamentoService {

    @Autowired
    private AgendamentoRepository agendamentoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ProfissionalRepository profissionalRepository;

    @Autowired
    private ServicoRepository servicoRepository;

    @Autowired
    private AgendaService agendaService;

    @Override
    @Transactional
    public AgendamentoResp agendar(AgendamentoReq dados) {

        if (dados == null
                || dados.getClienteId() == null
                || dados.getProfissionalId() == null
                || dados.getServicoId() == null
                || dados.getHorarioInicio() == null) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Preencha todos os dados do agendamento."
            );
        }

        LocalDateTime inicio = dados.getHorarioInicio();

        if (!inicio.isAfter(LocalDateTime.now())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "O horário do agendamento deve ser futuro."
            );
        }

        Profissional profissional = profissionalRepository
                .findById(dados.getProfissionalId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Profissional não encontrado."
                ));

        Cliente cliente = clienteRepository
                .findById(dados.getClienteId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Cliente não encontrado."
                ));

        Servico servico = servicoRepository
                .findById(dados.getServicoId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Serviço não encontrado."
                ));

        if (!profissional.pegarEspecialidade().pegarId()
                .equals(servico.pegarEspecialidade().pegarId())) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "O profissional não está habilitado para esse serviço."
            );
        }

        LocalDateTime fim = inicio.plusMinutes(
                servico.pegarDuracaoMinutos()
        );

        if (!agendaService.horarioDisponivel(
                profissional.pegarId(),
                inicio,
                fim)) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "O horário solicitado não está disponível."
            );
        }

        Agendamento agendamento = new Agendamento();

        agendamento.setCliente(cliente);
        agendamento.setProfissional(profissional);
        agendamento.setServico(servico);
        agendamento.setHorarioInicio(inicio);
        agendamento.setHorarioFim(fim);
        agendamento.setStatus(StatusAgendamento.PENDENTE_PAGAMENTO);

        Agendamento salvo = agendamentoRepository.save(agendamento);

        return converterParaResp(salvo);
    }

    @Override
    @Transactional(readOnly = true)
    public AgendamentoResp buscarPorId(Long id) {

        Agendamento agendamento = buscarAgendamento(id);

        return converterParaResp(agendamento);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AgendamentoResp> listarPorProfissional(Long profissionalId) {

        if (!profissionalRepository.existsById(profissionalId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Profissional não encontrado."
            );
        }

        return agendamentoRepository
                .findByProfissional_Id(profissionalId)
                .stream()
                .map(this::converterParaResp)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AgendamentoResp> listarPorCliente(Long clienteId) {

        if (!clienteRepository.existsById(clienteId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Cliente não encontrado."
            );
        }

        return agendamentoRepository
                .findByCliente_Id(clienteId)
                .stream()
                .map(this::converterParaResp)
                .toList();
    }

    @Override
    @Transactional
    public AgendamentoResp cancelar(Long id) {

        Agendamento agendamento = buscarAgendamento(id);

        if (agendamento.getStatus() == StatusAgendamento.CANCELADO) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Esse agendamento já foi cancelado."
            );
        }

        if (agendamento.getStatus() == StatusAgendamento.CONCLUIDO) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Não é possível cancelar um atendimento concluído."
            );
        }

        if (agendamento.getStatus() == StatusAgendamento.FALTOU) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Não é possível cancelar um agendamento com falta registrada."
            );
        }

        agendamento.setStatus(StatusAgendamento.CANCELADO);
        agendamento.setCanceladoEm(LocalDateTime.now());

        Agendamento salvo = agendamentoRepository.save(agendamento);

        return converterParaResp(salvo);
    }

    @Override
    @Transactional
    public AgendamentoResp confirmar(Long id) {

        Agendamento agendamento = buscarAgendamento(id);

        if (agendamento.getStatus() != StatusAgendamento.PENDENTE_PAGAMENTO) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Só é possível confirmar um agendamento que aguarda pagamento."
            );
        }

        agendamento.setStatus(StatusAgendamento.AGENDADO);

        return converterParaResp(agendamentoRepository.save(agendamento));
    }

    @Override
    @Transactional
    public AgendamentoResp registrarFalta(Long id) {

        Agendamento agendamento = buscarAgendamento(id);

        if (agendamento.getStatus() != StatusAgendamento.AGENDADO) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Só é possível registrar falta em um agendamento confirmado."
            );
        }

        agendamento.setStatus(StatusAgendamento.FALTOU);

        return converterParaResp(agendamentoRepository.save(agendamento));
    }

    private Agendamento buscarAgendamento(Long id) {

        return agendamentoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Agendamento não encontrado."
                ));
    }

    private AgendamentoResp converterParaResp(Agendamento agendamento) {

        AgendamentoResp resposta = new AgendamentoResp();

        resposta.setId(agendamento.getId());
        resposta.setClienteId(agendamento.getCliente().pegarId());
        resposta.setProfissionalId(agendamento.getProfissional().pegarId());
        resposta.setServicoId(agendamento.getServico().pegarId());
        resposta.setHorarioInicio(agendamento.getHorarioInicio());
        resposta.setHorarioFim(agendamento.getHorarioFim());
        resposta.setStatus(agendamento.getStatus());
        resposta.setCriadoEm(agendamento.getCriadoEm());
        resposta.setCanceladoEm(agendamento.getCanceladoEm());

        return resposta;
    }
}


package br.clinica.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import br.clinica.dto.DisponibilidadeReq;
import br.clinica.dto.ExcecaoAgendaReq;
import br.clinica.dto.HorarioLivreResp;
import br.clinica.model.Disponibilidade;
import br.clinica.model.ExcecaoAgenda;
import br.clinica.model.Profissional;
import br.clinica.model.Servico;
import br.clinica.model.StatusAgendamento;
import br.clinica.repository.AgendamentoRepository;
import br.clinica.repository.DisponibilidadeRepository;
import br.clinica.repository.ExcecaoAgendaRepository;
import br.clinica.repository.ProfissionalRepository;
import br.clinica.repository.ServicoRepository;

@Service
public class AgendaServiceImpl implements AgendaService {

    @Autowired
    private DisponibilidadeRepository disponibilidadeRepository;

    @Autowired
    private ExcecaoAgendaRepository excecaoAgendaRepository;

    @Autowired
    private AgendamentoRepository agendamentoRepository;

    @Autowired
    private ProfissionalRepository profissionalRepository;

    @Autowired
    private ServicoRepository servicoRepository;

    @Override
    @Transactional
    public Disponibilidade cadastrarDisponibilidade(DisponibilidadeReq dados) {

        validarDisponibilidade(dados);

        Profissional profissional = buscarProfissional(dados.getProfissionalId());

        List<Disponibilidade> existentes =
                disponibilidadeRepository
                        .findByProfissional_IdAndDiaSemanaOrderByHorarioInicioAsc(
                                profissional.getId(),
                                dados.getDiaSemana()
                        );

        for (Disponibilidade existente : existentes) {
            if (dados.getHorarioInicio().isBefore(existente.getHorarioFim())
                    && dados.getHorarioFim().isAfter(existente.getHorarioInicio())) {

                throw new ResponseStatusException(
                        HttpStatus.CONFLICT,
                        "Esse horário já está cadastrado na disponibilidade do profissional."
                );
            }
        }

        Disponibilidade disponibilidade = new Disponibilidade();

        disponibilidade.setProfissional(profissional);
        disponibilidade.setDiaSemana(dados.getDiaSemana());
        disponibilidade.setHorarioInicio(dados.getHorarioInicio());
        disponibilidade.setHorarioFim(dados.getHorarioFim());

        return disponibilidadeRepository.save(disponibilidade);
    }

    @Override
    public List<Disponibilidade> listarDisponibilidades(Long profissionalId) {

        buscarProfissional(profissionalId);

        return disponibilidadeRepository
                .findByProfissional_Id(profissionalId);
    }

    @Override
    @Transactional
    public ExcecaoAgenda cadastrarExcecao(ExcecaoAgendaReq dados) {

        if (dados.getProfissionalId() == null || dados.getData() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Informe o profissional e a data da exceção."
            );
        }

        Profissional profissional = buscarProfissional(dados.getProfissionalId());

        if (!dados.isDiaFechado()) {

            LocalTime inicio = dados.getHorarioInicio();
            LocalTime fim = dados.getHorarioFim();

            if (inicio == null || fim == null || !inicio.isBefore(fim)) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Informe um intervalo válido para a exceção."
                );
            }

            List<ExcecaoAgenda> existentes =
                    excecaoAgendaRepository.findByProfissional_IdAndData(
                            profissional.getId(),
                            dados.getData()
                    );

            for (ExcecaoAgenda existente : existentes) {

                if (existente.isDiaFechado()
                        || (inicio.isBefore(existente.getHorarioFim())
                        && fim.isAfter(existente.getHorarioInicio()))) {

                    throw new ResponseStatusException(
                            HttpStatus.CONFLICT,
                            "Já existe uma exceção nesse período."
                    );
                }
            }
        } else {

            List<ExcecaoAgenda> existentes =
                    excecaoAgendaRepository.findByProfissional_IdAndData(
                            profissional.getId(),
                            dados.getData()
                    );

            if (!existentes.isEmpty()) {
                throw new ResponseStatusException(
                        HttpStatus.CONFLICT,
                        "Já existe uma exceção cadastrada para essa data."
                );
            }
        }

        ExcecaoAgenda excecao = new ExcecaoAgenda();

        excecao.setProfissional(profissional);
        excecao.setData(dados.getData());
        excecao.setDiaFechado(dados.isDiaFechado());

        if (!dados.isDiaFechado()) {
            excecao.setHorarioInicio(dados.getHorarioInicio());
            excecao.setHorarioFim(dados.getHorarioFim());
        }

        return excecaoAgendaRepository.save(excecao);
    }

    @Override
    public List<ExcecaoAgenda> listarExcecoes(
            Long profissionalId,
            LocalDate data) {

        buscarProfissional(profissionalId);

        if (data == null) {
            return excecaoAgendaRepository
                    .findByProfissional_Id(profissionalId);
        }

        return excecaoAgendaRepository
                .findByProfissional_IdAndData(profissionalId, data);
    }

    @Override
    public List<HorarioLivreResp> listarHorariosLivres(
            Long profissionalId,
            Long servicoId,
            LocalDate data) {

        if (data == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Informe a data desejada."
            );
        }

        Profissional profissional = buscarProfissional(profissionalId);

        Servico servico = servicoRepository.findById(servicoId)
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

        int duracao = servico.pegarDuracaoMinutos();

        if (duracao <= 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "A duração do serviço é inválida."
            );
        }

        DayOfWeek diaSemana = data.getDayOfWeek();

        List<Disponibilidade> disponibilidades =
                disponibilidadeRepository
                        .findByProfissional_IdAndDiaSemanaOrderByHorarioInicioAsc(
                                profissionalId,
                                diaSemana
                        );

        List<ExcecaoAgenda> excecoes =
                excecaoAgendaRepository
                        .findByProfissional_IdAndData(profissionalId, data);

        for (ExcecaoAgenda excecao : excecoes) {
            if (excecao.isDiaFechado()) {
                return new ArrayList<>();
            }
        }

        LocalDateTime inicioDia = data.atStartOfDay();
        LocalDateTime fimDia = data.plusDays(1).atStartOfDay();

        List<br.clinica.model.Agendamento> agendamentos =
                agendamentoRepository.buscarAgendamentosNoPeriodo(
                        profissionalId,
                        inicioDia,
                        fimDia,
                        StatusAgendamento.CANCELADO
                );

        List<HorarioLivreResp> horarios = new ArrayList<>();

        LocalDateTime agora = LocalDateTime.now();

        for (Disponibilidade disponibilidade : disponibilidades) {

            LocalTime inicioPeriodo = disponibilidade.getHorarioInicio();
            LocalTime fimPeriodo = disponibilidade.getHorarioFim();

            LocalDateTime inicio = data.atTime(inicioPeriodo);
            LocalDateTime limite = data.atTime(fimPeriodo);

            while (!inicio.plusMinutes(duracao).isAfter(limite)) {

                LocalDateTime fim = inicio.plusMinutes(duracao);

                boolean bloqueado = false;

                if (inicio.isBefore(agora)) {
                    bloqueado = true;
                }

                for (ExcecaoAgenda excecao : excecoes) {

                    if (!excecao.isDiaFechado()
                            && inicio.toLocalTime().isBefore(excecao.getHorarioFim())
                            && fim.toLocalTime().isAfter(excecao.getHorarioInicio())) {

                        bloqueado = true;
                        break;
                    }
                }

                if (!bloqueado) {
                    for (br.clinica.model.Agendamento agendamento : agendamentos) {

                        if (inicio.isBefore(agendamento.getHorarioFim())
                                && fim.isAfter(agendamento.getHorarioInicio())) {

                            bloqueado = true;
                            break;
                        }
                    }
                }

                if (!bloqueado) {
                    horarios.add(new HorarioLivreResp(inicio, fim));
                }

                // Avança um minuto para permitir horários sem grade fixa de 30 minutos.
                inicio = inicio.plusMinutes(1);
            }
        }

        return horarios;
    }

    @Override
    public boolean horarioDisponivel(
            Long profissionalId,
            LocalDateTime inicio,
            LocalDateTime fim) {

        if (inicio == null || fim == null || !inicio.isBefore(fim)) {
            return false;
        }

        if (!inicio.toLocalDate().equals(fim.toLocalDate())) {
            return false;
        }

        buscarProfissional(profissionalId);

        List<Disponibilidade> disponibilidades =
                disponibilidadeRepository
                        .findByProfissional_IdAndDiaSemanaOrderByHorarioInicioAsc(
                                profissionalId,
                                inicio.getDayOfWeek()
                        );

        boolean dentroDaDisponibilidade = disponibilidades.stream().anyMatch(d ->
                !inicio.toLocalTime().isBefore(d.getHorarioInicio())
                && !fim.toLocalTime().isAfter(d.getHorarioFim())
        );

        if (!dentroDaDisponibilidade) {
            return false;
        }

        List<ExcecaoAgenda> excecoes =
                excecaoAgendaRepository.findByProfissional_IdAndData(
                        profissionalId,
                        inicio.toLocalDate()
                );

        for (ExcecaoAgenda excecao : excecoes) {

            if (excecao.isDiaFechado()) {
                return false;
            }

            if (inicio.toLocalTime().isBefore(excecao.getHorarioFim())
                    && fim.toLocalTime().isAfter(excecao.getHorarioInicio())) {
                return false;
            }
        }

        return !agendamentoRepository.existeConflito(
                profissionalId,
                inicio,
                fim,
                StatusAgendamento.CANCELADO
        );
    }

    private Profissional buscarProfissional(Long id) {

        if (id == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Informe o profissional."
            );
        }

        return profissionalRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Profissional não encontrado."
                ));
    }

    private void validarDisponibilidade(DisponibilidadeReq dados) {

        if (dados.getProfissionalId() == null
                || dados.getDiaSemana() == null
                || dados.getHorarioInicio() == null
                || dados.getHorarioFim() == null) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Preencha todos os dados da disponibilidade."
            );
        }

        if (!dados.getHorarioInicio().isBefore(dados.getHorarioFim())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "O horário inicial deve ser anterior ao horário final."
            );
        }
    }
}

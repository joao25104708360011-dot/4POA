package br.AP8.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import br.AP8.conexao.HibernateUtil;
import br.AP8.model.Tarefa;

public class TarefaDAO {

	public void salvar(String descricao) {
		Transaction tx = null;

		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			tx = session.beginTransaction();

			Tarefa tarefa = new Tarefa();
			tarefa.setDescricao(descricao);
			tarefa.setConcluida(false);
			session.save(tarefa);

			tx.commit();
			System.out.println("-> Tarefa '" + descricao + "' adicionada!");

		} catch (Exception e) {
			if (tx != null && tx.isActive()) tx.rollback();
			System.err.println("Erro ao salvar tarefa: " + e.getMessage());
		}
	}

	public List<Tarefa> listarTodas() {
		List<Tarefa> tarefas = new ArrayList<>();

		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			tarefas = session.createQuery("from Tarefa order by id", Tarefa.class).list();

		} catch (Exception e) {
			System.err.println("Erro ao listar tarefas: " + e.getMessage());
		}

		return tarefas;
	}

	public void atualizarStatus(int id, boolean concluida) {
		Transaction tx = null;

		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			tx = session.beginTransaction();

			Tarefa tarefa = session.get(Tarefa.class, id);
			if (tarefa != null) {
				tarefa.setConcluida(concluida);
				session.update(tarefa);
			}

			tx.commit();
			System.out.println("-> Tarefa ID " + id + " atualizada!");

		} catch (Exception e) {
			if (tx != null && tx.isActive()) tx.rollback();
			System.err.println("Erro ao atualizar tarefa: " + e.getMessage());
		}
	}

	public void deletar(int id) {
		Transaction tx = null;

		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			tx = session.beginTransaction();

			Tarefa tarefa = session.get(Tarefa.class, id);
			if (tarefa != null) {
				session.delete(tarefa);
			}

			tx.commit();
			System.out.println("-> Tarefa ID " + id + " removida!");

		} catch (Exception e) {
			if (tx != null && tx.isActive()) tx.rollback();
			System.err.println("Erro ao deletar tarefa: " + e.getMessage());
		}
	}
}
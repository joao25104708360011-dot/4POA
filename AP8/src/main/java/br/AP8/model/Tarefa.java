package br.AP8.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tarefas")
public class Tarefa {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "descricao", length = 255)
	private String descricao;

	@Column(name = "concluida")
	private boolean concluida;

	public Tarefa() {
	}

	public Tarefa(int id, String descricao, boolean concluida) {
		this.id = id;
		this.descricao = descricao;
		this.concluida = concluida;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public boolean isConcluida() {
		return concluida;
	}

	public void setConcluida(boolean concluida) {
		this.concluida = concluida;
	}

	@Override
	public String toString() {
		return String.format("[%d] %s - %s", id, descricao, concluida ? "✓ Concluída" : "⭘ Pendente");
	}
}

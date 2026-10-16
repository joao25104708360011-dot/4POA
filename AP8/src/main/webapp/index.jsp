<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List, br.AP8.model.Tarefa" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Gerenciador de Tarefas</title>
<style>
    body { font-family: Arial, sans-serif; margin: 40px; background-color: #f9f9f9; }
    .container { max-width: 600px; background: white; padding: 20px; border-radius: 8px; box-shadow: 0 0 10px rgba(0,0,0,0.1); }
    .mensagem-api { background-color: #e3f2fd; padding: 10px; border-left: 4px solid #2196f3; margin-bottom: 20px; }
    .campo { margin-bottom: 15px; }
    table { width: 100%; border-collapse: collapse; margin-top: 20px; }
    th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }
    th { background-color: #f2f2f2; }
</style>
</head>
<body>

<div class="container">
    <h2>Minhas Tarefas + Web Service</h2>

    <% 
        String mensagem = (String) session.getAttribute("mensagemWebservice");
        if (mensagem != null) {
    %>
        <div class="mensagem-api">
            <strong>Dica do Web Service:</strong> "<%= mensagem %>"
        </div>
    <% 
            session.removeAttribute("mensagemWebservice");
        } 
    %>

    <form action="TarefaServlet" method="post">
        <div class="campo">
            <label for="descricao">Nova Tarefa:</label><br>
            <input type="text" id="descricao" name="descricao" style="width: 70%; padding: 8px;" required>
            <input type="submit" value="Adicionar" style="padding: 8px 15px;">
        </div>
    </form>

    <hr>

    <h3>Lista de Tarefas</h3>
    <table>
        <tr>
            <th>ID</th>
            <th>Descrição</th>
            <th>Status</th>
            <th>Ações</th>
        </tr>
        <% 
            List<Tarefa> lista = (List<Tarefa>) request.getAttribute("listaTarefas");
            if (lista != null && !lista.isEmpty()) {
                for (Tarefa t : lista) {
        %>
        <tr>
            <td><%= t.getId() %></td>
            <td><%= t.getDescricao() %></td>
            <td><%= t.isConcluida() ? "Concluída" : "Pendente" %></td>
            <td>
                <% if (!t.isConcluida()) { %>
                    <a href="TarefaServlet?acao=concluir&id=<%= t.getId() %>">Concluir</a> | 
                <% } %>
                <a href="TarefaServlet?acao=deletar&id=<%= t.getId() %>">Excluir</a>
            </td>
        </tr>
        <% 
                }
            } else {
        %>
        <tr>
            <td colspan="4">Nenhuma tarefa cadastrada. Liste acessando a Servlet!</td>
        </tr>
        <% } %>
    </table>
</div>

</body>
</html>
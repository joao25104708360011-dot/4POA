package br.AP8.servlet;

import br.AP8.dao.TarefaDAO;
import br.AP8.model.Tarefa;
import br.AP8.service.ConselhoService;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/TarefaServlet")
public class TarefaServlet extends HttpServlet {

    private TarefaDAO dao = new TarefaDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        String acao = request.getParameter("acao");

        if ("deletar".equals(acao)) {
            int id = Integer.parseInt(request.getParameter("id"));
            dao.deletar(id);
        } else if ("concluir".equals(acao)) {
            int id = Integer.parseInt(request.getParameter("id"));
            dao.atualizarStatus(id, true);
        }

        List<Tarefa> lista = dao.listarTodas();
        request.setAttribute("listaTarefas", lista);
        request.getRequestDispatcher("index.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        String descricao = request.getParameter("descricao");

        if (descricao != null && !descricao.trim().isEmpty()) {
            dao.salvar(descricao);
            
            String conselho = ConselhoService.buscarConselhoAleatorio();
            request.getSession().setAttribute("mensagemWebservice", conselho);
        }

        response.sendRedirect("TarefaServlet");
    }
}
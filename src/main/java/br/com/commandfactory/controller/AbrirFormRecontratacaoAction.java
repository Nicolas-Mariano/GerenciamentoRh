package br.com.commandfactory.controller;

import dao.DAOFactory;
import model.Funcionario;
import model.Setor;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AbrirFormRecontratacaoAction implements ICommand {
    @Override
    public String executar(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            String idStr = request.getParameter("id");
            if (idStr == null || idStr.isBlank()) {
                Object attr = request.getAttribute("id");
                if (attr != null) idStr = attr.toString();
            }
            if (idStr == null || idStr.isBlank()) throw new Exception("ID do funcionário não informado.");
            int id = Integer.parseInt(idStr);

            Funcionario funcionario = DAOFactory.getFuncionarioDAO().consultarById(id);
            List<Setor> listaSetores = DAOFactory.getSetorDAO().consultarTodos();

            request.setAttribute("funcionario", funcionario);
            request.setAttribute("setores", listaSetores);

            return "cadastro_contrato.jsp";
        } catch (Exception e) {
            request.setAttribute("erro", "Erro ao carregar formulário de recontratação: " + e.getMessage());
            return "index.jsp";
        }
    }
}

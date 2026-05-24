package br.com.commandfactory.controller;

import dao.DAOFactory;
import model.Contrato;
import model.Endereco;
import model.Funcionario;
import service.ServiceFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class EditarFuncionarioAction implements ICommand {
    @Override
    public String executar(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            String idStr = request.getParameter("id");
            if (idStr == null || idStr.isBlank()) {
                Object attr = request.getAttribute("id");
                if (attr != null) idStr = attr.toString();
            }
            if (idStr == null || idStr.isBlank()) {
                idStr = request.getParameter("txtId");
            }
            if (idStr == null || idStr.isBlank()) throw new Exception("ID do funcionário não informado.");
            int id = Integer.parseInt(idStr);

            Funcionario f = DAOFactory.getFuncionarioDAO().consultarById(id);
            Endereco e = null;
            if (f.getEndereco() != null && f.getEndereco().getId() > 0) {
                e = DAOFactory.getEnderecoDAO().consultarById(f.getEndereco().getId());
            }
            Contrato contratoAtivo = ServiceFactory.getContratoService().buscarAtivo(id);

            request.setAttribute("funcionario", f);
            request.setAttribute("endereco", e);
            request.setAttribute("contratoAtivo", contratoAtivo);
            request.setAttribute("setores", DAOFactory.getSetorDAO().consultarTodos());

            return "formeditar_funcionario.jsp";
        } catch (Exception ex) {
            request.setAttribute("erro", "Erro ao carregar edição: " + ex.getMessage());
            return new ListarFuncionariosAction().executar(request, response);
        }
    }
}

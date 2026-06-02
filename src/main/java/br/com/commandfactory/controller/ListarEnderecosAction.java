package br.com.commandfactory.controller;

import dao.DAOFactory;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.Endereco;

public class ListarEnderecosAction implements ICommand {
    @Override
    public String executar(HttpServletRequest request, HttpServletResponse response) throws Exception {
        boolean incluirDemitidos = "true".equalsIgnoreCase(request.getParameter("incluirDemitidos"));

        List<Endereco> listaEnderecos = incluirDemitidos
            ? DAOFactory.getEnderecoDAO().consultarTodos()
            : DAOFactory.getEnderecoDAO().consultarSomenteAtivos();

        request.setAttribute("enderecos", listaEnderecos);
        request.setAttribute("incluirDemitidos", incluirDemitidos);

        return "lista_enderecos.jsp";
    }
}

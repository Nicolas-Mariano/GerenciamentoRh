package br.com.commandfactory.controller;

import dao.DAOFactory;
import model.Funcionario;
import model.Setor;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ListarFuncionariosAction implements ICommand {
    @Override
    public String executar(HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<Funcionario> listaFuncionarios = DAOFactory.getFuncionarioDAO().consultarTodos();
        List<Setor> listaSetores = DAOFactory.getSetorDAO().consultarTodos();

        request.setAttribute("funcionarios", listaFuncionarios);
        request.setAttribute("setores", listaSetores);

        return "lista_funcionarios.jsp";
    }
}

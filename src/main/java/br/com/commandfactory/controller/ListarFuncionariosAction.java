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
        boolean incluirDemitidos = "true".equalsIgnoreCase(request.getParameter("incluirDemitidos"));
        List<Funcionario> listaFuncionarios = incluirDemitidos
            ? DAOFactory.getFuncionarioDAO().consultarTodosOrdenados()
            : DAOFactory.getFuncionarioDAO().consultarAtivos();
        List<Setor> listaSetores = DAOFactory.getSetorDAO().consultarTodos();
        request.setAttribute("funcionarios", listaFuncionarios);
        request.setAttribute("setores", listaSetores);
        request.setAttribute("incluirDemitidos", incluirDemitidos);
        return "lista_funcionarios.jsp";
    }
}

package br.com.commandfactory.controller;

import dao.DAOFactory;
import model.Contrato;
import model.NivelSenioridade;

import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AbrirVincularGerenteAction implements ICommand {
    @Override
    public String executar(HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setAttribute("setores", DAOFactory.getSetorDAO().consultarTodos());

        String idSetorStr = request.getParameter("idSetor");
        if (idSetorStr != null && !idSetorStr.isEmpty()) {
            int idSetor = Integer.parseInt(idSetorStr);
            List<Contrato> aptos = DAOFactory.getContratoDAO().buscarAtivosPorSetor(idSetor).stream()
                .filter(c -> c.getNivelSenioridade() == NivelSenioridade.PLENO
                          || c.getNivelSenioridade() == NivelSenioridade.SENIOR)
                .collect(Collectors.toList());
            request.setAttribute("contratos", aptos);
            request.setAttribute("setorSelecionado", idSetor);
        }
        return "vincular_gerente.jsp";
    }
}

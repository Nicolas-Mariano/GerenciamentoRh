package br.com.commandfactory.controller;

import dao.DAOFactory;
import model.Contrato;
import model.Setor;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AbrirAplicarAumentoAction implements ICommand {
    @Override
    public String executar(HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setAttribute("setores", DAOFactory.getSetorDAO().consultarTodos());

        String idSetorStr = request.getParameter("idSetor");
        if (idSetorStr != null && !idSetorStr.isEmpty()) {
            int idSetor = Integer.parseInt(idSetorStr);
            Setor setorRef = new Setor();
            setorRef.setId(idSetor);
            List<Contrato> contratos = DAOFactory.getContratoDAO().buscarAtivosPorSetor(setorRef);
            request.setAttribute("contratos", contratos);
            request.setAttribute("setorSelecionado", idSetor);
        }

        return "aumento_salarial.jsp";
    }
}

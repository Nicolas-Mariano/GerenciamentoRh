package br.com.commandfactory.controller;

import dao.DAOFactory;
import model.Setor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AtualizarSetorAction implements ICommand {
    @Override
    public String executar(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            int id = Integer.parseInt(request.getParameter("txtId"));
            String nomeSetor = request.getParameter("txtNome");

            Setor setor = DAOFactory.getSetorDAO().consultarById(id);
            setor.setNome(nomeSetor);
            DAOFactory.getSetorDAO().atualizar(setor);

            request.setAttribute("mensagem", "Setor '" + nomeSetor + "' atualizado com sucesso!");
            return new ListarSetoresAction().executar(request, response);

        } catch (Exception e) {
            request.setAttribute("erro", "Erro ao atualizar setor: " + e.getMessage());
            return new ListarSetoresAction().executar(request, response);
        }
    }
}

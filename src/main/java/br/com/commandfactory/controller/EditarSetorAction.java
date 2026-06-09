package br.com.commandfactory.controller;

import dao.DAOFactory;
import model.Contrato;
import model.Setor;

import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class EditarSetorAction implements ICommand {
    @Override
    public String executar(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            int idSetor = Integer.parseInt(request.getParameter("id"));

            Setor setor = DAOFactory.getSetorDAO().consultarById(idSetor);

            Setor setorRef = new Setor();
            setorRef.setId(idSetor);
            List<Contrato> aptosParaGerencia = DAOFactory.getContratoDAO()
                .buscarAtivosPorSetor(setorRef).stream()
                .filter(c -> c.getNivelSenioridade().podeSerGerente())
                .collect(Collectors.toList());

            request.setAttribute("setor", setor);
            request.setAttribute("contratos", aptosParaGerencia);

            return "formeditar_setor.jsp";

        } catch (Exception e) {
            request.setAttribute("erro", "Erro ao carregar dados para edição: " + e.getMessage());
            return new ListarSetoresAction().executar(request, response);
        }
    }
}

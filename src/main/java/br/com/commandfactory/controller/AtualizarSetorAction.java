package br.com.commandfactory.controller;

import dao.DAOFactory;
import model.Contrato;
import model.Setor;

import java.util.List;
import java.util.stream.Collectors;
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

            String idStr = request.getParameter("txtId");
            int idSetor = 0;
            try { idSetor = Integer.parseInt(idStr); } catch (NumberFormatException ignored) {}

            Setor formSetor = Setor.getBuilder()
                .comId(idSetor)
                .comNome(request.getParameter("txtNome"))
                .constroi();
            request.setAttribute("setor", formSetor);

            if (idSetor > 0) {
                Setor setorRef = new Setor();
                setorRef.setId(idSetor);
                List<Contrato> aptosParaGerencia = DAOFactory.getContratoDAO()
                    .buscarAtivosPorSetor(setorRef).stream()
                    .filter(c -> c.getNivelSenioridade().podeSerGerente())
                    .collect(Collectors.toList());
                request.setAttribute("contratos", aptosParaGerencia);
            }

            return "formeditar_setor.jsp";
        }
    }
}

package br.com.commandfactory.controller;

import model.Contrato;
import model.Setor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AtualizarGerenteSetorAction implements ICommand {
    @Override
    public String executar(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            int idSetor = Integer.parseInt(request.getParameter("txtIdSetor"));
            int idContrato = Integer.parseInt(request.getParameter("txtIdContrato"));

            Setor setorRef = new Setor();
            setorRef.setId(idSetor);
            Contrato contratoRef = new Contrato();
            contratoRef.setId(idContrato);
            service.ServiceFactory.getSetorService().vincularGerente(setorRef, contratoRef);

            request.setAttribute("mensagem", "Gerente vinculado ao setor com sucesso!");
            return "sucesso.jsp";

        } catch (Exception e) {
            request.setAttribute("erro", e.getMessage());
            return new AbrirVincularGerenteAction().executar(request, response);
        }
    }
}

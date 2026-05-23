package br.com.commandfactory.controller;

import service.ServiceFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DemitirFuncionarioAction implements ICommand {
    @Override
    public String executar(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            int idContrato = Integer.parseInt(request.getParameter("idContrato"));
            String motivo = request.getParameter("motivoDesligamento");
            if (motivo == null) motivo = "";

            ServiceFactory.getContratoService().demitir(idContrato, motivo, null);

            request.setAttribute("mensagem", "Demissão registrada com sucesso na data de hoje.");
        } catch (Exception ex) {
            request.setAttribute("erro", "Erro ao registrar demissão: " + ex.getMessage());
        }
        return new ListarFuncionariosAction().executar(request, response);
    }
}

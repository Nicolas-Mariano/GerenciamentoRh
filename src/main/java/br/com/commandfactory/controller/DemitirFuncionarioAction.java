package br.com.commandfactory.controller;

import model.Contrato;
import service.ServiceFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DemitirFuncionarioAction implements ICommand {
    @Override
    public String executar(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            String idContratoStr = request.getParameter("idContrato");
            if (idContratoStr == null || idContratoStr.isBlank()) {
                throw new Exception("Parâmetro obrigatório ausente: idContrato.");
            }
            int idContrato = Integer.parseInt(idContratoStr);
            String motivo = request.getParameter("motivoDesligamento");
            if (motivo == null || motivo.trim().isEmpty()) {
                throw new Exception("O motivo da demissão é obrigatório.");
            }
            motivo = motivo.trim();

            Contrato contratoRef = new Contrato();
            contratoRef.setId(idContrato);
            ServiceFactory.getContratoService().demitir(contratoRef, motivo, null);

            request.setAttribute("mensagem", "Demissão registrada com sucesso na data de hoje.");
        } catch (Exception ex) {
            request.setAttribute("erro", "Erro ao registrar demissão: " + ex.getMessage());
        }
        return new ListarFuncionariosAction().executar(request, response);
    }
}

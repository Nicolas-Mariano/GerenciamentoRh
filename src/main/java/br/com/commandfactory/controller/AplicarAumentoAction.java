package br.com.commandfactory.controller;

import dao.DAOFactory;
import model.Contrato;
import service.ServiceFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AplicarAumentoAction implements ICommand {
    @Override
    public String executar(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            int idContrato = Integer.parseInt(request.getParameter("idContrato"));
            String tipo = request.getParameter("tipoAumento");
            double valor = Double.parseDouble(request.getParameter("valorAumento").replace(",", "."));

            ServiceFactory.getContratoService().aplicarAumento(idContrato, tipo, valor);

            Contrato c = DAOFactory.getContratoDAO().consultarById(idContrato);
            request.setAttribute("mensagem", "Aumento do tipo " + tipo + " de " + valor + " aplicado com sucesso. Novo salário: R$ " + String.format("%.2f", c.getSalarioBase()));
            return "sucesso.jsp";

        } catch (Exception e) {
            request.setAttribute("erro", "Erro ao aplicar aumento: " + e.getMessage());
            return new AbrirAplicarAumentoAction().executar(request, response);
        }
    }
}

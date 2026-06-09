package br.com.commandfactory.controller;

import dao.DAOFactory;
import model.Contrato;
import model.NivelSenioridade;
import salary.TipoAumento;
import service.ServiceFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AplicarPromocaoAction implements ICommand {
    @Override
    public String executar(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            int idContrato = Integer.parseInt(request.getParameter("idContrato"));
            String novoNivelStr = request.getParameter("novoNivel");
            String tipoAumento = request.getParameter("tipoAumento");
            String valorStr = request.getParameter("valorAumento");

            if (novoNivelStr == null || novoNivelStr.isBlank()) {
                throw new Exception("Campo obrigatório ausente: novo nível de senioridade.");
            }
            if (valorStr == null || valorStr.isBlank()) {
                throw new Exception("Campo obrigatório ausente: valor do aumento.");
            }

            NivelSenioridade novoNivel = NivelSenioridade.valueOf(novoNivelStr);
            TipoAumento tipo = TipoAumento.valueOf(tipoAumento.toUpperCase());
            double valor = Double.parseDouble(valorStr.replace(",", "."));

            Contrato contratoRef = new Contrato();
            contratoRef.setId(idContrato);
            ServiceFactory.getContratoService().aplicarPromocao(contratoRef, novoNivel, tipo, valor);

            Contrato c = DAOFactory.getContratoDAO().consultarById(idContrato);
            request.setAttribute("mensagem",
                "Promoção aplicada com sucesso! Novo nível: " + c.getNivelSenioridade().getRotulo()
                + " — Novo salário: R$ " + String.format("%.2f", c.getSalarioBase()));
            return "sucesso.jsp";

        } catch (IllegalArgumentException e) {
            request.setAttribute("erro", "Erro ao aplicar promoção: Nível de senioridade inválido.");
            preservarCamposDigitados(request);
            return new AbrirAplicarPromocaoAction().executar(request, response);
        } catch (Exception e) {
            request.setAttribute("erro", "Erro ao aplicar promoção: " + e.getMessage());
            preservarCamposDigitados(request);
            return new AbrirAplicarPromocaoAction().executar(request, response);
        }
    }

    private void preservarCamposDigitados(HttpServletRequest request) {
        request.setAttribute("valorAumentoDigitado", request.getParameter("valorAumento"));
        request.setAttribute("tipoAumentoSelecionado", request.getParameter("tipoAumento"));
        request.setAttribute("novoNivelSelecionado", request.getParameter("novoNivel"));
    }
}

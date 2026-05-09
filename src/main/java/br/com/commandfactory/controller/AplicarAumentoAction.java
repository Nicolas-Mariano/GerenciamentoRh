/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.commandfactory.controller;

import dao.DAOFactory;
import model.Funcionario;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AplicarAumentoAction implements ICommand {
    @Override
    public String executar(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            int idFuncionario = Integer.parseInt(request.getParameter("idFuncionario"));
            double percentual = Double.parseDouble(request.getParameter("percentualAumento").replace(",", "."));

            service.ServiceFactory.getFuncionarioService().aplicarAumento(idFuncionario, percentual);
            Funcionario f = DAOFactory.getFuncionarioDAO().consultarById(idFuncionario);

            request.setAttribute("mensagem", "Aumento de " + percentual + "% aplicado com sucesso para " + f.getNome());
            return "sucesso.jsp";
            
        } catch (Exception e) {
            request.setAttribute("erro", "Erro ao aplicar aumento: " + e.getMessage());
            return new AbrirAplicarAumentoAction().executar(request, response);
        }
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.commandfactory.controller;

import model.Setor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AtualizarGerenteSetorAction implements ICommand {
    @Override
    public String executar(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            int idSetor = Integer.parseInt(request.getParameter("txtIdSetor"));
            int idFuncionario = Integer.parseInt(request.getParameter("txtIdFuncionario"));

            service.ServiceFactory.getSetorService().vincularGerente(idSetor, idFuncionario);
            
            request.setAttribute("mensagem", "Gerente vinculado ao setor com sucesso!");
            return "sucesso.jsp";
            
        } catch (Exception e) {
            request.setAttribute("erro", e.getMessage());
            return new AbrirVincularGerenteAction().executar(request, response);
        }
    }
}

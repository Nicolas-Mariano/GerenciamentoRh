/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.commandfactory.controller;

import dao.FuncionarioDAO;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DemitirFuncionarioAction implements ICommand {
    @Override
    public String executar(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            
            Date dataAtual = new Date();
            
            new FuncionarioDAO().registrarDemissao(id, dataAtual);
            
            request.setAttribute("mensagem", "Funcionário demitido com sucesso na data de hoje.");
        } catch (Exception ex) {
            request.setAttribute("erro", "Erro ao demitir: " + ex.getMessage());
        }
        
        return new ListarFuncionariosAction().executar(request, response);
    }
}

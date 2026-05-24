/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.commandfactory.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AbrirConfirmacaoAction implements ICommand {
    @Override
    public String executar(HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setAttribute("acaoDestino", request.getParameter("acaoDestino"));
        request.setAttribute("idItem", request.getParameter("id"));
        request.setAttribute("idContrato", request.getParameter("idContrato"));
        request.setAttribute("mensagem", request.getParameter("msg"));
        request.setAttribute("acaoVoltar", request.getParameter("acaoVoltar"));
        return "confirmacao.jsp";
    }
}

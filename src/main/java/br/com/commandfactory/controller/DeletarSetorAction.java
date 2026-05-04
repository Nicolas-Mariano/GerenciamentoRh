/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.commandfactory.controller;

import dao.SetorDAO;
import model.Setor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DeletarSetorAction implements ICommand {
    @Override
    public String executar(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            Setor setor = new Setor();
            setor.setId(id);
            
            new SetorDAO().deletar(setor);
            request.setAttribute("mensagem", "Setor deletado com sucesso!");
        } catch (Exception e) {
            request.setAttribute("erro", "Erro ao deletar: " + e.getMessage());
        }
        
        return new ListarSetoresAction().executar(request, response);
    }
}
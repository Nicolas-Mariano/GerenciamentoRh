/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.commandfactory.controller;

import dao.SetorDAO;
import model.Setor;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ListarSetoresAction implements ICommand {
    @Override
    public String executar(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            List<Setor> lista = dao.DAOFactory.getSetorDAO().consultarTodos();
            request.setAttribute("setores", lista);
            return "lista_setores.jsp";
        } catch (Exception e) {
            request.setAttribute("erro", "Erro ao listar setores: " + e.getMessage());
            return "index.jsp";
        }
    }
}

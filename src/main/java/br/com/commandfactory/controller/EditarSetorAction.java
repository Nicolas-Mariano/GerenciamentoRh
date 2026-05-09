/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.commandfactory.controller;

import dao.FuncionarioDAO;
import dao.SetorDAO;
import model.Funcionario;
import model.Setor;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class EditarSetorAction implements ICommand {
    @Override
    public String executar(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            int idSetor = Integer.parseInt(request.getParameter("id"));
            
            Setor setor = dao.DAOFactory.getSetorDAO().consultarById(idSetor);
            
            List<Funcionario> aptosParaGerencia = dao.DAOFactory.getFuncionarioDAO().consultarTodos().stream()
                    .filter(f -> f.getDataDemissao() == null)
                    .filter(f -> f.getSetor() != null && f.getSetor().getId() == idSetor)
                    .filter(f -> "Pleno".equalsIgnoreCase(f.getNivel()) || "Senior".equalsIgnoreCase(f.getNivel()))
                    .collect(Collectors.toList());

            request.setAttribute("setor", setor);
            request.setAttribute("funcionarios", aptosParaGerencia);
            
            return "formeditar_setor.jsp"; 
            
        } catch (Exception e) {
            request.setAttribute("erro", "Erro ao carregar dados para edição: " + e.getMessage());
            return new ListarSetoresAction().executar(request, response);
        }
    }
}

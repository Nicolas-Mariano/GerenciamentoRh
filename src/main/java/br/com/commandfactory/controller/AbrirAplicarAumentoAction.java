/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.commandfactory.controller;

import dao.FuncionarioDAO;
import dao.SetorDAO;
import model.Funcionario;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AbrirAplicarAumentoAction implements ICommand {
    @Override
    public String executar(HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setAttribute("setores", dao.DAOFactory.getSetorDAO().consultarTodos());
        
        String idSetorStr = request.getParameter("idSetor");
        
        if (idSetorStr != null && !idSetorStr.isEmpty()) {
            int idSetor = Integer.parseInt(idSetorStr);
            
            List<Funcionario> funcsDoSetor = dao.DAOFactory.getFuncionarioDAO().consultarTodos().stream()
                    .filter(f -> f.getSetor() != null && f.getSetor().getId() == idSetor && f.getDataDemissao() == null)
                    .collect(Collectors.toList());
            
            request.setAttribute("funcionarios", funcsDoSetor);
            request.setAttribute("setorSelecionado", idSetor);
        }
        
        return "aumento_salarial.jsp";
    }
}

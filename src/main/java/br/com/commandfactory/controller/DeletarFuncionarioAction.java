/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.commandfactory.controller;

import model.Funcionario;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DeletarFuncionarioAction implements ICommand {
    @Override
    public String executar(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            Funcionario funcRef = new Funcionario();
            funcRef.setId(id);
            dao.DAOFactory.getFuncionarioDAO().deletar(funcRef);
            
            request.setAttribute("mensagem", "Funcionário deletado permanentemente.");
        } catch (Exception ex) {
            request.setAttribute("erro", "Erro ao deletar funcionário: " + ex.getMessage());
        }
        
        return new ListarFuncionariosAction().executar(request, response);
    }
}

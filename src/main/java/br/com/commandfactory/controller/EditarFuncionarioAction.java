/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.commandfactory.controller;

import dao.FuncionarioDAO;
import dao.EnderecoDAO;
import dao.SetorDAO;
import model.Funcionario;
import model.Endereco;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class EditarFuncionarioAction implements ICommand {
    @Override
    public String executar(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            
            Funcionario f = new FuncionarioDAO().consultarById(id);
            Endereco e = new EnderecoDAO().consultarById(f.getIdEndereco());

            request.setAttribute("funcionario", f);
            request.setAttribute("endereco", e);
            request.setAttribute("setores", new SetorDAO().consultarTodos());
            
            return "formeditar_funcionario.jsp";
        } catch (Exception ex) {
            request.setAttribute("erro", "Erro ao carregar edição: " + ex.getMessage());
            return new ListarFuncionariosAction().executar(request, response);
        }
    }
}
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.commandfactory.controller;

import dao.FuncionarioDAO;
import dao.SetorDAO;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.Funcionario;
import model.Setor;

public class ListarFuncionariosAction implements ICommand {
    @Override
    public String executar(HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<Funcionario> listaFuncionarios = dao.DAOFactory.getFuncionarioDAO().consultarTodos();
        List<Setor> listaSetores = dao.DAOFactory.getSetorDAO().consultarTodos();
        
        request.setAttribute("funcionarios", listaFuncionarios);
        request.setAttribute("setores", listaSetores);
        
        return "lista_funcionarios.jsp";
    }
}

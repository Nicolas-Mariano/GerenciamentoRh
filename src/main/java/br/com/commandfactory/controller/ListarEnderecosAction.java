/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.commandfactory.controller;

import dao.EnderecoDAO;
import dao.FuncionarioDAO;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.Endereco;
import model.Funcionario;

public class ListarEnderecosAction implements ICommand {
    @Override
    public String executar(HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<Endereco> listaEnderecos = new EnderecoDAO().consultarTodos();
        List<Funcionario> listaFuncionarios = new FuncionarioDAO().consultarTodos();
        
        request.setAttribute("enderecos", listaEnderecos);
        request.setAttribute("funcionarios", listaFuncionarios);
        
        return "lista_enderecos.jsp";
    }
}
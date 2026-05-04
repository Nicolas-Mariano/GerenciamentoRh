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

public class AbrirFormFuncionarioAction implements ICommand {
    @Override
    public String executar(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            List<Setor> listaSetores = new SetorDAO().consultarTodos();
            request.setAttribute("setores", listaSetores);
            return "cadastro_funcionario.jsp";
        } catch (Exception e) {
            request.setAttribute("erro", "Erro ao carregar formulário: " + e.getMessage());
            return "index.jsp";
        }
    }
}
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
import model.Setor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DetalharFuncionarioAction implements ICommand {
    @Override
    public String executar(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            
            FuncionarioDAO funcDao = new FuncionarioDAO();
            Funcionario f = funcDao.consultarById(id);
            
            Endereco e = null;
            if (f.getEndereco() != null && f.getEndereco().getId() > 0) {
                e = new EnderecoDAO().consultarById(f.getEndereco().getId());
            }

            Setor s = null;
            if (f.getSetor() != null && f.getSetor().getId() > 0) {
                s = new SetorDAO().consultarById(f.getSetor().getId());
            }

            request.setAttribute("funcionario", f);
            request.setAttribute("endereco", e);
            request.setAttribute("setor", s); 
            
            return "detalhes_funcionario.jsp";
        } catch (Exception ex) {
            request.setAttribute("erro", "Erro ao carregar detalhes: " + ex.getMessage());
            return new ListarFuncionariosAction().executar(request, response);
        }
    }
}
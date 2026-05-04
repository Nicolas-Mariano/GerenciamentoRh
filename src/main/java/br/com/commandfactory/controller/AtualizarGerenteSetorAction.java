/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.commandfactory.controller;

import dao.FuncionarioDAO;
import dao.SetorDAO;
import model.Funcionario;
import model.Setor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AtualizarGerenteSetorAction implements ICommand {
    @Override
    public String executar(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            int idSetor = Integer.parseInt(request.getParameter("txtIdSetor"));
            int idFuncionario = Integer.parseInt(request.getParameter("txtIdFuncionario"));

            Funcionario f = new FuncionarioDAO().consultarById(idFuncionario);
            if (f.getIdSetor() != idSetor) {
                throw new Exception("Operação negada: O funcionário não pertence ao setor selecionado.");
            }
            
            if (!"Pleno".equalsIgnoreCase(f.getNivel()) && !"Senior".equalsIgnoreCase(f.getNivel())) {
                throw new Exception("Regra Violada: Apenas funcionários de nível Pleno ou Senior podem assumir a gerência.");
            }

            SetorDAO dao = new SetorDAO();
            Setor setor = dao.consultarById(idSetor);
            setor.setIdFuncResponsavel(idFuncionario);
            
            dao.atualizar(setor);
            
            request.setAttribute("mensagem", "Gerente vinculado ao setor com sucesso!");
            return "sucesso.jsp";
            
        } catch (Exception e) {
            request.setAttribute("erro", e.getMessage());
            return new AbrirVincularGerenteAction().executar(request, response);
        }
    }
}
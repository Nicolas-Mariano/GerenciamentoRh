/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.commandfactory.controller;

import dao.FuncionarioDAO;
import model.Funcionario;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AplicarAumentoAction implements ICommand {
    @Override
    public String executar(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            int idFuncionario = Integer.parseInt(request.getParameter("idFuncionario"));
            double percentual = Double.parseDouble(request.getParameter("percentualAumento").replace(",", "."));

            if (percentual <= 0) {
                throw new Exception("O percentual deve ser maior que zero.");
            }

            FuncionarioDAO dao = new FuncionarioDAO();
            Funcionario f = dao.consultarById(idFuncionario);

            if (f.getId() == 0) {
                throw new Exception("Funcionário não encontrado.");
            }

            double novoSalario = f.getSalarioBase() * (1 + (percentual / 100));
            f.setSalarioBase(novoSalario);

            dao.atualizar(f);

            request.setAttribute("mensagem", "Aumento de " + percentual + "% aplicado com sucesso para " + f.getNome());
            return "sucesso.jsp";
            
        } catch (Exception e) {
            request.setAttribute("erro", "Erro ao aplicar aumento: " + e.getMessage());
            return new AbrirAplicarAumentoAction().executar(request, response);
        }
    }
}
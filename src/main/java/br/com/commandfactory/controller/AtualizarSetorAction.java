/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.commandfactory.controller;

import dao.SetorDAO;
import model.Setor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AtualizarSetorAction implements ICommand {
    @Override
    public String executar(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            int id = Integer.parseInt(request.getParameter("txtId"));
            String nomeSetor = request.getParameter("txtNome");
            String idGerenteStr = request.getParameter("txtIdGerente");
            
            Integer idGerente = (idGerenteStr != null && !idGerenteStr.trim().isEmpty()) 
                                ? Integer.parseInt(idGerenteStr) : null;

            Setor setorAtualizado = Setor.getBuilder()
                    .comId(id)
                    .comNome(nomeSetor)
                    .constroi();
            
            setorAtualizado.setIdFuncResponsavel(idGerente);
            
            new SetorDAO().atualizar(setorAtualizado);
            
            request.setAttribute("mensagem", "Setor '" + nomeSetor + "' atualizado com sucesso!");
            return new ListarSetoresAction().executar(request, response);
            
        } catch (Exception e) {
            request.setAttribute("erro", "Erro ao atualizar setor: " + e.getMessage());
            return new ListarSetoresAction().executar(request, response);
        }
    }
}
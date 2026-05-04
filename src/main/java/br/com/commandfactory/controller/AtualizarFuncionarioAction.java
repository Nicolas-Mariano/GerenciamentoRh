/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.commandfactory.controller;

import dao.EnderecoDAO;
import dao.FuncionarioDAO;
import model.Endereco;
import model.Funcionario;
import java.text.SimpleDateFormat;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AtualizarFuncionarioAction implements ICommand {
    @Override
    public String executar(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            int idFuncionario = Integer.parseInt(request.getParameter("txtId"));
            int idEndereco = Integer.parseInt(request.getParameter("txtIdEndereco"));

            FuncionarioDAO funcDao = new FuncionarioDAO();
            EnderecoDAO endDao = new EnderecoDAO();

            Funcionario f = funcDao.consultarById(idFuncionario);
            Endereco e = endDao.consultarById(idEndereco);

            f.setNome(request.getParameter("txtNome"));
            f.setCpf(request.getParameter("txtCpf").replaceAll("[^0-9]", "")); 
            f.setTelefone(request.getParameter("txtTelefone").replaceAll("[^0-9]", ""));
            f.setIdSetor(Integer.parseInt(request.getParameter("txtIdSetor")));
            f.setNivel(request.getParameter("txtNivel"));
            f.setFuncao(request.getParameter("txtFuncao"));
            
            String salarioStr = request.getParameter("txtSalario");
            if (salarioStr != null) {
                salarioStr = salarioStr.replace("R$", "").replace(" ", "").replace(".", "").replace(",", ".");
                f.setSalarioBase(Double.parseDouble(salarioStr));
            }
            
            String dataStr = request.getParameter("txtDataAdmissao");
            if (dataStr != null && !dataStr.isEmpty()) {
                f.setDataAdmissao(new SimpleDateFormat("yyyy-MM-dd").parse(dataStr));
            }

            if ("true".equals(request.getParameter("chkReativar"))) {
                f.setDataDemissao(null);
            }

            e.setCep(request.getParameter("txtCep").replaceAll("[^0-9]", ""));
            e.setEstado(request.getParameter("txtEstado"));
            e.setCidade(request.getParameter("txtCidade"));
            e.setBairro(request.getParameter("txtBairro"));
            e.setLogradouro(request.getParameter("txtLogradouro"));
            e.setNumEndereco(request.getParameter("txtNumEndereco"));
            e.setComplemento(request.getParameter("txtComplemento"));

            endDao.atualizar(e);
            funcDao.atualizar(f);

            request.setAttribute("id", String.valueOf(f.getId())); 
            return new DetalharFuncionarioAction().executar(request, response);

        } catch (Exception ex) {
            request.setAttribute("erro", "Erro ao atualizar: " + ex.getMessage());
            return new EditarFuncionarioAction().executar(request, response);
        }
    }
}
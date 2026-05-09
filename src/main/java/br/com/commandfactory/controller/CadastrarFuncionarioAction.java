/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.commandfactory.controller;

import dao.DAOFactory;
import service.ServiceFactory;
import model.Funcionario;
import model.Endereco;
import model.Setor;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CadastrarFuncionarioAction implements ICommand {
    @Override
    public String executar(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            String dataStr = request.getParameter("txtDataAdmissao");
            Date dataAdmissao = new Date(); 
            if(dataStr != null && !dataStr.isEmpty()) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                dataAdmissao = sdf.parse(dataStr);
            }


            String nome = request.getParameter("txtNome");
            
            String cpf = request.getParameter("txtCpf").replaceAll("[^0-9]", "");
            String telefone = request.getParameter("txtTelefone").replaceAll("[^0-9]", "");
            
            String funcao = request.getParameter("txtFuncao");
            
            String salarioStr = request.getParameter("txtSalario");
            if (salarioStr != null && !salarioStr.trim().isEmpty()) {
                salarioStr = salarioStr.replaceAll("\\.", "").replace(",", ".");
            } else {
                salarioStr = "0.0";
            }
            Double salario = Double.valueOf(salarioStr);
            
            String nivel = request.getParameter("txtNivel");
            int idSetor = Integer.parseInt(request.getParameter("txtIdSetor"));

            String logradouro = request.getParameter("txtLogradouro");
            String bairro = request.getParameter("txtBairro");
            String cidade = request.getParameter("txtCidade");
            String estado = request.getParameter("txtEstado");
            String numEndereco = request.getParameter("txtNumEndereco");
            String complemento = request.getParameter("txtComplemento");
            
            String cep = request.getParameter("txtCep").replaceAll("[^0-9]", "");

            Funcionario novoFunc = Funcionario.getBuilder()
                    .comNome(nome)
                    .comCpf(cpf)
                    .comFuncao(funcao)
                    .comSalarioBase(salario)
                    .comTelefone(telefone)
                    .comNivel(nivel)
                    .comDataAdmissao(dataAdmissao)
                    .constroi();

            Endereco novoEnd = Endereco.getBuilder()
                    .comLogradouro(logradouro)
                    .comBairro(bairro)
                    .comCidade(cidade)
                    .comEstado(estado)
                    .comCep(cep)
                    .comNumEndereco(numEndereco)
                    .comComplemento(complemento)
                    .constroi();

            ServiceFactory.getFuncionarioService().cadastrar(novoFunc, novoEnd, idSetor);

            request.setAttribute("mensagem", "Funcionário contratado com sucesso! Matrícula: " + novoFunc.getMatricula());
            return "index.jsp";
            
        } catch (Exception e) {
            request.setAttribute("erro", "Erro ao contratar funcionário: " + e.getMessage());
            return new AbrirFormFuncionarioAction().executar(request, response);
        }
    }
}

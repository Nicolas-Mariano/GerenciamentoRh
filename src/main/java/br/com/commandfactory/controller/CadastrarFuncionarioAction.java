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
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;
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

            if (dataAdmissao.after(new Date())) {
                throw new Exception("Regra violada: A data de admissão não pode ser no futuro.");
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

            String anoAtual = String.valueOf(LocalDate.now().getYear());
            String matriculaGerada = anoAtual + "-" + UUID.randomUUID().toString().substring(0, 5).toUpperCase();

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
                    .comMatricula(matriculaGerada)
                    .comFuncao(funcao)
                    .comSalarioBase(salario)
                    .comTelefone(telefone)
                    .comNivel(nivel)
                    .comSetor(new SetorDAO().consultarById(idSetor))
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

            EnderecoDAO enderecoDAO = new EnderecoDAO();
            int idEnderecoGerado = enderecoDAO.cadastrarRetornandoId(novoEnd);
            novoEnd.setId(idEnderecoGerado);

            novoFunc.setEndereco(novoEnd);
            new FuncionarioDAO().cadastrar(novoFunc);

            request.setAttribute("mensagem", "Funcionário contratado com sucesso! Matrícula: " + matriculaGerada);
            return "index.jsp";
            
        } catch (Exception e) {
            request.setAttribute("erro", "Erro ao contratar funcionário: " + e.getMessage());
            return new AbrirFormFuncionarioAction().executar(request, response);
        }
    }
}
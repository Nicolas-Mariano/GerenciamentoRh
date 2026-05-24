package br.com.commandfactory.controller;

import model.Contrato;
import model.Endereco;
import model.Funcionario;
import model.NivelSenioridade;
import model.Setor;
import service.ServiceFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CadastrarFuncionarioAction implements ICommand {
    @Override
    public String executar(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            String nome = request.getParameter("txtNome");
            String cpfRaw = request.getParameter("txtCpf");
            String cpf = cpfRaw != null ? cpfRaw.replaceAll("[^0-9]", "") : "";
            String telefoneRaw = request.getParameter("txtTelefone");
            String telefone = telefoneRaw != null ? telefoneRaw.replaceAll("[^0-9]", "") : "";
            String email = request.getParameter("txtEmail");

            String logradouro = request.getParameter("txtLogradouro");
            String bairro = request.getParameter("txtBairro");
            String cidade = request.getParameter("txtCidade");
            String estado = request.getParameter("txtEstado");
            String numEndereco = request.getParameter("txtNumEndereco");
            String complemento = request.getParameter("txtComplemento");
            String cepRaw = request.getParameter("txtCep");
            String cep = cepRaw != null ? cepRaw.replaceAll("[^0-9]", "") : "";

            String dataStr = request.getParameter("txtDataAdmissao");
            Date dataAdmissao = new Date();
            if (dataStr != null && !dataStr.isEmpty()) {
                dataAdmissao = new SimpleDateFormat("yyyy-MM-dd").parse(dataStr);
            }

            String salarioStr = request.getParameter("txtSalario");
            if (salarioStr != null && !salarioStr.trim().isEmpty()) {
                salarioStr = salarioStr.replaceAll("\\.", "").replace(",", ".");
            } else {
                salarioStr = "0.0";
            }
            double salario = Double.parseDouble(salarioStr);

            String nivelStr = request.getParameter("txtNivel");
            if (nivelStr == null || nivelStr.isBlank()) {
                throw new Exception("Campo obrigatório ausente: nível de senioridade.");
            }
            NivelSenioridade nivel = NivelSenioridade.valueOf(nivelStr.toUpperCase());

            int idSetor = Integer.parseInt(request.getParameter("txtIdSetor"));

            Funcionario novoFunc = Funcionario.getBuilder()
                .comNome(nome).comCpf(cpf).comTelefone(telefone).comEmail(email)
                .constroi();

            Endereco novoEnd = Endereco.getBuilder()
                .comLogradouro(logradouro).comBairro(bairro).comCidade(cidade)
                .comEstado(estado).comCep(cep).comNumEndereco(numEndereco)
                .comComplemento(complemento).constroi();

            ServiceFactory.getFuncionarioService().cadastrar(novoFunc, novoEnd);

            Setor setor = new Setor();
            setor.setId(idSetor);

            Contrato contrato = Contrato.getBuilder()
                .comDataAdmissao(dataAdmissao)
                .comSalarioBase(salario)
                .comNivelSenioridade(nivel)
                .comSetor(setor)
                .constroi();

            ServiceFactory.getContratoService().contratar(novoFunc.getId(), contrato);

            request.setAttribute("mensagem", "Funcionário contratado com sucesso! Matrícula: " + contrato.getMatricula());
            return "index.jsp";

        } catch (Exception e) {
            request.setAttribute("erro", "Erro ao contratar funcionário: " + e.getMessage());
            return new AbrirFormFuncionarioAction().executar(request, response);
        }
    }
}

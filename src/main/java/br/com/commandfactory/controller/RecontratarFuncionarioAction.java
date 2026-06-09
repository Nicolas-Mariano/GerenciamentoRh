package br.com.commandfactory.controller;

import model.Contrato;
import model.Funcionario;
import model.NivelSenioridade;
import model.Setor;
import service.ServiceFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RecontratarFuncionarioAction implements ICommand {
    @Override
    public String executar(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String idStr = request.getParameter("id");
        int idFuncionario = Integer.parseInt(idStr);

        try {
            String dataStr = request.getParameter("txtDataAdmissao");
            Date dataAdmissao = new Date();
            if (dataStr != null && !dataStr.isEmpty()) {
                dataAdmissao = new SimpleDateFormat("yyyy-MM-dd").parse(dataStr);
            }

            String salarioStr = request.getParameter("txtSalario");
            if (salarioStr != null && !salarioStr.trim().isEmpty()) {
                salarioStr = salarioStr.replaceAll("[^\\d.,]", "");
                salarioStr = salarioStr.replace(".", "").replace(",", ".");
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

            Setor setor = new Setor();
            setor.setId(idSetor);

            Contrato contrato = Contrato.getBuilder()
                .comDataAdmissao(dataAdmissao)
                .comSalarioBase(salario)
                .comNivelSenioridade(nivel)
                .comSetor(setor)
                .constroi();

            Funcionario funcRef = new Funcionario();
            funcRef.setId(idFuncionario);
            ServiceFactory.getContratoService().recontratar(funcRef, contrato);

            request.setAttribute("mensagem", "Recontratação realizada com sucesso! Nova matrícula: " + contrato.getMatricula());
            request.setAttribute("id", String.valueOf(idFuncionario));
            return new DetalharFuncionarioAction().executar(request, response);

        } catch (Exception e) {
            request.setAttribute("erro", "Erro na recontratação: " + e.getMessage());
            request.setAttribute("id", String.valueOf(idFuncionario));

            request.setAttribute("salarioDigitado", request.getParameter("txtSalario"));
            request.setAttribute("dataAdmissaoDigitada", request.getParameter("txtDataAdmissao"));
            request.setAttribute("nivelSelecionado", request.getParameter("txtNivel"));
            request.setAttribute("idSetorSelecionado", request.getParameter("txtIdSetor"));

            return new AbrirFormRecontratacaoAction().executar(request, response);
        }
    }
}

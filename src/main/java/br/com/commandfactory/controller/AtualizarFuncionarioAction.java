package br.com.commandfactory.controller;

import dao.DAOFactory;
import model.Endereco;
import model.Funcionario;
import service.ServiceFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AtualizarFuncionarioAction implements ICommand {
    @Override
    public String executar(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            int idFuncionario = Integer.parseInt(request.getParameter("txtId"));
            int idEndereco = Integer.parseInt(request.getParameter("txtIdEndereco"));

            Funcionario f = DAOFactory.getFuncionarioDAO().consultarById(idFuncionario);
            Endereco e = DAOFactory.getEnderecoDAO().consultarById(idEndereco);

            f.setNome(request.getParameter("txtNome"));
            String cpfRaw = request.getParameter("txtCpf");
            f.setCpf(cpfRaw != null ? cpfRaw.replaceAll("[^0-9]", "") : "");
            String telefoneRaw = request.getParameter("txtTelefone");
            f.setTelefone(telefoneRaw != null ? telefoneRaw.replaceAll("[^0-9]", "") : "");
            f.setEmail(request.getParameter("txtEmail"));

            String cepRaw = request.getParameter("txtCep");
            e.setCep(cepRaw != null ? cepRaw.replaceAll("[^0-9]", "") : "");
            e.setEstado(request.getParameter("txtEstado"));
            e.setCidade(request.getParameter("txtCidade"));
            e.setBairro(request.getParameter("txtBairro"));
            e.setLogradouro(request.getParameter("txtLogradouro"));
            e.setNumEndereco(request.getParameter("txtNumEndereco"));
            e.setComplemento(request.getParameter("txtComplemento"));

            ServiceFactory.getFuncionarioService().atualizar(f, e);

            request.setAttribute("id", String.valueOf(idFuncionario));
            return new DetalharFuncionarioAction().executar(request, response);

        } catch (Exception ex) {
            request.setAttribute("erro", "Erro ao atualizar: " + ex.getMessage());

            String idStr = request.getParameter("txtId");
            String idEndStr = request.getParameter("txtIdEndereco");

            Funcionario formFunc = Funcionario.getBuilder()
                .comId(idStr != null && !idStr.isBlank() ? Integer.parseInt(idStr) : 0)
                .comNome(request.getParameter("txtNome"))
                .comCpf(request.getParameter("txtCpf"))
                .comTelefone(request.getParameter("txtTelefone"))
                .comEmail(request.getParameter("txtEmail"))
                .constroi();
            request.setAttribute("funcionario", formFunc);

            Endereco formEnd = Endereco.getBuilder()
                .comId(idEndStr != null && !idEndStr.isBlank() ? Integer.parseInt(idEndStr) : 0)
                .comCep(request.getParameter("txtCep"))
                .comEstado(request.getParameter("txtEstado"))
                .comCidade(request.getParameter("txtCidade"))
                .comBairro(request.getParameter("txtBairro"))
                .comLogradouro(request.getParameter("txtLogradouro"))
                .comNumEndereco(request.getParameter("txtNumEndereco"))
                .comComplemento(request.getParameter("txtComplemento"))
                .constroi();
            request.setAttribute("endereco", formEnd);

            request.setAttribute("setores", DAOFactory.getSetorDAO().consultarTodos());

            int idFunc = formFunc.getId();
            if (idFunc > 0) {
                model.Funcionario funcRef = new model.Funcionario();
                funcRef.setId(idFunc);
                model.Contrato contratoAtivo = ServiceFactory.getContratoService().buscarAtivo(funcRef);
                request.setAttribute("contratoAtivo", contratoAtivo);
            }

            return "formeditar_funcionario.jsp";
        }
    }
}

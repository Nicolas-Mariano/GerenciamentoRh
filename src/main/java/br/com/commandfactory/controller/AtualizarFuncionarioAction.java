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
            request.setAttribute("id", request.getParameter("txtId"));
            return new EditarFuncionarioAction().executar(request, response);
        }
    }
}

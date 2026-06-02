package br.com.commandfactory.controller;

import dao.SetorDAO;
import model.Setor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CadastrarSetorAction implements ICommand {

    @Override
    public String executar(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            String nomeSetor = request.getParameter("txtNome");
            
            if (nomeSetor == null || nomeSetor.trim().isEmpty()) {
                request.setAttribute("erro", "O nome do setor não pode ser vazio!");
                request.setAttribute("nomeSetor", nomeSetor);
                return "cadastro_setor.jsp"; // Retorna para o form em caso de erro
            }

            Setor novoSetor = Setor.getBuilder().comNome(nomeSetor).constroi();
            dao.DAOFactory.getSetorDAO().cadastrar(novoSetor);

            request.setAttribute("mensagem", "Setor '" + nomeSetor + "' cadastrado com sucesso!");
            return "index.jsp";
            
        } catch (Exception e) {
            request.setAttribute("erro", "Erro ao cadastrar setor: " + e.getMessage());
            request.setAttribute("nomeSetor", request.getParameter("txtNome"));
            return "cadastro_setor.jsp";
        }
    }
}

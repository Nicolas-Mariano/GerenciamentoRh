package controller;

import dao.SetorDAO;
import model.Setor;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import model.Funcionario;
import dao.FuncionarioDAO;
import java.time.LocalDate;
import java.math.BigDecimal;

@WebServlet("/controller.do")
public class ControllerServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String acao = request.getParameter("acao");
        SetorDAO dao = new SetorDAO(); // DAO instanciado para ser usado em qualquer ação

        try {
            if ("CadastrarSetor".equals(acao)) {
                String nomeSetor = request.getParameter("txtNome");

                if (nomeSetor == null || nomeSetor.trim().isEmpty()) {
                    throw new Exception("O nome do setor não pode ser vazio!");
                }

                Setor novoSetor = Setor.getBuilder().comNome(nomeSetor).constroi();
                dao.cadastrar(novoSetor);

                request.setAttribute("mensagem", "Setor '" + nomeSetor + "' cadastrado com sucesso!");
                request.getRequestDispatcher("index.jsp").forward(request, response);

            } else if ("ListarSetores".equals(acao)) {
                // 1. Busca a lista de setores usando o seu método consultarTodos()
                List<Setor> lista = dao.consultarTodos();

                // 2. Coloca a lista no "request" para o JSP conseguir ler
                request.setAttribute("setores", lista);

                // 3. Encaminha para o seu arquivo lista_setores.jsp
                request.getRequestDispatcher("lista_setores.jsp").forward(request, response);
            }
            else if ("AbrirFormFuncionario".equals(acao)) {
                // Antes de abrir a tela de cadastro, buscamos os setores para o <select>
                List<Setor> listaSetores = new SetorDAO().consultarTodos();
                request.setAttribute("setores", listaSetores);
                request.getRequestDispatcher("cadastro_funcionario.jsp").forward(request, response);

            } else if ("CadastrarFuncionario".equals(acao)) {
                // Captura dos dados (resumido, adicione os outros campos conforme necessário)
                String nome = request.getParameter("txtNome");
                String cpf = request.getParameter("txtCpf");
                String matricula = request.getParameter("txtMatricula");
                Double salario = new Double(request.getParameter("txtSalario"));
                int idSetor = Integer.parseInt(request.getParameter("txtIdSetor"));

                // Captura de endereço
                String logradouro = request.getParameter("txtLogradouro");
                String cidade = request.getParameter("txtCidade");

                // Aqui você usa o FuncionarioBuilder que vi no seu arquivo txt
                Funcionario novoFunc = Funcionario.getBuilder()
                        .comNome(nome)
                        .comCpf(cpf)
                        .comMatricula(matricula)
                        .comSalarioBase(salario)
                        .comIdSetor(idSetor) // Busca o objeto Setor completo // se você tiver o builder de endereço
                        .constroi();

                new FuncionarioDAO().cadastrar(novoFunc);

                request.setAttribute("mensagem", "Funcionário cadastrado com sucesso!");
                request.getRequestDispatcher("index.jsp").forward(request, response);
            }

        } catch (Exception e) {
            request.setAttribute("erro", e.getMessage());
            // Verifica para qual página voltar em caso de erro
            String paginaErro = "CadastrarSetor".equals(acao) ? "cadastro_setor.jsp" : "index.jsp";
            request.getRequestDispatcher(paginaErro).forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }
}
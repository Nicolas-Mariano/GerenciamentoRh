package command;

import dao.FuncionarioDAO;
import model.Funcionario;
import java.util.List;
import java.util.Scanner;

public class ListarFuncionariosCommand implements ComandoTerminal {
    private final FuncionarioDAO funcionarioDAO = new FuncionarioDAO();

    @Override
    public void executar(Scanner scanner) throws Exception {
        System.out.println("\n--- LISTA DE FUNCIONÁRIOS ---");
        List<Funcionario> lista = funcionarioDAO.consultarTodos();
        for (Funcionario f : lista) {
            System.out.printf("ID: %d | Nome: %s | Cargo: %s | Setor ID: %d\n",
                    f.getId(), f.getNome(), f.getFuncao(), f.getIdSetor());
        }
    }
}
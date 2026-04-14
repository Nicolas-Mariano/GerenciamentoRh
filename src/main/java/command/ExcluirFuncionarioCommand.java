package command;

import dao.FuncionarioDAO;
import model.Funcionario;
import java.util.Scanner;

public class ExcluirFuncionarioCommand implements ComandoTerminal {
    private final FuncionarioDAO funcionarioDAO = new FuncionarioDAO();

    @Override
    public void executar(Scanner scanner) throws Exception {
        System.out.println("\n-- OFFBOARDING: EXCLUIR FUNCIONÁRIO --");
        new ListarFuncionariosCommand().executar(scanner);
        System.out.print("ID do Funcionário a ser desligado: ");
        int id = Integer.parseInt(scanner.nextLine());

        Funcionario f = new Funcionario();
        f.setId(id);
        funcionarioDAO.deletar(f);
        System.out.println("✅ Funcionário e endereço removidos com sucesso.");
    }
}

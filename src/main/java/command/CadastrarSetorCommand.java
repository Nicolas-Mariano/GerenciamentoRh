package command;

import dao.SetorDAO;
import model.Setor;
import java.util.Scanner;

public class CadastrarSetorCommand implements ComandoTerminal {

    private final SetorDAO setorDAO = new SetorDAO();

    @Override
    public void executar(Scanner scanner) throws Exception {
        System.out.println("\n-- NOVO SETOR --");
        System.out.print("Nome do Setor: ");
        String nome = scanner.nextLine();

        Setor s = Setor.getBuilder().comNome(nome).constroi();
        setorDAO.cadastrar(s);

        System.out.println("✅ Setor cadastrado com sucesso! (Sem gerente no momento)");
    }
}

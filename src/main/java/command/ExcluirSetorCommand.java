package command;

import dao.SetorDAO;
import model.Setor;
import java.util.Scanner;

public class ExcluirSetorCommand implements ComandoTerminal {
    private final SetorDAO setorDAO = new SetorDAO();

    @Override
    public void executar(Scanner scanner) throws Exception {
        System.out.println("\n-- EXCLUIR SETOR --");
        new ListarSetoresCommand().executar(scanner);
        System.out.print("Digite o ID do Setor que deseja excluir: ");
        int idSetor = Integer.parseInt(scanner.nextLine());

        Setor s = new Setor();
        s.setId(idSetor);
        setorDAO.deletar(s);
        System.out.println("✅ Setor removido com sucesso.");
    }
}
package command;

import dao.SetorDAO;
import model.Setor;
import java.util.List;
import java.util.Scanner;

public class ListarSetoresCommand implements ComandoTerminal {

    private final SetorDAO setorDAO = new SetorDAO();

    @Override
    public void executar(Scanner scanner) throws Exception {
        System.out.println("\n--- LISTA DE SETORES ---");
        List<Setor> lista = setorDAO.consultarTodos();

        for (Setor s : lista) {
            String gerente = (s.getNomeResponsavel() != null) ? s.getNomeResponsavel() : "Sem Gerente";
            System.out.printf("ID: %d | Nome: %s | Gerente: %s\n", s.getId(), s.getNome(), gerente);
        }
    }
}
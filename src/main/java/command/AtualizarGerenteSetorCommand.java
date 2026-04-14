package command;

import dao.SetorDAO;
import model.Setor;
import java.util.Scanner;

public class AtualizarGerenteSetorCommand implements ComandoTerminal {

    private final SetorDAO setorDAO = new SetorDAO();

    @Override
    public void executar(Scanner scanner) throws Exception {
        System.out.println("\n-- DEFINIR GERENTE DO SETOR --");

        // Reutiliza o comando de listagem que você já criou [cite: 77, 78]
        new ListarSetoresCommand().executar(scanner);

        System.out.print("Digite o ID do Setor que deseja atualizar: ");
        int idSetor = Integer.parseInt(scanner.nextLine());

        // Por enquanto, listamos os IDs manualmente até você criar o ListarFuncionariosCommand
        System.out.print("Digite o ID do Funcionário que será o responsável: ");
        int idFuncionario = Integer.parseInt(scanner.nextLine());

        // Busca o setor no banco para garantir que ele existe [cite: 199-204]
        Setor setorNoBanco = setorDAO.consultarById(idSetor);

        if (setorNoBanco.getNome() != null) {
            // Atualiza o ID do responsável e salva [cite: 196-198]
            setorNoBanco.setIdFuncResponsavel(idFuncionario);
            setorDAO.atualizar(setorNoBanco);
            System.out.println("✅ Gerente vinculado ao setor com sucesso!");
        } else {
            System.out.println("❌ Setor não encontrado.");
        }
    }
}
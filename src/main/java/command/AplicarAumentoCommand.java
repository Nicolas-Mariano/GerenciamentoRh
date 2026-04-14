package command;
import service.FuncionarioService;
import java.util.Scanner;

public class AplicarAumentoCommand implements ComandoTerminal {

    private final FuncionarioService service = new FuncionarioService();

    @Override
    public void executar(Scanner scanner) throws Exception {
        System.out.println("\n-- APLICAÇÃO DE AUMENTO SALARIAL --");
        System.out.print("Digite o ID do Funcionário: ");
        int id = Integer.parseInt(scanner.nextLine());

        System.out.print("Qual o percentual de aumento? (Ex: 10.5): ");
        double percentual = Double.parseDouble(scanner.nextLine());

        // A tela (Command) apenas coleta os dados e manda para o Service validar!
        service.aplicarAumento(id, percentual);

        System.out.println("✅ Salário reajustado com sucesso!");
    }
}

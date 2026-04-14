package app;

import command.ComandoTerminal;
import factory.MenuFactory;
import java.util.Scanner;

public class MainTerminal {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        int opcao = -1;

        while (opcao != 0) {
            System.out.println("\n==================================================");
            System.out.println("  SISTEMA DE RH");
            System.out.println("==================================================");
            System.out.println("1. Novo Setor         | 2. Listar Setores");
            System.out.println("3. Vincular Gerente   | 4. Excluir Setor");
            System.out.println("--------------------------------------------------");
            System.out.println("5. Nova Contratação   | 6. Listar Funcionários");
            System.out.println("7. Desligar (Excluir) | 8. Listar Endereços");
            System.out.println("--------------------------------------------------");
            System.out.println("9. Aplicar Aumento Salarial");
            System.out.println("0. Sair");
            System.out.print("Escolha uma ação: ");

            try {
                opcao = Integer.parseInt(scanner.nextLine());
                if (opcao == 0) break;

                ComandoTerminal comando = MenuFactory.obterComando(opcao);
                if (comando != null) {
                    comando.executar(scanner);
                } else {
                    System.out.println("❌ Opção inválida.");
                }
            } catch (Exception e) {
                System.out.println("⚠️ Erro: " + e.getMessage());
            }
        }
        scanner.close();
        System.out.println("Sistema encerrado.");
    }
}
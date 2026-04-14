package command;
import java.util.Scanner;

public interface ComandoTerminal {
    // Todos os comandos receberão o scanner para poder perguntar coisas ao usuário
    void executar(Scanner scanner) throws Exception;
}
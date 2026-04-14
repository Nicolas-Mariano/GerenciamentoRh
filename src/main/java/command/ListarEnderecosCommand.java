package command;

import dao.EnderecoDAO;
import model.Endereco;
import java.util.List;
import java.util.Scanner;

public class ListarEnderecosCommand implements ComandoTerminal {
    private final EnderecoDAO enderecoDAO = new EnderecoDAO();

    @Override
    public void executar(Scanner scanner) throws Exception {
        System.out.println("\n--- LISTA DE ENDEREÇOS ---");
        List<Endereco> lista = enderecoDAO.consultarTodos();
        for (Endereco e : lista) {
            String dono = (e.getNomeFuncionario() != null) ? e.getNomeFuncionario() : "Sem morador vinculado";
            System.out.printf("ID: %d | Residente: %s | Rua: %s, %s - %s/%s\n",
                    e.getId(), dono, e.getLogradouro(), e.getNumEndereco(), e.getCidade(), e.getEstado());
        }
    }
}
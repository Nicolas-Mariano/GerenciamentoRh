package service;

import dao.FuncionarioDAO;
import dao.EnderecoDAO;
import model.Funcionario;
import model.Endereco;
import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

public class FuncionarioService {

    private final FuncionarioDAO funcionarioDAO = new FuncionarioDAO();
    private final EnderecoDAO enderecoDAO = new EnderecoDAO();

    // Método 1: Contratar (Regras do HU-004)
    public void contratar(Funcionario f, Endereco e) throws Exception {
        // Validação de negócio (AC 05 do docx)
        if (f.getDataAdmissao().after(new Date())) {
            throw new Exception("Regra violada: A data de admissão não pode ser no futuro.");
        }

        // Geração Automática de Matrícula (Ideia refinada do docx)
        String anoAtual = String.valueOf(LocalDate.now().getYear());
        String matriculaGerada = anoAtual + "-" + UUID.randomUUID().toString().substring(0, 5).toUpperCase();
        f.setMatricula(matriculaGerada);

        // HU-004 (AC 08): O sistema deve salvar o Endereço primeiro
        int idEnderecoGerado = enderecoDAO.cadastrarRetornandoId(e);
        f.setIdEndereco(idEnderecoGerado);

        funcionarioDAO.cadastrar(f);
    }

    // Método 2: Regra de Negócio - Aumentar Salário
    public void aplicarAumento(int idFuncionario, double percentualAumento) throws Exception {
        if (percentualAumento <= 0) {
            throw new Exception("O percentual de aumento deve ser maior que zero.");
        }

        Funcionario f = funcionarioDAO.consultarById(idFuncionario);
        if (f.getNome() == null) {
            throw new Exception("Funcionário não encontrado.");
        }

        double valorAumento = f.getSalarioBase() * (percentualAumento / 100);
        f.setSalarioBase(f.getSalarioBase() + valorAumento);

        funcionarioDAO.atualizar(f);
    }

    // Método 3: Regra de Negócio - Transferência de Setor
    public void transferirSetor(int idFuncionario, int novoIdSetor) throws Exception {
        Funcionario f = funcionarioDAO.consultarById(idFuncionario);
        if (f.getNome() == null) {
            throw new Exception("Funcionário não encontrado.");
        }
        if (f.getIdSetor() == novoIdSetor) {
            throw new Exception("O funcionário já pertence a este setor.");
        }

        f.setIdSetor(novoIdSetor);
        funcionarioDAO.atualizar(f);
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app;

import dao.EnderecoDAO;
import dao.FuncionarioDAO;
import dao.SetorDAO;
import model.Endereco;
import model.Funcionario;
import model.Setor;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class MainTerminal {

    private static final Scanner scanner = new Scanner(System.in);
    private static final SetorDAO setorDAO = new SetorDAO();
    private static final EnderecoDAO enderecoDAO = new EnderecoDAO();
    private static final FuncionarioDAO funcionarioDAO = new FuncionarioDAO();
    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    public static void main(String[] args) {
        int opcao = -1;

        System.out.println("==================================================");
        System.out.println("  SISTEMA DE RH - FLUXO DE GESTÃO INTEGRADA");
        System.out.println("==================================================");

        while (opcao != 0) {
            System.out.println("\n--- SETUP DA EMPRESA ---");
            System.out.println("1. Cadastrar Novo Setor");
            System.out.println("2. Listar Todos os Setores");
            System.out.println("3. Definir/Atualizar Gerente de um Setor");
            System.out.println("4. Excluir Setor");
            
            System.out.println("\n--- GESTÃO DE FUNCIONÁRIOS (RH) ---");
            System.out.println("5. Fluxo de Contratação (Novo Funcionário Completo)");
            System.out.println("6. Listar Todos os Funcionários");
            System.out.println("7. Excluir Funcionário (Offboarding)");
            
            System.out.println("\n--- GESTÃO DE ENDEREÇOS ---");
            System.out.println("8. Listar Todos os Endereços");
            
            System.out.println("\n0. Sair");
            System.out.print("Escolha uma ação: ");

            try {
                opcao = Integer.parseInt(scanner.nextLine());

                switch (opcao) {
                    case 1 -> cadastrarSetor();
                    case 2 -> listarSetores();
                    case 3 -> atualizarGerenteSetor();
                    case 4 -> excluirSetor();
                    case 5 -> fluxoContratacaoCompleto();
                    case 6 -> listarFuncionarios();
                    case 7 -> excluirFuncionario();
                    case 8 -> listarEnderecos();
                    case 0 -> System.out.println("Encerrando o sistema...");
                    default -> System.out.println("Opção inválida. Tente novamente.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Erro: Digite apenas números válidos.");
            } catch (Exception e) {
                System.out.println("Erro inesperado: " + e.getMessage());
            }
        }
        scanner.close();
    }

    // ==========================================
    // FASE 1: SETUP (SETORES)
    // ==========================================

    private static void cadastrarSetor() throws Exception {
        System.out.println("\n-- NOVO SETOR --");
        System.out.print("Nome do Setor: ");
        String nome = scanner.nextLine();

        Setor s = Setor.getBuilder().comNome(nome).constroi();
        setorDAO.cadastrar(s);
        System.out.println("✅ Setor cadastrado com sucesso! (Sem gerente no momento)");
    }

    private static void atualizarGerenteSetor() throws Exception {
        System.out.println("\n-- DEFINIR GERENTE DO SETOR --");
        listarSetores();
        System.out.print("Digite o ID do Setor que deseja atualizar: ");
        int idSetor = Integer.parseInt(scanner.nextLine());

        listarFuncionarios();
        System.out.print("Digite o ID do Funcionário que será o responsável: ");
        int idFuncionario = Integer.parseInt(scanner.nextLine());

        // Usando o método atualizado do DAO que recebe um int
        Setor setorNoBanco = setorDAO.consultarById(idSetor);

        if (setorNoBanco.getNome() != null) {
            setorNoBanco.setIdFuncResponsavel(idFuncionario);
            setorDAO.atualizar(setorNoBanco);
            System.out.println("✅ Gerente vinculado ao setor com sucesso!");
        } else {
            System.out.println("❌ Setor não encontrado.");
        }
    }

    private static void excluirSetor() {
        System.out.println("\n-- EXCLUIR SETOR --");
        try {
            listarSetores();
            System.out.print("Digite o ID do Setor que deseja excluir: ");
            int idSetor = Integer.parseInt(scanner.nextLine());
            
            Setor s = new Setor();
            s.setId(idSetor);
            
            // O DAO vai barrar automaticamente se houver funcionários
            setorDAO.deletar(s);
            System.out.println("✅ Setor excluído com sucesso!");
        } catch (Exception e) {
            System.out.println("❌ AÇÃO BLOQUEADA: " + e.getMessage());
        }
    }

    // ==========================================
    // FASE 2: ONBOARDING FLUIDO (FUNCIONÁRIOS)
    // ==========================================

    private static void fluxoContratacaoCompleto() throws Exception {
        System.out.println("\n-- FLUXO DE CONTRATAÇÃO: NOVO FUNCIONÁRIO --");
        System.out.println("Passo 1: Dados Corporativos e Pessoais");
        
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("CPF (11 dígitos, sem pontos): ");
        String cpf = scanner.nextLine();
        System.out.print("Matrícula: ");
        String matricula = scanner.nextLine();
        System.out.print("Função: ");
        String funcao = scanner.nextLine();
        System.out.print("Nível (Estagiário, Junior, Pleno, Senior): ");
        String nivel = scanner.nextLine();
        System.out.print("Salário Base (ex: 3500.50): ");
        double salario = Double.parseDouble(scanner.nextLine());
        System.out.print("Telefone (ex: 11999999999): ");
        String telefone = scanner.nextLine();
        System.out.print("Data de Admissão (dd/MM/yyyy): ");
        Date dataAdmissao = sdf.parse(scanner.nextLine());

        System.out.println("\nPasso 2: Vinculação de Setor");
        listarSetores();
        System.out.print("Digite o ID do Setor existente OU digite 0 para CRIAR UM NOVO SETOR agora: ");
        int idSetor = Integer.parseInt(scanner.nextLine());

        // Lógica para criar o setor no meio do fluxo sem perder os dados já digitados
        if (idSetor == 0) {
            System.out.print("Digite o nome do novo setor: ");
            String nomeSetor = scanner.nextLine();
            Setor novoSetor = Setor.getBuilder().comNome(nomeSetor).constroi();
            setorDAO.cadastrar(novoSetor);
            
            // Busca o setor recém-criado para pegar o ID dele
            List<Setor> busca = setorDAO.consultarByNome(nomeSetor);
            idSetor = busca.get(0).getId();
            System.out.println("➡️ Setor '" + nomeSetor + "' criado! ID " + idSetor + " vinculado ao funcionário.");
        }

        System.out.println("\nPasso 3: Dados de Residência (Endereço Obrigatório)");
        System.out.print("Logradouro (Rua/Av): ");
        String logradouro = scanner.nextLine();
        System.out.print("Número: ");
        String numero = scanner.nextLine();
        System.out.print("Complemento: ");
        String complemento = scanner.nextLine();
        System.out.print("Bairro: ");
        String bairro = scanner.nextLine();
        System.out.print("Cidade: ");
        String cidade = scanner.nextLine();
        System.out.print("Estado (Sigla, ex: SP): ");
        String estado = scanner.nextLine().toUpperCase();
        System.out.print("CEP (Somente números, 8 dígitos): ");
        String cep = scanner.nextLine();

        Endereco end = Endereco.getBuilder()
                .comLogradouro(logradouro).comNumEndereco(numero).comComplemento(complemento)
                .comBairro(bairro).comCidade(cidade).comEstado(estado).comCep(cep).constroi();

        // Salva o endereço e resgata a chave primária gerada
        int idEndereco = enderecoDAO.cadastrarRetornandoId(end);

        System.out.println("\nPasso 4: Finalizando Contratação...");
        Funcionario f = Funcionario.getBuilder()
                .comNome(nome).comCpf(cpf).comMatricula(matricula).comFuncao(funcao)
                .comNivel(nivel).comSalarioBase(salario).comTelefone(telefone)
                .comDataAdmissao(dataAdmissao)
                .comIdSetor(idSetor)          // ID do setor escolhido ou recém-criado
                .comIdEndereco(idEndereco)    // ID do endereço recém-salvo
                .constroi();

        funcionarioDAO.cadastrar(f);
        System.out.println("✅ Funcionário contratado e sistema atualizado com sucesso!");
    }

    private static void excluirFuncionario() {
        System.out.println("\n-- OFFBOARDING: EXCLUIR FUNCIONÁRIO --");
        try {
            listarFuncionarios();
            System.out.print("Digite o ID do Funcionário que será desligado: ");
            int idFunc = Integer.parseInt(scanner.nextLine());
            
            Funcionario f = new Funcionario();
            f.setId(idFunc);
            
            // O DAO fará a exclusão em cascata (Funcionario -> Endereco)
            funcionarioDAO.deletar(f);
            System.out.println("✅ Funcionário e seu respectivo endereço foram removidos do sistema.");
        } catch (Exception e) {
            System.out.println("❌ Erro ao excluir: " + e.getMessage());
        }
    }

    // ==========================================
    // MÉTODOS DE LISTAGEM (COM OS NOVOS JOINS)
    // ==========================================

    private static void listarSetores() throws Exception {
        System.out.println("\n--- LISTA DE SETORES ---");
        List<Setor> lista = setorDAO.consultarTodos();
        for (Setor s : lista) {
            String gerente = (s.getNomeResponsavel() != null) ? s.getNomeResponsavel() : "Sem Gerente";
            System.out.printf("ID: %d | Nome: %s | Gerente: %s\n", s.getId(), s.getNome(), gerente);
        }
    }

    private static void listarFuncionarios() throws Exception {
        System.out.println("\n--- LISTA DE FUNCIONÁRIOS ---");
        List<Funcionario> lista = funcionarioDAO.consultarTodos();
        for (Funcionario f : lista) {
            System.out.printf("ID: %d | Nome: %s | Cargo: %s | Setor(ID): %d\n", 
                f.getId(), f.getNome(), f.getFuncao(), f.getIdSetor());
        }
    }

    private static void listarEnderecos() throws Exception {
        System.out.println("\n--- LISTA DE ENDEREÇOS ---");
        List<Endereco> lista = enderecoDAO.consultarTodos();
        for (Endereco e : lista) {
            // Agora exibimos o nome do dono do endereço graças ao campo auxiliar
            String dono = (e.getNomeFuncionario() != null) ? e.getNomeFuncionario() : "Desconhecido";
            System.out.printf("ID: %d | Funcionário: %s | Rua: %s, %s - %s/%s\n", 
                e.getId(), dono, e.getLogradouro(), e.getNumEndereco(), e.getCidade(), e.getEstado());
        }
    }
}
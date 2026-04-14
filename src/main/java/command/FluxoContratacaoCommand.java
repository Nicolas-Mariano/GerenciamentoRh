package command;

import model.Endereco;
import model.Funcionario;
import model.Setor;
import service.FuncionarioService;
import dao.SetorDAO;
import java.text.SimpleDateFormat;
import java.util.Scanner;

public class FluxoContratacaoCommand implements ComandoTerminal {
    private final FuncionarioService service = new FuncionarioService();
    private final SetorDAO setorDAO = new SetorDAO();
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    @Override
    public void executar(Scanner scanner) throws Exception {
        System.out.println("\n-- FLUXO DE CONTRATAÇÃO UNIFICADO --");

        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("CPF (11 dígitos): ");
        String cpf = scanner.nextLine();
        System.out.print("Função: ");
        String funcao = scanner.nextLine();
        System.out.print("Nível (Estagiário, Junior, Pleno, Senior): ");
        String nivel = scanner.nextLine();
        System.out.print("Salário Base: ");
        double salario = Double.parseDouble(scanner.nextLine());
        System.out.print("Telefone: ");
        String telefone = scanner.nextLine();
        System.out.print("Data de Admissão (dd/MM/yyyy): ");
        java.util.Date dataAdmissao = sdf.parse(scanner.nextLine());

        new ListarSetoresCommand().executar(scanner);
        System.out.print("ID do Setor (ou 0 para criar novo): ");
        int idSetor = Integer.parseInt(scanner.nextLine());

        if (idSetor == 0) {
            System.out.print("Nome do novo setor: ");
            String nomeSetor = scanner.nextLine();
            Setor novoSetor = Setor.getBuilder().comNome(nomeSetor).constroi();
            setorDAO.cadastrar(novoSetor);
            idSetor = setorDAO.consultarByNome(nomeSetor).get(0).getId();
        }

        System.out.println("\n-- DADOS DE RESIDÊNCIA --");
        System.out.print("Rua: "); String rua = scanner.nextLine();
        System.out.print("Número: "); String num = scanner.nextLine();
        System.out.print("Bairro: "); String bairro = scanner.nextLine();
        System.out.print("Cidade: "); String cidade = scanner.nextLine();
        System.out.print("Estado (Sigla): "); String estado = scanner.nextLine();
        System.out.print("CEP: "); String cep = scanner.nextLine();

        Endereco end = Endereco.getBuilder()
                .comLogradouro(rua).comNumEndereco(num).comBairro(bairro)
                .comCidade(cidade).comEstado(estado).comCep(cep).constroi();

        Funcionario func = Funcionario.getBuilder()
                .comNome(nome).comCpf(cpf).comFuncao(funcao).comNivel(nivel)
                .comSalarioBase(salario).comTelefone(telefone).comDataAdmissao(dataAdmissao)
                .comIdSetor(idSetor).constroi();

        // O Service cuida da matrícula automática e da ordem de gravação
        service.contratar(func, end);
        System.out.println("✅ Funcionário e endereço registrados com sucesso!");
    }
}

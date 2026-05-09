package service;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;
import dao.DAOFactory;
import dao.IEnderecoDAO;
import dao.IFuncionarioDAO;
import model.Endereco;
import model.Funcionario;
import util.FabricaConexao;

public class FuncionarioServiceImpl implements IFuncionarioService {

    @Override
    public void cadastrar(Funcionario f, Endereco e, int idSetor) throws Exception {
        if (f.getDataAdmissao() != null && f.getDataAdmissao().after(new Date())) {
            throw new Exception("Regra violada: A data de admissão não pode ser no futuro.");
        }

        String anoAtual = String.valueOf(LocalDate.now().getYear());
        String matriculaGerada = anoAtual + "-" + UUID.randomUUID().toString().substring(0, 5).toUpperCase();
        f.setMatricula(matriculaGerada);

        f.setSetor(DAOFactory.getSetorDAO().consultarById(idSetor));

        Connection con = FabricaConexao.getConexaoPostgres();
        con.setAutoCommit(false);
        try {
            IEnderecoDAO enderecoDAO = DAOFactory.getEnderecoDAO();
            int idEndereco = enderecoDAO.cadastrarRetornandoId(con, e);
            e.setId(idEndereco);
            f.setEndereco(e);

            IFuncionarioDAO funcionarioDAO = DAOFactory.getFuncionarioDAO();
            funcionarioDAO.cadastrar(con, f);

            con.commit();
        } catch (Exception ex) {
            con.rollback();
            throw ex;
        } finally {
            con.setAutoCommit(true);
            con.close();
        }
    }

    @Override
    public void aplicarAumento(int idFuncionario, double percentual) throws Exception {
        if (percentual <= 0) {
            throw new Exception("O percentual deve ser maior que zero.");
        }
        IFuncionarioDAO dao = DAOFactory.getFuncionarioDAO();
        Funcionario f = dao.consultarById(idFuncionario);
        if (f.getId() == 0) {
            throw new Exception("Funcionário não encontrado.");
        }
        double novoSalario = f.getSalarioBase() * (1 + (percentual / 100));
        f.setSalarioBase(novoSalario);
        dao.atualizar(f);
    }

    @Override
    public void demitirFuncionario(int idFuncionario, Date dataDemissao) throws Exception {
        if (dataDemissao.after(new Date())) {
            throw new Exception("Regra Violada: A data de demissão não pode ser no futuro.");
        }
        DAOFactory.getFuncionarioDAO().registrarDemissao(idFuncionario, dataDemissao);
    }
}

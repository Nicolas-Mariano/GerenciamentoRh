package service;

import java.sql.Connection;
import dao.DAOFactory;
import dao.IEnderecoDAO;
import dao.IFuncionarioDAO;
import model.Endereco;
import model.Funcionario;
import util.FabricaConexao;

public class FuncionarioServiceImpl implements IFuncionarioService {

    @Override
    public void cadastrar(Funcionario f, Endereco e) throws Exception {
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
    public void atualizar(Funcionario f, Endereco e) throws Exception {
        DAOFactory.getEnderecoDAO().atualizar(e);
        DAOFactory.getFuncionarioDAO().atualizar(f);
    }
}

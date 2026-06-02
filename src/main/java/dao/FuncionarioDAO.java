package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import model.Endereco;
import model.Funcionario;
import util.FabricaConexao;

public class FuncionarioDAO implements IFuncionarioDAO {

    private static Connection getConexao() throws ClassNotFoundException, SQLException {
        return FabricaConexao.getConexaoPostgres();
    }

    @Override
    public void cadastrar(Connection con, Funcionario f) throws Exception {
        PreparedStatement comando = con.prepareStatement(
            "INSERT INTO funcionario (nome, cpf, telefone, email, id_endereco) VALUES (?, ?, ?, ?, ?)",
            Statement.RETURN_GENERATED_KEYS
        );
        preencherStatement(comando, f);
        comando.executeUpdate();
        ResultSet resultado = comando.getGeneratedKeys();
        if (resultado.next()) {
            f.setId(resultado.getInt(1));
        }
    }

    @Override
    public void cadastrar(Funcionario f) throws Exception {
        Connection conexao = getConexao();
        try {
            cadastrar(conexao, f);
        } finally {
            conexao.close();
        }
    }

    @Override
    public void deletar(Connection con, int idFuncionario) throws Exception {
        PreparedStatement comando = con.prepareStatement("DELETE FROM funcionario WHERE id = ?");
        comando.setInt(1, idFuncionario);
        comando.executeUpdate();
    }

    @Override
    public void deletar(int idFuncionario) throws Exception {
        Connection conexao = getConexao();
        try {
            deletar(conexao, idFuncionario);
        } finally {
            conexao.close();
        }
    }

    @Override
    public void deletar(Funcionario f) throws Exception {
        deletar(f.getId());
    }

    @Override
    public void atualizar(Funcionario f) throws Exception {
        Connection conexao = getConexao();
        PreparedStatement comando = conexao.prepareStatement(
            "UPDATE funcionario SET nome=?, cpf=?, telefone=?, email=?, id_endereco=? WHERE id=?"
        );
        preencherStatement(comando, f);
        comando.setInt(6, f.getId());
        comando.execute();
        conexao.close();
    }

    @Override
    public Funcionario consultarById(int id) throws Exception {
        Connection conexao = getConexao();
        PreparedStatement comando = conexao.prepareStatement("SELECT * FROM funcionario WHERE id = ?");
        comando.setInt(1, id);
        ResultSet resultado = comando.executeQuery();
        Funcionario func = null;
        if (resultado.next()) {
            func = popularFuncionario(resultado);
        }
        conexao.close();
        return func;
    }

    @Override
    public List<Funcionario> consultarTodos() throws Exception {
        Connection conexao = getConexao();
        PreparedStatement comando = conexao.prepareStatement("SELECT * FROM funcionario ORDER BY nome");
        ResultSet resultado = comando.executeQuery();
        List<Funcionario> lista = new ArrayList<>();
        while (resultado.next()) {
            lista.add(popularFuncionario(resultado));
        }
        conexao.close();
        return lista;
    }

    @Override
    public List<Funcionario> consultarAtivos() throws Exception {
        Connection conexao = getConexao();
        PreparedStatement comando = conexao.prepareStatement(
            "SELECT DISTINCT f.* FROM funcionario f " +
            "JOIN contrato c ON c.id_funcionario = f.id " +
            "WHERE c.data_demissao IS NULL ORDER BY f.nome"
        );
        ResultSet resultado = comando.executeQuery();
        List<Funcionario> lista = new ArrayList<>();
        while (resultado.next()) { lista.add(popularFuncionario(resultado)); }
        conexao.close();
        return lista;
    }

    @Override
    public List<Funcionario> consultarTodosOrdenados() throws Exception {
        Connection conexao = getConexao();
        PreparedStatement comando = conexao.prepareStatement(
            "SELECT f.* FROM funcionario f " +
            "ORDER BY " +
            "  CASE WHEN EXISTS (" +
            "    SELECT 1 FROM contrato c WHERE c.id_funcionario = f.id AND c.data_demissao IS NULL" +
            "  ) THEN 0 ELSE 1 END ASC, " +
            "  f.nome ASC"
        );
        ResultSet resultado = comando.executeQuery();
        List<Funcionario> lista = new ArrayList<>();
        while (resultado.next()) { lista.add(popularFuncionario(resultado)); }
        conexao.close();
        return lista;
    }

    private void preencherStatement(PreparedStatement cmd, Funcionario f) throws SQLException {
        cmd.setString(1, f.getNome());
        cmd.setString(2, f.getCpf());
        cmd.setString(3, f.getTelefone());
        cmd.setString(4, f.getEmail());
        if (f.getEndereco() != null && f.getEndereco().getId() > 0) {
            cmd.setInt(5, f.getEndereco().getId());
        } else {
            cmd.setNull(5, java.sql.Types.INTEGER);
        }
    }

    private Funcionario popularFuncionario(ResultSet rs) throws SQLException {
        Funcionario func = new Funcionario();
        func.setId(rs.getInt("id"));
        func.setNome(rs.getString("nome"));
        func.setCpf(rs.getString("cpf"));
        func.setTelefone(rs.getString("telefone"));
        func.setEmail(rs.getString("email"));
        int idEndereco = rs.getInt("id_endereco");
        if (!rs.wasNull()) {
            Endereco e = new Endereco();
            e.setId(idEndereco);
            func.setEndereco(e);
        }
        return func;
    }
}

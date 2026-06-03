package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import model.Contrato;
import model.Funcionario;
import model.NivelSenioridade;
import model.Setor;
import util.FabricaConexao;

public class ContratoDAO implements IContratoDAO {

    private static Connection getConexao() throws ClassNotFoundException, SQLException {
        return FabricaConexao.getConexaoPostgres();
    }

    @Override
    public void cadastrar(Connection con, Contrato c) throws Exception {
        PreparedStatement comando = con.prepareStatement(
            "INSERT INTO contrato (matricula, data_admissao, salario_base, nivel_senioridade, id_funcionario, id_setor) " +
            "VALUES (?, ?, ?, ?, ?, ?)",
            Statement.RETURN_GENERATED_KEYS
        );
        comando.setString(1, c.getMatricula());
        comando.setDate(2, new java.sql.Date(c.getDataAdmissao().getTime()));
        comando.setDouble(3, c.getSalarioBase());
        comando.setString(4, c.getNivelSenioridade().name());
        comando.setInt(5, c.getFuncionario().getId());
        comando.setInt(6, c.getSetor().getId());
        comando.executeUpdate();
        ResultSet resultado = comando.getGeneratedKeys();
        if (resultado.next()) {
            c.setId(resultado.getInt(1));
        }
    }

    @Override
    public void cadastrar(Contrato c) throws Exception {
        Connection conexao = getConexao();
        try {
            cadastrar(conexao, c);
        } finally {
            conexao.close();
        }
    }

    @Override
    public void atualizar(Contrato c) throws Exception {
        Connection conexao = getConexao();
        PreparedStatement comando = conexao.prepareStatement(
            "UPDATE contrato SET data_demissao = ?, motivo_desligamento = ?, salario_base = ? WHERE id = ?"
        );
        if (c.getDataDemissao() != null) {
            comando.setDate(1, new java.sql.Date(c.getDataDemissao().getTime()));
        } else {
            comando.setNull(1, java.sql.Types.DATE);
        }
        comando.setString(2, c.getMotivoDesligamento());
        comando.setDouble(3, c.getSalarioBase());
        comando.setInt(4, c.getId());
        comando.execute();
        conexao.close();
    }

    @Override
    public void atualizarPromocao(Contrato c) throws Exception {
        Connection conexao = getConexao();
        PreparedStatement comando = conexao.prepareStatement(
            "UPDATE contrato SET nivel_senioridade = ?, salario_base = ? WHERE id = ?"
        );
        comando.setString(1, c.getNivelSenioridade().name());
        comando.setDouble(2, c.getSalarioBase());
        comando.setInt(3, c.getId());
        comando.execute();
        conexao.close();
    }

    @Override
    public Contrato consultarById(int id) throws Exception {
        Connection conexao = getConexao();
        PreparedStatement comando = conexao.prepareStatement(
            "SELECT c.*, f.nome AS nome_funcionario, s.nome AS nome_setor " +
            "FROM contrato c " +
            "JOIN funcionario f ON c.id_funcionario = f.id " +
            "JOIN setor s ON c.id_setor = s.id " +
            "WHERE c.id = ?"
        );
        comando.setInt(1, id);
        ResultSet resultado = comando.executeQuery();
        Contrato contrato = null;
        if (resultado.next()) {
            contrato = popularContrato(resultado);
        }
        conexao.close();
        return contrato;
    }

    @Override
    public Contrato buscarAtivo(int idFuncionario) throws Exception {
        Connection conexao = getConexao();
        PreparedStatement comando = conexao.prepareStatement(
            "SELECT c.*, f.nome AS nome_funcionario, s.nome AS nome_setor " +
            "FROM contrato c " +
            "JOIN funcionario f ON c.id_funcionario = f.id " +
            "JOIN setor s ON c.id_setor = s.id " +
            "WHERE c.id_funcionario = ? AND c.data_demissao IS NULL"
        );
        comando.setInt(1, idFuncionario);
        ResultSet resultado = comando.executeQuery();
        Contrato contrato = null;
        if (resultado.next()) {
            contrato = popularContrato(resultado);
        }
        conexao.close();
        return contrato;
    }

    @Override
    public List<Contrato> buscarHistorico(int idFuncionario) throws Exception {
        Connection conexao = getConexao();
        PreparedStatement comando = conexao.prepareStatement(
            "SELECT c.*, f.nome AS nome_funcionario, s.nome AS nome_setor " +
            "FROM contrato c " +
            "JOIN funcionario f ON c.id_funcionario = f.id " +
            "JOIN setor s ON c.id_setor = s.id " +
            "WHERE c.id_funcionario = ? ORDER BY c.data_admissao DESC"
        );
        comando.setInt(1, idFuncionario);
        ResultSet resultado = comando.executeQuery();
        List<Contrato> lista = new ArrayList<>();
        while (resultado.next()) {
            lista.add(popularContrato(resultado));
        }
        conexao.close();
        return lista;
    }

    @Override
    public List<Contrato> buscarAtivosPorSetor(int idSetor) throws Exception {
        Connection conexao = getConexao();
        PreparedStatement comando = conexao.prepareStatement(
            "SELECT c.*, f.nome AS nome_funcionario, s.nome AS nome_setor " +
            "FROM contrato c " +
            "JOIN funcionario f ON c.id_funcionario = f.id " +
            "JOIN setor s ON c.id_setor = s.id " +
            "WHERE c.id_setor = ? AND c.data_demissao IS NULL"
        );
        comando.setInt(1, idSetor);
        ResultSet resultado = comando.executeQuery();
        List<Contrato> lista = new ArrayList<>();
        while (resultado.next()) {
            lista.add(popularContrato(resultado));
        }
        conexao.close();
        return lista;
    }

    @Override
    public Contrato buscarUltimoDemitido(int idFuncionario) throws Exception {
        Connection conexao = getConexao();
        PreparedStatement comando = conexao.prepareStatement(
            "SELECT c.*, f.nome AS nome_funcionario, s.nome AS nome_setor " +
            "FROM contrato c " +
            "JOIN funcionario f ON c.id_funcionario = f.id " +
            "JOIN setor s ON c.id_setor = s.id " +
            "WHERE c.id_funcionario = ? AND c.data_demissao IS NOT NULL " +
            "ORDER BY c.data_demissao DESC LIMIT 1"
        );
        comando.setInt(1, idFuncionario);
        ResultSet resultado = comando.executeQuery();
        Contrato contrato = null;
        if (resultado.next()) {
            contrato = popularContrato(resultado);
        }
        conexao.close();
        return contrato;
    }

    private Contrato popularContrato(ResultSet rs) throws SQLException {
        Funcionario func = new Funcionario();
        func.setId(rs.getInt("id_funcionario"));
        func.setNome(rs.getString("nome_funcionario"));

        Setor setor = new Setor();
        setor.setId(rs.getInt("id_setor"));
        setor.setNome(rs.getString("nome_setor"));

        return Contrato.getBuilder()
            .comId(rs.getInt("id"))
            .comMatricula(rs.getString("matricula"))
            .comDataAdmissao(rs.getDate("data_admissao"))
            .comDataDemissao(rs.getDate("data_demissao"))
            .comMotivoDesligamento(rs.getString("motivo_desligamento"))
            .comSalarioBase(rs.getDouble("salario_base"))
            .comNivelSenioridade(NivelSenioridade.valueOf(rs.getString("nivel_senioridade")))
            .comFuncionario(func)
            .comSetor(setor)
            .constroi();
    }
}

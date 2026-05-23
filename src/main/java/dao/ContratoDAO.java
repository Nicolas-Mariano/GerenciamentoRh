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
        PreparedStatement cmd = con.prepareStatement(
            "INSERT INTO contrato (matricula, data_admissao, salario_base, nivel_senioridade, id_funcionario, id_setor) " +
            "VALUES (?, ?, ?, ?, ?, ?)",
            Statement.RETURN_GENERATED_KEYS
        );
        cmd.setString(1, c.getMatricula());
        cmd.setDate(2, new java.sql.Date(c.getDataAdmissao().getTime()));
        cmd.setDouble(3, c.getSalarioBase());
        cmd.setString(4, c.getNivelSenioridade().name());
        cmd.setInt(5, c.getFuncionario().getId());
        cmd.setInt(6, c.getSetor().getId());
        cmd.executeUpdate();
        ResultSet rs = cmd.getGeneratedKeys();
        if (rs.next()) {
            c.setId(rs.getInt(1));
        }
    }

    @Override
    public void cadastrar(Contrato c) throws Exception {
        Connection con = getConexao();
        try {
            cadastrar(con, c);
        } finally {
            con.close();
        }
    }

    @Override
    public void atualizar(Contrato c) throws Exception {
        Connection con = getConexao();
        PreparedStatement cmd = con.prepareStatement(
            "UPDATE contrato SET data_demissao = ?, motivo_desligamento = ?, salario_base = ? WHERE id = ?"
        );
        if (c.getDataDemissao() != null) {
            cmd.setDate(1, new java.sql.Date(c.getDataDemissao().getTime()));
        } else {
            cmd.setNull(1, java.sql.Types.DATE);
        }
        cmd.setString(2, c.getMotivoDesligamento());
        cmd.setDouble(3, c.getSalarioBase());
        cmd.setInt(4, c.getId());
        cmd.execute();
        con.close();
    }

    @Override
    public Contrato consultarById(int id) throws Exception {
        Connection con = getConexao();
        PreparedStatement cmd = con.prepareStatement(
            "SELECT c.*, f.nome AS nome_funcionario, s.nome AS nome_setor " +
            "FROM contrato c " +
            "JOIN funcionario f ON c.id_funcionario = f.id " +
            "JOIN setor s ON c.id_setor = s.id " +
            "WHERE c.id = ?"
        );
        cmd.setInt(1, id);
        ResultSet rs = cmd.executeQuery();
        Contrato contrato = null;
        if (rs.next()) {
            contrato = popularContrato(rs);
        }
        con.close();
        return contrato;
    }

    @Override
    public Contrato buscarAtivo(int idFuncionario) throws Exception {
        Connection con = getConexao();
        PreparedStatement cmd = con.prepareStatement(
            "SELECT c.*, f.nome AS nome_funcionario, s.nome AS nome_setor " +
            "FROM contrato c " +
            "JOIN funcionario f ON c.id_funcionario = f.id " +
            "JOIN setor s ON c.id_setor = s.id " +
            "WHERE c.id_funcionario = ? AND c.data_demissao IS NULL"
        );
        cmd.setInt(1, idFuncionario);
        ResultSet rs = cmd.executeQuery();
        Contrato contrato = null;
        if (rs.next()) {
            contrato = popularContrato(rs);
        }
        con.close();
        return contrato;
    }

    @Override
    public List<Contrato> buscarHistorico(int idFuncionario) throws Exception {
        Connection con = getConexao();
        PreparedStatement cmd = con.prepareStatement(
            "SELECT c.*, f.nome AS nome_funcionario, s.nome AS nome_setor " +
            "FROM contrato c " +
            "JOIN funcionario f ON c.id_funcionario = f.id " +
            "JOIN setor s ON c.id_setor = s.id " +
            "WHERE c.id_funcionario = ? ORDER BY c.data_admissao DESC"
        );
        cmd.setInt(1, idFuncionario);
        ResultSet rs = cmd.executeQuery();
        List<Contrato> lista = new ArrayList<>();
        while (rs.next()) {
            lista.add(popularContrato(rs));
        }
        con.close();
        return lista;
    }

    @Override
    public List<Contrato> buscarAtivosPorSetor(int idSetor) throws Exception {
        Connection con = getConexao();
        PreparedStatement cmd = con.prepareStatement(
            "SELECT c.*, f.nome AS nome_funcionario, s.nome AS nome_setor " +
            "FROM contrato c " +
            "JOIN funcionario f ON c.id_funcionario = f.id " +
            "JOIN setor s ON c.id_setor = s.id " +
            "WHERE c.id_setor = ? AND c.data_demissao IS NULL"
        );
        cmd.setInt(1, idSetor);
        ResultSet rs = cmd.executeQuery();
        List<Contrato> lista = new ArrayList<>();
        while (rs.next()) {
            lista.add(popularContrato(rs));
        }
        con.close();
        return lista;
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

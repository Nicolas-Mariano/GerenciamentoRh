package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Contrato;
import model.Setor;
import util.FabricaConexao;

public class SetorDAO implements ISetorDAO {

    private static Connection getConexao() throws ClassNotFoundException, SQLException {
        return FabricaConexao.getConexaoPostgres();
    }

    @Override
    public void cadastrar(Setor setor) throws Exception {
        Connection con = getConexao();
        PreparedStatement cmd = con.prepareStatement(
            "INSERT INTO setor (nome, id_contrato_responsavel) VALUES (?, ?)"
        );
        cmd.setString(1, setor.getNome());
        if (setor.getContratoResponsavel() != null) {
            cmd.setObject(2, setor.getContratoResponsavel().getId(), java.sql.Types.INTEGER);
        } else {
            cmd.setObject(2, null, java.sql.Types.INTEGER);
        }
        cmd.execute();
        con.close();
    }

    @Override
    public void deletar(Setor setor) throws Exception {
        Connection con = getConexao();
        PreparedStatement cmdVerifica = con.prepareStatement(
            "SELECT COUNT(*) AS total FROM contrato WHERE id_setor = ? AND data_demissao IS NULL"
        );
        cmdVerifica.setInt(1, setor.getId());
        ResultSet rs = cmdVerifica.executeQuery();
        if (rs.next()) {
            int total = rs.getInt("total");
            if (total > 0) {
                con.close();
                throw new Exception("Exclusão negada: O setor possui " + total + " contrato(s) ativo(s).");
            }
        }
        PreparedStatement cmd = con.prepareStatement("DELETE FROM setor WHERE id = ?");
        cmd.setInt(1, setor.getId());
        cmd.execute();
        con.close();
    }

    @Override
    public void atualizar(Setor setor) throws Exception {
        Connection con = getConexao();
        PreparedStatement cmd = con.prepareStatement(
            "UPDATE setor SET nome = ?, id_contrato_responsavel = ? WHERE id = ?"
        );
        cmd.setString(1, setor.getNome());
        if (setor.getContratoResponsavel() != null) {
            cmd.setObject(2, setor.getContratoResponsavel().getId(), java.sql.Types.INTEGER);
        } else {
            cmd.setObject(2, null, java.sql.Types.INTEGER);
        }
        cmd.setInt(3, setor.getId());
        cmd.execute();
        con.close();
    }

    @Override
    public Setor consultarById(int id) throws Exception {
        Connection con = getConexao();
        PreparedStatement cmd = con.prepareStatement(
            "SELECT s.*, f.nome AS nome_responsavel " +
            "FROM setor s " +
            "LEFT JOIN contrato ct ON s.id_contrato_responsavel = ct.id AND ct.data_demissao IS NULL " +
            "LEFT JOIN funcionario f ON ct.id_funcionario = f.id " +
            "WHERE s.id = ?"
        );
        cmd.setInt(1, id);
        ResultSet rs = cmd.executeQuery();
        Setor s = null;
        if (rs.next()) {
            s = popularSetor(rs);
        }
        con.close();
        return s;
    }

    @Override
    public List<Setor> consultarTodos() throws Exception {
        Connection con = getConexao();
        PreparedStatement cmd = con.prepareStatement(
            "SELECT s.*, f.nome AS nome_responsavel " +
            "FROM setor s " +
            "LEFT JOIN contrato ct ON s.id_contrato_responsavel = ct.id AND ct.data_demissao IS NULL " +
            "LEFT JOIN funcionario f ON ct.id_funcionario = f.id " +
            "ORDER BY s.nome"
        );
        ResultSet rs = cmd.executeQuery();
        List<Setor> lista = new ArrayList<>();
        while (rs.next()) {
            lista.add(popularSetor(rs));
        }
        con.close();
        return lista;
    }

    private Setor popularSetor(ResultSet rs) throws SQLException {
        Setor s = new Setor();
        s.setId(rs.getInt("id"));
        s.setNome(rs.getString("nome"));
        s.setNomeResponsavel(rs.getString("nome_responsavel"));
        Integer idContrato = (Integer) rs.getObject("id_contrato_responsavel");
        if (idContrato != null) {
            Contrato ct = new Contrato();
            ct.setId(idContrato);
            s.setContratoResponsavel(ct);
        }
        return s;
    }
}

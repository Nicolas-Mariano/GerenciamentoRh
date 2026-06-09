package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Contrato;
import model.Funcionario;
import model.Setor;
import util.FabricaConexao;

public class SetorDAO implements ISetorDAO {

    private static Connection getConexao() throws ClassNotFoundException, SQLException {
        return FabricaConexao.getConexaoPostgres();
    }

    @Override
    public void cadastrar(Setor setor) throws Exception {
        Connection conexao = getConexao();
        PreparedStatement comando = conexao.prepareStatement(
            "INSERT INTO setor (nome, id_contrato_responsavel) VALUES (?, ?)"
        );
        comando.setString(1, setor.getNome());
        if (setor.getContratoResponsavel() != null) {
            comando.setObject(2, setor.getContratoResponsavel().getId(), java.sql.Types.INTEGER);
        } else {
            comando.setObject(2, null, java.sql.Types.INTEGER);
        }
        comando.execute();
        conexao.close();
    }

    @Override
    public void deletar(Setor setor) throws Exception {
        Connection conexao = getConexao();
        PreparedStatement comandoVerifica = conexao.prepareStatement(
            "SELECT COUNT(*) AS total FROM contrato WHERE id_setor = ? AND data_demissao IS NULL"
        );
        comandoVerifica.setInt(1, setor.getId());
        ResultSet resultado = comandoVerifica.executeQuery();
        if (resultado.next()) {
            int total = resultado.getInt("total");
            if (total > 0) {
                conexao.close();
                throw new Exception("Exclusão negada: O setor possui " + total + " contrato(s) ativo(s).");
            }
        }
        PreparedStatement comando = conexao.prepareStatement("DELETE FROM setor WHERE id = ?");
        comando.setInt(1, setor.getId());
        comando.execute();
        conexao.close();
    }

    @Override
    public void atualizar(Setor setor) throws Exception {
        Connection conexao = getConexao();
        PreparedStatement comando = conexao.prepareStatement(
            "UPDATE setor SET nome = ?, id_contrato_responsavel = ? WHERE id = ?"
        );
        comando.setString(1, setor.getNome());
        if (setor.getContratoResponsavel() != null) {
            comando.setObject(2, setor.getContratoResponsavel().getId(), java.sql.Types.INTEGER);
        } else {
            comando.setObject(2, null, java.sql.Types.INTEGER);
        }
        comando.setInt(3, setor.getId());
        comando.execute();
        conexao.close();
    }

    @Override
    public Setor consultarById(int id) throws Exception {
        Connection conexao = getConexao();
        PreparedStatement comando = conexao.prepareStatement(
            "SELECT s.*, ct.id_funcionario AS ct_id_func, f.nome AS nome_responsavel " +
            "FROM setor s " +
            "LEFT JOIN contrato ct ON s.id_contrato_responsavel = ct.id AND ct.data_demissao IS NULL " +
            "LEFT JOIN funcionario f ON ct.id_funcionario = f.id " +
            "WHERE s.id = ?"
        );
        comando.setInt(1, id);
        ResultSet resultado = comando.executeQuery();
        Setor setor = null;
        if (resultado.next()) {
            setor = popularSetor(resultado);
        }
        conexao.close();
        return setor;
    }

    @Override
    public List<Setor> consultarTodos() throws Exception {
        Connection conexao = getConexao();
        PreparedStatement comando = conexao.prepareStatement(
            "SELECT s.*, ct.id_funcionario AS ct_id_func, f.nome AS nome_responsavel " +
            "FROM setor s " +
            "LEFT JOIN contrato ct ON s.id_contrato_responsavel = ct.id AND ct.data_demissao IS NULL " +
            "LEFT JOIN funcionario f ON ct.id_funcionario = f.id " +
            "ORDER BY s.nome"
        );
        ResultSet resultado = comando.executeQuery();
        List<Setor> lista = new ArrayList<>();
        while (resultado.next()) {
            lista.add(popularSetor(resultado));
        }
        conexao.close();
        return lista;
    }

    private Setor popularSetor(ResultSet rs) throws SQLException {
        Setor setor = new Setor();
        setor.setId(rs.getInt("id"));
        setor.setNome(rs.getString("nome"));
        Integer idContrato = (Integer) rs.getObject("id_contrato_responsavel");
        if (idContrato != null) {
            int idFuncResp = rs.getInt("ct_id_func");
            if (!rs.wasNull()) {
                Funcionario resp = Funcionario.getBuilder()
                    .comId(idFuncResp)
                    .comNome(rs.getString("nome_responsavel"))
                    .constroi();
                Contrato ct = Contrato.getBuilder()
                    .comId(idContrato)
                    .comFuncionario(resp)
                    .constroi();
                setor.setContratoResponsavel(ct);
            }
        }
        return setor;
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Funcionario;
import model.Setor;
import util.FabricaConexao;

public class SetorDAO implements ISetorDAO {
    public static Connection getConexao() throws ClassNotFoundException, SQLException {
        return FabricaConexao.getConexaoPostgres();
    }

    public void cadastrar(Setor setor) throws Exception {
        Connection con = getConexao();
        PreparedStatement comando = con.prepareStatement(
            "INSERT INTO setor (nome, id_func_responsavel) VALUES (?, ?)"
        ); 
        comando.setString(1, setor.getNome());
        if (setor.getFuncResponsavel() != null) {
            comando.setObject(2, setor.getFuncResponsavel().getId(), java.sql.Types.INTEGER);
        } else {
            comando.setObject(2, null, java.sql.Types.INTEGER);
        }
        comando.execute();
        con.close();
    }

    public void deletar(Setor setor) throws Exception {
        Connection con = getConexao();
        PreparedStatement cmdVerifica = con.prepareStatement(
            "SELECT COUNT(*) AS total FROM funcionario WHERE id_setor = ?"
        );
        cmdVerifica.setInt(1, setor.getId());
        ResultSet rs = cmdVerifica.executeQuery();
        if (rs.next()) {
            int qtdFuncionarios = rs.getInt("total");
            if (qtdFuncionarios > 0) {
                con.close();
                throw new Exception("ExclusÃ£o negada: O setor possui " + qtdFuncionarios + " funcionÃ¡rio(s) vinculado(s)."); 
            }
        }
        PreparedStatement comando = con.prepareStatement("DELETE FROM setor WHERE id = ?");
        comando.setInt(1, setor.getId()); 
        comando.execute();
        con.close();
    }

    public void atualizar(Setor setor) throws Exception {
        Connection con = getConexao();
        PreparedStatement comando = con.prepareStatement(
            "UPDATE setor SET nome = ?, id_func_responsavel = ? WHERE id = ?"
        ); 
        comando.setString(1, setor.getNome());
        if (setor.getFuncResponsavel() != null) {
            comando.setObject(2, setor.getFuncResponsavel().getId(), java.sql.Types.INTEGER);
        } else {
            comando.setObject(2, null, java.sql.Types.INTEGER);
        }
        comando.setInt(3, setor.getId());
        comando.execute();
        con.close();
    }

    public Setor consultarById(int id) throws Exception {
        Connection con = getConexao();
        PreparedStatement comando = con.prepareStatement(
            "SELECT s.*, f.nome AS nome_responsavel " +
            "FROM setor s " +
            "LEFT JOIN funcionario f ON s.id_func_responsavel = f.id " +
            "WHERE s.id = ?"
        );
        comando.setInt(1, id);
        ResultSet rs = comando.executeQuery();
        Setor s = new Setor();
        if (rs.next()) {
            s.setId(rs.getInt("id"));
            s.setNome(rs.getString("nome"));
            Integer idFuncResp = (Integer) rs.getObject("id_func_responsavel");
            if (idFuncResp != null) {
                Funcionario func = new Funcionario();
                func.setId(idFuncResp);
                s.setFuncResponsavel(func);
            }
            s.setNomeResponsavel(rs.getString("nome_responsavel")); 
        }
        con.close();
        return s;
    }

    public List<Setor> consultarTodos() throws Exception {
        Connection con = getConexao();
        String sql = "SELECT s.*, f.nome as nome_responsavel " +
                     "FROM setor s " +
                     "LEFT JOIN funcionario f ON s.id_func_responsavel = f.id AND f.data_demissao IS NULL";
        PreparedStatement comando = con.prepareStatement(sql);
        ResultSet rs = comando.executeQuery();
        List<Setor> lista = new ArrayList<>();
        while (rs.next()) {
            Funcionario func = null;
            int idFuncResp = rs.getInt("id_func_responsavel");
            if (!rs.wasNull()) {
                func = new Funcionario();
                func.setId(idFuncResp);
            }
            Setor s = Setor.getBuilder()
                    .comId(rs.getInt("id"))
                    .comNome(rs.getString("nome"))
                    .comNomeResponsavel(rs.getString("nome_responsavel"))
                    .comFuncResponsavel(func)
                    .constroi(); 
            lista.add(s);
        }
        con.close();
        return lista;
    }
}
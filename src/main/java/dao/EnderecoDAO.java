/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import model.Endereco;
import util.FabricaConexao;

public class EnderecoDAO {
    public static Connection getConexao() throws ClassNotFoundException, SQLException {
        return FabricaConexao.getConexaoPostgres(); 
    }

    public int cadastrarRetornandoId(Endereco e) throws ClassNotFoundException, SQLException {
        Connection con = getConexao();
        PreparedStatement comando = con.prepareStatement(
            "INSERT INTO endereco (logradouro, bairro, cidade, estado, cep, num_endereco, complemento) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?)",
            Statement.RETURN_GENERATED_KEYS 
        );
        comando.setString(1, e.getLogradouro());
        comando.setString(2, e.getBairro());
        comando.setString(3, e.getCidade());
        comando.setString(4, e.getEstado());
        comando.setString(5, e.getCep());
        comando.setString(6, e.getNumEndereco());
        comando.setString(7, e.getComplemento());

        comando.executeUpdate();
        ResultSet rs = comando.getGeneratedKeys();
        int idGerado = -1;
        if (rs.next()) {
            idGerado = rs.getInt(1);
        }
        con.close();
        return idGerado;
    }

    public void atualizar(Endereco e) throws ClassNotFoundException, SQLException {
        Connection con = getConexao();
        PreparedStatement comando = con.prepareStatement(
            "UPDATE endereco SET logradouro=?, bairro=?, cidade=?, estado=?, cep=?, num_endereco=?, complemento=? WHERE id=?"
        );
        comando.setString(1, e.getLogradouro());
        comando.setString(2, e.getBairro());
        comando.setString(3, e.getCidade());
        comando.setString(4, e.getEstado());
        comando.setString(5, e.getCep());
        comando.setString(6, e.getNumEndereco());
        comando.setString(7, e.getComplemento());
        comando.setInt(8, e.getId()); 
        comando.execute();
        con.close();
    }

    public Endereco consultarById(int id) throws ClassNotFoundException, SQLException {
        Connection con = getConexao();
        PreparedStatement comando = con.prepareStatement(
            "SELECT e.*, f.nome AS nome_funcionario " +
            "FROM endereco e " +
            "LEFT JOIN funcionario f ON f.id_endereco = e.id " +
            "WHERE e.id = ?"
        ); 
        comando.setInt(1, id);
        ResultSet rs = comando.executeQuery();
        Endereco end = new Endereco();
        if (rs.next()) {
            popularEndereco(end, rs); 
        }
        con.close();
        return end;
    }

    public List<Endereco> consultarTodos() throws ClassNotFoundException, SQLException {
        Connection con = getConexao();
        String sql = "SELECT e.*, f.nome as nome_func, f.id as id_func " +
                     "FROM endereco e " +
                     "LEFT JOIN funcionario f ON f.id_endereco = e.id";
        PreparedStatement comando = con.prepareStatement(sql);
        ResultSet rs = comando.executeQuery();
        List<Endereco> lista = new ArrayList<>();
        while (rs.next()) {
            Endereco e = Endereco.getBuilder()
                    .comId(rs.getInt("id"))
                    .comLogradouro(rs.getString("logradouro"))
                    .comBairro(rs.getString("bairro"))
                    .comCidade(rs.getString("cidade"))
                    .comEstado(rs.getString("estado"))
                    .comCep(rs.getString("cep"))
                    .comNumEndereco(rs.getString("num_endereco"))
                    .comComplemento(rs.getString("complemento"))
                    .comNomeFuncionario(rs.getString("nome_func"))
                    .constroi(); 
            int idF = rs.getInt("id_func");
            if (!rs.wasNull()) e.setIdFuncionario(idF); 
            lista.add(e);
        }
        con.close();
        return lista;
    }

    private void popularEndereco(Endereco e, ResultSet rs) throws SQLException {
        e.setId(rs.getInt("id"));
        e.setLogradouro(rs.getString("logradouro"));
        e.setBairro(rs.getString("bairro"));
        e.setCidade(rs.getString("cidade"));
        e.setEstado(rs.getString("estado"));
        e.setCep(rs.getString("cep"));
        e.setNumEndereco(rs.getString("num_endereco"));
        e.setComplemento(rs.getString("complemento"));
        e.setNomeFuncionario(rs.getString("nome_funcionario"));
    }
}
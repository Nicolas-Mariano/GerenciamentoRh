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

/**
 * @author 55119
 */

/**
 *Classe responsável pelas operações de banco de dados da entidade Endereco.
 */
public class EnderecoDAO {
    public static Connection getConexao() throws ClassNotFoundException, SQLException {
        return FabricaConexao.getConexaoPostgres();
    }

    /**
     * MÉTODOS ESPECIAL: Cadastra o endereço e retorna o ID gerado pelo banco.
     * Isso é essencial para o fluxo fluido de cadastro, pois o Funcionário precisará deste ID.
     */
    public int cadastrarRetornandoId(Endereco e) throws ClassNotFoundException, SQLException {
        Connection con = getConexao();
        // A flag RETURN_GENERATED_KEYS avisa ao JDBC que queremos capturar a chave primária criada
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

        comando.executeUpdate(); // Usa executeUpdate para comandos de INSERT

        // Captura o ID gerado pelo PostgreSQL
        ResultSet rs = comando.getGeneratedKeys();
        int idGerado = -1;
        if (rs.next()) {
            // O PostgreSQL geralmente retorna a chave na primeira coluna (índice 1)
            idGerado = rs.getInt(1); 
        }

        con.close();
        return idGerado;
    }

    /**
     * Atualiza os dados de um endereço existente.
     */
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

    /**
     * Deleta um endereço de forma direta.
     * Cuidado: Só pode ser chamado se nenhum funcionário estiver usando este ID de endereço.
     */
    public void deletar(Endereco e) throws ClassNotFoundException, SQLException {
        Connection con = getConexao();
        PreparedStatement comando = con.prepareStatement("DELETE FROM endereco WHERE id = ?");
        comando.setInt(1, e.getId());
        comando.execute();
        con.close();
    }

    /**
     * Consulta um endereço pelo ID, trazendo também o nome do funcionário dono dele.
     */
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

    /**
     * Lista todos os endereços, mostrando o nome do funcionário associado.
     */
    public List<Endereco> consultarTodos() throws ClassNotFoundException, SQLException {
        Connection con = getConexao();
        PreparedStatement comando = con.prepareStatement(
            "SELECT e.*, f.nome AS nome_funcionario " +
            "FROM endereco e " +
            "LEFT JOIN funcionario f ON f.id_endereco = e.id"
        );
        ResultSet rs = comando.executeQuery();

        List<Endereco> lista = new ArrayList<>();
        while (rs.next()) {
            Endereco e = new Endereco();
            popularEndereco(e, rs);
            lista.add(e);
        }

        con.close();
        return lista;
    }

    /**
     * Busca endereços pelo nome da rua (logradouro).
     */
    public List<Endereco> consultarByLogradouro(String logradouro) throws ClassNotFoundException, SQLException {
        Connection con = getConexao();
        PreparedStatement comando = con.prepareStatement(
            "SELECT e.*, f.nome AS nome_funcionario " +
            "FROM endereco e " +
            "LEFT JOIN funcionario f ON f.id_endereco = e.id " +
            "WHERE e.logradouro ILIKE ?"
        );
        comando.setString(1, "%" + logradouro + "%");
        ResultSet rs = comando.executeQuery();

        List<Endereco> lista = new ArrayList<>();
        while (rs.next()) {
            Endereco e = new Endereco();
            popularEndereco(e, rs);
            lista.add(e);
        }

        con.close();
        return lista;
    }

    /**
     * Método auxiliar privado para evitar repetição de código ao ler o ResultSet.
     */
    private void popularEndereco(Endereco e, ResultSet rs) throws SQLException {
        e.setId(rs.getInt("id"));
        e.setLogradouro(rs.getString("logradouro"));
        e.setBairro(rs.getString("bairro"));
        e.setCidade(rs.getString("cidade"));
        e.setEstado(rs.getString("estado"));
        e.setCep(rs.getString("cep"));
        e.setNumEndereco(rs.getString("num_endereco"));
        e.setComplemento(rs.getString("complemento"));
        // Preenche o campo auxiliar criado na model
        e.setNomeFuncionario(rs.getString("nome_funcionario"));
    }
}
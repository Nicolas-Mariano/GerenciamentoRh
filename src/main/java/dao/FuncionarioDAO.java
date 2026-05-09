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
import model.Endereco;
import model.Funcionario;
import model.Setor;
import util.FabricaConexao;

public class FuncionarioDAO {
    public static Connection getConexao() throws ClassNotFoundException, SQLException {
        return FabricaConexao.getConexaoPostgres();
    }

    public void cadastrar(Funcionario f) throws ClassNotFoundException, SQLException {
        Connection con = getConexao();
        PreparedStatement comando = con.prepareStatement(
            "INSERT INTO funcionario " +
            "(nome, cpf, matricula, funcao, salario_base, data_admissao, data_demissao, telefone, nivel, id_setor, id_endereco) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
        );
        preencherStatement(comando, f);
        comando.execute();
        con.close();
    }

    public void deletar(int idFuncionario) throws ClassNotFoundException, SQLException {
        Connection con = getConexao();
        con.setAutoCommit(false); 
        
        try {
            PreparedStatement getEnd = con.prepareStatement("SELECT id_endereco FROM funcionario WHERE id = ?");
            getEnd.setInt(1, idFuncionario);
            ResultSet rs = getEnd.executeQuery();
            
            int idEndereco = -1;
            if (rs.next()) {
                idEndereco = rs.getInt("id_endereco");
            }

            PreparedStatement deleteFunc = con.prepareStatement("DELETE FROM funcionario WHERE id = ?");
            deleteFunc.setInt(1, idFuncionario);
            deleteFunc.executeUpdate();

            if (idEndereco != -1) {
                PreparedStatement deleteEnd = con.prepareStatement("DELETE FROM endereco WHERE id = ?");
                deleteEnd.setInt(1, idEndereco);
                deleteEnd.executeUpdate();
            }

            con.commit();
            
        } catch (SQLException ex) {
            con.rollback(); 
            throw ex;
        } finally {
            con.setAutoCommit(true);
            con.close();
        }
    }

 
    public void deletar(Funcionario f) throws ClassNotFoundException, SQLException {
        // Ele apenas extrai o número e joga para o método principal!
        this.deletar(f.getId()); 
    }

  
    public void registrarDemissao(int idFuncionario, java.util.Date dataDemissao) throws ClassNotFoundException, SQLException {
        Connection con = getConexao();
        PreparedStatement comando = con.prepareStatement(
            "UPDATE funcionario SET data_demissao = ? WHERE id = ?"
        );
        comando.setDate(1, new java.sql.Date(dataDemissao.getTime()));
        comando.setInt(2, idFuncionario);
        comando.execute();
        con.close();
    }

    public void atualizar(Funcionario f) throws ClassNotFoundException, SQLException {
        Connection con = getConexao();
        PreparedStatement comando = con.prepareStatement(
            "UPDATE funcionario SET " +
            "nome=?, cpf=?, matricula=?, funcao=?, salario_base=?, data_admissao=?, data_demissao=?, telefone=?, nivel=?, id_setor=?, id_endereco=? " +
            "WHERE id=?"
        );

        preencherStatement(comando, f);
        comando.setInt(12, f.getId()); 

        comando.execute();
        con.close();
    }


    public Funcionario consultarById(int id) throws ClassNotFoundException, SQLException {
        Connection con = getConexao();
        PreparedStatement comando = con.prepareStatement("SELECT * FROM funcionario WHERE id = ?");
        comando.setInt(1, id);
        ResultSet rs = comando.executeQuery();

        Funcionario func = new Funcionario();
        if (rs.next()) {
            popularFuncionario(func, rs);
        }

        con.close();
        return func;
    }
    
    public List<Funcionario> consultarTodos() throws ClassNotFoundException, SQLException {
        Connection con = getConexao();
        PreparedStatement comando = con.prepareStatement("SELECT * FROM funcionario");
        ResultSet rs = comando.executeQuery();

        List<Funcionario> lista = new ArrayList<>();
        while (rs.next()) {
            Funcionario f = new Funcionario();
            popularFuncionario(f, rs);
            lista.add(f);
        }

        con.close();
        return lista;
    }

    //MÉTODOS PRIVADOS AUXILIARES PARA EVITAR CÓDIGO REPETITIVO
    private void preencherStatement(PreparedStatement comando, Funcionario f) throws SQLException {
        comando.setString(1, f.getNome());
        comando.setString(2, f.getCpf());
        comando.setString(3, f.getMatricula());
        comando.setString(4, f.getFuncao());
        comando.setDouble(5, f.getSalarioBase());
        comando.setDate(6, new java.sql.Date(f.getDataAdmissao().getTime()));

        if (f.getDataDemissao() != null) {
            comando.setDate(7, new java.sql.Date(f.getDataDemissao().getTime()));
        } else {
            comando.setNull(7, java.sql.Types.DATE);
        }

        comando.setString(8, f.getTelefone());
        comando.setString(9, f.getNivel());
        if (f.getSetor() != null) {
            comando.setInt(10, f.getSetor().getId());
        } else {
            comando.setNull(10, java.sql.Types.INTEGER);
        }

        if (f.getEndereco() != null) {
            comando.setInt(11, f.getEndereco().getId());
        } else {
            comando.setNull(11, java.sql.Types.INTEGER);
        }
    }

    private void popularFuncionario(Funcionario func, ResultSet rs) throws SQLException {
        func.setId(rs.getInt("id"));
        func.setNome(rs.getString("nome"));
        func.setCpf(rs.getString("cpf"));
        func.setMatricula(rs.getString("matricula"));
        func.setFuncao(rs.getString("funcao"));
        func.setSalarioBase(rs.getDouble("salario_base"));
        func.setDataAdmissao(rs.getDate("data_admissao"));
        func.setDataDemissao(rs.getDate("data_demissao"));
        func.setTelefone(rs.getString("telefone"));
        func.setNivel(rs.getString("nivel"));
        int idSetor = rs.getInt("id_setor");
        if (!rs.wasNull()) {
            Setor s = new Setor();
            s.setId(idSetor);
            func.setSetor(s);
        }

        int idEndereco = rs.getInt("id_endereco");
        if (!rs.wasNull()) {
            Endereco e = new Endereco();
            e.setId(idEndereco);
            func.setEndereco(e);
        }
    }
}
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
import util.FabricaConexao;

/**
 * @author 55119
 */

/**
 * Classe responsável pelas operações de banco de dados da entidade Funcionario.
 */
public class FuncionarioDAO {
    public static Connection getConexao() throws ClassNotFoundException, SQLException {
        return FabricaConexao.getConexaoPostgres();
    }

    /**
     * Cadastra apenas os dados do funcionário.
     * Presume-se que o id_endereco e id_setor já foram gerados/selecionados e inseridos no objeto Funcionario.
     */
    public void cadastrar(Funcionario f) throws ClassNotFoundException, SQLException {
        Connection con = getConexao();
        PreparedStatement comando = con.prepareStatement(
            "INSERT INTO funcionario " +
            "(nome, cpf, matricula, funcao, salario_base, data_admissao, data_demissao, telefone, nivel, id_setor, id_endereco) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
        );
        
        preencherStatement(comando, f); // Método auxiliar chamado para preencher os dados
        comando.execute();
        con.close();
    }

    /**
     * DELEÇÃO EM CASCATA COM TRANSAÇÃO DE BANCO.
     * Deleta o funcionário e, em seguida, deleta o endereço dele para não deixar lixo no banco.
     * Se falhar em qualquer etapa, faz o ROLLBACK (desfaz tudo).
     */
    public void deletar(Funcionario f) throws ClassNotFoundException, SQLException {
        Connection con = getConexao();
        // Desliga o commit automático para garantirmos a transação
        con.setAutoCommit(false); 
        
        try {
            // 1. Precisamos pegar o ID do endereço do funcionário antes de apagá-lo
            PreparedStatement getEnd = con.prepareStatement("SELECT id_endereco FROM funcionario WHERE id = ?");
            getEnd.setInt(1, f.getId());
            ResultSet rs = getEnd.executeQuery();
            
            int idEndereco = -1;
            if (rs.next()) {
                idEndereco = rs.getInt("id_endereco");
            }

            // 2. Deleta o Funcionario primeiro (pois ele depende do endereço)
            PreparedStatement deleteFunc = con.prepareStatement("DELETE FROM funcionario WHERE id = ?");
            deleteFunc.setInt(1, f.getId());
            deleteFunc.executeUpdate();

            // 3. Deleta o Endereco (agora que ele está livre)
            if (idEndereco != -1) {
                PreparedStatement deleteEnd = con.prepareStatement("DELETE FROM endereco WHERE id = ?");
                deleteEnd.setInt(1, idEndereco);
                deleteEnd.executeUpdate();
            }

            // Se tudo deu certo, comita as alterações no banco
            con.commit();
            
        } catch (SQLException ex) {
            // Se deu QUALQUER erro, desfaz tudo que tentou apagar
            con.rollback(); 
            throw ex; // Repassa o erro para a tela
        } finally {
            con.setAutoCommit(true); // Restaura o comportamento padrão
            con.close();
        }
    }

    /**
     * Atualiza os dados completos de um funcionário.
     * Permite a troca do id_setor de forma fluida.
     */
    public void atualizar(Funcionario f) throws ClassNotFoundException, SQLException {
        Connection con = getConexao();
        PreparedStatement comando = con.prepareStatement(
            "UPDATE funcionario SET " +
            "nome=?, cpf=?, matricula=?, funcao=?, salario_base=?, data_admissao=?, data_demissao=?, telefone=?, nivel=?, id_setor=?, id_endereco=? " +
            "WHERE id=?"
        );

        preencherStatement(comando, f);
        comando.setInt(12, f.getId()); // O ID vai no último '?' do UPDATE

        comando.execute();
        con.close();
    }

    /**
     * Consulta um funcionário pelo seu ID.
     */
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
    
    /**
     * Busca funcionários pelo nome (útil para pesquisa no Front-end).
     */
    public List<Funcionario> consultarByNome(String nomeBusca) throws ClassNotFoundException, SQLException {
        Connection con = getConexao();
        PreparedStatement comando = con.prepareStatement("SELECT * FROM funcionario WHERE nome ILIKE ?");
        comando.setString(1, "%" + nomeBusca + "%");
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

    /**
     * Lista todos os funcionários registrados no banco.
     */
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

    // ==============================================================
    // MÉTODOS PRIVADOS AUXILIARES PARA EVITAR CÓDIGO REPETITIVO
    // ==============================================================

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
        comando.setInt(10, f.getIdSetor());
        comando.setInt(11, f.getIdEndereco());
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
        func.setIdSetor(rs.getInt("id_setor"));
        func.setIdEndereco(rs.getInt("id_endereco"));
    }
}
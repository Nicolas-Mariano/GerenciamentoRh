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
import model.Setor;
import util.FabricaConexao;

/**
 * @author 55119
 */

/**
 * Classe responsável pelas operações de banco de dados da entidade Setor.
 */
public class SetorDAO {
    public static Connection getConexao() throws ClassNotFoundException, SQLException {
        return FabricaConexao.getConexaoPostgres();
    }

    /**
     * Cadastra um novo setor no banco.
     * O idFuncResponsavel pode ser nulo caso o setor ainda não tenha um gerente.
     */
    public void cadastrar(Setor setor) throws ClassNotFoundException, SQLException {
        Connection con = getConexao();
        PreparedStatement comando = con.prepareStatement(
            "INSERT INTO setor (nome, id_func_responsavel) VALUES (?, ?)"
        );
        comando.setString(1, setor.getNome());
        comando.setObject(2, setor.getIdFuncResponsavel(), java.sql.Types.INTEGER);
        comando.execute();
        con.close();
    }

    /**
     * Tenta deletar um setor, mas aplica a REGRA DE NEGÓCIO:
     * Só permite a exclusão se não houver NENHUM funcionário vinculado a ele.
     */
    public void deletar(Setor setor) throws Exception {
        Connection con = getConexao();
        
        // 1. Verifica quantos funcionários pertencem a este setor
        PreparedStatement cmdVerifica = con.prepareStatement(
            "SELECT COUNT(*) AS total FROM funcionario WHERE id_setor = ?"
        );
        cmdVerifica.setInt(1, setor.getId());
        ResultSet rs = cmdVerifica.executeQuery();
        
        if (rs.next()) {
            int qtdFuncionarios = rs.getInt("total");
            // Se tiver funcionário, barra a exclusão lançando uma exceção
            if (qtdFuncionarios > 0) {
                con.close();
                throw new Exception("Exclusão negada: O setor possui " + qtdFuncionarios + " funcionário(s) vinculado(s).");
            }
        }

        // 2. Se chegou aqui, é porque o setor está vazio. Pode deletar.
        PreparedStatement comando = con.prepareStatement("DELETE FROM setor WHERE id = ?");
        comando.setInt(1, setor.getId());
        comando.execute();
        
        con.close();
    }

    /**
     * Atualiza os dados do Setor. 
     * Usado principalmente para definir ou trocar o gerente (id_func_responsavel).
     */
    public void atualizar(Setor setor) throws ClassNotFoundException, SQLException {
        Connection con = getConexao();
        PreparedStatement comando = con.prepareStatement(
            "UPDATE setor SET nome = ?, id_func_responsavel = ? WHERE id = ?"
        );
        comando.setString(1, setor.getNome());
        comando.setObject(2, setor.getIdFuncResponsavel(), java.sql.Types.INTEGER);
        comando.setInt(3, setor.getId());
        comando.execute();
        con.close();
    }

    /**
     * Consulta um setor pelo ID.
     * Usa LEFT JOIN para já trazer o nome do gerente (campo auxiliar).
     */
    public Setor consultarById(int id) throws ClassNotFoundException, SQLException {
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
            s.setIdFuncResponsavel((Integer) rs.getObject("id_func_responsavel"));
            s.setNomeResponsavel(rs.getString("nome_responsavel"));
        }
        con.close();
        return s;
    }

    /**
     * Busca setores por nome (pesquisa aproximada).
     */
    public List<Setor> consultarByNome(String nomeBusca) throws ClassNotFoundException, SQLException {
        Connection con = getConexao();
        PreparedStatement comando = con.prepareStatement(
            "SELECT s.*, f.nome AS nome_responsavel " +
            "FROM setor s " +
            "LEFT JOIN funcionario f ON s.id_func_responsavel = f.id " +
            "WHERE s.nome ILIKE ?" // ILIKE no Postgres ignora maiúsculas/minúsculas
        );
        comando.setString(1, "%" + nomeBusca + "%");
        ResultSet rs = comando.executeQuery();

        List<Setor> lista = new ArrayList<>();
        while (rs.next()) {
            Setor s = new Setor();
            s.setId(rs.getInt("id"));
            s.setNome(rs.getString("nome"));
            s.setIdFuncResponsavel((Integer) rs.getObject("id_func_responsavel"));
            s.setNomeResponsavel(rs.getString("nome_responsavel"));
            lista.add(s);
        }
        con.close();
        return lista;
    }

    /**
     * Lista todos os setores, incluindo o nome dos seus respectivos responsáveis.
     */
    public List<Setor> consultarTodos() throws ClassNotFoundException, SQLException {
        Connection con = getConexao();
        PreparedStatement comando = con.prepareStatement(
            "SELECT s.*, f.nome AS nome_responsavel " +
            "FROM setor s " +
            "LEFT JOIN funcionario f ON s.id_func_responsavel = f.id"
        );
        ResultSet rs = comando.executeQuery();

        List<Setor> lista = new ArrayList<>();
        while (rs.next()) {
            Setor s = new Setor();
            s.setId(rs.getInt("id"));
            s.setNome(rs.getString("nome"));
            s.setIdFuncResponsavel((Integer) rs.getObject("id_func_responsavel"));
            s.setNomeResponsavel(rs.getString("nome_responsavel"));
            lista.add(s);
        }
        con.close();
        return lista;
    }
}
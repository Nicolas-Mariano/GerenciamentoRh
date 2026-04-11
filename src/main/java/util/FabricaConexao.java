/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author 55119
 */
public class FabricaConexao {
    public static Connection getConexaoPostgres() throws ClassNotFoundException, SQLException {
        //Postgres
        // O método forName carrega e inicia o driver passado por parâmetro
        Class.forName("org.postgresql.Driver");
        String URL = "jdbc:postgresql://localhost:5432/gerenciamento_rh";
        String USER = "postgres";
        String PASSWORD = "Nico2005";
        // Estabelecendo a conexão
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}

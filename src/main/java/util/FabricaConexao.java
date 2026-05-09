/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class FabricaConexao {
    public static Connection getConexaoPostgres() throws ClassNotFoundException, SQLException {
        Class.forName("org.postgresql.Driver");
        String URL = "jdbc:postgresql://localhost:5433/gerenciamento_rh";
        String USER = "postgres";
        String PASSWORD = "Nico2005";
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}

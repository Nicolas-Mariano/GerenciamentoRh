package util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class FabricaConexao {

    private static final Properties props = carregarPropriedades();

    private static Properties carregarPropriedades() {
        Properties p = new Properties();
        try (InputStream in = FabricaConexao.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (in == null) {
                throw new RuntimeException("Arquivo db.properties não encontrado no classpath. " +
                        "Copie db.properties.example para db.properties e configure suas credenciais.");
            }
            p.load(in);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao carregar db.properties", e);
        }
        return p;
    }

    public static Connection getConexaoPostgres() throws ClassNotFoundException, SQLException {
        Class.forName("org.postgresql.Driver");
        return DriverManager.getConnection(
                props.getProperty("db.url"),
                props.getProperty("db.user"),
                props.getProperty("db.password")
        );
    }
}

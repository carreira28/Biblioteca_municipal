import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String URL = "jdbc:postgresql://localhost:5432/bibliotecaMunicipal(v2)";
    private static final String USER = "postgres";
    private static final String PASSWORD = "R1M2V3C4";

    private static Connection ligacao = null;

    private DatabaseConnection() {}

    public static Connection getConnection() throws SQLException {
        if (ligacao == null || ligacao.isClosed()) {
            ligacao = DriverManager.getConnection(URL, USER, PASSWORD);
        }
        return ligacao;
    }

    public static void fechar() {
        try {
            if (ligacao != null && !ligacao.isClosed()) {
                ligacao.close();
                System.out.println("Ligação à base de dados encerrada.");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao fechar ligação: " + e.getMessage());
        }
    }
}
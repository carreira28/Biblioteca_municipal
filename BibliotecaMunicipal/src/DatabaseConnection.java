import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String URL      = "jdbc:postgresql://localhost:5432/bibliotecaMunicipal";
    private static final String USER     = "postgres";
    private static final String PASSWORD = "R1M2V3C4";

    private static Connection instancia = null;

    private DatabaseConnection() {}

    public static Connection getConnection() throws SQLException {
        if (instancia == null || instancia.isClosed()) {
            instancia = DriverManager.getConnection(URL, USER, PASSWORD);
        }
        return instancia;
    }

    public static void fechar() {
        try {
            if (instancia != null && !instancia.isClosed()) {
                instancia.close();
                System.out.println("Ligação à base de dados encerrada.");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao fechar ligação: " + e.getMessage());
        }
    }
}
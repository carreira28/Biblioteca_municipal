import java.sql.*;

public class Main {

    public static void main(String[] args) {

        String url        = "jdbc:postgresql://localhost:5432/bibliotecaMunicipal(v2)";
        String utilizador = "postgres";
        String password   = "R1M2V3C4";

        String sql = """
        SELECT l.id_livro, l.isbn, l.titulo, l.stock, l.ano_publicacao, c.tipo_categoria
        FROM livros l
        INNER JOIN categorias c ON l.id_categoria = c.id_categoria
        """;

        try (Connection conn = DriverManager.getConnection(url, utilizador, password);
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            System.out.printf("%-10s %-20s %-50s %-8s %-6s %-15s%n",
                    "ID", "ISBN", "Título", "Stock", "Ano", "Categoria");
            System.out.println("-".repeat(110));

            while (rs.next()) {
                int id = rs.getInt("id_livro");
                String isbn = rs.getString("isbn");
                String titulo = rs.getString("titulo");
                int stock = rs.getInt("stock");
                int ano = rs.getInt("ano_publicacao");
                String categoria = rs.getString("tipo_categoria");

                System.out.printf("%-10d %-20s %-50s %-8d %-6d %-15s%n",
                        id, isbn, titulo, stock, ano, categoria);
            }

        } catch (SQLException e) {
            System.err.println("Erro SQL: " + e.getMessage());
        }
    }
}
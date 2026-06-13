import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AutorService {

    public List<Autor> listarTodos() throws SQLException {
        String sql = "SELECT id_autor, nome, data_nas FROM autor ORDER BY nome";
        List<Autor> lista = new ArrayList<>();
        try (PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Autor autor = mapear(rs);
                lista.add(autor);
            }
        }
        return lista;
    }

    public List<Autor> listarPorLivro(int idLivro) throws SQLException {
        String sql = """
                SELECT a.id_autor, a.nome, a.data_nas
                FROM autor a
                INNER JOIN autor_livro al ON a.id_autor = al.id_autor
                WHERE al.id_livro = ?
                ORDER BY a.nome
                """;
        List<Autor> lista = new ArrayList<>();
        try (PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, idLivro);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Autor autor = mapear(rs);
                    lista.add(autor);
                }
            }
        }
        return lista;
    }

    public List<Autor> pesquisarPorNome(String termo) throws SQLException {
        String sql = "SELECT id_autor, nome, data_nas FROM autor WHERE LOWER(nome) LIKE LOWER(?) ORDER BY nome";
        List<Autor> lista = new ArrayList<>();
        try (PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            stmt.setString(1, "%" + termo + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Autor autor = mapear(rs);
                    lista.add(autor);
                }
            }
        }
        return lista;
    }


    private Autor mapear(ResultSet rs) throws SQLException {
        Date dataNas = rs.getDate("data_nas");
        return new Autor(
                rs.getInt("id_autor"),
                rs.getString("nome"),
                dataNas != null ? dataNas.toLocalDate() : null
        );
    }
}
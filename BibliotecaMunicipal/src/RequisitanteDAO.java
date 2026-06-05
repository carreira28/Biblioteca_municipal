import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RequisitanteDAO {

    public List<Requisitante> listarTodos() throws SQLException {
        String sql = """
                SELECT r.id_requisitante, r.nome, r.contacto, cp.localidade
                FROM requisitantes r
                JOIN codigo_postal cp ON r.id_codigo_postal = cp.id_codigo_postal
                ORDER BY r.nome
                """;
        List<Requisitante> lista = new ArrayList<>();
        try (PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }

    public List<Requisitante> pesquisarPorNome(String termo) throws SQLException {
        String sql = """
                SELECT r.id_requisitante, r.nome, r.contacto, cp.localidade
                FROM requisitantes r
                JOIN codigo_postal cp ON r.id_codigo_postal = cp.id_codigo_postal
                WHERE LOWER(r.nome) LIKE LOWER(?)
                ORDER BY r.nome
                """;
        List<Requisitante> lista = new ArrayList<>();
        try (PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            stmt.setString(1, "%" + termo + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }
        }
        return lista;
    }

    public Requisitante buscarPorId(int id) throws SQLException {
        String sql = """
                SELECT r.id_requisitante, r.nome, r.contacto, cp.localidade
                FROM requisitantes r
                JOIN codigo_postal cp ON r.id_codigo_postal = cp.id_codigo_postal
                WHERE r.id_requisitante = ?
                """;
        try (PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
        }
        return null;
    }

    public boolean inserir(String nome, String contacto, int idCodigoPostal) throws SQLException {
        String sql = "INSERT INTO requisitantes (nome, contacto, id_codigo_postal) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            stmt.setString(1, nome);
            stmt.setString(2, contacto);
            stmt.setInt(3, idCodigoPostal);
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean atualizar(int id, String novoNome, String novoContacto) throws SQLException {
        String sql = "UPDATE requisitantes SET nome = ?, contacto = ? WHERE id_requisitante = ?";
        try (PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            stmt.setString(1, novoNome);
            stmt.setString(2, novoContacto);
            stmt.setInt(3, id);
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean eliminar(int id) throws SQLException {
        String sql = "DELETE FROM requisitantes WHERE id_requisitante = ?";
        try (PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    public void listarCodigosPostais() throws SQLException {
        String sql = "SELECT id_codigo_postal, n_codigo_postal, localidade FROM codigo_postal ORDER BY localidade";
        System.out.println("\n-- Codigos Postais --");
        try (PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next())
                System.out.printf("[%d] %s - %s%n",
                        rs.getInt(1), rs.getString(2), rs.getString(3));
        }
    }

    private Requisitante mapear(ResultSet rs) throws SQLException {
        return new Requisitante(
                rs.getInt("id_requisitante"),
                rs.getString("nome"),
                rs.getString("contacto"),
                rs.getString("localidade")
        );
    }
}
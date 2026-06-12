import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LivroService {

    public List<Livro> listarTodos() throws SQLException {
        String sql = """
            SELECT l.id_livro, l.isbn, l.titulo, l.ano_publicacao,
            c.tipo_categoria, i.idioma, e.nome AS editora, g.tipo_genero,
            es.tipo_estado
            FROM livros l
            INNER JOIN categorias c ON l.id_categoria  = c.id_categoria
            INNER JOIN idioma i ON l.id_idioma = i.id_idioma
            INNER JOIN editoras e ON l.id_editoras = e.id_editoras
            INNER JOIN generos g ON l.id_generos = g.id_generos
            LEFT  JOIN exemplar ex ON ex.id_livro = l.id_livro 
            LEFT  JOIN estado es ON ex.id_estado = es.id_estado 
            ORDER BY l.id_livro
                """;
        List<Livro> lista = new ArrayList<>();
        try (PreparedStatement consulta = DatabaseConnection.getConnection().prepareStatement(sql);
             ResultSet rs = consulta.executeQuery()) {
            while (rs.next()) {
                Livro livro = mapear(rs);
                lista.add(livro);
            }
        }
        return lista;
    }

    public List<Livro> pesquisarPorTitulo(String termo) throws SQLException {
        String sql = """
            SELECT l.id_livro, l.isbn, l.titulo, l.ano_publicacao,
            c.tipo_categoria, i.idioma, e.nome AS editora, g.tipo_genero,
            es.tipo_estado
            FROM livros l
            INNER JOIN categorias c ON l.id_categoria = c.id_categoria
            INNER JOIN idioma i ON l.id_idioma = i.id_idioma
            INNER JOIN editoras e ON l.id_editoras = e.id_editoras
            INNER JOIN generos g ON l.id_generos = g.id_generos
            LEFT JOIN exemplar ex ON ex.id_livro = l.id_livro 
            LEFT JOIN estado es ON ex.id_estado = es.id_estado\s
            WHERE LOWER(l.titulo) LIKE LOWER(?)
            ORDER BY l.id_livro;
                """;
        List<Livro> lista = new ArrayList<>();
        try (PreparedStatement consulta = DatabaseConnection.getConnection().prepareStatement(sql)) {
            consulta.setString(1, "%" + termo + "%");
            try (ResultSet rs = consulta.executeQuery()) {
                while (rs.next()) {
                    Livro livro = mapear(rs);
                    lista.add(livro);
                }
            }
        }
        return lista;
    }

    public List<Livro> listarDisponiveis() throws SQLException {
        String sql = """
            SELECT l.id_livro, l.isbn, l.titulo, l.ano_publicacao,
            c.tipo_categoria, i.idioma, e.nome AS editora, g.tipo_genero,
            es.tipo_estado
            FROM livros l
            INNER JOIN categorias c ON l.id_categoria  = c.id_categoria
            INNER JOIN idioma i ON l.id_idioma = i.id_idioma
            INNER JOIN editoras e ON l.id_editoras = e.id_editoras
            INNER JOIN generos g ON l.id_generos = g.id_generos
            LEFT JOIN exemplar ex ON ex.id_livro = l.id_livro  
            LEFT JOIN estado es ON ex.id_estado = es.id_estado  
            WHERE es.tipo_estado LIKE '%Disponível%'
            ORDER BY l.id_livro
                """;
        List<Livro> lista = new ArrayList<>();
        try (PreparedStatement consulta = DatabaseConnection.getConnection().prepareStatement(sql);
             ResultSet rs = consulta.executeQuery()) {
            while (rs.next()) {
                Livro livro = mapear(rs);
                lista.add(livro);
            }
        }
        return lista;
    }

    public boolean inserir(String isbn, String titulo, int ano, int idCategoria, int idIdioma, int idEditora, int idGenero) throws SQLException {
        String sql = """
                INSERT INTO livros (isbn, titulo, ano_publicacao,
                id_categoria, id_idioma, id_editoras, id_generos)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;
        try (PreparedStatement consulta = DatabaseConnection.getConnection().prepareStatement(sql)) {
            consulta.setString(1, isbn);
            consulta.setString(2, titulo);
            consulta.setInt(3, ano);
            consulta.setInt(4, idCategoria);
            consulta.setInt(5, idIdioma);
            consulta.setInt(6, idEditora);
            consulta.setInt(7, idGenero);
            return consulta.executeUpdate() > 0;
        }
    }

    public boolean atualizar(int id, String novoTitulo) throws SQLException {
        String sql = "UPDATE livros SET titulo = ? WHERE id_livro = ?";
        try (PreparedStatement consulta = DatabaseConnection.getConnection().prepareStatement(sql)) {
            consulta.setString(1, novoTitulo);
            consulta.setInt(2, id);
            return consulta.executeUpdate() > 0;
        }
    }

    public boolean eliminar(int id) throws SQLException {
        String sql = "DELETE FROM livros WHERE id_livro = ?";
        try (PreparedStatement consulta = DatabaseConnection.getConnection().prepareStatement(sql)) {
            consulta.setInt(1, id);
            return consulta.executeUpdate() > 0;
        }
    }

    public void listarCategorias() throws SQLException {
        String sql = "SELECT id_categoria, tipo_categoria FROM categorias ORDER BY tipo_categoria";
        try (PreparedStatement consulta = DatabaseConnection.getConnection().prepareStatement(sql);
             ResultSet rs = consulta.executeQuery()) {
            System.out.println("\n-- Categorias disponíveis --");
            while (rs.next())
                System.out.printf("[%d] %s%n", rs.getInt(1), rs.getString(2));
        }
    }

    public void listarIdiomas() throws SQLException {
        String sql = "SELECT id_idioma, idioma FROM idioma ORDER BY idioma";
        try (PreparedStatement consulta = DatabaseConnection.getConnection().prepareStatement(sql);
             ResultSet rs = consulta.executeQuery()) {
            System.out.println("\n-- Idiomas disponíveis --");
            while (rs.next())
                System.out.printf("[%d] %s%n", rs.getInt(1), rs.getString(2));
        }
    }

    public void listarEditoras() throws SQLException {
        String sql = "SELECT id_editoras, nome FROM editoras ORDER BY nome";
        try (PreparedStatement consulta = DatabaseConnection.getConnection().prepareStatement(sql);
             ResultSet rs = consulta.executeQuery()) {
            System.out.println("\n-- Editoras disponíveis --");
            while (rs.next())
                System.out.printf("[%d] %s%n", rs.getInt(1), rs.getString(2));
        }
    }

    public void listarGeneros() throws SQLException {
        String sql = "SELECT id_generos, tipo_genero FROM generos ORDER BY tipo_genero";
        try (PreparedStatement consulta = DatabaseConnection.getConnection().prepareStatement(sql);
             ResultSet rs = consulta.executeQuery()) {
            System.out.println("\n-- Géneros disponíveis --");
            while (rs.next())
                System.out.printf("[%d] %s%n", rs.getInt(1), rs.getString(2));
        }
    }

    private Livro mapear(ResultSet rs) throws SQLException {
        String tipoEstado = null;
        try {
            tipoEstado = rs.getString("tipo_estado");
        } catch (SQLException e) {
        }
        return new Livro(
                rs.getInt("id_livro"),
                rs.getString("isbn"),
                rs.getString("titulo"),
                rs.getInt("ano_publicacao"),
                rs.getString("tipo_categoria"),
                rs.getString("idioma"),
                rs.getString("editora"),
                rs.getString("tipo_genero"),
                tipoEstado
        );
    }
}

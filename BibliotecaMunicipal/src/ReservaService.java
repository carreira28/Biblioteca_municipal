import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReservaService {

    public List<Reserva> listarTodas() throws SQLException {
        String sql = """
                SELECT res.id_reserva, res.data_saida, res.data_dev_prevista, res.data_dev_real,
                       req.nome AS nome_requisitante, f.nome AS nome_funcionario,
                       res.id_exemplar, l.titulo
                FROM reserva res
                JOIN requisitantes req ON res.id_requisitante = req.id_requisitante
                JOIN funcionarios  f   ON res.id_funcionario  = f.id_funcionario
                JOIN exemplar      ex  ON res.id_exemplar     = ex.id_exemplar
                JOIN livros        l   ON ex.id_livro         = l.id_livro
                ORDER BY res.data_saida DESC
                """;
        return executarQuery(sql);
    }

    public List<Reserva> listarEmAberto() throws SQLException {
        String sql = """
                SELECT res.id_reserva, res.data_saida, res.data_dev_prevista, res.data_dev_real,
                       req.nome AS nome_requisitante, f.nome AS nome_funcionario,
                       res.id_exemplar, l.titulo
                FROM reserva res
                JOIN requisitantes req ON res.id_requisitante = req.id_requisitante
                JOIN funcionarios  f   ON res.id_funcionario  = f.id_funcionario
                JOIN exemplar      ex  ON res.id_exemplar     = ex.id_exemplar
                JOIN livros        l   ON ex.id_livro         = l.id_livro
                WHERE res.data_dev_real IS NULL
                ORDER BY res.data_dev_prevista
                """;
        return executarQuery(sql);
    }

    public List<Reserva> listarAtrasadas() throws SQLException {
        String sql = """
                SELECT res.id_reserva, res.data_saida, res.data_dev_prevista, res.data_dev_real,
                       req.nome AS nome_requisitante, f.nome AS nome_funcionario,
                       res.id_exemplar, l.titulo
                FROM reserva res
                JOIN requisitantes req ON res.id_requisitante = req.id_requisitante
                JOIN funcionarios  f   ON res.id_funcionario  = f.id_funcionario
                JOIN exemplar      ex  ON res.id_exemplar     = ex.id_exemplar
                JOIN livros        l   ON ex.id_livro         = l.id_livro
                WHERE res.data_dev_real IS NULL
                  AND res.data_dev_prevista < CURRENT_DATE
                ORDER BY res.data_dev_prevista
                """;
        return executarQuery(sql);
    }

    public List<Reserva> listarPorRequisitante(int idRequisitante) throws SQLException {
        String sql = """
                SELECT res.id_reserva, res.data_saida, res.data_dev_prevista, res.data_dev_real,
                       req.nome AS nome_requisitante, f.nome AS nome_funcionario,
                       res.id_exemplar, l.titulo
                FROM reserva res
                JOIN requisitantes req ON res.id_requisitante = req.id_requisitante
                JOIN funcionarios  f   ON res.id_funcionario  = f.id_funcionario
                JOIN exemplar      ex  ON res.id_exemplar     = ex.id_exemplar
                JOIN livros        l   ON ex.id_livro         = l.id_livro
                WHERE res.id_requisitante = ?
                ORDER BY res.data_saida DESC
                """;
        List<Reserva> lista = new ArrayList<>();
        try (PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, idRequisitante);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }
        }
        return lista;
    }

    public boolean criar(LocalDate dataSaida, LocalDate dataDevPrevista,
                         int idRequisitante, int idFuncionario,
                         int idExemplar) throws SQLException {
        String sql = """
                INSERT INTO reserva (data_saida, data_dev_prevista,
                                     id_requisitante, id_funcionario, id_exemplar)
                VALUES (?, ?, ?, ?, ?)
                """;
        try (PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(dataSaida));
            stmt.setDate(2, Date.valueOf(dataDevPrevista));
            stmt.setInt(3, idRequisitante);
            stmt.setInt(4, idFuncionario);
            stmt.setInt(5, idExemplar);
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean registarDevolucao(int idReserva) throws SQLException {
        String sql = """
                UPDATE reserva SET data_dev_real = CURRENT_DATE
                WHERE id_reserva = ? AND data_dev_real IS NULL
                """;
        try (PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, idReserva);
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean exemplarDisponivel(int idExemplar) throws SQLException {
        String sql = """
                SELECT COUNT(*) FROM reserva
                WHERE id_exemplar = ? AND data_dev_real IS NULL
                """;
        try (PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, idExemplar);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getInt(1) == 0;
            }
        }
        return false;
    }

    public void listarExemplaresDisponiveis(int idLivro) throws SQLException {
        String sql = """
                SELECT ex.id_exemplar, lo.n_piso, lo.n_estante, lo.parteleira, e.tipo_estado
                FROM exemplar ex
                JOIN localizacao lo ON ex.id_localizacao = lo.id_localizacao
                JOIN estado      e  ON ex.id_estado      = e.id_estado
                WHERE ex.id_livro = ?
                  AND ex.id_exemplar NOT IN (
                      SELECT id_exemplar FROM reserva WHERE data_dev_real IS NULL
                  )
                """;
        System.out.println("\n-- Exemplares disponiveis --");
        try (PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, idLivro);
            try (ResultSet rs = stmt.executeQuery()) {
                boolean encontrou = false;
                while (rs.next()) {
                    encontrou = true;
                    System.out.printf("[%d] Piso %d | Estante %d | Prateleira %d | Estado: %s%n",
                            rs.getInt("id_exemplar"),
                            rs.getInt("n_piso"),
                            rs.getInt("n_estante"),
                            rs.getInt("parteleira"),
                            rs.getString("tipo_estado"));
                }
                if (!encontrou) System.out.println("Nenhum exemplar disponivel.");
            }
        }
    }

    public void listarFuncionarios() throws SQLException {
        String sql = "SELECT id_funcionario, nome FROM funcionarios ORDER BY nome";
        System.out.println("\n-- Funcionarios --");
        try (PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next())
                System.out.printf("[%d] %s%n", rs.getInt(1), rs.getString(2));
        }
    }

    private List<Reserva> executarQuery(String sql) throws SQLException {
        List<Reserva> lista = new ArrayList<>();
        try (PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }

    private Reserva mapear(ResultSet rs) throws SQLException {
        Date devReal = rs.getDate("data_dev_real");
        return new Reserva(
                rs.getInt("id_reserva"),
                rs.getDate("data_saida").toLocalDate(),
                rs.getDate("data_dev_prevista").toLocalDate(),
                devReal != null ? devReal.toLocalDate() : null,
                rs.getString("nome_requisitante"),
                rs.getString("nome_funcionario"),
                rs.getInt("id_exemplar"),
                rs.getString("titulo")
        );
    }
}
package gestaoeventos.dao;

import gestaoeventos.model.Evento;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class EventoDAO {

    public void create(Evento evento) throws Exception {
        String sql = "INSERT INTO eventos (titulo, descricao, data_evento, local, capacidade_maxima) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, evento.getTitulo());
            stmt.setString(2, evento.getDescricao());
            stmt.setDate(3, evento.getDataEvento());
            stmt.setString(4, evento.getLocal());
            stmt.setInt(5, evento.getCapacidadeMaxima());
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    evento.setId(generatedKeys.getInt(1));
                }
            }
        }
    }

    public List<Evento> listAll() throws Exception {
        List<Evento> list = new ArrayList<>();
        String sql = "SELECT id, titulo, descricao, data_evento, local, capacidade_maxima FROM eventos ORDER BY data_evento DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                list.add(new Evento(
                        rs.getInt("id"),
                        rs.getString("titulo"),
                        rs.getString("descricao"),
                        rs.getDate("data_evento"),
                        rs.getString("local"),
                        rs.getInt("capacidade_maxima")
                ));
            }
        }
        return list;
    }

    public Evento findById(int id) throws Exception {
        String sql = "SELECT id, titulo, descricao, data_evento, local, capacidade_maxima FROM eventos WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Evento(
                            rs.getInt("id"),
                            rs.getString("titulo"),
                            rs.getString("descricao"),
                            rs.getDate("data_evento"),
                            rs.getString("local"),
                            rs.getInt("capacidade_maxima")
                    );
                }
            }
        }
        return null;
    }

    public void update(Evento evento) throws Exception {
        String sql = "UPDATE eventos SET titulo = ?, descricao = ?, data_evento = ?, local = ?, capacidade_maxima = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, evento.getTitulo());
            stmt.setString(2, evento.getDescricao());
            stmt.setDate(3, evento.getDataEvento());
            stmt.setString(4, evento.getLocal());
            stmt.setInt(5, evento.getCapacidadeMaxima());
            stmt.setInt(6, evento.getId());
            stmt.executeUpdate();
        }
    }

    public void delete(int id) throws Exception {
        String sql = "DELETE FROM eventos WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public List<Evento> search(String query) throws Exception {
        List<Evento> list = new ArrayList<>();
        String sql = "SELECT id, titulo, descricao, data_evento, local, capacidade_maxima FROM eventos WHERE LOWER(titulo) LIKE ? OR LOWER(local) LIKE ? ORDER BY data_evento DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            String q = "%" + query.toLowerCase() + "%";
            stmt.setString(1, q);
            stmt.setString(2, q);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(new Evento(
                            rs.getInt("id"),
                            rs.getString("titulo"),
                            rs.getString("descricao"),
                            rs.getDate("data_evento"),
                            rs.getString("local"),
                            rs.getInt("capacidade_maxima")
                    ));
                }
            }
        }
        return list;
    }


    public int getConfirmedInscriptionsCount(int eventId) throws Exception {
        String sql = "SELECT COUNT(*) FROM inscricoes WHERE evento_id = ? AND status = 'Confirmada'";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, eventId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }
}

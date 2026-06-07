package gestaoeventos.dao;

import gestaoeventos.model.Inscricao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class InscricaoDAO {

    public void create(Inscricao inscricao) throws Exception {
        String sql = "INSERT INTO inscricoes (participante_id, evento_id, status) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, inscricao.getParticipanteId());
            stmt.setInt(2, inscricao.getEventoId());
            stmt.setString(3, inscricao.getStatus());
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    inscricao.setId(generatedKeys.getInt(1));
                }
            }
        }
    }

    public List<Inscricao> listAll() throws Exception {
        List<Inscricao> list = new ArrayList<>();
        String sql = "SELECT i.id, i.participante_id, i.evento_id, i.data_inscricao, i.status, " +
                     "p.nome AS participante_nome, p.email AS participante_email, e.titulo AS evento_titulo " +
                     "FROM inscricoes i " +
                     "JOIN participantes p ON i.participante_id = p.id " +
                     "JOIN eventos e ON i.evento_id = e.id " +
                     "ORDER BY i.data_inscricao DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Inscricao insc = new Inscricao(
                        rs.getInt("id"),
                        rs.getInt("participante_id"),
                        rs.getInt("evento_id"),
                        rs.getTimestamp("data_inscricao"),
                        rs.getString("status")
                );
                insc.setParticipanteNome(rs.getString("participante_nome"));
                insc.setParticipanteEmail(rs.getString("participante_email"));
                insc.setEventoTitulo(rs.getString("evento_titulo"));
                list.add(insc);
            }
        }
        return list;
    }

    public Inscricao findById(int id) throws Exception {
        String sql = "SELECT i.id, i.participante_id, i.evento_id, i.data_inscricao, i.status, " +
                     "p.nome AS participante_nome, p.email AS participante_email, e.titulo AS evento_titulo " +
                     "FROM inscricoes i " +
                     "JOIN participantes p ON i.participante_id = p.id " +
                     "JOIN eventos e ON i.evento_id = e.id " +
                     "WHERE i.id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Inscricao insc = new Inscricao(
                            rs.getInt("id"),
                            rs.getInt("participante_id"),
                            rs.getInt("evento_id"),
                            rs.getTimestamp("data_inscricao"),
                            rs.getString("status")
                    );
                    insc.setParticipanteNome(rs.getString("participante_nome"));
                    insc.setParticipanteEmail(rs.getString("participante_email"));
                    insc.setEventoTitulo(rs.getString("evento_titulo"));
                    return insc;
                }
            }
        }
        return null;
    }

    public Inscricao findByParticipanteAndEvento(int participanteId, int eventoId) throws Exception {
        String sql = "SELECT id, participante_id, evento_id, data_inscricao, status FROM inscricoes WHERE participante_id = ? AND evento_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, participanteId);
            stmt.setInt(2, eventoId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Inscricao(
                            rs.getInt("id"),
                            rs.getInt("participante_id"),
                            rs.getInt("evento_id"),
                            rs.getTimestamp("data_inscricao"),
                            rs.getString("status")
                    );
                }
            }
        }
        return null;
    }

    public void updateStatus(int id, String status) throws Exception {
        String sql = "UPDATE inscricoes SET status = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setInt(2, id);
            stmt.executeUpdate();
        }
    }

    public void delete(int id) throws Exception {
        String sql = "DELETE FROM inscricoes WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public List<Inscricao> listByEvento(int eventoId) throws Exception {
        List<Inscricao> list = new ArrayList<>();
        String sql = "SELECT i.id, i.participante_id, i.evento_id, i.data_inscricao, i.status, " +
                     "p.nome AS participante_nome, p.email AS participante_email, e.titulo AS evento_titulo " +
                     "FROM inscricoes i " +
                     "JOIN participantes p ON i.participante_id = p.id " +
                     "JOIN eventos e ON i.evento_id = e.id " +
                     "WHERE i.evento_id = ? " +
                     "ORDER BY p.nome";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, eventoId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Inscricao insc = new Inscricao(
                            rs.getInt("id"),
                            rs.getInt("participante_id"),
                            rs.getInt("evento_id"),
                            rs.getTimestamp("data_inscricao"),
                            rs.getString("status")
                    );
                    insc.setParticipanteNome(rs.getString("participante_nome"));
                    insc.setParticipanteEmail(rs.getString("participante_email"));
                    insc.setEventoTitulo(rs.getString("evento_titulo"));
                    list.add(insc);
                }
            }
        }
        return list;
    }
}

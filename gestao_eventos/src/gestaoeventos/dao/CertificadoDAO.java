package gestaoeventos.dao;

import gestaoeventos.model.Certificado;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CertificadoDAO {

    public void create(Certificado certificado) throws Exception {
        String sql = "INSERT INTO certificados (inscricao_id, codigo_autenticacao) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, certificado.getInscricaoId());
            stmt.setString(2, certificado.getCodigoAutenticacao());
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    certificado.setId(generatedKeys.getInt(1));
                }
            }
        }
    }

    public List<Certificado> listAll() throws Exception {
        List<Certificado> list = new ArrayList<>();
        String sql = "SELECT c.id, c.inscricao_id, c.codigo_autenticacao, c.data_emissao, " +
                     "p.nome AS participante_nome, p.email AS participante_email, " +
                     "e.titulo AS evento_titulo, e.data_evento AS evento_data " +
                     "FROM certificados c " +
                     "JOIN inscricoes i ON c.inscricao_id = i.id " +
                     "JOIN participantes p ON i.participante_id = p.id " +
                     "JOIN eventos e ON i.evento_id = e.id " +
                     "ORDER BY c.data_emissao DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Certificado cert = new Certificado(
                        rs.getInt("id"),
                        rs.getInt("inscricao_id"),
                        rs.getString("codigo_autenticacao"),
                        rs.getTimestamp("data_emissao")
                );
                cert.setParticipanteNome(rs.getString("participante_nome"));
                cert.setParticipanteEmail(rs.getString("participante_email"));
                cert.setEventoTitulo(rs.getString("evento_titulo"));
                cert.setEventoData(rs.getDate("evento_data").toString());
                list.add(cert);
            }
        }
        return list;
    }

    public Certificado findById(int id) throws Exception {
        String sql = "SELECT c.id, c.inscricao_id, c.codigo_autenticacao, c.data_emissao, " +
                     "p.nome AS participante_nome, p.email AS participante_email, " +
                     "e.titulo AS evento_titulo, e.data_evento AS evento_data " +
                     "FROM certificados c " +
                     "JOIN inscricoes i ON c.inscricao_id = i.id " +
                     "JOIN participantes p ON i.participante_id = p.id " +
                     "JOIN eventos e ON i.evento_id = e.id " +
                     "WHERE c.id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Certificado cert = new Certificado(
                            rs.getInt("id"),
                            rs.getInt("inscricao_id"),
                            rs.getString("codigo_autenticacao"),
                            rs.getTimestamp("data_emissao")
                    );
                    cert.setParticipanteNome(rs.getString("participante_nome"));
                    cert.setParticipanteEmail(rs.getString("participante_email"));
                    cert.setEventoTitulo(rs.getString("evento_titulo"));
                    cert.setEventoData(rs.getDate("evento_data").toString());
                    return cert;
                }
            }
        }
        return null;
    }

    public Certificado findByInscricaoId(int inscricaoId) throws Exception {
        String sql = "SELECT c.id, c.inscricao_id, c.codigo_autenticacao, c.data_emissao, " +
                     "p.nome AS participante_nome, p.email AS participante_email, " +
                     "e.titulo AS evento_titulo, e.data_evento AS evento_data " +
                     "FROM certificados c " +
                     "JOIN inscricoes i ON c.inscricao_id = i.id " +
                     "JOIN participantes p ON i.participante_id = p.id " +
                     "JOIN eventos e ON i.evento_id = e.id " +
                     "WHERE c.inscricao_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, inscricaoId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Certificado cert = new Certificado(
                            rs.getInt("id"),
                            rs.getInt("inscricao_id"),
                            rs.getString("codigo_autenticacao"),
                            rs.getTimestamp("data_emissao")
                    );
                    cert.setParticipanteNome(rs.getString("participante_nome"));
                    cert.setParticipanteEmail(rs.getString("participante_email"));
                    cert.setEventoTitulo(rs.getString("evento_titulo"));
                    cert.setEventoData(rs.getDate("evento_data").toString());
                    return cert;
                }
            }
        }
        return null;
    }

    public void delete(int id) throws Exception {
        String sql = "DELETE FROM certificados WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}

package gestaoeventos.dao;

import gestaoeventos.model.Atividade;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class AtividadeDAO {

    public List<Atividade> listByEvento(int eventoId) throws Exception {
        List<Atividade> list = new ArrayList<>();
        String sql = "SELECT a.id, a.evento_id, a.palestrante_id, a.titulo, a.data_hora, a.duracao_minutos, " +
                     "e.titulo AS evento_titulo, p.nome AS palestrante_nome " +
                     "FROM atividades a " +
                     "JOIN eventos e ON a.evento_id = e.id " +
                     "LEFT JOIN palestrantes p ON a.palestrante_id = p.id " +
                     "WHERE a.evento_id = ? " +
                     "ORDER BY a.data_hora";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, eventoId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Atividade at = new Atividade(
                            rs.getInt("id"),
                            rs.getInt("evento_id"),
                            rs.getInt("palestrante_id"),
                            rs.getString("titulo"),
                            rs.getTimestamp("data_hora"),
                            rs.getInt("duracao_minutos")
                    );
                    at.setEventoTitulo(rs.getString("evento_titulo"));
                    at.setPalestranteNome(rs.getString("palestrante_nome"));
                    list.add(at);
                }
            }
        }
        return list;
    }
}

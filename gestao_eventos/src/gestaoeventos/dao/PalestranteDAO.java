package gestaoeventos.dao;

import gestaoeventos.model.Palestrante;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PalestranteDAO {

    public void create(Palestrante palestrante) throws Exception {
        String sql = "INSERT INTO palestrantes (nome, email, especialidade) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, palestrante.getNome());
            stmt.setString(2, palestrante.getEmail());
            stmt.setString(3, palestrante.getEspecialidade());
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    palestrante.setId(generatedKeys.getInt(1));
                }
            }
        }
    }

    public List<Palestrante> listAll() throws Exception {
        List<Palestrante> list = new ArrayList<>();
        String sql = "SELECT id, nome, email, especialidade FROM palestrantes ORDER BY nome";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                list.add(new Palestrante(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("email"),
                        rs.getString("especialidade")
                ));
            }
        }
        return list;
    }
}

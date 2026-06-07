package gestaoeventos.ui;

import gestaoeventos.dao.DatabaseConnection;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class DashboardPanel extends JPanel {
    private JLabel lblTotalParticipantes;
    private JLabel lblTotalEventos;
    private JLabel lblTotalInscricoes;
    private JLabel lblTotalCertificados;

    private JTable tableRelatorio;
    private DefaultTableModel tableModel;

    public DashboardPanel() {
        setLayout(new BorderLayout(15, 15));
        setBorder(new EmptyBorder(20, 20, 20, 20));


        JLabel lblTitulo = new JLabel("Painel Geral e Relatórios");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        add(lblTitulo, BorderLayout.NORTH);


        JPanel panelContent = new JPanel(new BorderLayout(15, 15));
        add(panelContent, BorderLayout.CENTER);


        JPanel panelCards = new JPanel(new GridLayout(1, 4, 15, 0));

        panelCards.add(criarCard("Participantes", lblTotalParticipantes = new JLabel("0"), new Color(33, 150, 243))); 
        panelCards.add(criarCard("Eventos", lblTotalEventos = new JLabel("0"), new Color(76, 175, 80)));       
        panelCards.add(criarCard("Inscrições", lblTotalInscricoes = new JLabel("0"), new Color(255, 152, 0)));    
        panelCards.add(criarCard("Certificados", lblTotalCertificados = new JLabel("0"), new Color(156, 39, 176)));

        panelContent.add(panelCards, BorderLayout.NORTH);


        JPanel panelRelatorio = new JPanel(new BorderLayout(10, 10));
        panelRelatorio.setBorder(BorderFactory.createTitledBorder("Relatório de Ocupação e Vagas por Evento"));

        String[] colunas = {"ID", "Título do Evento", "Data do Evento", "Capacidade Máxima", "Inscrições Confirmadas", "Vagas Restantes", "Taxa de Ocupação (%)"};
        tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableRelatorio = new JTable(tableModel);
        tableRelatorio.getTableHeader().setReorderingAllowed(false);
        JScrollPane scrollPane = new JScrollPane(tableRelatorio);
        panelRelatorio.add(scrollPane, BorderLayout.CENTER);

        panelContent.add(panelRelatorio, BorderLayout.CENTER);


        atualizarDados();
    }

    private JPanel criarCard(String titulo, JLabel lblValor, Color corBorda) {
        JPanel card = new JPanel(new BorderLayout(5, 5));
        card.setBackground(UIManager.getColor("Panel.background"));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 5, 0, 0, corBorda), 
            BorderFactory.createEmptyBorder(12, 12, 12, 12)
        ));

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblTitulo.setForeground(UIManager.getColor("Label.disabledForeground"));

        lblValor.setFont(new Font("Segoe UI", Font.BOLD, 28));

        card.add(lblTitulo, BorderLayout.NORTH);
        card.add(lblValor, BorderLayout.CENTER);
        return card;
    }

    public final void atualizarDados() {
        carregarEstatisticas();
        carregarRelatorioOcupacao();
    }

    private void carregarEstatisticas() {
        String sqlPart = "SELECT COUNT(*) FROM participantes";
        String sqlEv = "SELECT COUNT(*) FROM eventos";
        String sqlInsc = "SELECT COUNT(*) FROM inscricoes";
        String sqlCert = "SELECT COUNT(*) FROM certificados";

        try (Connection conn = DatabaseConnection.getConnection()) {

            try (PreparedStatement stmt = conn.prepareStatement(sqlPart);
                 ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) lblTotalParticipantes.setText(String.valueOf(rs.getInt(1)));
            }

            try (PreparedStatement stmt = conn.prepareStatement(sqlEv);
                 ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) lblTotalEventos.setText(String.valueOf(rs.getInt(1)));
            }

            try (PreparedStatement stmt = conn.prepareStatement(sqlInsc);
                 ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) lblTotalInscricoes.setText(String.valueOf(rs.getInt(1)));
            }

            try (PreparedStatement stmt = conn.prepareStatement(sqlCert);
                 ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) lblTotalCertificados.setText(String.valueOf(rs.getInt(1)));
            }
        } catch (Exception e) {
            System.err.println("Erro ao carregar estatísticas do dashboard: " + e.getMessage());
        }
    }

    private void carregarRelatorioOcupacao() {
        String sql = "SELECT e.id, e.titulo, e.data_evento, e.capacidade_maxima, " +
                     "(SELECT COUNT(*) FROM inscricoes WHERE evento_id = e.id AND status = 'Confirmada') AS confirmados " +
                     "FROM eventos e ORDER BY confirmados DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            tableModel.setRowCount(0);
            while (rs.next()) {
                int id = rs.getInt("id");
                String titulo = rs.getString("titulo");
                String data = rs.getDate("data_evento").toString();
                int capMax = rs.getInt("capacidade_maxima");
                int confirmados = rs.getInt("confirmados");

                int vagasRestantes = capMax - confirmados;
                if (vagasRestantes < 0) vagasRestantes = 0;

                double taxa = capMax > 0 ? ((double) confirmados / capMax) * 100 : 0.0;
                String taxaStr = String.format("%.1f%%", taxa);

                tableModel.addRow(new Object[]{
                    id, titulo, data, capMax, confirmados, vagasRestantes, taxaStr
                });
            }
        } catch (Exception e) {
            System.err.println("Erro ao carregar relatório de ocupação: " + e.getMessage());
        }
    }
}

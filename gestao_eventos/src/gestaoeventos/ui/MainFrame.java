package gestaoeventos.ui;

import com.formdev.flatlaf.FlatLightLaf;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class MainFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel panelContentCards;


    private DashboardPanel dashboardPanel;
    private ParticipantesPanel participantesPanel;
    private EventosPanel eventosPanel;
    private InscricoesPanel inscricoesPanel;
    private CertificadosPanel certificadosPanel;


    private JButton btnMenuDashboard;
    private JButton btnMenuParticipantes;
    private JButton btnMenuEventos;
    private JButton btnMenuInscricoes;
    private JButton btnMenuCertificados;

    public MainFrame() {

        try {
            FlatLightLaf.setup();
        } catch (Exception e) {
            System.err.println("Falha ao inicializar o FlatLaf Look and Feel: " + e.getMessage());
        }

        setTitle("Sistema de Gestão de Eventos - CRUD & Relatórios");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1100, 700));
        setLocationRelativeTo(null); 


        setLayout(new BorderLayout());


        JPanel panelSidebar = new JPanel(new BorderLayout());
        panelSidebar.setBackground(new Color(38, 50, 56)); 
        panelSidebar.setPreferredSize(new Dimension(220, 0));


        JPanel panelHeader = new JPanel(new BorderLayout());
        panelHeader.setOpaque(false);
        panelHeader.setBorder(new EmptyBorder(25, 15, 25, 15));

        JLabel lblLogo = new JLabel("EVENTFLOW");
        lblLogo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblLogo.setForeground(Color.WHITE);
        lblLogo.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel lblSublogo = new JLabel("Gestor Acadêmico");
        lblSublogo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSublogo.setForeground(new Color(176, 190, 197));
        lblSublogo.setHorizontalAlignment(SwingConstants.CENTER);

        panelHeader.add(lblLogo, BorderLayout.NORTH);
        panelHeader.add(lblSublogo, BorderLayout.CENTER);
        panelSidebar.add(panelHeader, BorderLayout.NORTH);


        JPanel panelMenuBody = new JPanel(new GridLayout(6, 1, 0, 10));
        panelMenuBody.setOpaque(false);
        panelMenuBody.setBorder(new EmptyBorder(10, 15, 10, 15));

        btnMenuDashboard = criarBotaoMenu("Dashboard");
        btnMenuParticipantes = criarBotaoMenu("Participantes");
        btnMenuEventos = criarBotaoMenu("Eventos");
        btnMenuInscricoes = criarBotaoMenu("Inscrições");
        btnMenuCertificados = criarBotaoMenu("Certificados");

        panelMenuBody.add(btnMenuDashboard);
        panelMenuBody.add(btnMenuParticipantes);
        panelMenuBody.add(btnMenuEventos);
        panelMenuBody.add(btnMenuInscricoes);
        panelMenuBody.add(btnMenuCertificados);

        panelSidebar.add(panelMenuBody, BorderLayout.CENTER);


        JLabel lblVersion = new JLabel("Versão 1.0.0");
        lblVersion.setForeground(new Color(120, 144, 156));
        lblVersion.setHorizontalAlignment(SwingConstants.CENTER);
        lblVersion.setBorder(new EmptyBorder(15, 15, 15, 15));
        panelSidebar.add(lblVersion, BorderLayout.SOUTH);

        add(panelSidebar, BorderLayout.WEST);


        cardLayout = new CardLayout();
        panelContentCards = new JPanel(cardLayout);
        panelContentCards.setBackground(Color.WHITE);


        dashboardPanel = new DashboardPanel();
        participantesPanel = new ParticipantesPanel();
        eventosPanel = new EventosPanel();
        certificadosPanel = new CertificadosPanel();


        inscricoesPanel = new InscricoesPanel(() -> {
            dashboardPanel.atualizarDados();
            certificadosPanel.atualizarTabela();
        });


        panelContentCards.add(dashboardPanel, "dashboard");
        panelContentCards.add(participantesPanel, "participantes");
        panelContentCards.add(eventosPanel, "eventos");
        panelContentCards.add(inscricoesPanel, "inscricoes");
        panelContentCards.add(certificadosPanel, "certificados");

        add(panelContentCards, BorderLayout.CENTER);



        btnMenuDashboard.addActionListener(e -> {
            marcarBotaoAtivo(btnMenuDashboard);
            dashboardPanel.atualizarDados();
            cardLayout.show(panelContentCards, "dashboard");
        });

        btnMenuParticipantes.addActionListener(e -> {
            marcarBotaoAtivo(btnMenuParticipantes);
            cardLayout.show(panelContentCards, "participantes");
        });

        btnMenuEventos.addActionListener(e -> {
            marcarBotaoAtivo(btnMenuEventos);
            cardLayout.show(panelContentCards, "eventos");
        });

        btnMenuInscricoes.addActionListener(e -> {
            marcarBotaoAtivo(btnMenuInscricoes);
            inscricoesPanel.atualizarCombos();
            inscricoesPanel.atualizarTabela();
            cardLayout.show(panelContentCards, "inscricoes");
        });

        btnMenuCertificados.addActionListener(e -> {
            marcarBotaoAtivo(btnMenuCertificados);
            certificadosPanel.atualizarTabela();
            cardLayout.show(panelContentCards, "certificados");
        });


        marcarBotaoAtivo(btnMenuDashboard);
        cardLayout.show(panelContentCards, "dashboard");
    }

    private JButton criarBotaoMenu(String texto) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btn.setForeground(Color.WHITE);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setIconTextGap(10);
        btn.setMargin(new Insets(10, 15, 10, 15));


        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (btn.getBackground() == null || btn.getBackground().getAlpha() == 0) {
                    btn.setContentAreaFilled(true);
                    btn.setBackground(new Color(55, 71, 79)); 
                }
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (!btn.isOpaque() || btn.getBackground().equals(new Color(55, 71, 79))) {
                    btn.setContentAreaFilled(false);
                }
            }
        });

        return btn;
    }

    private void marcarBotaoAtivo(JButton botaoAtivo) {

        JButton[] botoes = {btnMenuDashboard, btnMenuParticipantes, btnMenuEventos, btnMenuInscricoes, btnMenuCertificados};
        for (JButton b : botoes) {
            b.setOpaque(false);
            b.setContentAreaFilled(false);
            b.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            b.setForeground(new Color(207, 216, 220)); 
        }


        botaoAtivo.setOpaque(true);
        botaoAtivo.setContentAreaFilled(true);
        botaoAtivo.setBackground(new Color(3, 169, 244)); 
        botaoAtivo.setForeground(Color.WHITE);
        botaoAtivo.setFont(new Font("Segoe UI", Font.BOLD, 14));
    }
}

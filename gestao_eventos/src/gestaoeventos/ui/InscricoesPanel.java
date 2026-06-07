package gestaoeventos.ui;

import gestaoeventos.dao.EventoDAO;
import gestaoeventos.dao.InscricaoDAO;
import gestaoeventos.dao.ParticipanteDAO;
import gestaoeventos.model.Certificado;
import gestaoeventos.model.Evento;
import gestaoeventos.model.Inscricao;
import gestaoeventos.model.Participante;
import gestaoeventos.service.EventosService;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class InscricoesPanel extends JPanel {
    private final InscricaoDAO inscricaoDAO = new InscricaoDAO();
    private final ParticipanteDAO participanteDAO = new ParticipanteDAO();
    private final EventoDAO eventoDAO = new EventoDAO();
    private final EventosService eventosService = new EventosService();

    private JTable table;
    private DefaultTableModel tableModel;

    private JComboBox<Participante> cbParticipante;
    private JComboBox<Evento> cbEvento;
    private JComboBox<String> cbStatus;

    private JButton btnRegistrar;
    private JButton btnConfirmar;
    private JButton btnCancelar;
    private JButton btnRemover;
    private JButton btnEmitirCertificado;

    private Inscricao inscricaoSelecionada = null;
    private Runnable onCertificateIssuedCallback;

    public InscricoesPanel(Runnable onCertificateIssuedCallback) {
        this.onCertificateIssuedCallback = onCertificateIssuedCallback;
        setLayout(new BorderLayout(15, 15));
        setBorder(new EmptyBorder(20, 20, 20, 20));


        JLabel lblTitulo = new JLabel("Gerenciamento de Inscrições (N:N)");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        add(lblTitulo, BorderLayout.NORTH);


        JPanel panelContent = new JPanel(new BorderLayout(15, 15));
        add(panelContent, BorderLayout.CENTER);


        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBorder(BorderFactory.createTitledBorder("Nova Inscrição"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;


        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.0;
        panelForm.add(new JLabel("Selecionar Participante:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        cbParticipante = new JComboBox<>();
        panelForm.add(cbParticipante, gbc);


        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.0;
        panelForm.add(new JLabel("Selecionar Evento:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        cbEvento = new JComboBox<>();
        panelForm.add(cbEvento, gbc);


        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.0;
        panelForm.add(new JLabel("Status Inicial:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        cbStatus = new JComboBox<>(new String[]{"Confirmada", "Pendente"});
        panelForm.add(cbStatus, gbc);


        gbc.gridx = 1; gbc.gridy = 3; gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.LINE_END;
        btnRegistrar = new JButton("Registrar Inscrição");
        btnRegistrar.putClientProperty("JButton.buttonType", "roundRect");
        panelForm.add(btnRegistrar, gbc);

        panelContent.add(panelForm, BorderLayout.NORTH);


        JPanel panelTabelaEAcoes = new JPanel(new BorderLayout(10, 10));


        String[] colunas = {"Inscrição ID", "Participante", "E-mail", "Evento", "Data", "Status"};
        tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setReorderingAllowed(false);
        JScrollPane scrollPane = new JScrollPane(table);
        panelTabelaEAcoes.add(scrollPane, BorderLayout.CENTER);


        JPanel panelAcoes = new JPanel(new GridLayout(5, 1, 10, 10));
        panelAcoes.setBorder(new EmptyBorder(0, 10, 0, 0));

        btnConfirmar = new JButton("Confirmar");
        btnCancelar = new JButton("Cancelar");
        btnRemover = new JButton("Remover");
        btnEmitirCertificado = new JButton("Emitir Certificado");

        btnConfirmar.putClientProperty("JButton.buttonType", "roundRect");
        btnCancelar.putClientProperty("JButton.buttonType", "roundRect");
        btnRemover.putClientProperty("JButton.buttonType", "roundRect");
        btnEmitirCertificado.putClientProperty("JButton.buttonType", "roundRect");


        btnEmitirCertificado.setBackground(new Color(33, 150, 243));
        btnEmitirCertificado.setForeground(Color.WHITE);

        panelAcoes.add(new JLabel("Ações da Inscrição:"));
        panelAcoes.add(btnConfirmar);
        panelAcoes.add(btnCancelar);
        panelAcoes.add(btnEmitirCertificado);
        panelAcoes.add(btnRemover);

        panelTabelaEAcoes.add(panelAcoes, BorderLayout.EAST);
        panelContent.add(panelTabelaEAcoes, BorderLayout.CENTER);




        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    int id = (int) tableModel.getValueAt(row, 0);
                    carregarInscricao(id);
                } else {
                    desabilitarBotoesAcao();
                }
            }
        });


        btnRegistrar.addActionListener(e -> criarInscricao());


        btnConfirmar.addActionListener(e -> alterarStatus("Confirmada"));
        btnCancelar.addActionListener(e -> alterarStatus("Cancelada"));
        btnRemover.addActionListener(e -> removerInscricao());
        btnEmitirCertificado.addActionListener(e -> emitirCertificado());


        atualizarCombos();
        atualizarTabela();
        desabilitarBotoesAcao();
    }

    public void atualizarCombos() {
        try {
            cbParticipante.removeAllItems();
            List<Participante> participantes = participanteDAO.listAll();
            for (Participante p : participantes) {
                cbParticipante.addItem(p);
            }

            cbEvento.removeAllItems();
            List<Evento> eventos = eventoDAO.listAll();
            for (Evento ev : eventos) {
                cbEvento.addItem(ev);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar dados do formulário: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void atualizarTabela() {
        try {
            List<Inscricao> lista = inscricaoDAO.listAll();
            tableModel.setRowCount(0);
            for (Inscricao i : lista) {
                tableModel.addRow(new Object[]{
                    i.getId(),
                    i.getParticipanteNome(),
                    i.getParticipanteEmail(),
                    i.getEventoTitulo(),
                    i.getDataInscricao().toString(),
                    i.getStatus()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar inscrições: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void carregarInscricao(int id) {
        try {
            inscricaoSelecionada = inscricaoDAO.findById(id);
            if (inscricaoSelecionada != null) {
                btnRemover.setEnabled(true);

                if ("Confirmada".equals(inscricaoSelecionada.getStatus())) {
                    btnConfirmar.setEnabled(false);
                    btnCancelar.setEnabled(true);
                    btnEmitirCertificado.setEnabled(true);
                } else if ("Pendente".equals(inscricaoSelecionada.getStatus())) {
                    btnConfirmar.setEnabled(true);
                    btnCancelar.setEnabled(true);
                    btnEmitirCertificado.setEnabled(false);
                } else { 
                    btnConfirmar.setEnabled(true);
                    btnCancelar.setEnabled(false);
                    btnEmitirCertificado.setEnabled(false);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar inscrição: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void desabilitarBotoesAcao() {
        inscricaoSelecionada = null;
        btnConfirmar.setEnabled(false);
        btnCancelar.setEnabled(false);
        btnEmitirCertificado.setEnabled(false);
        btnRemover.setEnabled(false);
    }

    private void criarInscricao() {
        Participante part = (Participante) cbParticipante.getSelectedItem();
        Evento ev = (Evento) cbEvento.getSelectedItem();
        String status = (String) cbStatus.getSelectedItem();

        if (part == null || ev == null) {
            JOptionPane.showMessageDialog(this, "Selecione um participante e um evento válido!", "Validação", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Inscricao nova = new Inscricao(part.getId(), ev.getId(), status);
            eventosService.registrarInscricao(nova);
            JOptionPane.showMessageDialog(this, "Inscrição realizada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            atualizarTabela();
            desabilitarBotoesAcao();


            if (onCertificateIssuedCallback != null) {
                onCertificateIssuedCallback.run();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao registrar inscrição: " + e.getMessage(), "Regra de Negócio / Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void alterarStatus(String novoStatus) {
        if (inscricaoSelecionada == null) return;
        try {
            eventosService.atualizarStatusInscricao(inscricaoSelecionada.getId(), novoStatus);
            JOptionPane.showMessageDialog(this, "Status alterado para '" + novoStatus + "' com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            atualizarTabela();
            carregarInscricao(inscricaoSelecionada.getId());

            if (onCertificateIssuedCallback != null) {
                onCertificateIssuedCallback.run();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao alterar status: " + e.getMessage(), "Regra de Negócio / Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void removerInscricao() {
        if (inscricaoSelecionada == null) return;

        int confirm = JOptionPane.showConfirmDialog(this,
                "Tem certeza que deseja remover a inscrição do participante '" + inscricaoSelecionada.getParticipanteNome() + "' no evento '" + inscricaoSelecionada.getEventoTitulo() + "'?\n" +
                "Isso também removerá o certificado dele caso tenha sido emitido.",
                "Confirmar Exclusão",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                inscricaoDAO.delete(inscricaoSelecionada.getId());
                JOptionPane.showMessageDialog(this, "Inscrição removida com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                atualizarTabela();
                desabilitarBotoesAcao();

                if (onCertificateIssuedCallback != null) {
                    onCertificateIssuedCallback.run();
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Erro ao remover inscrição: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void emitirCertificado() {
        if (inscricaoSelecionada == null) return;
        try {
            Certificado cert = eventosService.emitirCertificado(inscricaoSelecionada.getId());


            String mensagemDiploma = "========================================================\n" +
                                     "                     CERTIFICADO DE PARTICIPAÇÃO        \n" +
                                     "========================================================\n\n" +
                                     "Certificamos que " + cert.getParticipanteNome().toUpperCase() + ",\n" +
                                     "portador do e-mail " + cert.getParticipanteEmail() + ",\n" +
                                     "concluiu com êxito sua participação no evento:\n\n" +
                                     "      \"" + cert.getEventoTitulo().toUpperCase() + "\"\n\n" +
                                     "Realizado em: " + cert.getEventoData() + ".\n\n" +
                                     "--------------------------------------------------------\n" +
                                     "Código de Autenticação: " + cert.getCodigoAutenticacao() + "\n" +
                                     "Emitido em: " + cert.getDataEmissao() + "\n" +
                                     "========================================================\n";

            JTextArea textArea = new JTextArea(mensagemDiploma);
            textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
            textArea.setEditable(false);
            JScrollPane scroll = new JScrollPane(textArea);
            scroll.setPreferredSize(new Dimension(500, 300));

            JOptionPane.showMessageDialog(this, scroll, "Certificado Emitido com Sucesso!", JOptionPane.INFORMATION_MESSAGE);

            atualizarTabela();
            carregarInscricao(inscricaoSelecionada.getId());


            if (onCertificateIssuedCallback != null) {
                onCertificateIssuedCallback.run();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao emitir certificado: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}

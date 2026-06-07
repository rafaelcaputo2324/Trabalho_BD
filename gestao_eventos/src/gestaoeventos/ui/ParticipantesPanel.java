package gestaoeventos.ui;

import gestaoeventos.dao.ParticipanteDAO;
import gestaoeventos.model.Participante;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class ParticipantesPanel extends JPanel {
    private final ParticipanteDAO participanteDAO = new ParticipanteDAO();

    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtNome;
    private JTextField txtEmail;
    private JTextField txtTelefone;
    private JTextField txtBusca;

    private JButton btnNovo;
    private JButton btnSalvar;
    private JButton btnExcluir;

    private Participante participanteSelecionado = null;

    public ParticipantesPanel() {
        setLayout(new BorderLayout(15, 15));
        setBorder(new EmptyBorder(20, 20, 20, 20));


        JLabel lblTitulo = new JLabel("Gerenciamento de Participantes");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        add(lblTitulo, BorderLayout.NORTH);


        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(500);
        splitPane.setResizeWeight(0.6);
        add(splitPane, BorderLayout.CENTER);


        JPanel panelEsquerda = new JPanel(new BorderLayout(10, 10));


        JPanel panelBusca = new JPanel(new BorderLayout(10, 10));
        txtBusca = new JTextField();
        txtBusca.putClientProperty("JTextField.placeholderText", "Buscar por nome ou e-mail...");
        JButton btnBusca = new JButton("Buscar");
        JButton btnLimparBusca = new JButton("Limpar");

        JPanel panelBotoesBusca = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        panelBotoesBusca.add(btnBusca);
        panelBotoesBusca.add(btnLimparBusca);

        panelBusca.add(txtBusca, BorderLayout.CENTER);
        panelBusca.add(panelBotoesBusca, BorderLayout.EAST);
        panelEsquerda.add(panelBusca, BorderLayout.NORTH);


        String[] colunas = {"ID", "Nome", "E-mail", "Telefone"};
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
        panelEsquerda.add(scrollPane, BorderLayout.CENTER);

        splitPane.setLeftComponent(panelEsquerda);


        JPanel panelDireita = new JPanel(new BorderLayout(10, 10));
        panelDireita.setBorder(BorderFactory.createTitledBorder("Detalhes do Participante"));

        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBorder(new EmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;


        gbc.gridx = 0; gbc.gridy = 0;
        panelForm.add(new JLabel("Nome: *"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtNome = new JTextField();
        panelForm.add(txtNome, gbc);


        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.0;
        panelForm.add(new JLabel("E-mail: *"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtEmail = new JTextField();
        panelForm.add(txtEmail, gbc);


        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.0;
        panelForm.add(new JLabel("Telefone:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtTelefone = new JTextField();
        panelForm.add(txtTelefone, gbc);

        panelDireita.add(panelForm, BorderLayout.NORTH);


        JPanel panelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        btnNovo = new JButton("Novo");
        btnSalvar = new JButton("Salvar");
        btnExcluir = new JButton("Excluir");


        btnSalvar.putClientProperty("JButton.buttonType", "roundRect");
        btnNovo.putClientProperty("JButton.buttonType", "roundRect");
        btnExcluir.putClientProperty("JButton.buttonType", "roundRect");

        panelBotoes.add(btnNovo);
        panelBotoes.add(btnSalvar);
        panelBotoes.add(btnExcluir);
        panelDireita.add(panelBotoes, BorderLayout.SOUTH);

        splitPane.setRightComponent(panelDireita);




        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow >= 0) {
                    int id = (int) tableModel.getValueAt(selectedRow, 0);
                    carregarParticipante(id);
                }
            }
        });


        btnNovo.addActionListener(e -> limparFormulario());


        btnSalvar.addActionListener(e -> salvarParticipante());


        btnExcluir.addActionListener(e -> excluirParticipante());


        btnBusca.addActionListener(e -> buscarParticipantes());
        txtBusca.addActionListener(e -> buscarParticipantes());


        btnLimparBusca.addActionListener(e -> {
            txtBusca.setText("");
            atualizarTabela();
        });


        atualizarTabela();
        limparFormulario();
    }

    private void atualizarTabela() {
        try {
            List<Participante> lista = participanteDAO.listAll();
            preencherTabela(lista);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar participantes: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void preencherTabela(List<Participante> lista) {
        tableModel.setRowCount(0);
        for (Participante p : lista) {
            tableModel.addRow(new Object[]{p.getId(), p.getNome(), p.getEmail(), p.getTelefone()});
        }
    }

    private void carregarParticipante(int id) {
        try {
            participanteSelecionado = participanteDAO.findById(id);
            if (participanteSelecionado != null) {
                txtNome.setText(participanteSelecionado.getNome());
                txtEmail.setText(participanteSelecionado.getEmail());
                txtTelefone.setText(participanteSelecionado.getTelefone() != null ? participanteSelecionado.getTelefone() : "");
                btnExcluir.setEnabled(true);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar detalhes: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limparFormulario() {
        participanteSelecionado = null;
        txtNome.setText("");
        txtEmail.setText("");
        txtTelefone.setText("");
        table.clearSelection();
        btnExcluir.setEnabled(false);
        txtNome.requestFocus();
    }

    private void salvarParticipante() {
        String nome = txtNome.getText().trim();
        String email = txtEmail.getText().trim();
        String telefone = txtTelefone.getText().trim();


        if (nome.isEmpty()) {
            JOptionPane.showMessageDialog(this, "O campo Nome é obrigatório!", "Validação", JOptionPane.WARNING_MESSAGE);
            txtNome.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "O campo E-mail é obrigatório!", "Validação", JOptionPane.WARNING_MESSAGE);
            txtEmail.requestFocus();
            return;
        }

        if (!email.contains("@") || !email.contains(".")) {
            JOptionPane.showMessageDialog(this, "Insira um endereço de e-mail válido!", "Validação", JOptionPane.WARNING_MESSAGE);
            txtEmail.requestFocus();
            return;
        }

        try {
            if (participanteSelecionado == null) {

                Participante novo = new Participante(nome, email, telefone);
                participanteDAO.create(novo);
                JOptionPane.showMessageDialog(this, "Participante cadastrado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            } else {

                participanteSelecionado.setNome(nome);
                participanteSelecionado.setEmail(email);
                participanteSelecionado.setTelefone(telefone);
                participanteDAO.update(participanteSelecionado);
                JOptionPane.showMessageDialog(this, "Cadastro atualizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            }
            atualizarTabela();
            limparFormulario();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar participante (verifique se o e-mail já está cadastrado): " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void excluirParticipante() {
        if (participanteSelecionado == null) return;

        int confirm = JOptionPane.showConfirmDialog(this, 
                "Tem certeza que deseja excluir o participante '" + participanteSelecionado.getNome() + "'?\n" +
                "Isso removerá também todas as inscrições dele em eventos.", 
                "Confirmar Exclusão", 
                JOptionPane.YES_NO_OPTION, 
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                participanteDAO.delete(participanteSelecionado.getId());
                JOptionPane.showMessageDialog(this, "Participante excluído com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                atualizarTabela();
                limparFormulario();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Erro ao excluir participante: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void buscarParticipantes() {
        String busca = txtBusca.getText().trim();
        if (busca.isEmpty()) {
            atualizarTabela();
            return;
        }
        try {
            List<Participante> lista = participanteDAO.search(busca);
            preencherTabela(lista);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao buscar: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}

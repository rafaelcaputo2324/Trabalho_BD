package gestaoeventos.ui;

import gestaoeventos.dao.EventoDAO;
import gestaoeventos.model.Evento;
import java.awt.*;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class EventosPanel extends JPanel {
    private final EventoDAO eventoDAO = new EventoDAO();

    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtTitulo;
    private JTextArea txtDescricao;
    private JTextField txtData;
    private JTextField txtLocal;
    private JTextField txtCapacidade;
    private JTextField txtBusca;

    private JButton btnNovo;
    private JButton btnSalvar;
    private JButton btnExcluir;

    private Evento eventoSelecionado = null;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public EventosPanel() {
        setLayout(new BorderLayout(15, 15));
        setBorder(new EmptyBorder(20, 20, 20, 20));


        JLabel lblTitulo = new JLabel("Gerenciamento de Eventos");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        add(lblTitulo, BorderLayout.NORTH);


        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(500);
        splitPane.setResizeWeight(0.6);
        add(splitPane, BorderLayout.CENTER);


        JPanel panelEsquerda = new JPanel(new BorderLayout(10, 10));


        JPanel panelBusca = new JPanel(new BorderLayout(10, 10));
        txtBusca = new JTextField();
        txtBusca.putClientProperty("JTextField.placeholderText", "Buscar por título ou local...");
        JButton btnBusca = new JButton("Buscar");
        JButton btnLimparBusca = new JButton("Limpar");

        JPanel panelBotoesBusca = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        panelBotoesBusca.add(btnBusca);
        panelBotoesBusca.add(btnLimparBusca);

        panelBusca.add(txtBusca, BorderLayout.CENTER);
        panelBusca.add(panelBotoesBusca, BorderLayout.EAST);
        panelEsquerda.add(panelBusca, BorderLayout.NORTH);


        String[] colunas = {"ID", "Título", "Data", "Local", "Capacidade"};
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
        panelDireita.setBorder(BorderFactory.createTitledBorder("Detalhes do Evento"));

        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBorder(new EmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;


        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.0;
        panelForm.add(new JLabel("Título: *"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtTitulo = new JTextField();
        panelForm.add(txtTitulo, gbc);


        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.0;
        panelForm.add(new JLabel("Descrição:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtDescricao = new JTextArea(4, 20);
        txtDescricao.setLineWrap(true);
        txtDescricao.setWrapStyleWord(true);
        JScrollPane scrollDesc = new JScrollPane(txtDescricao);
        panelForm.add(scrollDesc, gbc);


        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.0;
        panelForm.add(new JLabel("Data (AAAA-MM-DD): *"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtData = new JTextField();
        txtData.putClientProperty("JTextField.placeholderText", "Ex: 2026-09-15");
        panelForm.add(txtData, gbc);


        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0.0;
        panelForm.add(new JLabel("Local: *"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtLocal = new JTextField();
        panelForm.add(txtLocal, gbc);


        gbc.gridx = 0; gbc.gridy = 4; gbc.weightx = 0.0;
        panelForm.add(new JLabel("Capacidade Máx: *"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtCapacidade = new JTextField();
        panelForm.add(txtCapacidade, gbc);

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
                    carregarEvento(id);
                }
            }
        });


        btnNovo.addActionListener(e -> limparFormulario());


        btnSalvar.addActionListener(e -> salvarEvento());


        btnExcluir.addActionListener(e -> excluirEvento());


        btnBusca.addActionListener(e -> buscarEventos());
        txtBusca.addActionListener(e -> buscarEventos());


        btnLimparBusca.addActionListener(e -> {
            txtBusca.setText("");
            atualizarTabela();
        });


        atualizarTabela();
        limparFormulario();
    }

    private void atualizarTabela() {
        try {
            List<Evento> lista = eventoDAO.listAll();
            preencherTabela(lista);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar eventos: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void preencherTabela(List<Evento> lista) {
        tableModel.setRowCount(0);
        for (Evento ev : lista) {
            tableModel.addRow(new Object[]{ev.getId(), ev.getTitulo(), ev.getDataEvento().toString(), ev.getLocal(), ev.getCapacidadeMaxima()});
        }
    }

    private void carregarEvento(int id) {
        try {
            eventoSelecionado = eventoDAO.findById(id);
            if (eventoSelecionado != null) {
                txtTitulo.setText(eventoSelecionado.getTitulo());
                txtDescricao.setText(eventoSelecionado.getDescricao() != null ? eventoSelecionado.getDescricao() : "");
                txtData.setText(eventoSelecionado.getDataEvento().toString());
                txtLocal.setText(eventoSelecionado.getLocal());
                txtCapacidade.setText(String.valueOf(eventoSelecionado.getCapacidadeMaxima()));
                btnExcluir.setEnabled(true);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar detalhes: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limparFormulario() {
        eventoSelecionado = null;
        txtTitulo.setText("");
        txtDescricao.setText("");
        txtData.setText("");
        txtLocal.setText("");
        txtCapacidade.setText("");
        table.clearSelection();
        btnExcluir.setEnabled(false);
        txtTitulo.requestFocus();
    }

    private void salvarEvento() {
        String titulo = txtTitulo.getText().trim();
        String descricao = txtDescricao.getText().trim();
        String dataStr = txtData.getText().trim();
        String local = txtLocal.getText().trim();
        String capStr = txtCapacidade.getText().trim();


        if (titulo.isEmpty() || dataStr.isEmpty() || local.isEmpty() || capStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos os campos marcados com * são obrigatórios!", "Validação", JOptionPane.WARNING_MESSAGE);
            return;
        }


        Date sqlDate;
        try {
            dateFormat.setLenient(false);
            java.util.Date parsedDate = dateFormat.parse(dataStr);
            sqlDate = new Date(parsedDate.getTime());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Data inválida! Use o formato AAAA-MM-DD (Ex: 2026-09-15).", "Validação", JOptionPane.WARNING_MESSAGE);
            txtData.requestFocus();
            return;
        }


        int capacidade;
        try {
            capacidade = Integer.parseInt(capStr);
            if (capacidade <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "A capacidade máxima deve ser um número inteiro maior que 0!", "Validação", JOptionPane.WARNING_MESSAGE);
            txtCapacidade.requestFocus();
            return;
        }

        try {
            if (eventoSelecionado == null) {

                Evento novo = new Evento(titulo, descricao, sqlDate, local, capacidade);
                eventoDAO.create(novo);
                JOptionPane.showMessageDialog(this, "Evento criado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            } else {

                eventoSelecionado.setTitulo(titulo);
                eventoSelecionado.setDescricao(descricao);
                eventoSelecionado.setDataEvento(sqlDate);
                eventoSelecionado.setLocal(local);
                eventoSelecionado.setCapacidadeMaxima(capacidade);
                eventoDAO.update(eventoSelecionado);
                JOptionPane.showMessageDialog(this, "Evento atualizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            }
            atualizarTabela();
            limparFormulario();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar evento: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void excluirEvento() {
        if (eventoSelecionado == null) return;

        int confirm = JOptionPane.showConfirmDialog(this, 
                "Tem certeza que deseja excluir o evento '" + eventoSelecionado.getTitulo() + "'?\n" +
                "Isso apagará permanentemente todas as inscrições e certificados associados.", 
                "Confirmar Exclusão", 
                JOptionPane.YES_NO_OPTION, 
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                eventoDAO.delete(eventoSelecionado.getId());
                JOptionPane.showMessageDialog(this, "Evento excluído com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                atualizarTabela();
                limparFormulario();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Erro ao excluir evento: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void buscarEventos() {
        String busca = txtBusca.getText().trim();
        if (busca.isEmpty()) {
            atualizarTabela();
            return;
        }
        try {
            List<Evento> lista = eventoDAO.search(busca);
            preencherTabela(lista);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao buscar: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}

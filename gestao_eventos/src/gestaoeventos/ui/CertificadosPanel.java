package gestaoeventos.ui;

import gestaoeventos.dao.CertificadoDAO;
import gestaoeventos.model.Certificado;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class CertificadosPanel extends JPanel {
    private final CertificadoDAO certificadoDAO = new CertificadoDAO();

    private JTable table;
    private DefaultTableModel tableModel;
    private JTextArea txtDiploma;
    private JButton btnExcluir;

    private Certificado certificadoSelecionado = null;

    public CertificadosPanel() {
        setLayout(new BorderLayout(15, 15));
        setBorder(new EmptyBorder(20, 20, 20, 20));


        JLabel lblTitulo = new JLabel("Consulta de Certificados Emitidos");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        add(lblTitulo, BorderLayout.NORTH);


        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(450);
        splitPane.setResizeWeight(0.5);
        add(splitPane, BorderLayout.CENTER);


        JPanel panelEsquerda = new JPanel(new BorderLayout(10, 10));

        String[] colunas = {"ID", "Participante", "Evento", "Cód. Autenticação"};
        tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setReorderingAllowed(false);
        JScrollPane scrollTabela = new JScrollPane(table);
        panelEsquerda.add(scrollTabela, BorderLayout.CENTER);

        splitPane.setLeftComponent(panelEsquerda);


        JPanel panelDireita = new JPanel(new BorderLayout(10, 10));
        panelDireita.setBorder(BorderFactory.createTitledBorder("Visualização do Certificado"));

        txtDiploma = new JTextArea();
        txtDiploma.setFont(new Font("Monospaced", Font.PLAIN, 12));
        txtDiploma.setEditable(false);
        txtDiploma.setMargin(new Insets(15, 15, 15, 15));
        JScrollPane scrollDiploma = new JScrollPane(txtDiploma);
        panelDireita.add(scrollDiploma, BorderLayout.CENTER);


        JPanel panelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        btnExcluir = new JButton("Revogar Certificado");
        btnExcluir.putClientProperty("JButton.buttonType", "roundRect");
        btnExcluir.setEnabled(false);
        panelBotoes.add(btnExcluir);
        panelDireita.add(panelBotoes, BorderLayout.SOUTH);

        splitPane.setRightComponent(panelDireita);




        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    int id = (int) tableModel.getValueAt(row, 0);
                    carregarCertificado(id);
                } else {
                    limparDiploma();
                }
            }
        });


        btnExcluir.addActionListener(e -> excluirCertificado());


        atualizarTabela();
        limparDiploma();
    }

    public void atualizarTabela() {
        try {
            List<Certificado> lista = certificadoDAO.listAll();
            tableModel.setRowCount(0);
            for (Certificado c : lista) {
                tableModel.addRow(new Object[]{
                    c.getId(),
                    c.getParticipanteNome(),
                    c.getEventoTitulo(),
                    c.getCodigoAutenticacao()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar certificados: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void carregarCertificado(int id) {
        try {
            certificadoSelecionado = certificadoDAO.findById(id);
            if (certificadoSelecionado != null) {
                btnExcluir.setEnabled(true);
                exibirDiploma(certificadoSelecionado);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar certificado: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void exibirDiploma(Certificado cert) {
        String texto = " ╔════════════════════════════════════════════════════════════╗\n" +
                       " ║                  DIPLOMA DE PARTICIPAÇÃO                   ║\n" +
                       " ╚════════════════════════════════════════════════════════════╝\n\n" +
                       "   Certificamos para os devidos fins de direito que\n\n" +
                       "   " + cert.getParticipanteNome().toUpperCase() + "\n" +
                       "   (E-mail: " + cert.getParticipanteEmail() + ")\n\n" +
                       "   completou com sucesso a sua participação no evento acadêmico\n\n" +
                       "   \"" + cert.getEventoTitulo().toUpperCase() + "\"\n\n" +
                       "   ocorrido em: " + cert.getEventoData() + ".\n\n" +
                       "   ──────────────────────────────────────────────────────────\n" +
                       "   Código de Validação: " + cert.getCodigoAutenticacao() + "\n" +
                       "   Emitido via sistema em: " + cert.getDataEmissao() + "\n" +
                       "   ──────────────────────────────────────────────────────────\n";
        txtDiploma.setText(texto);
    }

    private void limparDiploma() {
        certificadoSelecionado = null;
        txtDiploma.setText("Selecione um certificado na tabela à esquerda para visualizar o preview do diploma.");
        btnExcluir.setEnabled(false);
    }

    private void excluirCertificado() {
        if (certificadoSelecionado == null) return;

        int confirm = JOptionPane.showConfirmDialog(this, 
                "Tem certeza que deseja revogar/excluir o certificado '" + certificadoSelecionado.getCodigoAutenticacao() + "'?\n" +
                "O participante continuará inscrito no evento, mas seu certificado será apagado.", 
                "Confirmar Revogação", 
                JOptionPane.YES_NO_OPTION, 
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                certificadoDAO.delete(certificadoSelecionado.getId());
                JOptionPane.showMessageDialog(this, "Certificado revogado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                atualizarTabela();
                limparDiploma();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Erro ao excluir certificado: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}

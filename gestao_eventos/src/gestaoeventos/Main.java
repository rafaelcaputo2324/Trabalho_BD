package gestaoeventos;

import gestaoeventos.dao.DatabaseConnection;
import gestaoeventos.ui.MainFrame;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        System.out.println("Iniciando o sistema...");


        DatabaseConnection.initializeDatabase();


        SwingUtilities.invokeLater(() -> {
            try {
                MainFrame frame = new MainFrame();
                frame.setVisible(true);
                System.out.println("Interface gráfica iniciada com sucesso!");
            } catch (Exception e) {
                System.err.println("Erro ao iniciar a interface gráfica: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }
}

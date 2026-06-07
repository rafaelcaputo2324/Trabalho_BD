package gestaoeventos.dao;

import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DatabaseConnection {
    private static final String URL = "jdbc:h2:file:C:/Users/rafael/Documents/gestao_eventos/db/gestao_eventos_db;DB_CLOSE_ON_EXIT=TRUE";
    private static final String USER = "sa";
    private static final String PASSWORD = "";
    private static final String SCHEMA_PATH = "C:/Users/rafael/Documents/gestao_eventos/db/schema.sql";

    static {
        try {

            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("Driver H2 não encontrado: " + e.getMessage());
        }
    }

    public static Connection getConnection() throws Exception {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }


    public static void initializeDatabase() {
        Connection conn = null;
        try {
            conn = getConnection();


            boolean tablesExist = false;
            try (ResultSet rs = conn.getMetaData().getTables(null, null, "PARTICIPANTES", null)) {
                if (rs.next()) {
                    tablesExist = true;
                }
            }

            if (!tablesExist) {
                System.out.println("Banco de dados não inicializado. Criando tabelas a partir de " + SCHEMA_PATH + "...");

                File schemaFile = new File(SCHEMA_PATH);
                if (!schemaFile.exists()) {
                    throw new RuntimeException("Arquivo de schema não encontrado em: " + SCHEMA_PATH);
                }


                org.h2.tools.RunScript.execute(conn, new FileReader(schemaFile));
                System.out.println("Banco de dados inicializado com sucesso!");
            } else {
                System.out.println("Banco de dados já existente.");
            }
        } catch (Exception e) {
            System.err.println("Erro ao inicializar o banco de dados: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (Exception e) {

                }
            }
        }
    }
}

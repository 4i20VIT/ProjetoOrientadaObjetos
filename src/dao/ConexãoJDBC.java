package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class ConexãoJDBC {

    private static final String URL = "jdbc:mysql://localhost:3306/yourdatabase";
    private static final String USER = "yourusername";
    private static final String PASSWORD = "yourpassword";

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("Conectando ao banco de dados.");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } 
        catch (ClassNotFoundException ex) {
            System.out.println("Erro ao conectar ao banco de dados.");
            throw new SQLException(ex.getMessage());
        }
        catch (SQLException e) {
            System.out.println("Erro ao acessar o banco de dados.");
            throw new SQLException(e.getMessage());
        }

        
    }
    
}

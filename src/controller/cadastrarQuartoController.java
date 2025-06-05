package controller;

import vo.Quarto;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;

public class cadastrarQuartoController {
    @FXML private TextField txtNumeroQuarto;
    @FXML private TextField txtCapacidade;
    @FXML private TextField txtDiaria;
    @FXML private Button btnCadastrar;
    @FXML private Button btnExcluir;
    @FXML private TableView<Quarto> tabelaQuartos;
    @FXML private TableColumn<Quarto, String> colNumero;
    @FXML private TableColumn<Quarto, Integer> colCapacidade;
    @FXML private TableColumn<Quarto, Double> colDiaria;

    private Connection connection;

    public void initialize() {
        conectarBanco();
        carregarQuartos();
    }

    private void conectarBanco() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel", "root", "senha");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void carregarQuartos() {
        ObservableList<Quarto> lista = FXCollections.observableArrayList();
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT numero, capacidade, diaria FROM quartos");
            while (rs.next()) {
                lista.add(new Quarto(rs.getInt("numero"), rs.getInt("capacidade"), rs.getDouble("diaria")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        tabelaQuartos.setItems(lista);
    }

    @FXML
    private void cadastrarQuarto() {
        String numero = txtNumeroQuarto.getText();
        int capacidade = Integer.parseInt(txtCapacidade.getText());
        double diaria = Double.parseDouble(txtDiaria.getText());

        try {
            PreparedStatement stmt = connection.prepareStatement("INSERT INTO quartos (numero, capacidade, diaria) VALUES (?, ?, ?)");
            stmt.setString(1, numero);
            stmt.setInt(2, capacidade);
            stmt.setDouble(3, diaria);
            stmt.executeUpdate();
            carregarQuartos();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void excluirQuarto() {
        Quarto quartoSelecionado = tabelaQuartos.getSelectionModel().getSelectedItem();
        if (quartoSelecionado == null) return;

        try {
            PreparedStatement stmt = connection.prepareStatement("DELETE FROM quartos WHERE numero = ?");
            stmt.setString(1, String.valueOf(quartoSelecionado.getNumero()));
            stmt.executeUpdate();
            carregarQuartos();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

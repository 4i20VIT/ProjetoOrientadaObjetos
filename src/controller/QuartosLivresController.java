package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import vo.Cliente;
import vo.Quarto;
import dao.ConexãoJDBC;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class QuartosLivresController {


    @FXML
    private TableView<Quarto> tabelaQuartosLivres;
    @FXML
    private TableColumn<Quarto, Integer> colNumeroQuartos;
    @FXML
    private TableColumn<Quarto, Integer> colCapacidadeQuartos;
    @FXML
    private TableColumn<Quarto, Double> colDiariaQuartos;
    @FXML
    private TableView<Cliente> tabelaClientesCheckin;
    @FXML
    private TableColumn<Cliente, String> colNomeCliente;
    @FXML
    private TableColumn<Cliente, String> colCpfCliente;
    @FXML
    private TableColumn<Cliente, String> colNascimentoCliente;
    @FXML
    private TextField txtPesquisaCliente;
    @FXML
    private Button btnRealizarCheckin;

    private ObservableList<Quarto> listaQuartosLivres = FXCollections.observableArrayList();
    private ObservableList<Cliente> listaClientes = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        configurarTabelas();
            carregarClientes();
            carregarQuartosLivres();
    }

    private void configurarTabelas() {
        colNumeroQuartos.setCellValueFactory(new PropertyValueFactory<>("numero"));
        colCapacidadeQuartos.setCellValueFactory(new PropertyValueFactory<>("capacidade"));
        colDiariaQuartos.setCellValueFactory(new PropertyValueFactory<>("diaria"));

        colNomeCliente.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colCpfCliente.setCellValueFactory(new PropertyValueFactory<>("cpf"));
        colNascimentoCliente.setCellValueFactory(new PropertyValueFactory<>("dataNascimento"));

        tabelaQuartosLivres.setItems(listaQuartosLivres);
        tabelaClientesCheckin.setItems(listaClientes);
    }

    private void carregarQuartosLivres() {
        listaQuartosLivres.clear();
        String sql = "SELECT * FROM quartos WHERE ocupado = FALSE";
        try (Connection conn = ConexãoJDBC.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                listaQuartosLivres.add(new Quarto(
                        rs.getInt("numero"),
                        rs.getInt("capacidade"),
                        rs.getDouble("diaria")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void carregarClientes() {
        listaClientes.clear();
        String sql = "SELECT * FROM clientes";
        try (Connection conn = ConexãoJDBC.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                listaClientes.add(new Cliente(
                        rs.getString("nome"),
                        rs.getString("cpf"),
                        rs.getDate("dataNasc").toLocalDate(),
                        rs.getString("sexo"),
                        rs.getString("telefone")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void realizarCheckin() {
        Quarto quartoSelecionado = tabelaQuartosLivres.getSelectionModel().getSelectedItem();
        Cliente clienteSelecionado = tabelaClientesCheckin.getSelectionModel().getSelectedItem();

        if (quartoSelecionado == null || clienteSelecionado == null) {
            mostrarAlerta("Erro", "Selecione um quarto e um cliente para realizar o check-in.");
            return;
        }

        String sql = "INSERT INTO checkins (cliente_cpf, quarto_numero, data_checkin) VALUES (?, ?, NOW())";
        String updateQuarto = "UPDATE quartos SET ocupado = TRUE WHERE numero = ?";
        try (Connection conn = ConexãoJDBC.getConnection();
             PreparedStatement stmtCheckin = conn.prepareStatement(sql);
             PreparedStatement stmtUpdate = conn.prepareStatement(updateQuarto)) {

            stmtCheckin.setString(1, String.valueOf(clienteSelecionado.getCpf()));
            stmtCheckin.setInt(2, quartoSelecionado.getNumero());
            stmtCheckin.executeUpdate();

            stmtUpdate.setInt(1, quartoSelecionado.getNumero());
            stmtUpdate.executeUpdate();

            listaQuartosLivres.remove(quartoSelecionado);
            mostrarAlerta("Sucesso", "Check-in realizado com sucesso!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void mostrarAlerta(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}

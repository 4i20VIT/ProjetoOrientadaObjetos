package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import vo.Checkin;
import dao.ConexãoJDBC;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class CheckoutController {

    @FXML
    private TableView<Checkin> tabelaQuartos;
    @FXML
    private TableColumn<Checkin, Integer> colNumeroQuarto;
    @FXML
    private TableColumn<Checkin, String> colClienteQuarto;
    @FXML
    private TableColumn<Checkin, String> colCheckinQuarto;
    @FXML
    private TableColumn<Checkin, String> colCheckoutQuarto;
    @FXML
    private TextField txtQuartosOcupados;
    @FXML
    private TextField txtRealizarCheckout;
    @FXML
    private Button btnRealizarCheckout;
    @FXML
    private Label labelValorFinal;

    private ObservableList<Checkin> listaCheckins = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        configurarTabela();
        carregarCheckins();
    }

    private void configurarTabela() {
        colNumeroQuarto.setCellValueFactory(new PropertyValueFactory<>("quartoNumero"));
        colClienteQuarto.setCellValueFactory(new PropertyValueFactory<>("clienteNome"));
        colCheckinQuarto.setCellValueFactory(new PropertyValueFactory<>("dataCheckin"));
        colCheckoutQuarto.setCellValueFactory(new PropertyValueFactory<>("dataCheckout"));

        tabelaQuartos.setItems(listaCheckins);
    }

    private void carregarCheckins() {
        listaCheckins.clear();
        String sql = "SELECT c.id, c.cliente_cpf, cli.nome, c.quarto_numero, q.diaria, c.data_checkin " +
                     "FROM checkins c " +
                     "JOIN clientes cli ON c.cliente_cpf = cli.cpf " +
                     "JOIN quartos q ON c.quarto_numero = q.numero " +
                     "WHERE c.data_checkout IS NULL";

        try (Connection conn = ConexãoJDBC	.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                listaCheckins.add(new Checkin(
                        rs.getInt("id"),
                        rs.getInt("quarto_numero"),
                        rs.getString("nome"),
                        rs.getDate("data_checkin").toLocalDate(),
                        rs.getDouble("diaria")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        txtQuartosOcupados.setText(String.valueOf(listaCheckins.size()));
    }

    @FXML
    private void realizarCheckout() {
        Checkin checkinSelecionado = tabelaQuartos.getSelectionModel().getSelectedItem();
        if (checkinSelecionado == null) {
            mostrarAlerta("Erro", "Selecione um cliente para realizar o check-out.");
            return;
        }

        LocalDate dataCheckin = checkinSelecionado.getDataCheckin();
        LocalDate dataCheckout = LocalDate.now();
        long diasHospedado = ChronoUnit.DAYS.between(dataCheckin, dataCheckout);
        if (diasHospedado == 0) diasHospedado = 1; // Garantir pelo menos 1 diária cobrada

        double valorFinal = diasHospedado * checkinSelecionado.getDiaria();
        labelValorFinal.setText(String.format("R$ %.2f", valorFinal));

        String sqlCheckout = "UPDATE checkins SET data_checkout = NOW() WHERE id = ?";
        String sqlAtualizarQuarto = "UPDATE quartos SET ocupado = FALSE WHERE numero = ?";

        try (Connection conn = ConexãoJDBC.getConnection();
             PreparedStatement stmtCheckout = conn.prepareStatement(sqlCheckout);
             PreparedStatement stmtAtualizarQuarto = conn.prepareStatement(sqlAtualizarQuarto)) {

            stmtCheckout.setInt(1, checkinSelecionado.getId());
            stmtCheckout.executeUpdate();

            stmtAtualizarQuarto.setInt(1, checkinSelecionado.getQuartoNumero());
            stmtAtualizarQuarto.executeUpdate();

            listaCheckins.remove(checkinSelecionado);
            txtQuartosOcupados.setText(String.valueOf(listaCheckins.size()));

            mostrarAlerta("Sucesso", "Check-out realizado com sucesso!");
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
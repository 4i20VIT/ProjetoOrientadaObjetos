package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;
import vo.Quarto;
import vo.Produto;

public class ServiçoQuartoController {
    @FXML private TableView<Quarto> tabelaQuartos;
    @FXML private TableColumn<Quarto, String> colNumeroQuarto;
    @FXML private TableColumn<Quarto, String> colClienteQuarto;
    @FXML private TableView<Produto> tabelaProdutos;
    @FXML private TableColumn<Produto, String> colProduto;
    @FXML private TableColumn<Produto, Double> colValorProduto;
    @FXML private RadioButton radioCliente;
    @FXML private RadioButton radioQuarto;
    @FXML private TextField textFieldPesquisa;
    @FXML private Button btnConfirmarPesquisa;
    @FXML private Button btnAdicionarContaQuarto;

    private Connection connection;

    public void initialize() {
        conectarBanco();
        carregarQuartos();
        carregarProdutos();
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
            ResultSet rs = stmt.executeQuery("SELECT numero, cliente FROM quartos");
            while (rs.next()) {
                lista.add(new Quarto(rs.getInt("numero"), 0, 80.0));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        tabelaQuartos.setItems(lista);
    }

    private void carregarProdutos() {
        ObservableList<Produto> lista = FXCollections.observableArrayList();
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT nome, valor FROM produtos");
            while (rs.next()) {
                lista.add(new Produto(rs.getString("nome"), rs.getDouble("valor")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        tabelaProdutos.setItems(lista);
    }

    @FXML
    private void pesquisar() {
        String filtro = textFieldPesquisa.getText();
        if (filtro.isEmpty()) return;

        String query = "";
        if (radioCliente.isSelected()) {
            query = "SELECT numero, cliente FROM quartos WHERE cliente LIKE ?";
        } else if (radioQuarto.isSelected()) {
            query = "SELECT numero, cliente FROM quartos WHERE numero LIKE ?";
        }

        ObservableList<Quarto> lista = FXCollections.observableArrayList();
        try {
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, "%" + filtro + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                lista.add(new Quarto(rs.getInt("numero"), 0, 80.0));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        tabelaQuartos.setItems(lista);
    }

    @FXML
    private void adicionarContaQuarto() {
        Quarto quartoSelecionado = tabelaQuartos.getSelectionModel().getSelectedItem();
        Produto produtoSelecionado = tabelaProdutos.getSelectionModel().getSelectedItem();
        if (quartoSelecionado == null || produtoSelecionado == null) return;

        try {
            PreparedStatement stmt = connection.prepareStatement("INSERT INTO contas (quarto, produto, valor) VALUES (?, ?, ?)");
            stmt.setInt(1, quartoSelecionado.getNumero());
            stmt.setString(2, produtoSelecionado.getNome());
            stmt.setDouble(3, produtoSelecionado.getValor());
            stmt.executeUpdate();
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Produto adicionado à conta!");
            alert.show();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

package controller;

import vo.Produto;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class CadastrarServiçoController {

    @FXML
    private TableView<Produto> tabelaProdutos;
    @FXML
    private TableColumn<Produto, String> colNomeProduto;
    @FXML
    private TableColumn<Produto, Double> colValorProduto;
    @FXML
    private TextField txtNomeProduto, txtValorProduto;
    @FXML
    private Button btnCadastrarProduto, btnExcluirProduto;

    private ObservableList<Produto> listaProdutos;

    public CadastrarServiçoController() {
        listaProdutos = FXCollections.observableArrayList();
    }

    @FXML
    private void initialize() {
        // Inicializando as colunas da tabela
        colNomeProduto.setCellValueFactory(cellData -> cellData.getValue().nomeProperty());
        colValorProduto.setCellValueFactory(cellData -> cellData.getValue().valorProperty().asObject());
        
        // Carregar a tabela com os produtos cadastrados
        tabelaProdutos.setItems(listaProdutos);
    }

    // Método para cadastrar um novo produto
    @FXML
    private void cadastrarProduto(ActionEvent event) {
        String nome = txtNomeProduto.getText();
        String valorText = txtValorProduto.getText();

        if (nome.isEmpty() || valorText.isEmpty()) {
            showAlert(AlertType.ERROR, "Campos vazios", "Por favor, preencha todos os campos.");
            return;
        }

        try {
            double valor = Double.parseDouble(valorText);
            Produto produto = new Produto(nome, valor);
            listaProdutos.add(produto);
            txtNomeProduto.clear();
            txtValorProduto.clear();
        } catch (NumberFormatException e) {
            showAlert(AlertType.ERROR, "Valor inválido", "O valor do produto deve ser um número.");
        }
    }

    // Método para excluir um produto da tabela
    @FXML
    private void excluirProduto(ActionEvent event) {
        Produto produtoSelecionado = tabelaProdutos.getSelectionModel().getSelectedItem();
        if (produtoSelecionado != null) {
            listaProdutos.remove(produtoSelecionado);
        } else {
            showAlert(AlertType.WARNING, "Nenhum produto selecionado", "Selecione um produto para excluir.");
        }
    }

    // Método para exibir alertas
    private void showAlert(AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
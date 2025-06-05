package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import vo.Cliente;
import dao.ConexãoJDBC;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CadastrarClienteController {

    @FXML
    private TextField campoNome;
    @FXML
    private TextField campoCpf;
    @FXML
    private TextField campoDataNasc;
    @FXML
    private TextField campoSexo;
    @FXML
    private TextField campoTelefone;
    @FXML
    private TableView<Cliente> tabelaClientes;
    @FXML
    private TableColumn<Cliente, String> colNome;
    @FXML
    private TableColumn<Cliente, String> colCpf;
    @FXML
    private TableColumn<Cliente, String> colContato;
    @FXML
    private TableColumn<Cliente, String> colSexo;
    @FXML
    private TableColumn<Cliente, Integer> colIdade;
    @FXML
    private Button btnCadastrar;
    @FXML
    private Button btnExcluir;
    
    private ObservableList<Cliente> listaClientes = FXCollections.observableArrayList();
    
    @FXML
    public void initialize() {
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colCpf.setCellValueFactory(new PropertyValueFactory<>("cpf"));
        colContato.setCellValueFactory(new PropertyValueFactory<>("telefone"));
        colSexo.setCellValueFactory(new PropertyValueFactory<>("sexo"));
        colIdade.setCellValueFactory(new PropertyValueFactory<>("idade"));
        
        carregarClientes();
        tabelaClientes.setItems(listaClientes);
    }
    
    @FXML
    private void cadastrarCliente() {
        String nome = campoNome.getText();
        String cpf = campoCpf.getText();
        String dataNasc = campoDataNasc.getText();
        String sexo = campoSexo.getText();
        String telefone = campoTelefone.getText();

        String sql = "INSERT INTO clientes (nome, cpf, data_nasc, sexo, telefone) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = ConexãoJDBC.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nome);
            stmt.setString(2, cpf);
            stmt.setString(3, dataNasc);
            stmt.setString(4, sexo);
            stmt.setString(5, telefone);
            stmt.executeUpdate();
            carregarClientes();
            limparCampos();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void excluirCliente() {
        Cliente clienteSelecionado = tabelaClientes.getSelectionModel().getSelectedItem();
        if (clienteSelecionado != null) {
            String sql = "DELETE FROM clientes WHERE cpf = ?";
            try (Connection conn = ConexãoJDBC.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, String.valueOf(clienteSelecionado.getCpf()));
                stmt.executeUpdate();
                listaClientes.remove(clienteSelecionado);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    private void carregarClientes() {
        listaClientes.clear();
        String sql = "SELECT * FROM clientes";
        try (Connection conn = ConexãoJDBC.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Cliente cliente = new Cliente(
                        rs.getString("nome"),
                        rs.getString("cpf"),
                        rs.getDate("data_nasc").toLocalDate(),
                        rs.getString("sexo"),
                        rs.getString("telefone")
                );
                listaClientes.add(cliente);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private void limparCampos() {
        campoNome.clear();
        campoCpf.clear();
        campoDataNasc.clear();
        campoSexo.clear();
        campoTelefone.clear();
    }
}
package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.Parent;

import java.io.IOException;

public class Controller {

    @FXML
    private Button btnServicoQuarto, btnSair, btnCadastrarCliente, btnQuartosOcupados, btnQuartosLivres, btnCadastrarQuarto, btnCadastrarServico;

    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private void abrirServicoQuarto(ActionEvent event) throws IOException {
        trocarTela("ServicoQuarto.fxml");
    }

    @FXML
    private void abrirQuartosOcupados(ActionEvent event) throws IOException {
        trocarTela("QuartosOcupados.fxml");
    }

    @FXML
    private void abrirQuartosLivres(ActionEvent event) throws IOException {
        trocarTela("QuartosLivres.fxml");
    }

    @FXML
    private void abrirCadastroCliente(ActionEvent event) throws IOException {
        trocarTela("CadastroCliente.fxml");
    }

    @FXML
    private void abrirCadastroQuarto(ActionEvent event) throws IOException {
        trocarTela("CadastroQuarto.fxml");
    }

    @FXML
    private void abrirCadastroServico(ActionEvent event) throws IOException {
        trocarTela("CadastroServico.fxml");
    }

    @FXML
    private void sairDoSistema(ActionEvent event) {
        System.exit(0);
    }

    private void trocarTela(String fxml) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
        Parent novaTela = loader.load();
        stage.setScene(new Scene(novaTela));
        stage.show();
    }
}

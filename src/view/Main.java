package view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.stage.Stage;
import controller.Controller;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("menu.fxml"));
        Parent root = loader.load();

        Controller controller = loader.getController();
        controller.setStage(primaryStage);

        primaryStage.setScene(new Scene(root));
        primaryStage.setTitle("Sistema de Gestão de Hotel");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

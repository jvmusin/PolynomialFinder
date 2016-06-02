package core;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.stage.Stage;

public class MainApp extends Application {

    public static void main(String[] args) throws Exception {
        launch(args);
    }

    public void start(Stage stage) throws Exception {
        SplitPane root = new FXMLLoader().load(getClass().getResourceAsStream("/fxml/PolynomialFinder.fxml"));
        stage.setScene(new Scene(root, 800, 600));
        stage.setTitle("Root finder");
        stage.show();
    }
}

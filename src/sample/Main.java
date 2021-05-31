package sample;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
//import java.awt.event.ActionEvent;


public class Main extends Application {

    @FXML
    public TextField cas;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../sample.fxml"));

        primaryStage.setTitle("Autoservis");
        primaryStage.setScene(new Scene(root, 350, 420));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);

    }

}

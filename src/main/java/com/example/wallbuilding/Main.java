package com.example.wallbuilding;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage initialStage) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("startPage.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);

        initialStage.setScene(scene);
        initialStage.setTitle("Enter wall dimensions");
        initialStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

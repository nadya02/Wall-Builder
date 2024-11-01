package com.example.wallbuilding;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class StartPageController {
    @FXML private TextField inputHeight;

    @FXML private TextField inputWidth;

    @FXML
    private void onButtonBuildWallClick(){
        try {
            int wallWidth = Integer.parseInt(inputWidth.getText().trim());
            int wallHeight = Integer.parseInt(inputHeight.getText().trim());

            BrickWall wall = new BrickWall(wallWidth, wallHeight);
            //Loads the wall page
            FXMLLoader loader = new FXMLLoader(getClass().getResource("brickWallPage.fxml"));
            Parent root = loader.load();

            BrickWallPageController brickWallPageController = loader.getController();
            brickWallPageController.initializeWall(wall);

            Stage stage = (Stage) inputWidth.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Input Error");
            alert.setContentText("Please enter valid numbers for width and height.");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

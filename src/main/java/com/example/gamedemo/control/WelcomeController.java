package com.example.gamedemo.control;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import com.example.gamedemo.MainApplication;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;

public class WelcomeController {

    @FXML
    private Button playButton;

    @FXML
    private Button instructionsButton;

    @FXML
    private void onPlayButton() {
        MainApplication mainApp = MainApplication.getInstance();
        mainApp.cambiarEscena();
    }

    @FXML
    private void onExitButton() {
        Alert alert = new Alert(AlertType.CONFIRMATION);

        alert.setTitle("OMG!");
        alert.setHeaderText("¿Do you want to exit?");

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets()
                .add(getClass().getResource("/com/example/gamedemo/styles.css").toExternalForm());
        dialogPane.getStyleClass().add("dialog-pane");

        // Agregar las opciones de botones
        ButtonType buttonTypeYes = new ButtonType("Yes");
        ButtonType buttonTypeNo = new ButtonType("No");

        alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

        // Obtener la respuesta del usuario
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == buttonTypeYes) {
            Platform.exit();
        }
    }

    /**
     * @return Button return the playButton
     */
    public Button getPlayButton() {
        return playButton;
    }

    /**
     * @param playButton the playButton to set
     */
    public void setPlayButton(Button playButton) {
        this.playButton = playButton;
    }

    /**
     * @return Button return the instructionsButton
     */
    public Button getInstructionsButton() {
        return instructionsButton;
    }

    /**
     * @param instructionsButton the instructionsButton to set
     */
    public void setInstructionsButton(Button instructionsButton) {
        this.instructionsButton = instructionsButton;
    }

}

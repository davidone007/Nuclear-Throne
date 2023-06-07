package com.example.gamedemo.control;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import java.net.URL;
import java.util.ResourceBundle;

public class WelcomeController implements Initializable {

    @FXML
    private Button playButton;

    @FXML
    private Button instructionsButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Lógica de inicialización, si es necesario
    }

    @FXML
    private void handlePlayButton() {
        // Lógica para el botón "Jugar"
        // Puedes abrir la pantalla de juego o realizar cualquier otra acción necesaria
    }

    @FXML
    private void handleInstructionsButton() {
        // Lógica para el botón "Instrucciones"
        // Puedes abrir una pantalla de instrucciones o realizar cualquier otra acción
        // necesaria
    }
}

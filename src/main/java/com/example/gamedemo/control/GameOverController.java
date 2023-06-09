package com.example.gamedemo.control;

import com.example.gamedemo.MainApplication;
import com.example.gamedemo.model.*;
import com.example.gamedemo.screens.BaseScreen;
import com.example.gamedemo.screens.Scenario_1;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Screen;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;
import java.util.ResourceBundle;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Alert.AlertType;

public class GameOverController implements Initializable {



    private Clip backgroundMusic;

    public void initialize(URL url, ResourceBundle resourceBundle) {
        playBackgroundMusic();
    }

    public void playBackgroundMusic() {
        try {
            InputStream audioSrc = getClass().getResourceAsStream("/sounds/gameOver.wav");
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new BufferedInputStream(audioSrc));
            backgroundMusic = AudioSystem.getClip();
            backgroundMusic.open(audioInputStream);
            backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }

    }

    @FXML
    private void onTryAgainButton() throws IOException {
        MainApplication mainApp = MainApplication.getInstance();
        mainApp.changeSceneTryAgain();
        backgroundMusic.stop();
    }

    @FXML
    private void onExitButton() {
        Alert alert = new Alert(AlertType.CONFIRMATION);

        alert.setTitle("Exit");
        alert.setHeaderText("Are you sure you want to quit the game?");

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

}

package com.example.gamedemo;

import com.example.gamedemo.control.MainController;
import com.example.gamedemo.control.WelcomeController;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApplication extends Application {
    private Stage primaryStage;
    private static MainApplication instance;
    private MediaPlayer mediaPlayer;

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        this.primaryStage = primaryStage;
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("welcome-view.fxml"));
        Pane root = fxmlLoader.load();
        Screen screen = Screen.getPrimary();
        double screenWidth = screen.getBounds().getWidth();
        double screenHeight = screen.getBounds().getHeight();
        Scene scene = new Scene(root, screenWidth, screenHeight);
        primaryStage.setTitle("Nuclear-Throne");
        primaryStage.setFullScreen(true);
        Image logoImage = new Image(getClass().getResourceAsStream("/animations/logos/logo.png"));
        primaryStage.getIcons().add(logoImage);
        primaryStage.setScene(scene);

        primaryStage.setWidth(screenWidth);
        primaryStage.setHeight(screenHeight);

        // Agregar video de fondo
        String videoFile = getClass().getResource("/videos/initGame.mp4").toExternalForm();
        Media media = new Media(videoFile);
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);

        // Crear MediaView y establecer el MediaPlayer
        MediaView mediaView = new MediaView(mediaPlayer);
        mediaView.setFitWidth(screenWidth);
        mediaView.setFitHeight(screenHeight);

        StackPane mediaContainer = (StackPane) fxmlLoader.getNamespace().get("mediaContainer");
        mediaContainer.getChildren().add(mediaView);

        mediaPlayer.play();

        primaryStage.show();
        instance = this;

        primaryStage.setOnCloseRequest(windowEvent -> {
            WelcomeController controller = fxmlLoader.getController();
            mediaPlayer.stop();
            Platform.exit();
        });
    }

    public void changeSceneTryAgain() throws IOException {
        try {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("welcome-view.fxml"));
        Pane root = fxmlLoader.load();
        Screen screen = Screen.getPrimary();
        double screenWidth = screen.getBounds().getWidth();
        double screenHeight = screen.getBounds().getHeight();
        Scene scene = new Scene(root, screenWidth, screenHeight);
        primaryStage.setTitle("Nuclear-Throne");
        Image logoImage = new Image(getClass().getResourceAsStream("/animations/logos/logo.png"));
        primaryStage.getIcons().add(logoImage);
        primaryStage.setScene(scene);

        primaryStage.setWidth(screenWidth);
        primaryStage.setHeight(screenHeight);
        primaryStage.setOnCloseRequest(windowEvent -> {
            MainController controller = fxmlLoader.getController();
            controller.setRunning(false);
        });



        primaryStage.setFullScreen(true); // Establecer pantalla completa después de cambiar la escena

         // Agregar video de fondo
        String videoFile = getClass().getResource("/videos/initGame.mp4").toExternalForm();
        Media media = new Media(videoFile);
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);

        // Crear MediaView y establecer el MediaPlayer
        MediaView mediaView = new MediaView(mediaPlayer);
        mediaView.setFitWidth(screenWidth);
        mediaView.setFitHeight(screenHeight);

        StackPane mediaContainer = (StackPane) fxmlLoader.getNamespace().get("mediaContainer");
        mediaContainer.getChildren().add(mediaView);

        mediaPlayer.play();

        primaryStage.show();

    } catch (IOException e) {
        e.printStackTrace();
    }
    }

    public void changeScene() {
    try {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("game-view.fxml"));
        Pane root = fxmlLoader.load();
        Screen screen = Screen.getPrimary();
        double screenWidth = screen.getBounds().getWidth();
        double screenHeight = screen.getBounds().getHeight();
        Scene scene = new Scene(root, screenWidth, screenHeight);
        primaryStage.setTitle("Nuclear-Throne");
        Image logoImage = new Image(getClass().getResourceAsStream("/animations/logos/logo.png"));
        primaryStage.getIcons().add(logoImage);
        primaryStage.setScene(scene);

        primaryStage.setWidth(screenWidth);
        primaryStage.setHeight(screenHeight);
        primaryStage.setOnCloseRequest(windowEvent -> {
            MainController controller = fxmlLoader.getController();
            controller.setRunning(false);
        });

        // Oculta el puntero del mouse
        scene.setCursor(Cursor.NONE);

        primaryStage.setFullScreen(true); // Establecer pantalla completa después de cambiar la escena

        primaryStage.show();
        
    } catch (IOException e) {
        e.printStackTrace();
    }
}

 public void changeSceneGameOver() {
    try {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("gameOver-view.fxml"));
        Pane root = fxmlLoader.load();
        Screen screen = Screen.getPrimary();
        double screenWidth = screen.getBounds().getWidth();
        double screenHeight = screen.getBounds().getHeight();
        Scene scene = new Scene(root, screenWidth, screenHeight);
        primaryStage.setTitle("Nuclear-Throne");
        Image logoImage = new Image(getClass().getResourceAsStream("/animations/logos/logo.png"));
        primaryStage.getIcons().add(logoImage);
        primaryStage.setScene(scene);

        primaryStage.setWidth(screenWidth);
        primaryStage.setHeight(screenHeight);
        primaryStage.setOnCloseRequest(windowEvent -> {
            MainController controller = fxmlLoader.getController();
            controller.setRunning(false);
        });



        primaryStage.setFullScreen(true); // Establecer pantalla completa después de cambiar la escena

        // Agregar video de fondo
        String videoFile = getClass().getResource("/videos/gameOver.mp4").toExternalForm();
        Media media = new Media(videoFile);
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);

        // Crear MediaView y establecer el MediaPlayer
        MediaView mediaView = new MediaView(mediaPlayer);
        mediaView.setFitWidth(screenWidth);
        mediaView.setFitHeight(screenHeight);

        StackPane mediaContainer = (StackPane) fxmlLoader.getNamespace().get("mediaContainer");
        mediaContainer.getChildren().add(mediaView);

        mediaPlayer.play();

        primaryStage.show();
    } catch (IOException e) {
        e.printStackTrace();
    }
}



    public static MainApplication getInstance() {
        return instance;
    }
}

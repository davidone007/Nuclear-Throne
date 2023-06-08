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
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApplication extends Application {
    private Stage primaryStage;
    private static MainApplication instance;

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

        primaryStage.show();
        instance = this;
        // ----Cuando no es un hilo, va de último----
        primaryStage.setOnCloseRequest(windowEvent -> {
            WelcomeController controller = fxmlLoader.getController();
            Platform.exit();

        });
    }

    public void cambiarEscena() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("game-view.fxml"));
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
            primaryStage.setOnCloseRequest(windowEvent -> {
                MainController controller = fxmlLoader.getController();
                controller.setRunning(false);

            });
            // Oculta el puntero del mouse
            scene.setCursor(Cursor.NONE);

            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static MainApplication getInstance() {
        return instance;
    }
}

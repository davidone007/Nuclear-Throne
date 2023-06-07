package com.example.gamedemo;

import com.example.gamedemo.control.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

public class MainApplication extends Application {
    Pane root = new Pane();
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("hello-view.fxml"));
        Screen screen = Screen.getPrimary();
        double screenWidth = screen.getBounds().getWidth();
        double screenHeight = screen.getBounds().getHeight();
        Scene scene = new Scene(fxmlLoader.load(), screenWidth, screenHeight);
        stage.setTitle("Nuclear-Throne");
        Image logoImage = new Image(getClass().getResourceAsStream("/animations/logos/Logo.png"));
        stage.getIcons().add(logoImage);
        stage.setScene(scene);
        stage.setOnCloseRequest(windowEvent -> {
            MainController controller = fxmlLoader.getController();
            controller.setRunning(false);

        });
        stage.setWidth(screenWidth);
        stage.setHeight(screenHeight);
        stage.show();
        
       
    }

    public static void main(String[] args) {
        launch();
    }

    public static class Puntero extends ImageView {

        public Puntero(String imageUrl) {
            super(imageUrl);
        }

        public void seguirMouse() {
            setOnMouseMoved((MouseEvent event) -> {
                setTranslateX(event.getX() - getImage().getWidth() / 2);
                setTranslateY(event.getY() - getImage().getHeight() / 2);
            });
        }
    }
}
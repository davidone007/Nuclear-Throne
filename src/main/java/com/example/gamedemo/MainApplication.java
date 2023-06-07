package com.example.gamedemo;

import com.example.gamedemo.control.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
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
        stage.setFullScreen(true);
        Image logoImage = new Image(getClass().getResourceAsStream("/animations/logos/logo.png"));
        stage.getIcons().add(logoImage);
        stage.setScene(scene);
        stage.setOnCloseRequest(windowEvent -> {
            MainController controller = fxmlLoader.getController();
            controller.setRunning(false);

        });

        stage.setWidth(screenWidth);
        stage.setHeight(screenHeight);
        // Oculta el puntero del mouse
        scene.setCursor(Cursor.NONE);


        stage.show();

    }

    public static void main(String[] args) {
        launch();
    }

 

       
     
    }

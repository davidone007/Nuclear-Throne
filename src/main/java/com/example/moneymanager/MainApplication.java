package com.example.moneymanager;

import com.example.moneymanager.controller.MainController;
import com.example.moneymanager.model.MoneyList;
import com.example.moneymanager.model.MoneyList;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Objects;

public class MainApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        MoneyList moneyList = new MoneyList();
        FXMLLoader loader =  renderView("main-view.fxml");
        MainController controller = loader.getController();
        controller.initialize(moneyList);
    }

    public static FXMLLoader renderView(String fxml){
        FXMLLoader fxmlLoader = null;
        try {

            fxmlLoader = new FXMLLoader(
                    MainApplication.class.getResource(fxml)
            );

            Parent parent = fxmlLoader.load();
            Scene scene = new Scene(parent, 320, 240);
            Stage stage = new Stage();
            stage.setTitle("Banco Alfa App");
            URL imageUrl = new URL("https://icon-library.com/images/money-icon-16x16/money-icon-16x16-22.jpg");
            InputStream imageStream = imageUrl.openStream();
            Image image = new Image(imageStream);
            stage.getIcons().add(image);
            stage.setScene(scene);
            stage.show();

        }catch (IOException ex){
            ex.getMessage();
            ex.printStackTrace();
        }
        return fxmlLoader;
    }

    public static void main(String[] args) {
        launch();
    }
}
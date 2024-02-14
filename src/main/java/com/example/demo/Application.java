package com.example.demo;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Application extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("hello-view.fxml"));

        fxmlLoader.setControllerFactory(param -> new MainController());

        Parent root = fxmlLoader.load();

        stage.setScene(new Scene(root, 425, 550));
        stage.show();

        stage.setTitle("Simple Editor!");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}

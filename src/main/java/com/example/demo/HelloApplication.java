package com.example.demo;

import com.example.demo.Shapes.ShapesController;
import com.example.demo.mousePressRelease.MousePressReleaseController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        fxmlLoader.setControllerFactory(param -> new ShapesController(new MousePressReleaseController()));
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

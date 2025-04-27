package org.screen.form;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.awt.*;
import java.awt.event.ActionEvent;

public class MainForm extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/org/screen/form/MainForm.fxml"));
        Scene scene = new Scene(root, 756, 400);
        scene.getStylesheets().add(getClass().getResource("/org/screen/form/styles.css").toExternalForm());
        stage.setTitle("Presentation Server");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.setAlwaysOnTop(true);
        stage.show();

        stage.setOnCloseRequest(windowEvent -> {
            System.exit(0);
        });
    }
    public static void main(String[] args) {
        launch(args);
    }

}

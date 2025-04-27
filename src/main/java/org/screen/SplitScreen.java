package org.screen;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;

public class SplitScreen {
    private double width, height;
    private double x, y;
    private Stage stage;
    private ImageView imageView;
    final private int position;
    final private Image defaultImage;

    public SplitScreen(int position) {
        this.position = position;
        stage = new Stage();
        stage.initStyle(javafx.stage.StageStyle.UNDECORATED);

        // 이미지 로드 및 추가
        defaultImage = new Image(getClass().getResource("/org/screen/image/holdon_" + position + ".png").toExternalForm());
        imageView = new ImageView(defaultImage);
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);
        imageView.setPreserveRatio(true);

        StackPane root = new StackPane(imageView);
        Scene scene = new Scene(root, width, height);
        stage.setAlwaysOnTop(true);
        stage.setScene(scene);
    }

    public void setWidth(double width) {
        this.width = width;

        Platform.runLater(() -> {
            stage.setWidth(width);
            imageView.setFitWidth(width);
        });
    }

    public void setHeight(double height) {
        this.height = height;

        Platform.runLater(() -> {
            stage.setHeight(height);
            imageView.setFitHeight(height);
        });
    }

    public void setX(double x) {
        this.x = x;

        Platform.runLater(() -> {
            stage.setX(x);
        });
    }

    public void setY(double y) {
        this.y = y;

        Platform.runLater(() -> {
            stage.setY(y);
        });
    }

    public void setImage(byte[] data) {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        Image image = new Image(inputStream);

        Platform.runLater(() -> {
            imageView.setImage(image);
        });
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public Stage getStage() {
        return stage;
    }

    public boolean isShowing() {
        return stage != null && stage.isShowing();
    }

    public void show() {
        Platform.runLater(() -> {
            stage.show();
        });
    }


    public void hide() {
        Platform.runLater(() -> {
            stage.hide();
            imageView.setImage(defaultImage);
        });
    }
}

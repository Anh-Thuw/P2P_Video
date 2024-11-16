package com.mycompany.baitaplonmonhoc;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Joined extends Application {

    @Override
    public void start(Stage primaryStage) {
        HBox root = new HBox(20);
        root.setAlignment(Pos.CENTER);

        Button buttonJoin = new Button("Join");
        buttonJoin.setPrefSize(80, 50);
        buttonJoin.setStyle("-fx-background-color: #FFFFFF; -fx-text-fill: #3366CC");

        HBox hBox = new HBox(10);

        Label labelEnterName = new Label("Enter Name");
        labelEnterName.setFont(Font.font(STYLESHEET_MODENA));

        TextField textFieldEnterName = new TextField();
        textFieldEnterName.setPrefSize(100, 50);

        hBox.getChildren().addAll(labelEnterName, textFieldEnterName);
        hBox.setAlignment(Pos.CENTER);

        root.getChildren().addAll(hBox, buttonJoin);

        Scene scene = new Scene(root, 400, 200);

        primaryStage.setTitle("Simple JavaFX App");
        primaryStage.setScene(scene);

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.baitaplonmonhoc;

import javafx.application.Application;
import static javafx.application.Application.STYLESHEET_MODENA;
import static javafx.application.Application.launch;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 *
 * @author admin
 */
public class DefaulApplicationInterface extends Application {

    @Override
    public void start(Stage primaryStage) {
        StackPane root = new StackPane();

        // Tạo một nút
        Button buttonCreateVideoRoom = new Button("Create room video");
        buttonCreateVideoRoom.setPrefSize(120, 50);
        buttonCreateVideoRoom.setStyle("-fx-background-color: #3366CC; -fx-text-fill: #FFFFFF");

        Button buttonJoin = new Button("Join");
        buttonJoin.setPrefSize(80, 50);
        buttonJoin.setStyle("-fx-background-color: #FFFFFF; -fx-text-fill: #3366CC");

        HBox hBox = new HBox(10);

        Label labelEnterIP = new Label("Enter IP");
        labelEnterIP.setFont(Font.font(STYLESHEET_MODENA));

        TextField textFieldEnterIP = new TextField();
        textFieldEnterIP.setPrefSize(100, 50);

        hBox.getChildren().addAll(labelEnterIP, textFieldEnterIP);
        hBox.setAlignment(Pos.CENTER);

        StackPane.setMargin(buttonCreateVideoRoom, new Insets(0, 10, 0, 10));
        StackPane.setMargin(buttonJoin, new Insets(0, 80, 0, 0));

        StackPane.setAlignment(buttonCreateVideoRoom, Pos.CENTER_LEFT);
        StackPane.setAlignment(buttonJoin, Pos.CENTER_RIGHT);
        StackPane.setAlignment(hBox, Pos.CENTER);
        root.getChildren().addAll(buttonCreateVideoRoom, buttonJoin, hBox);

        Scene scene = new Scene(root, 500, 200);

        primaryStage.setTitle("Simple JavaFX App");
        primaryStage.setScene(scene);

        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }


}





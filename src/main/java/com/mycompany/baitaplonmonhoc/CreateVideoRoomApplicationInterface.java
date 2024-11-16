package com.mycompany.baitaplonmonhoc;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;
import javafx.application.Platform;
import javafx.embed.swing.SwingNode;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.shape.Line;

public class CreateVideoRoomApplicationInterface extends Application {
    private Webcam webcam;
    private boolean isCameraOn = false;
    private boolean isMicOn = false;
    
    @Override
    public void start(Stage primaryStage) {

        VBox vBoxRight = createVBoxRight();
        VBox vBoxLeft = createVBoxLeft();

        HBox hBoxContainerVBoxLeftAndVBoxRight = createHBoxContainer(vBoxLeft, vBoxRight);

        StackPane root = createStackPane(hBoxContainerVBoxLeftAndVBoxRight);

        Scene scene = new Scene(root, 1000, 780);

        primaryStage.setTitle("Video Room");
        primaryStage.setScene(scene);

        primaryStage.show();
        
        
    }

    @Override
    public void stop() throws Exception {
        // Ghi đè phương thức stop để xử lý tắt ứng dụng
        Platform.exit();
    }

    private VBox createVBoxRight() {
        VBox vBoxRight = new VBox();
        vBoxRight.setStyle("-fx-background-color: white; -fx-boder-color: black; -fx-border-width: 3px;");

        Label labelMemberJoinVboxRight = new Label("Member Join");
        labelMemberJoinVboxRight.setStyle(" -fx-font-size: 24;");
        HBox hBoxWithLine = new HBox();
        Line lineVboxRight = new Line(0, 0, 290, 0); // Đặt độ dài ban đầu là 1
        hBoxWithLine.getChildren().add(lineVboxRight);
        hBoxWithLine.setAlignment(Pos.CENTER);
        Label labelMember1VboxRigt = new Label("Name 1");
        labelMember1VboxRigt.setStyle(" -fx-font-size: 18;");

        VBox vBoxContainerLabelIDRoomAndLabelIPVBoxRight = new VBox();
        Label labelIDRoom = new Label("ID Room: 111");
        Label labelIP = new Label("IP: 1127.1.2.1");
        vBoxContainerLabelIDRoomAndLabelIPVBoxRight.getChildren().addAll(labelIDRoom, labelIP);
        vBoxContainerLabelIDRoomAndLabelIPVBoxRight.setStyle("-fx-background-color: #EFEEEE; -fx-boder-color: black; -fx-border-width: 3px;");

        vBoxRight.getChildren().addAll(labelMemberJoinVboxRight, lineVboxRight, labelMember1VboxRigt, vBoxContainerLabelIDRoomAndLabelIPVBoxRight);

        return vBoxRight;
    }

    private VBox createVBoxLeft() {
        VBox vBoxLeft = new VBox();
        vBoxLeft.setStyle("-fx-background-color: white; -fx-boder-color: black; -fx-border-width: 3px;");

        SwingNode swingNode = new SwingNode();

        Platform.runLater(() -> {
            webcam = Webcam.getDefault(); // Lấy webcam mặc định
            isCameraOn = true;
            isMicOn = true;
            webcam.setViewSize(WebcamResolution.VGA.getSize()); // Đặt kích thước hiển thị
            
            WebcamPanel panel = new WebcamPanel(webcam);
            panel.setMirrored(true); // Đảo hình ảnh nếu cần

            swingNode.setContent(panel);

            Thread thread = new Thread(() -> {
                webcam.open(); // Mở webcam
                while (true) {
                    if (webcam.isOpen()) {
                        panel.start(); // Bắt đầu hiển thị video
                    }
                }
            });
            thread.setDaemon(true);
            thread.start();
        });

        Button buttonOnOffMic = new Button();
        Label labelIconButonOnOffMic = new Label();
        labelIconButonOnOffMic.setStyle(
                "-fx-background-image: url('file:src/main/java/com/mycompany/baitaplonmonhoc/img/IconOnMic.png');"
                + "-fx-background-size: 30px 30px; "
                + "-fx-background-repeat: no-repeat;"
        );
        labelIconButonOnOffMic.setPrefSize(40, 30);
        buttonOnOffMic.setGraphic(labelIconButonOnOffMic);
        
        Button buttonOnOffVideo = new Button();
        Label labelIconButonOnOffVideo = new Label();
        labelIconButonOnOffVideo.setStyle(
                "-fx-background-image: url('file:src/main/java/com/mycompany/baitaplonmonhoc/img/IconOnVideo.png');"
                + "-fx-background-size: 30px 30px; "
                + "-fx-background-repeat: no-repeat;"
        );
        labelIconButonOnOffVideo.setPrefSize(40, 30);
        buttonOnOffVideo.setGraphic(labelIconButonOnOffVideo);
        
        Button buttonExitVideoRoom = new Button();
        Label labelIconButonExitVideoRoom = new Label();
        labelIconButonExitVideoRoom.setStyle(
                "-fx-background-image: url('file:src/main/java/com/mycompany/baitaplonmonhoc/img/IconExit.png');"
                + "-fx-background-size: 30px 30px; "
                + "-fx-background-repeat: no-repeat;"
        );
        labelIconButonExitVideoRoom.setPrefSize(40, 30);
        buttonExitVideoRoom.setGraphic(labelIconButonExitVideoRoom);
               
        buttonOnOffVideo.setOnAction(event -> {
            if (isCameraOn) {
                // Nếu camera đang bật, tắt nó
                webcam.close();
                isCameraOn = false;
                
                labelIconButonOnOffVideo.setStyle(
                    "-fx-background-image: url('file:src/main/java/com/mycompany/baitaplonmonhoc/img/IconOffVideo.png');"
                    + "-fx-background-size: 30px 30px; "
                    + "-fx-background-repeat: no-repeat;"
                );
            } else {
                // Nếu camera đang tắt, bật nó
                webcam.open();
                isCameraOn = true;
                
                labelIconButonOnOffVideo.setStyle(
                    "-fx-background-image: url('file:src/main/java/com/mycompany/baitaplonmonhoc/img/IconOnVideo.png');"
                    + "-fx-background-size: 30px 30px; "
                    + "-fx-background-repeat: no-repeat;"
                );
            }
        });
        
        buttonOnOffMic.setOnAction(event -> {
            if(isMicOn){
                isMicOn = false;
                
                labelIconButonOnOffMic.setStyle(
                "-fx-background-image: url('file:src/main/java/com/mycompany/baitaplonmonhoc/img/IconOffMic.png');"
                + "-fx-background-size: 30px 30px; "
                + "-fx-background-repeat: no-repeat;"
                );
            }else{
                isMicOn = true;
                
                labelIconButonOnOffMic.setStyle(
                "-fx-background-image: url('file:src/main/java/com/mycompany/baitaplonmonhoc/img/IconOnMic.png');"
                + "-fx-background-size: 30px 30px; "
                + "-fx-background-repeat: no-repeat;"
                );
            }
        });
                
        buttonExitVideoRoom.setOnAction(event -> {
            try {
                Platform.exit();
                System.out.println("com.mycompany.baitaplonmonhoc.CreateVideoRoomApplicationInterface.createVBoxLeft()");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // Đặt kích thước cho các nút
        buttonOnOffMic.setMinSize(30, 30);
        buttonOnOffVideo.setMinSize(30, 30);
        buttonExitVideoRoom.setMinSize(30, 30);

        // Sử dụng CSS để căn giữa nút và đặt chúng nằm ở trung tâm
        buttonOnOffMic.setStyle("-fx-background-color: lightgray; -fx-font-size: 18;");
        buttonOnOffVideo.setStyle("-fx-background-color: lightgray; -fx-font-size: 18;");
        buttonExitVideoRoom.setStyle("-fx-background-color: lightgray; -fx-font-size: 18;");

        HBox hBoxContainerButtonsVBoxLeft = new HBox();
        hBoxContainerButtonsVBoxLeft.setSpacing(10);
        hBoxContainerButtonsVBoxLeft.getChildren().addAll(buttonOnOffMic, buttonOnOffVideo, buttonExitVideoRoom);
        hBoxContainerButtonsVBoxLeft.setAlignment(Pos.CENTER);

        VBox.setMargin(hBoxContainerButtonsVBoxLeft, new Insets(10, 0, 10, 0));
        vBoxLeft.getChildren().addAll(swingNode, hBoxContainerButtonsVBoxLeft);
        VBox.setVgrow(swingNode, Priority.ALWAYS);
        VBox.setMargin(swingNode, new Insets(10));
        vBoxLeft.setAlignment(Pos.CENTER);

        return vBoxLeft;
    }

    private HBox createHBoxContainer(VBox vBoxLeft, VBox vBoxRight) {
        HBox hBoxContainerVBoxLeftAndVBoxRight = new HBox();

        hBoxContainerVBoxLeftAndVBoxRight.getChildren().addAll(vBoxLeft, vBoxRight);
        HBox.setHgrow(vBoxLeft, Priority.ALWAYS);
        HBox.setHgrow(vBoxRight, Priority.ALWAYS);

        return hBoxContainerVBoxLeftAndVBoxRight;
    }

    private StackPane createStackPane(HBox hBoxContainerVBoxLeftAndVBoxRight) {
        StackPane root = new StackPane();
        root.setStyle("-fx-background-color: #CFCFCF");
        StackPane.setAlignment(hBoxContainerVBoxLeftAndVBoxRight, Pos.CENTER);
        root.getChildren().add(hBoxContainerVBoxLeftAndVBoxRight);

        return root;
    }

    public static void main(String[] args) {
        launch(args);
    }
}

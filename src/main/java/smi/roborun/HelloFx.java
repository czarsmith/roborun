package smi.roborun;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import smi.roborun.ctl.BattleController;
import smi.roborun.ui.SvgImage;

public class HelloFx extends Application {

    @Override
    public void start(Stage stage) {
      BattleController ctl = new BattleController();
      stage.initStyle(StageStyle.UNDECORATED);
        String javaVersion = System.getProperty("java.version");
        String javafxVersion = System.getProperty("javafx.version");
        Label l = new Label("Hello, JavaFX " + javafxVersion + ", running on Java " + javaVersion + ".");

        SvgImage svgImg = new SvgImage("/icons/square-xmark-solid.svg", 24, 24);
        Image img = SwingFXUtils.toFXImage(svgImg.getImage(), null);
        ImageView imgView = new ImageView(img);
        Button closeBtn = new Button();
        closeBtn.setGraphic(imgView);
        closeBtn.setOnAction(e -> {
          Platform.exit();
          System.exit(0);
        });
        Button startBtn = new Button("Start");
        startBtn.setOnAction(e -> {
          ctl.execute();
          CompletableFuture.delayedExecutor(10, TimeUnit.SECONDS).execute(() -> {
            ctl.setTps(300);
          });
        });
//        HBox navBar = new HBox();
        //navBar.getChildren().addAll(closeBtn, startBtn);

        BorderPane borderPane = new BorderPane();
        borderPane.setStyle("-fx-background-color: gray;");
        ToolBar toolBar = new ToolBar();
//        int height = 25;
//        toolBar.setPrefHeight(height);
//        toolBar.setMinHeight(height);
//        toolBar.setMaxHeight(height);
        toolBar.getItems().addAll(closeBtn, startBtn);

        borderPane.setTop(toolBar);
//        Rectangle box = new Rectangle(100, 50, Color.BLUE);
//        box.setTranslateX(100);
//        box.setTranslateY(100);
//        box.setScaleX(1.5);
//        box.setRotate(30);
        Scene scene = new Scene(borderPane, 640, 480, Color.GRAY);

//        Scene scene = new Scene(new StackPane(l), 640, 480, Color.rgb(50, 50, 50));
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}
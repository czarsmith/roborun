package smi.roborun;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import smi.roborun.ctl.BattleController;
import smi.roborun.ui.MeleePane;
import smi.roborun.ui.VsPane;
import smi.roborun.ui.settings.SettingsPane;
import smi.roborun.ui.widgets.CardPane;
import smi.roborun.ui.widgets.SvgButton;
import smi.roborun.ui.widgets.UiUtil;

public class Roborun extends Application {
  @Override
  public void start(Stage stage) {
    BattleController ctl = new BattleController(stage);

    SettingsPane settingsPane = new SettingsPane(ctl);
    MeleePane meleePane = new MeleePane(ctl);
    VsPane vsPane = new VsPane(ctl);
    CardPane center = new CardPane(settingsPane, meleePane, vsPane);

    Button closeBtn = new SvgButton("/icons/square-xmark-solid.svg", e -> {
      Platform.exit(); // For JavaFx
      System.exit(0); // For Robocode
    });
    Button startBtn = new SvgButton("/icons/play-solid.svg", e -> {
      ctl.execute(settingsPane.createTourney());
    });
    Button settingsBtn = new SvgButton("/icons/gear-solid.svg", e -> center.show(settingsPane));
    Button meleeBtn = new SvgButton("/icons/people-group-solid.svg", e -> center.show(meleePane));
    Button vsBtn = new SvgButton("/icons/people-arrows-solid.svg", e -> center.show(vsPane));

    ToolBar toolBar = new ToolBar();
    toolBar.getItems().addAll(UiUtil.hspace(), startBtn, settingsBtn, meleeBtn, vsBtn, UiUtil.hspace(), closeBtn);

    BorderPane borderPane = new BorderPane();
    borderPane.setTop(toolBar);
    borderPane.setCenter(center);
    borderPane.getStyleClass().add("region");

    Scene scene = new Scene(borderPane, 640, 480, Color.GRAY);
    scene.getStylesheets().add("index.css");

    stage.initStyle(StageStyle.UNDECORATED);
    stage.setMaximized(true);
    stage.setScene(scene);
    stage.show();
  }

  public static void main(String[] args) {
    launch();
  }
}
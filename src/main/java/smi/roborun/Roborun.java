package smi.roborun;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import smi.roborun.ctl.BattleController;
import smi.roborun.mdl.Tourney;
import smi.roborun.ui.BattleBoard;
import smi.roborun.ui.MeleePane;
import smi.roborun.ui.SettingsPane;
import smi.roborun.ui.TitledNode;
import smi.roborun.ui.VsPane;
import smi.roborun.ui.widgets.CardPane;
import smi.roborun.ui.widgets.SvgButton;
import smi.roborun.ui.widgets.UiUtil;

public class Roborun extends Application {
  private BattleController ctl;
  private Label title;
  private CardPane centerPane;
  private SettingsPane settingsPane;
  private BattleBoard battleBoard;

  @Override
  public void start(Stage stage) {
    Tourney tourney = new Tourney();
    ctl = new BattleController(stage, tourney);

    Label appName = new Label("Roborun");
    appName.setFont(new Font("Arial", 24));

    title = new Label();
    title.setFont(new Font("Arial", 24));

    settingsPane = new SettingsPane(ctl, tourney);
    battleBoard = new BattleBoard(ctl, tourney);
    MeleePane meleePane = new MeleePane(ctl);
    VsPane vsPane = new VsPane(ctl);
    centerPane = new CardPane(settingsPane, battleBoard, meleePane, vsPane);

    Button closeBtn = new SvgButton("/icons/square-xmark-solid.svg", e -> {
      Platform.exit(); // For JavaFx
      System.exit(0); // For Robocode
    });
    Button startBtn = new SvgButton("/icons/play-solid.svg", e -> show("Battle Board", battleBoard));
    Button settingsBtn = new SvgButton("/icons/gear-solid.svg", e -> show("Settings", settingsPane));
    Button meleeBtn = new SvgButton("/icons/people-group-solid.svg", e -> show("Melee Bracket", meleePane));
    Button vsBtn = new SvgButton("/icons/people-arrows-solid.svg", e -> show("1v1 Bracket", vsPane));

    ToolBar toolBar = new ToolBar();
    Pane titlePane = new Pane(title);
    toolBar.getItems().addAll(appName, titlePane, UiUtil.hspace(),
      startBtn, settingsBtn, meleeBtn, vsBtn, UiUtil.hspace(16), closeBtn);

    BorderPane borderPane = new BorderPane();
    borderPane.setTop(toolBar);
    borderPane.setCenter(centerPane);
    borderPane.getStyleClass().add("region");

    Scene scene = new Scene(borderPane, 640, 480, Color.GRAY);
    scene.getStylesheets().add("index.css");

    stage.initStyle(StageStyle.UNDECORATED);
    stage.setMaximized(true);
    stage.setScene(scene);
    stage.show();

    show("Settings", settingsPane);
  }

  private void show(String label, Node node) {
    title.textProperty().unbind();
    if (node instanceof TitledNode titled) {
      title.textProperty().bind(Bindings.createStringBinding(() -> " - " + titled.getTitle(), titled.getTitleProperty()));
    } else {
      title.setText(" - " + label);
    }
    centerPane.show(node);
  }

  public static void main(String[] args) {
    launch();
  }
}
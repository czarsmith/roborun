package hcs.roborun;

import java.util.List;

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
import hcs.roborun.ctl.BattleController;
import hcs.roborun.mdl.Tourney;
import hcs.roborun.ui.BattleBoard;
import hcs.roborun.ui.ResultsPane;
import hcs.roborun.ui.SettingsPane;
import hcs.roborun.ui.TitledNode;
import hcs.roborun.ui.widgets.CardPane;
import hcs.roborun.ui.widgets.SvgButton;
import hcs.roborun.ui.widgets.UiUtil;

public class Roborun extends Application {
  private BattleController ctl;
  private Label title;
  private CardPane centerPane;
  private SettingsPane settingsPane;
  private BattleBoard battleBoard;

  @Override
  public void start(Stage stage) {
    List<String> args = getParameters().getRaw();
    String robocodeDir = getArg(args, "--robocode-dir");
    
    Tourney tourney = new Tourney();
    ctl = new BattleController(stage, tourney, robocodeDir);

    Label appName = new Label("Roborun");
    appName.setFont(new Font("Arial", 24));

    title = new Label();
    title.setFont(new Font("Arial", 24));

    settingsPane = new SettingsPane(ctl, tourney);
    battleBoard = new BattleBoard(ctl, tourney, robocodeDir);
    ResultsPane resultsPane = new ResultsPane(tourney);
    centerPane = new CardPane(settingsPane, battleBoard, resultsPane);

    Button closeBtn = new SvgButton("/icons/square-xmark-solid.svg", e -> {
      Platform.exit(); // For JavaFx
      System.exit(0); // For Robocode
    });
    Button startBtn = new SvgButton("/icons/play-solid.svg", e -> show("Battle Board", battleBoard));
    Button settingsBtn = new SvgButton("/icons/gear-solid.svg", e -> show("Settings", settingsPane));
    Button resultsBtn = new SvgButton("/icons/people-group-solid.svg", e -> show("Results", resultsPane));

    ToolBar toolBar = new ToolBar();
    Pane titlePane = new Pane(title);
    toolBar.getItems().addAll(appName, titlePane, UiUtil.hspace(),
      settingsBtn, startBtn, resultsBtn, UiUtil.hspace(16), closeBtn);

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

  private String getArg(List<String> args, String key) {
    String value = null;
    for (int i = 0; i < args.size(); i++) {
      if (key.equals(args.get(i)) && args.size() > i) {
        value = args.get(++i);
      }
    }
    return value;
  }

  public static void main(String[] args) {
    launch(args);
  }
}
package smi.roborun;

import java.util.List;
import java.util.stream.Collectors;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import robocode.BattleResults;
import smi.roborun.ctl.BattleController;
import smi.roborun.ctl.BattleEvent;
import smi.roborun.mdl.Battle;
import smi.roborun.mdl.Robot;
import smi.roborun.mdl.Round;
import smi.roborun.mdl.Tourney;
import smi.roborun.ui.MeleePane;
import smi.roborun.ui.VsPane;
import smi.roborun.ui.settings.SettingsPane;
import smi.roborun.ui.widgets.CardPane;
import smi.roborun.ui.widgets.SvgButton;
import smi.roborun.ui.widgets.UiUtil;

public class Roborun extends Application {
  private Tourney tourney;
  private Round round;
  private Battle battle;
  private BattleController ctl;
  private SettingsPane settingsPane;
  private long tourneyStartTime;

  @Override
  public void start(Stage stage) {
    ctl = new BattleController(stage);
    ctl.addEventHandler(BattleEvent.FINISHED, this::onBattleFinished);

    settingsPane = new SettingsPane(ctl);
    MeleePane meleePane = new MeleePane(ctl);
    VsPane vsPane = new VsPane(ctl);
    CardPane center = new CardPane(settingsPane, meleePane, vsPane);

    Button closeBtn = new SvgButton("/icons/square-xmark-solid.svg", e -> {
      Platform.exit(); // For JavaFx
      System.exit(0); // For Robocode
    });
    Button startBtn = new SvgButton("/icons/play-solid.svg", e -> runNextBattle());
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

  private void runNextBattle() {
    if (this.battle == null) { // First battle
      tourneyStartTime = System.currentTimeMillis();
      tourney = settingsPane.createTourney();
      round = tourney.getMeleeRounds().get(0);
      battle = round.getBattles().get(0);  
    } else if (round.getNumBattles() > battle.getBattleNumber()) { // Same Round, Next Battle
      battle = round.getBattles().get(battle.getBattleNumber());
    } else if (tourney.getMeleeRounds().size() > round.getRoundNumber()) { // Next Round, Next Battle
      round = tourney.getMeleeRounds().get(round.getRoundNumber());
      battle = round.getBattles().get(0);
    } else {
      double seconds = (System.currentTimeMillis() - tourneyStartTime) / 1000d;
      System.out.println("Tournament finished in " + seconds + " seconds");
      battle = null;
    }

    if (battle != null) {
      new Thread(new TourneyThread()).start();
    }
  }

  private class TourneyThread implements Runnable {
    public void run() {
      System.out.println("START Round" + round.getRoundNumber() + " Battle " + battle.getBattleNumber());
      ctl.execute(tourney, round, battle);
    }  
  }

  private void onBattleFinished(BattleEvent e) {
    System.out.println("DONE Round" + round.getRoundNumber() + " Battle " + battle.getBattleNumber());
    Tourney tourney = e.getTourney();
    Round round = e.getRound();
    Battle battle = e.getBattle();
    if (round.getNumBattles() > 1) {
      battle.getResults().subList(0, battle.getResults().size() / 2).stream()
        .forEach(br -> System.out.println(br.getTeamLeaderName()));
      List<Robot> topFinishers = battle.getResults().subList(0, battle.getResults().size() / 2).stream()
        .map(BattleResults::getTeamLeaderName)
        .map(name -> battle.getRobots().stream().filter(r -> name.equals(r.getRobotName())).findFirst().orElse(null))
        .collect(Collectors.toList());
      Round nextRound = tourney.getMeleeRounds().get(round.getRoundNumber());
      nextRound.getBattles().get((battle.getBattleNumber() - 1) / 2).getRobots().addAll(topFinishers);
    }
    runNextBattle();
  }

  public static void main(String[] args) {
    launch();
  }
}
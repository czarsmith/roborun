package smi.roborun.ui;

import java.util.List;
import java.util.stream.Collectors;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import robocode.BattleResults;
import smi.roborun.ctl.BattleController;
import smi.roborun.ctl.BattleEvent;
import smi.roborun.mdl.Battle;
import smi.roborun.mdl.Robot;
import smi.roborun.mdl.Round;
import smi.roborun.mdl.Tourney;

public class BattleBoard extends GridPane {
  private BattleController ctl;
  private Tourney tourney;
  private Round round;
  private Battle battle;
  private long tourneyStartTime;
  private FlowPane nowPlayingCards;

  public BattleBoard(BattleController ctl) {
    this.ctl = ctl;
    ctl.addEventHandler(BattleEvent.FINISHED, this::onBattleFinished);

    nowPlayingCards = new FlowPane();

    Label nowPlayingTitle = new Label("Now Playing");
    nowPlayingTitle.setFont(new Font("Arial", 24));
//    nowPlayingTitle.setAlignment(Pos.CENTER);

    GridPane nowPlaying = new GridPane();
    nowPlaying.setBorder(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, new CornerRadii(5), BorderStroke.THICK)));
    nowPlaying.add(nowPlayingTitle, 0, 0);
    nowPlaying.add(nowPlayingCards, 0, 1);
    ColumnConstraints cc1 = new ColumnConstraints();
    cc1.setHalignment(HPos.CENTER);
    nowPlaying.getColumnConstraints().add(cc1);

    add(new Pane(new Label("On Deck")), 0, 0);
    add(nowPlaying, 1, 0);
    add(new Pane(new Label("Completed")), 2, 0);
    add(new Pane(new Label("Ticker")), 0, 1, 3, 1);

    ColumnConstraints col1 = new ColumnConstraints();
    col1.setHgrow(Priority.ALWAYS);
    ColumnConstraints col2 = new ColumnConstraints(800);
    ColumnConstraints col3 = new ColumnConstraints();
    col3.setHgrow(Priority.ALWAYS);
    getColumnConstraints().addAll(col1, col2, col3);

    RowConstraints row1 = new RowConstraints();
    row1.setVgrow(Priority.ALWAYS);
    RowConstraints row2 = new RowConstraints();
    row2.setPrefHeight(100);
    getRowConstraints().addAll(row1, row2);
  }

  public void startTourney(Tourney tourney) {
    this.tourney = tourney;
    runNextBattle();
  }

  private void runNextBattle() {
    if (this.battle == null) { // First battle
      tourneyStartTime = System.currentTimeMillis();
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
      nowPlayingCards.getChildren().clear();
      battle.getRobots().stream().map(RobotCard::new).forEach(card -> {
        FlowPane.setMargin(card, new Insets(4));
        nowPlayingCards.getChildren().add(card);
      });

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
}

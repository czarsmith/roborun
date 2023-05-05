package hcs.roborun.ui;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import hcs.roborun.mdl.Battle;

public class BracketBattle extends Label {
  public static final double WIDTH = 100;
  public static final double HEIGHT = 75;
  public static final double SPACING_X = 50;
  public static final double SPACING_Y = 50;

  private Battle battle;

  public BracketBattle(Battle battle, double gridX, double gridY) {
    this.battle = battle;
    getStyleClass().add("bracket-battle");

    if (battle.getBattleNumber() > battle.getRound().getNumBattles()) {
      setText("N/A");
    } else {
      setText("R" + battle.getRoundNumber() + " B" + battle.getBattleNumber());
    }

    setAlignment(Pos.CENTER);
    setPrefWidth(WIDTH);
    setMinWidth(WIDTH);
    setMaxWidth(WIDTH);
    setPrefHeight(HEIGHT);
    setMinHeight(HEIGHT);
    setMaxHeight(HEIGHT);
    setFont(new Font("Arial", 18));
    setLayoutX(gridX * (WIDTH + SPACING_X));
    setLayoutY(gridY * (HEIGHT + SPACING_Y));
  }

  public Battle getBattle() {
    return battle;
  }
}

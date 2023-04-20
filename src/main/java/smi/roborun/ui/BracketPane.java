package smi.roborun.ui;

import javafx.collections.ListChangeListener;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import smi.roborun.mdl.Battle;
import smi.roborun.mdl.Battle.BattleType;
import smi.roborun.mdl.Tourney;

public class BracketPane extends ScrollPane implements ListChangeListener<Battle> {
  private Pane viewport;

  public BracketPane(Tourney tourney) {
    tourney.getBattles().addListener(this);

    viewport = new Pane();
    setContent(viewport);

    getStyleClass().add("bracket");
  }

  @Override
  public void onChanged(Change<? extends Battle> battles) {
    viewport.getChildren().clear();

    double maxY = 0;
    int x = 0;
    int roundNumber = 1;
    BattleType battleType = BattleType.MELEE;
    for (Battle battle : battles.getList()) {
      if (battle.getRoundNumber() != roundNumber || battle.getType() != battleType) {
        x++;
        battleType = battle.getType();
        roundNumber = battle.getRoundNumber();
      }
      double y = (Math.pow(2, battle.getRoundNumber() - 1) - 1) * 0.5
        + (battle.getBattleNumber() - 1) * Math.pow(2, battle.getRoundNumber() - 1);
      viewport.getChildren().add(new BracketBattle(battle, x, y));
      maxY = Math.max(maxY, y);
    }

    viewport.setPrefWidth(x * 140);
    viewport.setMinWidth(x * 140);
    viewport.setMaxWidth(x * 140);
    viewport.setPrefHeight(maxY * 140);
    viewport.setMinHeight(maxY * 140);
    viewport.setMaxHeight(maxY * 140);
  }
}

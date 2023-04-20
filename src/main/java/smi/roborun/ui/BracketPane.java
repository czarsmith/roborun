package smi.roborun.ui;

import java.util.HashMap;
import java.util.Map;

import javafx.collections.ListChangeListener;
import javafx.scene.Node;
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

    int x = 0;
    int roundNumber = 1;
    BattleType battleType = BattleType.MELEE;
    Map<String, BracketBattle> battleMap = new HashMap<>();
    for (Battle battle : battles.getList()) {
      if (battle.getRoundNumber() != roundNumber || battle.getType() != battleType) {
        x++;
        battleType = battle.getType();
        roundNumber = battle.getRoundNumber();
      }
      double y = (Math.pow(2, battle.getRoundNumber() - 1) - 1) * 0.5
        + (battle.getBattleNumber() - 1) * Math.pow(2, battle.getRoundNumber() - 1);
      int xconst = x;
      viewport.getChildren().add(battleMap.computeIfAbsent(battle.getId(), id -> new BracketBattle(battle, xconst, y)));
    }

    for (BracketBattle bb : battleMap.values()) {
      if (bb.getBattle().getAdvanceToBattleNumber() != null) {
        String otherId = bb.getBattle().getType() + "-" + (bb.getBattle().getRoundNumber() + 1) + "-" + bb.getBattle().getAdvanceToBattleNumber();
        viewport.getChildren().add(0, new BracketLine(bb, battleMap.get(otherId)));
      }
    }
  }

  @Override
  public void layoutChildren() {
    super.layoutChildren();

    double maxX = 0;
    double maxY = 0;
    for (Node node : viewport.getChildren()) {
      maxX = Math.max(maxX, node.getBoundsInParent().getMaxX());
      maxY = Math.max(maxY, node.getBoundsInParent().getMaxY());
    }
    viewport.setPrefWidth(maxX);
    viewport.setMinWidth(maxX);
    viewport.setMaxWidth(maxX);
    viewport.setPrefHeight(maxY);
    viewport.setMinHeight(maxY);
    viewport.setMaxHeight(maxY);
  }
}

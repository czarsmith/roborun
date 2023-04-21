package smi.roborun.ui;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import javafx.collections.ListChangeListener;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import smi.roborun.mdl.Battle;
import smi.roborun.mdl.Battle.BattleType;
import smi.roborun.mdl.Round;
import smi.roborun.mdl.Tourney;

public class BracketPane extends ScrollPane implements ListChangeListener<Battle> {
  private Tourney tourney;
  private Pane viewport;

  public BracketPane(Tourney tourney) {
    this.tourney = tourney;
    tourney.getBattles().addListener(this);

    viewport = new Pane();
    setContent(viewport);

    getStyleClass().add("bracket");
  }

  @Override
  public void onChanged(Change<? extends Battle> battles) {
    viewport.getChildren().clear();

    List<Battle> sortedBattles = new ArrayList<>(battles.getList());
    
    // Create placeholder battles to fill up partial (preliminary) rounds
    tourney.getRounds().stream().filter(Round::isPreliminary).forEach(round -> {
      Round nextRound = tourney.getRound(round.getType(), round.getRoundNumber() + 1);
      if (nextRound != null) {
        IntStream.range(0, round.getMaxBattles() - round.getNumBattles()).forEach(battleIdx -> {
          Battle b = new Battle(round);
          b.setType(round.getType());
          b.setBattleNumber(round.getNumBattles() + battleIdx + 1);
  
          int effectiveBattleIdx = round.getMaxBattles() - round.getNumBattles() - battleIdx - 1;
          int nextBattleIdx = effectiveBattleIdx % (round.getMaxBattles() / 2);
          if (effectiveBattleIdx < round.getMaxBattles() / 2) {
            b.setAdvanceToBattleNumber(nextBattleIdx + 1);
          } else {
            b.setAdvanceToBattleNumber(nextRound.getNumBattles() - nextBattleIdx);
          }
  
          sortedBattles.add(b);
        });  
      }
    });

    sortedBattles.sort(Comparator
      .comparing(Battle::getType)
      .thenComparingInt(Battle::getRoundNumber)
      .thenComparing(Comparator.nullsFirst(Comparator.comparing(Battle::getAdvanceToBattleNumber)))
      .thenComparingInt(Battle::getBattleNumber));

    int roundIdx = 0;
    int battleIdx = 0;
    int roundNumber = 1;
    BattleType battleType = BattleType.MELEE;
    Map<String, BracketBattle> battleMap = new HashMap<>();
    for (Battle battle : sortedBattles) {
      if (battle.getRoundNumber() != roundNumber || battle.getType() != battleType) {
        roundIdx++;
        battleIdx = 0;
        battleType = battle.getType();
        roundNumber = battle.getRoundNumber();
      }
      double gridY = (Math.pow(2, battle.getRoundNumber() - 1) - 1) * 0.5
        + battleIdx * Math.pow(2, battle.getRoundNumber() - 1);
      BracketBattle bb = new BracketBattle(battle, roundIdx, gridY);
      battleMap.put(battle.getId(), bb);
      viewport.getChildren().add(bb);
      battleIdx++;
    }

    for (BracketBattle bb : battleMap.values()) {
      if (bb.getBattle().getAdvanceToBattleNumber() != null) {
        String otherId = bb.getBattle().getType() + "-" + (bb.getBattle().getRoundNumber() + 1) + "-" + bb.getBattle().getAdvanceToBattleNumber();
        if (battleMap.containsKey(otherId)) {
          viewport.getChildren().add(0, new BracketLine(bb, battleMap.get(otherId)));
        }
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

package smi.roborun.ui;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javafx.collections.ListChangeListener;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import smi.roborun.mdl.Battle;
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

    Map<String, Battle> battleMap = new HashMap<>();
    battles.getList().forEach(b -> battleMap.put(b.getId(), b));
    Map<String, List<Battle>> sortedBattlesByRound = new HashMap<>();
        
    // Create placeholder battles to fill up partial (preliminary) rounds
    tourney.getRounds().stream().forEach(round -> {
      List<Battle> sortedBattlesInRound = sortedBattlesByRound.computeIfAbsent(round.getId(), id -> new ArrayList<>());
      sortedBattlesInRound.addAll(tourney.getBattles().stream()
        .filter(b -> b.getRound() == round).collect(Collectors.toList()));

      Round nextRound = tourney.getRound(round.getType(), round.getRoundNumber() + 1);
      if (round.isPreliminary() && nextRound != null) {
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
  
          sortedBattlesInRound.add(b);
        });  
      }
    });

    List<Round> sortedRounds = tourney.getRounds().stream()
      .sorted(Comparator.comparing(Round::getType).thenComparing(Round::getMaxBattles))
      .collect(Collectors.toList());
    
    sortedRounds.forEach(round -> {
      sortedBattlesByRound.get(round.getId()).sort(Comparator.<Battle, Integer>comparing(a -> {
        List<Battle> battlesInRound = sortedBattlesByRound.get(round.getType() + "-" + (round.getRoundNumber() + 1));
        return battlesInRound == null ? 0 : battlesInRound.indexOf(
          battleMap.get(a.getType() + "-" + (a.getRoundNumber() + 1) + "-" + a.getAdvanceToBattleNumber()));
      }).thenComparing(Battle::getBattleNumber));
    });

    sortedRounds.sort(Comparator.comparing(Round::getType).thenComparing(Comparator.comparing(Round::getMaxBattles).reversed()));

    Map<String, BracketBattle> bracketMap = new HashMap<>();
    for (int roundIdx = 0; roundIdx < sortedRounds.size(); roundIdx++) {
      Round round = sortedRounds.get(roundIdx);
      double spacesInRound = Math.pow(2, round.getRoundNumber() - 1);
      int battleIdx = 0;
      for (Battle battle : sortedBattlesByRound.get(round.getId())) {
        double gridY = (spacesInRound - 1) * 0.5 + battleIdx * spacesInRound;
        BracketBattle bb = new BracketBattle(battle, roundIdx, gridY);
        bracketMap.put(battle.getId(), bb);
        viewport.getChildren().add(bb);
        battleIdx++;
      }
    }

    for (BracketBattle bb : bracketMap.values()) {
      if (bb.getBattle().getAdvanceToBattleNumber() != null) {
        String otherId = bb.getBattle().getType() + "-" + (bb.getBattle().getRoundNumber() + 1) + "-" + bb.getBattle().getAdvanceToBattleNumber();
        if (bracketMap.containsKey(otherId)) {
          viewport.getChildren().add(0, new BracketLine(bb, bracketMap.get(otherId)));
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

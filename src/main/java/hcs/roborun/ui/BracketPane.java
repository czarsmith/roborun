package hcs.roborun.ui;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import hcs.roborun.mdl.Battle;
import hcs.roborun.mdl.Round;
import hcs.roborun.mdl.Tourney;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class BracketPane extends ScrollPane implements ListChangeListener<Battle> {
  private Tourney tourney;
  private Pane viewport;
  private Map<String, BracketBattle> bracketMap;
  private Timeline animX;
  private Timeline animY;

  public BracketPane(Tourney tourney) {
    this.tourney = tourney;
    bracketMap = new HashMap<>();

    tourney.getBattles().addListener(this);
    tourney.getBattleProperty().addListener((a,b,c) -> onBattleChanged());

    viewport = new Pane();
    viewport.widthProperty().addListener((a, b, c) -> scrollToBattle());
    setContent(viewport);

    getStyleClass().add("bracket");
  }

  private void onBattleChanged() {
    if (tourney.getBattle() != null) {
      bracketMap.values().forEach(bb -> bb.getStyleClass().remove("now-playing"));
      BracketBattle bb = bracketMap.get(tourney.getBattle().getId());
      if (bb != null) {
        bb.getStyleClass().add("now-playing");
      }
    }
    scrollToBattle();
  }

  private void scrollToBattle() {
    if (tourney.getBattle() != null) {
      BracketBattle bb = bracketMap.get(tourney.getBattle().getId());
      if (bb != null) {
        Platform.runLater(() -> scrollTo(bb));
      }
    }
  }
  
  private void scrollTo(Node node) {
    if (animX != null) {
      animX.stop();
    }
    if (animY != null) {
      animY.stop();
    }

    if (node != null) {
      double scrollWidth = getWidth();
      double scrollHeight = getHeight();

      double width = viewport.getWidth();
      double height = viewport.getHeight();
  
      double x = (node.getBoundsInParent().getMaxX() + node.getBoundsInParent().getMinX()) / 2 - scrollWidth / 2;
      double y = (node.getBoundsInParent().getMaxY() + node.getBoundsInParent().getMinY()) / 2 - scrollHeight / 2;
  
      animX = new Timeline(new KeyFrame(Duration.millis(1000),
        new KeyValue(this.hvalueProperty(), Math.min(1, Math.max(0, x / (width - scrollWidth))), Interpolator.LINEAR)));
      animX.play();
  
      animY = new Timeline(new KeyFrame(Duration.millis(1000),
        new KeyValue(this.vvalueProperty(), Math.min(1, Math.max(0, y / (height - scrollHeight))), Interpolator.LINEAR)));
      animY.play();
    }
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

    bracketMap.clear();
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

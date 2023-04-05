package smi.roborun.ui;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
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
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import robocode.BattleResults;
import smi.roborun.ctl.BattleController;
import smi.roborun.ctl.BattleEvent;
import smi.roborun.mdl.Battle;
import smi.roborun.mdl.Battle.BattleType;
import smi.roborun.mdl.Robot;
import smi.roborun.mdl.Tourney;
import smi.roborun.ui.widgets.SvgButton;
import smi.roborun.ui.widgets.UiUtil;

public class BattleBoard extends GridPane implements TitledNode {
  private BattleController ctl;
  private Tourney tourney;
  private FlowPane nowPlayingCards;
  private FlowPane upNextTiles;
  private StringProperty title;

  public BattleBoard(BattleController ctl, Tourney tourney) {
    this.ctl = ctl;
    this.tourney = tourney;
    title = new SimpleStringProperty("Battle Board");
    ctl.addEventHandler(BattleEvent.FINISHED, this::onBattleFinished);
    ctl.addEventHandler(BattleEvent.ROUND_STARTED, this::onRoundStarted);
    ctl.addEventHandler(BattleEvent.ROUND_FINISHED, this::onRoundFinished);
    ctl.addEventHandler(BattleEvent.TURN_FINISHED, this::onTurnFinished);

    // Center Pane
    Button playBtn = new SvgButton("/icons/play-solid.svg", e -> startTourney());
    Button resetBtn = new SvgButton("/icons/rotate-right-solid.svg", e -> reset());
    ToolBar toolBar = new ToolBar();
    toolBar.getItems().addAll(UiUtil.hspace(), playBtn, resetBtn, UiUtil.hspace());
    Pane robocodePlaceholder = new Pane();
    VBox centerPane = new VBox(toolBar, robocodePlaceholder);
    centerPane.setBorder(new Border(new BorderStroke(Color.GRAY, BorderStrokeStyle.SOLID, new CornerRadii(5), BorderStroke.THIN)));
    VBox.setVgrow(robocodePlaceholder, Priority.ALWAYS);

    // Now Playing
    Label nowPlayingTitle = new Label("Now Playing");
    nowPlayingTitle.setFont(new Font("Arial", 24));

    nowPlayingCards = new FlowPane();
    nowPlayingCards.setBorder(new Border(new BorderStroke(Color.BLUE, BorderStrokeStyle.SOLID, new CornerRadii(5), BorderStroke.THIN)));

    GridPane nowPlaying = new GridPane();
    nowPlaying.add(nowPlayingTitle, 0, 0);
    nowPlaying.add(nowPlayingCards, 0, 1);
    ColumnConstraints cc1 = new ColumnConstraints();
    cc1.setHalignment(HPos.CENTER);
    nowPlaying.getColumnConstraints().add(cc1);
    RowConstraints row1 = new RowConstraints();
    RowConstraints row2 = new RowConstraints();
    row2.setVgrow(Priority.ALWAYS);
    nowPlaying.getRowConstraints().addAll(row1, row2);

    // Bracket
    Label bracketTitle = new Label("Bracket");
    bracketTitle.setFont(new Font("Arial", 24));

    FlowPane bracketGui = new FlowPane();
    bracketGui.setBorder(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, new CornerRadii(5), BorderStroke.THIN)));

    GridPane bracket = new GridPane();
    bracket.add(bracketTitle, 0, 0);
    bracket.add(bracketGui, 0, 1);
    
    // Up Next
    Label upNextTitle = new Label("Up Next: ");
    upNextTitle.setFont(new Font("Arial", 24));
    upNextTitle.setPadding(new Insets(0, 8, 0, 4));
    upNextTiles = new FlowPane();
    GridPane upNext = new GridPane();
    upNext.setPadding(new Insets(4));
    upNext.add(upNextTitle, 0, 0);
    upNext.add(upNextTiles, 1, 0);
    ColumnConstraints uncol1 = new ColumnConstraints();
    ColumnConstraints uncol2 = new ColumnConstraints();
    uncol2.setHgrow(Priority.ALWAYS);
    upNext.getColumnConstraints().addAll(uncol1, uncol2);
    GridPane.setColumnSpan(upNext, 3);

    add(nowPlaying, 0, 0);
    add(upNext, 0, 1);
    add(centerPane, 1, 0);
    add(bracket, 2, 0);

    ColumnConstraints col1 = new ColumnConstraints();
    col1.setHgrow(Priority.ALWAYS);
    ColumnConstraints col2 = new ColumnConstraints(800);
    ColumnConstraints col3 = new ColumnConstraints();
    col3.setHgrow(Priority.ALWAYS);
    getColumnConstraints().addAll(col1, col2, col3);

    RowConstraints r1 = new RowConstraints();
    r1.setVgrow(Priority.ALWAYS);
    RowConstraints r2 = new RowConstraints();
    getRowConstraints().addAll(r1, r2);

    tourney.getBattleProperty().addListener((a,b,c) -> onBattleChanged());

    reset();
  }

  private void reset() {
    tourney.reset(false);
    updateTitle();
  }

  @Override
  public StringProperty getTitleProperty() {
    return title;
  }

  @Override
  public String getTitle() {
    return title.get();
  }

  private void startTourney() {
    System.out.println("Tournament Started at: " + new Date());
    tourney.setStartTime(System.currentTimeMillis());
    tourney.setEndTime(0L);
    runBattle();
  }

  private void onBattleChanged() {
    updateTitle();

    if (tourney.getBattles().isEmpty()) {
      return;
    }

    // Up Next
    upNextTiles.getChildren().clear();
    if (tourney.getBattle() != null) {
      int currBattleIdx = tourney.getBattles().indexOf(tourney.getBattle());
      Battle nextBattle = tourney.getBattles().size() > currBattleIdx + 1 ? tourney.getBattles().get(currBattleIdx + 1) : null;
      if (nextBattle != null) {
        nextBattle.getRobots().stream().map(RobotTile::new).forEach(tile -> {
          FlowPane.setMargin(tile, new Insets(4));
          upNextTiles.getChildren().add(tile);
        });
        // Add placeholders for robots who's identity we don't yet know.  TODO: fix this
        IntStream.range(0, nextBattle.getNumRobots() - nextBattle.getRobots().size())
            .mapToObj(i -> new RobotTile()).forEach(tile -> {
          FlowPane.setMargin(tile, new Insets(4));
          upNextTiles.getChildren().add(tile);
        });
      }
    }

    // Now Playing
    nowPlayingCards.getChildren().clear();
    if (tourney.getBattle() != null) {
      tourney.getBattle().getRobots().stream().map(RobotCard::new).forEach(card -> {
        FlowPane.setMargin(card, new Insets(4));
        nowPlayingCards.getChildren().add(card);
      });
    }
  }

  private Battle setNextBattle() {
    int currBattleIdx = tourney.getBattles().indexOf(tourney.getBattle());
    Battle nextBattle = tourney.getBattles().size() > currBattleIdx + 1 ? tourney.getBattles().get(currBattleIdx + 1) : null;

    if (nextBattle == null) {
      System.out.println("Tournament Ended at: " + new Date());
      tourney.setEndTime(System.currentTimeMillis());
      double seconds = (tourney.getEndTime() - tourney.getStartTime()) / 1000d;
      System.out.println("Tournament finished in " + seconds + " seconds");
    } else {
      tourney.setBattle(nextBattle);
    }

    updateTitle();

    return nextBattle;
  }

  private void updateTitle() {
    if (tourney.getBattle() != null) {
      Battle battle = tourney.getBattle();
      BattleType type = battle.getType();
      int maxRound = tourney.getBattles().stream().filter(b -> b.getType().equals(type))
        .map(Battle::getRoundNumber).max(Comparator.naturalOrder()).orElse(0);
      int maxBattle = tourney.getBattles().stream().filter(b -> b.getType().equals(type))
        .filter(b -> b.getRoundNumber() == battle.getRoundNumber())
        .map(Battle::getBattleNumber).max(Comparator.naturalOrder()).orElse(0);
      title.set(
        "Battle Board (" + type + " Round " + battle.getRoundNumber() + " of " + maxRound
        + ", Battle " + battle.getBattleNumber() + " of " + maxBattle + ")");
    } else {
      title.set("Battle Board");
    }
  }

  private void applyTiming() {
    long remainingTourneyTime = tourney.getDesiredRuntimeMillis() - (System.currentTimeMillis() - tourney.getStartTime());
    System.out.println("Remaining Time: " + remainingTourneyTime);
    List<Battle> remainingBattles = tourney.getBattles().stream()
      .filter(battle -> battle.getResults().isEmpty()).collect(Collectors.toList());
    System.out.println("Remaining Battles: " + remainingBattles.size());    
    long battleMillis = remainingTourneyTime / remainingBattles.size();
    System.out.println("Remaining Battle Millis: " + battleMillis);    
    remainingBattles.forEach(battle -> battle.setDesiredRuntimeMillis(battleMillis));
  }

  private void runBattle() {
    if (tourney.getBattle() != null) {
      applyTiming(); // Adjust timing before each battle
      new Thread(new TourneyThread()).start();
    }
  }

  private class TourneyThread implements Runnable {
    public void run() {
      System.out.println("STARTED Round " + tourney.getBattle().getRoundNumber()
        + " Battle " + tourney.getBattle().getBattleNumber());
      ctl.execute();
    }  
  }

  private void onBattleFinished(BattleEvent e) {
    Battle battle = e.getBattle();
    System.out.println("FINSHED Round " + battle.getRoundNumber() + " Battle " + battle.getBattleNumber());

    if (battle.getAdvanceToBattleNumber() != null) {
      List<Robot> topFinishers = battle.getResults().subList(0, (int)Math.ceil(battle.getResults().size() / 2d)).stream()
        .map(BattleResults::getTeamLeaderName)
        .map(name -> battle.getRobots().stream().filter(r -> name.equals(r.getRobotName())).findFirst().orElse(null))
        .collect(Collectors.toList());
      Battle nextBattle = tourney.getBattles().stream().filter(b ->
        b.getBattleNumber() == battle.getAdvanceToBattleNumber()
          && b.getType() == battle.getType() && b.getRoundNumber() == battle.getRoundNumber() + 1).findAny().orElse(null);
      nextBattle.getRobots().addAll(topFinishers);
    }

    if (setNextBattle() != null) {
      runBattle();
    }
  }

  private void onTurnFinished(BattleEvent e) {
    nowPlayingCards.getChildren().stream().sorted(
        Comparator.comparing(card -> ((RobotCard)card).getRobot().getBattleScore().getScore())
          .thenComparing(card -> ((RobotCard)card).getRobot().getRobotName()))
      .forEach(n -> n.toBack());
  }

  private void onRoundStarted(BattleEvent e) {
    updateTitle();
  }

  private void onRoundFinished(BattleEvent e) {
    updateTitle();
  }
}

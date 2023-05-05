package smi.roborun.ui;

import java.io.File;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import net.sf.robocode.io.FileUtil;
import smi.roborun.ctl.BattleController;
import smi.roborun.ctl.BattleEvent;
import smi.roborun.mdl.Battle;
import smi.roborun.mdl.Battle.BattleType;
import smi.roborun.mdl.Robot;
import smi.roborun.mdl.RobotScore;
import smi.roborun.mdl.Tourney;
import smi.roborun.ui.widgets.PlayClock;
import smi.roborun.ui.widgets.SvgButton;
import smi.roborun.ui.widgets.UiUtil;

public class BattleBoard extends GridPane implements TitledNode {
  private BattleController ctl;
  private Tourney tourney;
  private FlowPane nowPlayingCards;
  private FlowPane upNextTiles;
  private StringProperty title;
  private String robocodeDir;
  private PlayClock playClock;

  public BattleBoard(BattleController ctl, Tourney tourney, String robocodeDir) {
    this.ctl = ctl;
    this.tourney = tourney;
    this.robocodeDir = robocodeDir;

    title = new SimpleStringProperty("Battle Board");
    ctl.addEventHandler(BattleEvent.FINISHED, this::onBattleFinished);
    ctl.addEventHandler(BattleEvent.ROUND_STARTED, this::onRoundStarted);
    ctl.addEventHandler(BattleEvent.ROUND_FINISHED, this::onRoundFinished);
    ctl.addEventHandler(BattleEvent.TURN_FINISHED, this::onTurnFinished);

    // Center Pane
    Button playBtn = new SvgButton("/icons/play-solid.svg", e -> startTourney());
    Button resetBtn = new SvgButton("/icons/rotate-right-solid.svg", e -> reset());
    playClock = new PlayClock();
    playClock.setPrefWidth(200);
    playClock.setAlignment(Pos.CENTER_RIGHT);
    playClock.setFont(new Font("Arial", 24));
    ToolBar toolBar = new ToolBar();
    toolBar.getItems().addAll(UiUtil.hspace(), playBtn, resetBtn, UiUtil.hspace(), playClock, UiUtil.hspace(16));
    Pane robocodePlaceholder = new Pane();
    VBox centerPane = new VBox(toolBar, robocodePlaceholder);
    VBox.setVgrow(robocodePlaceholder, Priority.ALWAYS);

    // Now Playing
    Label nowPlayingTitle = new Label("Now Playing");
    nowPlayingTitle.setFont(new Font("Arial", 24));
    nowPlayingTitle.setUnderline(true);

    nowPlayingCards = new FlowPane();
    nowPlayingCards.setAlignment(Pos.CENTER);
    ScrollPane nowPlayingScrollPane = new ScrollPane();
    nowPlayingScrollPane.getStyleClass().add("now-playing");
    nowPlayingScrollPane.setContent(nowPlayingCards);

    VBox nowPlaying = new VBox(nowPlayingTitle, nowPlayingScrollPane);
    VBox.setVgrow(nowPlayingScrollPane, Priority.ALWAYS);
    nowPlaying.setAlignment(Pos.CENTER);

    // Bracket
    Label bracketTitle = new Label("Bracket");
    bracketTitle.setFont(new Font("Arial", 24));
    bracketTitle.setUnderline(true);

    BracketPane bracketGui = new BracketPane(tourney);
    
    VBox bracket = new VBox(bracketTitle, bracketGui);
    bracket.setFillWidth(true);
    bracket.setAlignment(Pos.CENTER);
    VBox.setVgrow(bracketGui, Priority.ALWAYS);
    bracketGui.prefWidthProperty().bind(bracket.widthProperty());

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
    ColumnConstraints col3 = new ColumnConstraints();
    col1.setHgrow(Priority.SOMETIMES);
    col3.setHgrow(Priority.SOMETIMES);
    col3.setFillWidth(true);
    getColumnConstraints().addAll(col1, new ColumnConstraints(800), col3);

    RowConstraints r1 = new RowConstraints();
    r1.setVgrow(Priority.ALWAYS);
    getRowConstraints().addAll(r1, new RowConstraints());

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

    updateUpNext();
    updateNowPlaying();
  }

  private void updateNowPlaying() {
    nowPlayingCards.getChildren().clear();
    if (tourney.getBattle() != null) {
      tourney.getBattle().getRobots().stream().map(RobotCard::new).forEach(card -> {
        FlowPane.setMargin(card, new Insets(4));
        nowPlayingCards.getChildren().add(card);
      });
      sortNowPlaying();
    }
  }

  private void sortNowPlaying() {
    nowPlayingCards.getChildren().stream().sorted(
      Comparator.comparing(card -> ((RobotCard)card).getRobot().getBattleScore().getScore())
        .thenComparing(card -> ((RobotCard)card).getRobot().getTotalScore().getScore())
        .thenComparing(card -> ((RobotCard)card).getRobot().getRobotName()))
    .forEach(n -> n.toBack());
  }

  private void updateUpNext() {
    upNextTiles.getChildren().clear();
    if (tourney.getBattle() != null) {
      int currBattleIdx = tourney.getBattles().indexOf(tourney.getBattle());
      Battle nextBattle = tourney.getBattles().size() > currBattleIdx + 1 ? tourney.getBattles().get(currBattleIdx + 1) : null;
      if (nextBattle != null) {
        nextBattle.getRobots().stream()
          .sorted(Comparator.comparing(r -> ((Robot)r).getTotalScore().getScore()).reversed()
            .thenComparing(r -> ((Robot)r).getRobotName()))
          .map(RobotTile::new).forEach(tile -> {
            FlowPane.setMargin(tile, new Insets(4));
            upNextTiles.getChildren().add(tile);
          });
        IntStream.range(0, nextBattle.getNumRobots() - nextBattle.getRobots().size())
            .mapToObj(i -> new RobotTile()).forEach(tile -> {
          FlowPane.setMargin(tile, new Insets(4));
          upNextTiles.getChildren().add(tile);
        });
      }
    }
  }

  private Battle getNextBattle() {
    int currBattleIdx = tourney.getBattles().indexOf(tourney.getBattle());
    Battle nextBattle = tourney.getBattles().size() > currBattleIdx + 1 ? tourney.getBattles().get(currBattleIdx + 1) : null;

    if (nextBattle == null) {
      System.out.println("Tournament Ended at: " + new Date());
      tourney.setEndTime(System.currentTimeMillis());
      double seconds = (tourney.getEndTime() - tourney.getStartTime()) / 1000d;
      System.out.println("Tournament finished in " + seconds + " seconds");

      // Save the tournament model
      File file = new File(robocodeDir, "roborun/tourney-" + System.currentTimeMillis() + ".json");
      FileUtil.createDir(file.getParentFile());
      try {
        ObjectMapper om = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
        om.writeValue(file, tourney);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

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
    List<Battle> remainingBattles = tourney.getBattles().stream()
      .filter(battle -> battle.getResults().isEmpty()).collect(Collectors.toList());
    long prePostTimeRemaining = remainingBattles.size() * tourney.getPregameDelayMillis()
      + remainingBattles.size() * tourney.getPostgameDelayMillis() - tourney.getPostgameDelayMillis();
    long remainingTourneyTime = tourney.getDesiredRuntimeMillis() - (System.currentTimeMillis() - tourney.getStartTime());
    System.out.println("Remaining Time: " + remainingTourneyTime);
    System.out.println("Remaining Battles: " + remainingBattles.size());    
    long battleMillis = Math.max(0, remainingTourneyTime - prePostTimeRemaining) / remainingBattles.size();
    System.out.println("Remaining Battle Millis: " + battleMillis);
    remainingBattles.forEach(battle -> battle.setDesiredRuntimeMillis(battleMillis));
  }

  private void runBattle() {
    if (tourney.getBattle() != null) {
      applyTiming(); // Adjust timing before each battle
      pregame();
    }
  }

  private void pregame() {
    playClock.start("Preview", tourney.getPregameDelayMillis(), e -> {
      playClock.start("Battle", tourney.getBattle().getDesiredRuntimeMillis(), null);
      new Thread(new BattleThread()).start();
    });
  }

  private void assignRobotsToVsBattles() {
    List<Robot> sorted = tourney.getRobots().stream()
      .sorted(Comparator.comparing(r -> r.getTotalScore().getScore(), Comparator.reverseOrder()))
      .collect(Collectors.toList());

    int totalRobots = tourney.getRobots().size();
    int numRounds = tourney.getBattles().stream().filter(b -> b.getType() == BattleType.VS).map(Battle::getRoundNumber)
      .max(Comparator.naturalOrder()).orElse(1);

    int numFirstRoundBattles = totalRobots - (int)Math.pow(2, numRounds - 1);
    int maxFirstRoundBattles = (int)Math.pow(2, numRounds - 1);
    final int constNumRounds = numRounds;
    IntStream.range(0, 2).forEach(roundIdx -> {
      int maxNumBattlesInRound = (int)Math.pow(2, constNumRounds - roundIdx - 1);
      int numBattlesInRound = roundIdx == 0 ? numFirstRoundBattles : maxNumBattlesInRound;
      IntStream.range(0, numBattlesInRound).forEach(battleIdx -> {
        if (roundIdx == 0) {
          Battle battle = getBattle(BattleType.VS, roundIdx + 1, battleIdx + 1);
          battle.getRobots().add(sorted.get(totalRobots - numBattlesInRound * 2 + battleIdx));
          battle.getRobots().add(sorted.get(totalRobots - battleIdx - 1));  
        } else if (numFirstRoundBattles < maxFirstRoundBattles) {
          Battle battle = getBattle(BattleType.VS, roundIdx + 1, battleIdx + 1);
          if (battleIdx < totalRobots - numFirstRoundBattles * 2) {
            battle.getRobots().add(sorted.get(battleIdx));
          }
          if (totalRobots - battleIdx * 2 <= totalRobots - numFirstRoundBattles * 2) {
            battle.getRobots().add(sorted.get(totalRobots - numFirstRoundBattles * 2 - battleIdx));  
          }
        }
      });
    });
  }

  private Battle getBattle(BattleType type, int roundNumber, int battleNumber) {
    return tourney.getBattles().stream()
      .filter(b -> b.getType() == type)
      .filter(b -> b.getRoundNumber() == roundNumber)
      .filter(b -> b.getBattleNumber() == battleNumber)
      .findAny().orElse(null);
  }

  private class BattleThread implements Runnable {
    public void run() {
      System.out.println("STARTED Round " + tourney.getBattle().getRoundNumber()
        + " Battle " + tourney.getBattle().getBattleNumber());
      ctl.playBattle();
    }  
  }

  private void onBattleFinished(BattleEvent e) {
    Battle battle = e.getBattle();
    playClock.reset(0);
    System.out.println("FINSHED Round " + battle.getRoundNumber() + " Battle " + battle.getBattleNumber());

    Battle nextBattle = getNextBattle();
    if (nextBattle != null) {
      if (battle.getType() == BattleType.MELEE && nextBattle.getType() == BattleType.VS) {
        // We're at a special point in the tournament where the melee just ended and the
        // 1v1 is just beginning.  We need to assign the robots to the 1v1 battles based
        // on their current ranks.
        assignRobotsToVsBattles();
      }

      // Move the winners into their next battle
      if (battle.getAdvanceToBattleNumber() != null) {
        List<Robot> topFinishers = battle.getResults().subList(0, (int)Math.ceil(battle.getResults().size() / 2d)).stream()
          .map(RobotScore::getRobotName)
          .map(name -> battle.getRobots().stream().filter(r -> name.equals(r.getRobotName())).findFirst().orElse(null))
          .collect(Collectors.toList());
        Battle advanceToBattle = tourney.getBattles().stream().filter(b ->
          b.getBattleNumber() == battle.getAdvanceToBattleNumber()
            && b.getType() == battle.getType() && b.getRoundNumber() == battle.getRoundNumber() + 1).findAny().orElse(null);
          advanceToBattle.getRobots().addAll(topFinishers);
      }

      sortNowPlaying();
      updateUpNext();

      playClock.start("Wrap Up", tourney.getPostgameDelayMillis(), e2 -> {
        tourney.getBattle().getRobots().forEach(r -> r.getBattleScore().reset(false));
        tourney.setBattle(nextBattle);
        updateTitle();
        runBattle();
      });
    }
  }

  private void onTurnFinished(BattleEvent e) {
    sortNowPlaying();
  }

  private void onRoundStarted(BattleEvent e) {
    updateTitle();
  }

  private void onRoundFinished(BattleEvent e) {
    updateTitle();
  }
}

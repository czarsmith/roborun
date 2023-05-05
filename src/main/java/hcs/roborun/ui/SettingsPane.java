package smi.roborun.ui;

import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.layout.GridPane;
import smi.roborun.ctl.BattleController;
import smi.roborun.mdl.Battle;
import smi.roborun.mdl.Battle.BattleType;
import smi.roborun.mdl.Robot;
import smi.roborun.mdl.Round;
import smi.roborun.mdl.Tourney;
import smi.roborun.ui.widgets.TextInput;
import smi.roborun.ui.widgets.UiUtil;

public class SettingsPane extends GridPane {
  private ObservableList<Robot> robots;
  private TableView<Robot> robotGrid;
  private TextInput tourneyTimeField;
  private TextInput tpsField;
  private TextInput roundsPerMeleeField;
  private TextInput maxMeleeSizeField;
  private TextInput meleeWidthField;
  private TextInput meleeHeightField;
  private TextInput roundsPerVsField;
  private TextInput vsWidthField;
  private TextInput vsHeightField;
  private Tourney tourney;

  public SettingsPane(BattleController ctl, Tourney tourney) {
    this.tourney = tourney;
    this.setAlignment(Pos.CENTER);

    robots = FXCollections.observableArrayList(ctl.getRobots().stream().map(Robot::new).collect(Collectors.toList()));

    robotGrid = new TableView<>();
    robotGrid.setPrefWidth(400);
    robotGrid.setEditable(true);
    TableColumn<Robot, Boolean> selectedCol = UiUtil.tableCol("Selected", cd -> cd.getValue().getSelectedProperty());
    selectedCol.setCellFactory(col -> new CheckBoxTableCell<>());
    robotGrid.getColumns().add(selectedCol);
    robotGrid.getColumns().add(UiUtil.<Robot, String>tableCol("Author", c -> c.getValue().getAuthorProperty()));
    robotGrid.getColumns().add(UiUtil.<Robot, String>tableCol("Robot", c -> c.getValue().getRobotNameProperty()));
    robotGrid.getColumns().add(UiUtil.<Robot, Integer>tableCol("Code Size", c -> c.getValue().getCodeSizeProperty()));
    robotGrid.getItems().addAll(robots);
    robotGrid.getColumns().get(2).setSortType(TableColumn.SortType.DESCENDING);
    robotGrid.getSortOrder().add(robotGrid.getColumns().get(2));
    add(robotGrid, 0, 0);

    ChangeListener<String> cl = (a, b, c) -> createTourney();

    GridPane form = new GridPane();
    form.setPadding(new Insets(16));
    form.setHgap(16);
    form.setVgap(16);

    tourneyTimeField = new TextInput(UiUtil.millisToHMS(tourney.getDesiredRuntimeMillis()),
      "[0-9][0-9]:[0-9][0-9]:[0-9][0-9]", cl);
    form.add(new Label("Tournament Duration (HH:MM:SS): "), 0, 0);
    form.add(tourneyTimeField, 1, 0);

    tpsField = new TextInput(Integer.toString(tourney.getDesiredTps()), "[1-9][0-9]*", cl);
    form.add(new Label("Minimum TPS: "), 0, 1);
    form.add(tpsField, 1, 1);

    roundsPerMeleeField = new TextInput(Long.toString(tourney.getNumMeleeRoundsPerBattle()), "[1-9][0-9]*", cl);
    form.add(new Label("Rounds Per Melee Battle: "), 2, 0);
    form.add(roundsPerMeleeField, 3, 0);

    meleeWidthField = new TextInput(Integer.toString(tourney.getMeleeBattlefieldWidth()), "[1-9][0-9]*", cl);
    form.add(new Label("Melee Battlefield Width: "), 2, 1);
    form.add(meleeWidthField, 3, 1);

    meleeHeightField = new TextInput(Integer.toString(tourney.getMeleeBattlefieldHeight()), "[1-9][0-9]*", cl);
    form.add(new Label("Melee Battlefield Height: "), 2, 2);
    form.add(meleeHeightField, 3, 2);

    maxMeleeSizeField = new TextInput(Integer.toString(tourney.getMaxMeleeSize()), "6|8|10|12|16", cl);
    form.add(new Label("Max Melee Size (6 - 16): "), 2, 3);
    form.add(maxMeleeSizeField, 3, 3);

    roundsPerVsField = new TextInput(Long.toString(tourney.getNumVsRoundsPerBattle()), "[1-9][0-9]*", cl);
    form.add(new Label("Rounds Per 1v1 Battle: "), 4, 0);
    form.add(roundsPerVsField, 5, 0);

    vsWidthField = new TextInput(Integer.toString(tourney.getVsBattlefieldWidth()), "[1-9][0-9]*", cl);
    form.add(new Label("1v1 Battlefield Width: "), 4, 1);
    form.add(vsWidthField, 5, 1);

    vsHeightField = new TextInput(Integer.toString(tourney.getVsBattlefieldHeight()), "[1-9][0-9]*", cl);
    form.add(new Label("1v1 Battlefield Height: "), 4, 2);
    form.add(vsHeightField, 5, 2);

    add(form, 0, 1);

    robots.forEach(r -> r.getSelectedProperty().addListener(b -> createTourney()));
  }

  public void createTourney() {
    tourney.reset(true);
    tourney.setRobots(robots.filtered(Robot::getSelected));
    tourney.setDesiredRuntimeMillis(UiUtil.hmsToMillis(tourneyTimeField.getText()));
    tourney.setDesiredTps(Integer.parseInt(tpsField.getText()));
    tourney.setNumMeleeRoundsPerBattle(Integer.parseInt(roundsPerMeleeField.getText()));
    tourney.setMeleeBattlefieldWidth(Integer.parseInt(meleeWidthField.getText()));
    tourney.setMeleeBattlefieldHeight(Integer.parseInt(meleeHeightField.getText()));
    tourney.setMaxMeleeSize(Integer.parseInt(maxMeleeSizeField.getText()));
    tourney.setNumVsRoundsPerBattle(Integer.parseInt(roundsPerVsField.getText()));
    tourney.setVsBattlefieldWidth(Integer.parseInt(vsWidthField.getText()));
    tourney.setVsBattlefieldHeight(Integer.parseInt(vsHeightField.getText()));

    if (tourney.getRobots().size() < 2) {
      return;
    }

    // Assign random seeds
    Random rand = new Random();
    tourney.getRobots().forEach(r -> r.setRandomSeed(rand.nextInt(1000)));
    int seed = 1;
    List<Robot> sorted = tourney.getRobots().stream().sorted(Comparator.comparing(Robot::getRandomSeed))
      .collect(Collectors.toList());
    for (int i = 0; i < sorted.size(); i++) {
      sorted.get(i).setRandomSeed(seed++);
    }
    
    createBattles(tourney);
    tourney.setBattle(tourney.getBattles().get(0));
  }

  private void createBattles(Tourney tourney) {
    createMeleeBattles(tourney);
    createVsBattles(tourney);
  }

  private void createMeleeBattles(Tourney tourney) {
    int numRounds = computeNumRounds(tourney.getMaxMeleeSize());
    IntStream.range(0, numRounds).forEach(roundIdx -> {
      int numBattlesInRound = (int)Math.pow(2, numRounds - roundIdx - 1);
      Round round = tourney.addRound(new Round(BattleType.MELEE, roundIdx + 1, numBattlesInRound, numBattlesInRound));
      IntStream.range(0, numBattlesInRound).forEach(battleIdx -> {
        Battle battle = new Battle(round);
        battle.setType(BattleType.MELEE);
        battle.setBattlefieldHeight(tourney.getMeleeBattlefieldWidth());
        battle.setBattlefieldHeight(tourney.getMeleeBattlefieldHeight());
        battle.setNumBattleRounds(tourney.getNumMeleeRoundsPerBattle());
        battle.setTps(tourney.getDesiredTps());
        battle.setBattleNumber(battleIdx + 1);
        tourney.getBattles().add(battle);

        if (numBattlesInRound > 1) {
          battle.setAdvanceToBattleNumber((battle.getBattleNumber() - 1) / 2 + 1);
        }    
      });
    });

    List<Robot> sorted = tourney.getRobots().stream().sorted(Comparator.comparing(Robot::getRandomSeed))
      .collect(Collectors.toList());

    // Distribute all of the robots evenly in the first melee round
    List<Battle> firstRoundBattles = tourney.getBattles().stream()
      .filter(b -> b.getRoundNumber() == 1).collect(Collectors.toList());
    IntStream.range(0, sorted.size()).forEach(robotIdx ->
      firstRoundBattles.get(robotIdx % firstRoundBattles.size()).getRobots().add(sorted.get(robotIdx)));
    
    // For each battle, record the number of robots that will participate.
    tourney.getBattles().stream().filter(b -> b.getRoundNumber() == 1).forEach(b ->
      b.setNumRobots(b.getRobots().size()));
    IntStream.range(1, numRounds).forEach(roundIdx -> {
      tourney.getBattles().stream().filter(b -> b.getRoundNumber() == roundIdx + 1).forEach(b -> {
        b.setNumRobots(tourney.getBattles().stream()
          .filter(b2 -> b2.getRoundNumber() == b.getRoundNumber() - 1)
          .filter(b2 -> b2.getAdvanceToBattleNumber() == b.getBattleNumber())
          .map(b2 -> (int)Math.ceil(b2.getNumRobots() / 2d))
          .collect(Collectors.summingInt(o -> o)));
      });
    });
  }

  private void createVsBattles(Tourney tourney) {
    int totalRobots = tourney.getRobots().size();
    int numRounds = computeNumRounds(2);
    int numFirstRoundBattles = totalRobots - (int)Math.pow(2, numRounds - 1);
    IntStream.range(0, numRounds).forEach(roundIdx -> {
      int maxNumBattlesInRound = (int)Math.pow(2, numRounds - roundIdx - 1);
      Round round = tourney.addRound(new Round(BattleType.VS, roundIdx + 1,
        roundIdx == 0 ? numFirstRoundBattles : maxNumBattlesInRound,
        (int)Math.pow(2, numRounds - roundIdx - 1)));
      IntStream.range(0, round.getNumBattles()).forEach(battleIdx -> {
        Battle battle = new Battle(round);
        battle.setType(BattleType.VS);
        battle.setBattlefieldWidth(tourney.getVsBattlefieldWidth());
        battle.setBattlefieldHeight(tourney.getVsBattlefieldHeight());
        battle.setNumBattleRounds(tourney.getNumVsRoundsPerBattle());
        battle.setTps(tourney.getDesiredTps());
        battle.setBattleNumber(battleIdx + 1);
        battle.setNumRobots(2);

        // Figure out where the winners will go
        if (roundIdx < numRounds - 1) {
          int numBattlesInNextRound = (int)Math.pow(2, numRounds - roundIdx - 2);
          int effectiveBattleIdx = battleIdx + maxNumBattlesInRound - round.getNumBattles();
          int nextBattleIdx = effectiveBattleIdx % (maxNumBattlesInRound / 2);
          if (effectiveBattleIdx < maxNumBattlesInRound / 2) {
            battle.setAdvanceToBattleNumber(nextBattleIdx + 1);
          } else {
            battle.setAdvanceToBattleNumber(numBattlesInNextRound - nextBattleIdx);
          }
        }
        tourney.getBattles().add(battle);
      });
    });
  }

  private int computeNumRounds(int numRobotsPerBattle) {
    int totalRobots = tourney.getRobots().size();
    int numRounds = 1;
    while (totalRobots > Math.pow(2, numRounds - 1) * numRobotsPerBattle){
      numRounds++; // There has to be a better way to calculate this
    }
    return numRounds;
  }
}

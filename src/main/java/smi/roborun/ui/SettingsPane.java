package smi.roborun.ui;

import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import smi.roborun.ctl.BattleController;
import smi.roborun.mdl.Battle;
import smi.roborun.mdl.Battle.BattleType;
import smi.roborun.mdl.Robot;
import smi.roborun.mdl.Tourney;
import smi.roborun.ui.widgets.UiUtil;

public class SettingsPane extends GridPane {
  private ObservableList<Robot> robots;
  private TableView<Robot> robotGrid;
  private TextField maxMeleeSizeField;
  private TextField tourneyTimeField;
  private Tourney tourney;

  public SettingsPane(BattleController ctl, Tourney tourney) {
    this.tourney = tourney;
    this.setAlignment(Pos.CENTER);

    robots = FXCollections.observableArrayList(ctl.getRobots().stream().map(Robot::new).collect(Collectors.toList()));

    robotGrid = new TableView<>();
    robotGrid.setEditable(true);
    robotGrid.setMaxWidth(Double.MAX_VALUE);
    TableColumn<Robot, Boolean> selectedCol = UiUtil.tableCol("Selected", cd -> cd.getValue().getSelectedProperty());
    selectedCol.setCellFactory(col -> new CheckBoxTableCell<>());
    robotGrid.getColumns().add(selectedCol);
    robotGrid.getColumns().add(UiUtil.<Robot, String>tableCol("Author", c -> c.getValue().getAuthorProperty()));
    robotGrid.getColumns().add(UiUtil.<Robot, String>tableCol("Robot", c -> c.getValue().getRobotNameProperty()));
    robotGrid.getColumns().add(UiUtil.<Robot, Integer>tableCol("Code Size", c -> c.getValue().getCodeSizeProperty()));
    robotGrid.getItems().addAll(robots);
    robotGrid.getColumns().get(2).setSortType(TableColumn.SortType.DESCENDING);
    robotGrid.getSortOrder().add(robotGrid.getColumns().get(2));
    GridPane.setHgrow(robotGrid, Priority.ALWAYS);
    add(robotGrid, 0, 0);

    GridPane form = new GridPane();
    maxMeleeSizeField = new TextField(Integer.toString(tourney.getMaxMeleeSize()));
    maxMeleeSizeField.setOnAction(e -> this.createTourney());
    form.add(new Label("Max Melee Size: "), 0, 0);
    form.add(maxMeleeSizeField, 1, 0);
    tourneyTimeField = new TextField(Long.toString(tourney.getDesiredRuntimeMillis() / 1000));
    tourneyTimeField.setOnAction(e -> this.createTourney());
    form.add(new Label("Tournament Duration Seconds: "), 0, 1);
    form.add(tourneyTimeField, 1, 1);
    add(form, 0, 1);

    robots.forEach(r -> r.getSelectedProperty().addListener(b -> this.createTourney()));
  }

  public void createTourney() {
    tourney.reset(true);
    tourney.setRobots(robots.filtered(Robot::getSelected));
    tourney.setMaxMeleeSize(Integer.parseInt(maxMeleeSizeField.getText()));
    tourney.setDesiredRuntimeMillis(Long.parseLong(tourneyTimeField.getText()) * 1000);

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

    // Log the entire tournament model
    try {
      ObjectMapper om = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
      System.out.println(om.writeValueAsString(tourney));  
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void createBattles(Tourney tourney) {
    createMeleeBattles(tourney);
    createVsBattles(tourney);
  }

  private void createMeleeBattles(Tourney tourney) {
    int numRounds = computeNumRounds(tourney.getMaxMeleeSize());
    IntStream.range(0, numRounds).forEach(roundIdx -> {
      int numBattlesInRound = (int)Math.pow(2, numRounds - roundIdx - 1);
      IntStream.range(0, numBattlesInRound).forEach(battleIdx -> {
        Battle battle = new Battle();
        battle.setType(BattleType.MELEE);
        battle.setBattlefieldHeight(tourney.getMeleeBattlefieldWidth());
        battle.setBattlefieldHeight(tourney.getMeleeBattlefieldHeight());
        battle.setNumRounds(tourney.getNumMeleeRoundsPerBattle());
        battle.setTps(tourney.getDesiredTps());
        battle.setRoundNumber(roundIdx + 1);
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
    IntStream.range(0, numRounds).forEach(roundIdx -> {
      tourney.getBattles().stream().filter(b -> b.getRoundNumber() == roundIdx + 1).forEach(b ->
        b.setNumRobots(roundIdx == 0 ? b.getRobots().size() : tourney.getMaxMeleeSize()));
    });
  }

  private void createVsBattles(Tourney tourney) {
    int totalRobots = tourney.getRobots().size();
    int numRounds = computeNumRounds(2);
    int numFirstRoundBattles = totalRobots - (int)Math.pow(2, numRounds - 1);
    IntStream.range(0, numRounds).forEach(roundIdx -> {
      int maxNumBattlesInRound = (int)Math.pow(2, numRounds - roundIdx - 1);
      int numBattlesInRound = roundIdx == 0 ? numFirstRoundBattles : maxNumBattlesInRound;
      boolean isPreliminary = numBattlesInRound < maxNumBattlesInRound;
      IntStream.range(0, numBattlesInRound).forEach(battleIdx -> {
        Battle battle = new Battle();
        battle.setType(BattleType.VS);
        battle.setBattlefieldWidth(tourney.getVsBattlefieldWidth());
        battle.setBattlefieldHeight(tourney.getVsBattlefieldHeight());
        battle.setNumRounds(tourney.getNumVsRoundsPerBattle());
        battle.setTps(tourney.getDesiredTps());
        battle.setRoundNumber(roundIdx + 1);
        battle.setBattleNumber(battleIdx + 1);
        battle.setNumRobots(2);
        battle.setPreliminary(isPreliminary);

        // Figure out where the winners will go
        if (roundIdx < numRounds - 1) {
          int numBattlesInNextRound = (int)Math.pow(2, numRounds - roundIdx - 2);
          int effectiveBattleIdx = battleIdx + maxNumBattlesInRound - numBattlesInRound;
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

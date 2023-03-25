package smi.roborun.ui.settings;

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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import smi.roborun.ctl.BattleController;
import smi.roborun.mdl.Battle;
import smi.roborun.mdl.Battle.BattleType;
import smi.roborun.mdl.Robot;
import smi.roborun.mdl.Round;
import smi.roborun.mdl.Tourney;
import smi.roborun.ui.widgets.UiUtil;

public class SettingsPane extends GridPane {
  private BattleController ctl;

  private ObservableList<Robot> robots;
  private TableView<Robot> robotGrid;

  public SettingsPane(BattleController ctl) {
    this.ctl = ctl;
    this.setAlignment(Pos.CENTER);
    this.setBorder(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, new CornerRadii(5), BorderStroke.THICK)));

    robots = FXCollections.observableArrayList(ctl.getRobots().stream().map(Robot::new).collect(Collectors.toList()));

    robotGrid = new TableView<>();
    robotGrid.setEditable(true);
    robotGrid.setMaxWidth(Double.MAX_VALUE);
    TableColumn<Robot, Boolean> selectedCol = UiUtil.tableCol("Selected", cd -> cd.getValue().getSelectedProperty());
    selectedCol.setCellFactory(col -> new CheckBoxTableCell<>());
    robotGrid.getColumns().add(selectedCol);
    robotGrid.getColumns().add(UiUtil.<Robot, String>tableCol("Author", c -> c.getValue().getAuthorProperty()));
    robotGrid.getColumns().add(UiUtil.<Robot, String>tableCol("Robot", c -> c.getValue().getRobotNameProperty()));
    robotGrid.getColumns().add(UiUtil.<Robot, Long>tableCol("Code Size", c -> c.getValue().getCodeSizeProperty()));
    robotGrid.getItems().addAll(robots);
    robotGrid.getColumns().get(2).setSortType(TableColumn.SortType.DESCENDING);
    robotGrid.getSortOrder().add(robotGrid.getColumns().get(2));
    GridPane.setHgrow(robotGrid, Priority.ALWAYS);
    add(robotGrid, 0, 0);
  }

  public Tourney createTourney() {
    Tourney tourney = new Tourney();
    tourney.setRobots(robots.filtered(Robot::getSelected));
    if (tourney.getRobots().size() < 2) {
      UiUtil.error("You must select at least two robots.");
    }
    
    createBattles(tourney);

    applyTiming(tourney);

    // Log the entire tournament model
    try {
      ObjectMapper om = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
//      System.out.println(om.writeValueAsString(tourney));  
    } catch (Exception e) {
      e.printStackTrace();
    }

    return tourney;
  }

  private void createBattles(Tourney tourney) {
    createMeleeBattles(tourney);
    createVsBattles(tourney);
  }

  private void createMeleeBattles(Tourney tourney) {
    int totalRobots = tourney.getRobots().size();

    // Assign random seeds for melee
    Random rand = new Random();
    int seed = 1;
    tourney.getRobots().forEach(r -> r.setMeleeSeed(rand.nextInt(1000)));
    List<Robot> sorted = tourney.getRobots().stream().sorted(Comparator.comparing(Robot::getMeleeSeed))
      .collect(Collectors.toList());
    for (int i = 0; i < sorted.size(); i++) {
      sorted.get(i).setMeleeSeed(seed++);
    }

    int numRounds = 1;
    while (totalRobots > Math.pow(2, numRounds - 1) * tourney.getMaxMeleeSize()){
      numRounds++; // There has to be a better way to calculate this
    }
    for (int i = 0; i < numRounds; i++) {
      tourney.getMeleeRounds().add(new Round(i + 1, (int)Math.pow(2, numRounds - i - 1)));
    }

    // Initialize all of the battles
    tourney.getMeleeRounds().forEach(meleeRound -> {
      IntStream.range(0, meleeRound.getNumBattles()).forEach(battleIdx -> {
        Battle battle = new Battle();
        battle.setType(BattleType.MELEE);
        battle.setBattlefieldHeight(tourney.getMeleeBattlefieldWidth());
        battle.setBattlefieldHeight(tourney.getMeleeBattlefieldHeight());
        battle.setNumRounds(tourney.getNumMeleeRoundsPerBattle());
        battle.setTps(tourney.getDesiredTps());
        battle.setRoundNumber(meleeRound.getRoundNumber());
        battle.setBattleNumber(battleIdx + 1);
        meleeRound.getBattles().add(battle);
        tourney.setNumBattles(tourney.getNumBattles() + 1);
      });  
    });

    // Distribute all of the robots evenly in the first melee round
    Round firstMeleeRound = tourney.getMeleeRounds().get(0);
    IntStream.range(0, sorted.size()).forEach(robotIdx ->
      firstMeleeRound.getBattles().get(robotIdx % firstMeleeRound.getNumBattles())
        .getRobots().add(sorted.get(robotIdx)));
  }

  private void createVsBattles(Tourney tourney) {
  }

  private void applyTiming(Tourney tourney) {
    long battleMillis = tourney.getDesiredRuntimeMillis() / tourney.getNumBattles();
    tourney.getMeleeRounds().forEach(round -> round.getBattles().forEach(battle ->
      battle.setDesiredRuntimeMillis(battleMillis)));
  }
}

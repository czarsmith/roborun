package smi.roborun.ui;

import java.util.Comparator;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import smi.roborun.mdl.Battle;
import smi.roborun.mdl.RobotScore;
import smi.roborun.mdl.Tourney;
import smi.roborun.ui.widgets.UiUtil;

public class BattleResultsPane extends VBox {
  private TableView<RobotScore> grid;

  public BattleResultsPane(Tourney tourney, Battle battle) {
    Label lbl = new Label(battle.getType() + " Round " + battle.getRoundNumber() + " Battle " + battle.getBattleNumber());
    lbl.setFont(new Font("Arial", 18));
    getChildren().add(lbl);

    grid = new TableView<>();
    grid.getStyleClass().add("results");
    grid.getColumns().add(UiUtil.<RobotScore, Number>tableCol("Rank", c -> c.getValue().getRankProperty(), 50));
    grid.getColumns().add(UiUtil.<RobotScore, String>tableCol("Robot", c -> tourney.getRobot(c.getValue().getRobotName()).getShortNameProperty(), 150));
    grid.getColumns().add(UiUtil.<RobotScore, String>tableCol("Author", c -> tourney.getRobot(c.getValue().getRobotName()).getAuthorProperty(), 150));
    grid.getColumns().add(UiUtil.<RobotScore, Integer>tableCol("Weight", c -> tourney.getRobot(c.getValue().getRobotName()).getCodeSizeProperty(), 60));
    grid.getColumns().add(UiUtil.<RobotScore, Number>tableCol("Score", c -> c.getValue().getScoreProperty(), 70));
    grid.getColumns().add(UiUtil.<RobotScore, Number>tableCol("Survival", c -> c.getValue().getSurvivalProperty(), 70));
    grid.getColumns().add(UiUtil.<RobotScore, Number>tableCol("Surv Bonus", c -> c.getValue().getLastSurvivorBonusProperty(), 50));
    grid.getColumns().add(UiUtil.<RobotScore, Number>tableCol("Bullet Dmg", c -> c.getValue().getBulletDamageProperty(), 50));
    grid.getColumns().add(UiUtil.<RobotScore, Number>tableCol("Bullet Bonus", c -> c.getValue().getBulletDamageBonusProperty(), 50));
    grid.getColumns().add(UiUtil.<RobotScore, Number>tableCol("Ram Dmg", c -> c.getValue().getRamDamageProperty(), 50));
    grid.getColumns().add(UiUtil.<RobotScore, Number>tableCol("Ram Bonus", c -> c.getValue().getRamDamageBonusProperty(), 50));
    grid.getColumns().add(UiUtil.<RobotScore, Number>tableCol("1sts", c -> c.getValue().getFirstsProperty(), 50));
    grid.getColumns().add(UiUtil.<RobotScore, Number>tableCol("2nds", c -> c.getValue().getSecondsProperty(), 50));
    grid.getColumns().add(UiUtil.<RobotScore, Number>tableCol("3rds", c -> c.getValue().getThirdsProperty(), 50));

    // Table sorting
    ObservableList<RobotScore> observableScores = FXCollections.observableArrayList(
      r -> new Observable[] { r.getScoreProperty() });
    observableScores.addAll(battle.getResults());
    SortedList<RobotScore> sortedScores = new SortedList<>(observableScores,
      Comparator.comparing(r -> r.getScore(), Comparator.reverseOrder()));
    battle.getResults().addListener((ListChangeListener<? super RobotScore>)c -> {
      observableScores.clear();
      observableScores.addAll(battle.getResults());
    });
    grid.setItems(sortedScores);

    // Table size
    grid.setFixedCellSize(25);
    grid.setPrefHeight(25 * (battle.getNumRobots() + 1));
    grid.setMinHeight(grid.getPrefHeight());
    grid.setMaxHeight(grid.getPrefHeight());
    grid.setPrefWidth(grid.getColumns().stream().map(TableColumn::getPrefWidth).reduce((a, b) -> a + b).orElse(0d));

    getChildren().add(grid);
  }
}

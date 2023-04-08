package smi.roborun.ui;

import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import smi.roborun.mdl.Battle;
import smi.roborun.mdl.RobotScore;
import smi.roborun.ui.widgets.UiUtil;

public class BattleResultsPane extends VBox {
  private TableView<RobotScore> resultsGrid;

  public BattleResultsPane(Battle battle) {
    getChildren().add(new Label(battle.getType() + " Round " + battle.getRoundNumber() + " Battle " + battle.getBattleNumber()));

    resultsGrid = new TableView<>();
    resultsGrid.getColumns().add(UiUtil.<RobotScore, Number>tableCol("Rank", rs -> rs.getValue().getRankProperty()));
    resultsGrid.getColumns().add(UiUtil.<RobotScore, String>tableCol("Author", rs -> rs.getValue().getAuthorProperty()));
    resultsGrid.getColumns().add(UiUtil.<RobotScore, String>tableCol("Robot", rs -> rs.getValue().getRobotNameProperty()));
    resultsGrid.getColumns().add(UiUtil.<RobotScore, Number>tableCol("Score", rs -> rs.getValue().getScoreProperty()));
    resultsGrid.getSortOrder().add(resultsGrid.getColumns().get(0));
    resultsGrid.setItems(battle.getResults());
    getChildren().add(resultsGrid);
  }
}

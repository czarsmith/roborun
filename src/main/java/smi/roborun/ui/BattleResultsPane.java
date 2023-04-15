package smi.roborun.ui;

import javafx.beans.binding.Bindings;
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
    grid.getColumns().add(UiUtil.<RobotScore, Number>tableCol("Score", c -> c.getValue().getScoreProperty(), 50));
    grid.getColumns().add(UiUtil.<RobotScore, Number>tableCol("Survival", c -> c.getValue().getSurvivalProperty(), 50));
    grid.getColumns().add(UiUtil.<RobotScore, Number>tableCol("Surv Bonus", c -> c.getValue().getLastSurvivorBonusProperty(), 50));
    grid.getColumns().add(UiUtil.<RobotScore, Number>tableCol("Bullet Dmg", c -> c.getValue().getBulletDamageProperty(), 50));
    grid.getColumns().add(UiUtil.<RobotScore, Number>tableCol("Bullet Bonus", c -> c.getValue().getBulletDamageBonusProperty(), 50));
    grid.getColumns().add(UiUtil.<RobotScore, Number>tableCol("Ram Dmg", c -> c.getValue().getRamDamageProperty(), 50));
    grid.getColumns().add(UiUtil.<RobotScore, Number>tableCol("Ram Bonus", c -> c.getValue().getRamDamageBonusProperty(), 50));
    grid.getColumns().add(UiUtil.<RobotScore, Number>tableCol("1sts", c -> c.getValue().getFirstsProperty(), 50));
    grid.getColumns().add(UiUtil.<RobotScore, Number>tableCol("2nds", c -> c.getValue().getSecondsProperty(), 50));
    grid.getColumns().add(UiUtil.<RobotScore, Number>tableCol("3rds", c -> c.getValue().getThirdsProperty(), 50));
    grid.getColumns().get(0).setSortType(TableColumn.SortType.ASCENDING);
    grid.getSortOrder().add(grid.getColumns().get(0));
    grid.setItems(battle.getResults());
    grid.setFixedCellSize(25);
    grid.prefHeightProperty().bind(grid.fixedCellSizeProperty().multiply(Bindings.size(grid.getItems()).add(1)));
    grid.minHeightProperty().bind(grid.prefHeightProperty());
    grid.maxHeightProperty().bind(grid.prefHeightProperty());
    grid.setPrefWidth(grid.getColumns().stream().map(TableColumn::getPrefWidth).reduce((a, b) -> a + b).orElse(0d));
    getChildren().add(grid);
  }
}

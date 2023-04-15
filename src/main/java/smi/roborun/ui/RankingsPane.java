package smi.roborun.ui;

import javafx.beans.binding.Bindings;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import smi.roborun.mdl.Robot;
import smi.roborun.mdl.Tourney;
import smi.roborun.ui.widgets.UiUtil;

public class RankingsPane extends VBox {
  private TableView<Robot> grid;

  public RankingsPane(Tourney tourney) {
    Label lbl = new Label("Total Scores");
    lbl.setFont(new Font("Arial", 18));
    getChildren().add(lbl);

    grid = new TableView<>();
    grid.getStyleClass().add("results");
    grid.getColumns().add(UiUtil.<Robot, Number>tableCol("Rank", c -> c.getValue().getTotalScore().getRankProperty(), 50));
    grid.getColumns().add(UiUtil.<Robot, String>tableCol("Robot", c -> c.getValue().getShortNameProperty(), 150));
    grid.getColumns().add(UiUtil.<Robot, String>tableCol("Author", c -> c.getValue().getAuthorProperty(), 150));
    grid.getColumns().add(UiUtil.<Robot, Integer>tableCol("Weight", c -> c.getValue().getCodeSizeProperty(), 60));
    grid.getColumns().add(UiUtil.<Robot, Number>tableCol("Score", c -> c.getValue().getTotalScore().getScoreProperty(), 50));
    grid.getColumns().add(UiUtil.<Robot, Number>tableCol("Survival", c -> c.getValue().getTotalScore().getSurvivalProperty(), 50));
    grid.getColumns().add(UiUtil.<Robot, Number>tableCol("Surv Bonus", c -> c.getValue().getTotalScore().getLastSurvivorBonusProperty(), 50));
    grid.getColumns().add(UiUtil.<Robot, Number>tableCol("Bullet Dmg", c -> c.getValue().getTotalScore().getBulletDamageProperty(), 50));
    grid.getColumns().add(UiUtil.<Robot, Number>tableCol("Bullet Bonus", c -> c.getValue().getTotalScore().getBulletDamageBonusProperty(), 50));
    grid.getColumns().add(UiUtil.<Robot, Number>tableCol("Ram Dmg", c -> c.getValue().getTotalScore().getRamDamageProperty(), 50));
    grid.getColumns().add(UiUtil.<Robot, Number>tableCol("Ram Bonus", c -> c.getValue().getTotalScore().getRamDamageBonusProperty(), 50));
    grid.getColumns().add(UiUtil.<Robot, Number>tableCol("1sts", c -> c.getValue().getTotalScore().getFirstsProperty(), 50));
    grid.getColumns().add(UiUtil.<Robot, Number>tableCol("2nds", c -> c.getValue().getTotalScore().getSecondsProperty(), 50));
    grid.getColumns().add(UiUtil.<Robot, Number>tableCol("3rds", c -> c.getValue().getTotalScore().getThirdsProperty(), 50));
    
    // Table sorting
    grid.getColumns().get(0).setSortType(TableColumn.SortType.ASCENDING);
    grid.getSortOrder().add(grid.getColumns().get(0));
    SortedList<Robot> sortedRobots = new SortedList<>(tourney.getRobots());
		sortedRobots.comparatorProperty().bind(grid.comparatorProperty());
    grid.setItems(sortedRobots);

    // Table size
    grid.setFixedCellSize(25);
    grid.prefHeightProperty().bind(grid.fixedCellSizeProperty().multiply(Bindings.size(grid.getItems()).add(1)));
    grid.minHeightProperty().bind(grid.prefHeightProperty());
    grid.maxHeightProperty().bind(grid.prefHeightProperty());
    grid.setPrefWidth(grid.getColumns().stream().map(TableColumn::getPrefWidth).reduce((a, b) -> a + b).orElse(0d));

    getChildren().add(grid);
  }
}

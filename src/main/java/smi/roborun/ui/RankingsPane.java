package smi.roborun.ui;

import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import smi.roborun.mdl.Robot;
import smi.roborun.mdl.Tourney;
import smi.roborun.ui.widgets.UiUtil;

public class RankingsPane extends VBox {
  private TableView<Robot> robotGrid;

  public RankingsPane(Tourney tourney) {
    getChildren().add(new Label("Total Scores"));

    robotGrid = new TableView<>();
    robotGrid.getColumns().add(UiUtil.<Robot, Number>tableCol("Rank", c -> c.getValue().getTotalScore().getRankProperty()));
    robotGrid.getColumns().add(UiUtil.<Robot, String>tableCol("Author", c -> c.getValue().getAuthorProperty()));
    robotGrid.getColumns().add(UiUtil.<Robot, String>tableCol("Robot", c -> c.getValue().getRobotNameProperty()));
    robotGrid.getColumns().add(UiUtil.<Robot, Integer>tableCol("Weight", c -> c.getValue().getCodeSizeProperty()));
    robotGrid.getColumns().add(UiUtil.<Robot, Number>tableCol("Score", c -> c.getValue().getTotalScore().getScoreProperty()));
    robotGrid.getSortOrder().add(robotGrid.getColumns().get(0));
    robotGrid.setItems(tourney.getRobots());
    getChildren().add(robotGrid);
  }
}

package smi.roborun.ui;

import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import smi.roborun.mdl.Robot;
import smi.roborun.mdl.Tourney;
import smi.roborun.ui.widgets.UiUtil;

public class RankingsPane extends VBox {
  private TableView<Robot> robotGrid;

  public RankingsPane(Tourney tourney) {
    Label lbl = new Label("Total Scores");
    lbl.setFont(new Font("Arial", 18));
    getChildren().add(lbl);

    robotGrid = new TableView<>();
    robotGrid.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    robotGrid.getColumns().add(UiUtil.<Robot, Number>tableCol("Rank", c -> c.getValue().getTotalScore().getRankProperty(), 50));
    robotGrid.getColumns().add(UiUtil.<Robot, String>tableCol("Robot", c -> c.getValue().getRobotNameProperty(), 150));
    robotGrid.getColumns().add(UiUtil.<Robot, String>tableCol("Author", c -> c.getValue().getAuthorProperty(), 150));
    robotGrid.getColumns().add(UiUtil.<Robot, Integer>tableCol("Weight", c -> c.getValue().getCodeSizeProperty(), 60));
    robotGrid.getColumns().add(UiUtil.<Robot, Number>tableCol("Score", c -> c.getValue().getTotalScore().getScoreProperty(), 50));
    robotGrid.getColumns().add(UiUtil.<Robot, Number>tableCol("Survival", c -> c.getValue().getTotalScore().getSurvivalProperty(), 50));
    robotGrid.getColumns().add(UiUtil.<Robot, Number>tableCol("Surv Bonus", c -> c.getValue().getTotalScore().getLastSurvivorBonusProperty(), 50));
    robotGrid.getColumns().add(UiUtil.<Robot, Number>tableCol("Bullet Dmg", c -> c.getValue().getTotalScore().getBulletDamageProperty(), 50));
    robotGrid.getColumns().add(UiUtil.<Robot, Number>tableCol("Bullet Bonus", c -> c.getValue().getTotalScore().getBulletDamageBonusProperty(), 50));
    robotGrid.getColumns().add(UiUtil.<Robot, Number>tableCol("Ram Dmg", c -> c.getValue().getTotalScore().getRamDamageProperty(), 50));
    robotGrid.getColumns().add(UiUtil.<Robot, Number>tableCol("Ram Bonus", c -> c.getValue().getTotalScore().getRamDamageBonusProperty(), 50));
    robotGrid.getColumns().add(UiUtil.<Robot, Number>tableCol("1sts", c -> c.getValue().getTotalScore().getFirstsProperty(), 50));
    robotGrid.getColumns().add(UiUtil.<Robot, Number>tableCol("2nds", c -> c.getValue().getTotalScore().getSecondsProperty(), 50));
    robotGrid.getColumns().add(UiUtil.<Robot, Number>tableCol("3rds", c -> c.getValue().getTotalScore().getThirdsProperty(), 50));
    robotGrid.getSortOrder().add(robotGrid.getColumns().get(0));
    robotGrid.setItems(tourney.getRobots());
    getChildren().add(robotGrid);
  }
}

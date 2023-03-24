package smi.roborun.ui.settings;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
import smi.roborun.mdl.Tourney;
import smi.roborun.ui.widgets.UiUtil;

public class SettingsPane extends GridPane {
  private BattleController ctl;

  private ObservableList<Robot> robots;
  private TableView<Robot> robotGrid;
  private Tourney tourney;

  public SettingsPane(BattleController ctl) {
    this.ctl = ctl;

    this.setAlignment(Pos.CENTER);
    this.setBorder(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, new CornerRadii(5), BorderStroke.THICK)));

    robots = FXCollections.observableArrayList(ctl.getRobots().stream().map(Robot::new).collect(Collectors.toList()));

    robotGrid = new TableView<>();
    robotGrid.setEditable(true);
    robotGrid.setMaxWidth(Double.MAX_VALUE);
//    participantGrid.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    TableColumn<Robot, Boolean> selectedCol = UiUtil.tableCol("Selected", cd -> cd.getValue().getSelectedProperty());
    selectedCol.setCellFactory(col -> new CheckBoxTableCell<>());
    robotGrid.getColumns().add(selectedCol);
    robotGrid.getColumns().add(UiUtil.<Robot, String>tableCol("Author", c -> c.getValue().getAuthorProperty()));
    robotGrid.getColumns().add(UiUtil.<Robot, String>tableCol("Robot", c -> c.getValue().getRobotNameProperty()));
    robotGrid.getColumns().add(UiUtil.<Robot, Long>tableCol("Code Size", c -> c.getValue().getCodeSizeProperty()));
    robotGrid.getItems().addAll(robots);
    this.add(robotGrid, 0, 0);
    GridPane.setHgrow(robotGrid, Priority.ALWAYS);
  }

  public Tourney createTourney() {
    Tourney tourney = new Tourney();
    tourney.setRobots(robots.filtered(Robot::getSelected));
    if (tourney.getRobots().size() < 2) {
      UiUtil.error("You must select at least two robots.");
    }
    
    List<Battle> battles = new ArrayList<>();
    Battle b = new Battle();
    b.setRobots(tourney.getRobots().stream().map(Robot::getRobotName).collect(Collectors.toList()));
    b.setType(BattleType.MELEE);
    battles.add(b);
    
    tourney.setBattles(battles);
    return tourney;
  }
}

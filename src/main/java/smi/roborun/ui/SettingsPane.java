package smi.roborun.ui;

import java.util.Comparator;
import java.util.stream.Collectors;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import smi.roborun.ctl.BattleController;

public class SettingsPane extends Pane {
  private BattleController ctl;

  private ReadOnlyObjectProperty<ObservableList<String>> allRobots;
  private ListView<String> robotsList;

  public SettingsPane(BattleController ctl) {
    super(new Label("Hello Settings"));
    this.ctl = ctl;

    allRobots =	new SimpleObjectProperty<ObservableList<String>>(FXCollections.observableArrayList());
    robotsList = new ListView<>();
    robotsList.itemsProperty().bind(allRobots);
    getChildren().add(robotsList);
    allRobots.get().addAll(ctl.getRobots().stream().map(r -> r.getName()).collect(Collectors.toList()));
    allRobots.get().sort(Comparator.naturalOrder());
  }
}

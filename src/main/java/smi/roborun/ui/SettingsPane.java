package smi.roborun.ui;

import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import smi.roborun.ctl.BattleController;

public class SettingsPane extends Pane {
  private BattleController ctl;
  
  public SettingsPane(BattleController ctl) {
    super(new Label("Hello Settings"));
    this.ctl = ctl;
//    getChildren().add(new Label("Hello Settings"));
  }
}

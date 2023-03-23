package smi.roborun.ui;

import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import smi.roborun.ctl.BattleController;

public class VsPane extends Pane {
  private BattleController ctl;
  
  public VsPane(BattleController ctl) {
    this.ctl = ctl;
    getChildren().add(new Label("Hello 1v1"));
  }
}

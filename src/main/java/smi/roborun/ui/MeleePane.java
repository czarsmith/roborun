package smi.roborun.ui;

import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import smi.roborun.ctl.BattleController;

public class MeleePane extends Pane {
  private BattleController ctl;
  
  public MeleePane(BattleController ctl) {
    this.ctl = ctl;
    getChildren().add(new Label("Hello Melee"));
  }
}

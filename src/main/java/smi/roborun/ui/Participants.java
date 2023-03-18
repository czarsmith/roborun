package smi.roborun.ui;

import javax.swing.JLabel;
import javax.swing.JPanel;

import smi.roborun.ctl.BattleController;

public class Participants extends JPanel {
  private BattleController ctl;
  
  public Participants(BattleController ctl) {
    this.ctl = ctl;

    this.add(new JLabel("Hello Participants"));
  }
}

package smi.roborun.ui;

import javax.swing.JLabel;
import javax.swing.JPanel;

import smi.roborun.ctl.BattleController;

public class VsBracket extends JPanel {
  private BattleController ctl;
  
  public VsBracket(BattleController ctl) {
    this.ctl = ctl;

    this.add(new JLabel("Hello 1v1 Bracket"));
  }
}

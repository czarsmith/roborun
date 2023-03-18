package smi.roborun.ui;

import javax.swing.JLabel;
import javax.swing.JPanel;

import smi.roborun.ctl.BattleController;

public class MeleeBracket extends JPanel {
  private BattleController ctl;
  
  public MeleeBracket(BattleController ctl) {
    this.ctl = ctl;

    this.add(new JLabel("Hello Melee Bracket"));
  }
}

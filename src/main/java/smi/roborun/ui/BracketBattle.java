package smi.roborun.ui;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import smi.roborun.mdl.Battle;

public class BracketBattle extends Label {
  private Battle battle;

  public BracketBattle(Battle battle, int x, int y) {
    super("R" + battle.getRoundNumber() + " B" + battle.getBattleNumber());
    this.battle = battle;
    setPadding(new Insets(16));
    setFont(new Font("Arial", 18));
    setBorder(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, new CornerRadii(5),
      BorderWidths.DEFAULT)));
    setTranslateX(x * (100 + 20));
    setTranslateY(y * (100 + 20));
  }
}

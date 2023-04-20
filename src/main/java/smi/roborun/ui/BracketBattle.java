package smi.roborun.ui;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import smi.roborun.mdl.Battle;

public class BracketBattle extends Label {
  public static final double WIDTH = 100;
  public static final double HEIGHT = 75;
  public static final double SPACING_X = 50;
  public static final double SPACING_Y = 50;

  private Battle battle;

  public BracketBattle(Battle battle, double gridX, double gridY) {
    super("R" + battle.getRoundNumber() + " B" + battle.getBattleNumber());
    this.battle = battle;
    setPadding(new Insets(16));
    setFont(new Font("Arial", 18));
    setBorder(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, new CornerRadii(5),
      BorderWidths.DEFAULT)));
    setLayoutX(gridX * (WIDTH + SPACING_X));
    setLayoutY(gridY * (HEIGHT + SPACING_Y));

    DropShadow dropShadow = new DropShadow();
    dropShadow.setRadius(5.0);
    dropShadow.setOffsetX(3.0);
    dropShadow.setOffsetY(3.0);
    dropShadow.setColor(Color.color(0.4, 0.5, 0.5));
    setEffect(dropShadow);

    setBackground(new Background(new BackgroundFill(Color.BLACK, new CornerRadii(5), new Insets(0))));
  }

  public Battle getBattle() {
    return battle;
  }
}

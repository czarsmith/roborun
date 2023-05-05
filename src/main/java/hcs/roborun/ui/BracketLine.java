package hcs.roborun.ui;

import javafx.beans.binding.Bindings;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class BracketLine extends Line {
  private BracketBattle src;
  private BracketBattle dest;

  public BracketLine(BracketBattle src, BracketBattle dest) {
    this.src = src;
    this.dest = dest;
    
    this.startXProperty().bind(Bindings.createDoubleBinding(
      () -> src.getLayoutX() + BracketBattle.WIDTH / 2, src.layoutXProperty()));
    this.startYProperty().bind(Bindings.createDoubleBinding(
      () -> src.getLayoutY() + BracketBattle.HEIGHT / 2, src.layoutYProperty()));
    this.endXProperty().bind(Bindings.createDoubleBinding(
      () -> dest.getLayoutX() + BracketBattle.WIDTH / 2, dest.layoutXProperty()));
    this.endYProperty().bind(Bindings.createDoubleBinding(
      () -> dest.getLayoutY() + BracketBattle.HEIGHT / 2, dest.layoutYProperty()));
    
    setStroke(Color.WHITE);
  }
}

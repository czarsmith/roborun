package smi.roborun.ui.widgets;

import javafx.scene.Node;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

public class CardPane extends StackPane {
  public CardPane(Node... children) {
    super(children);
    this.setBorder(new Border(new BorderStroke(Color.WHITE, BorderStrokeStyle.SOLID, new CornerRadii(5), BorderStroke.THICK)));
    show(getChildren().get(0));
  }
  
  public void show(Node card) {
    getChildren().forEach(n-> n.setVisible(n == card));
  }
}

package smi.roborun.ui.widgets;

import javafx.scene.Node;
import javafx.scene.layout.Pane;

public class CardPane extends Pane {
  public CardPane(Node... children) {
    super(children);
    show(getChildren().get(0));
  }
  
  public void show(Node card) {
    getChildren().forEach(n-> n.setVisible(n == card));
  }
}

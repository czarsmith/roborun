package smi.roborun.ui.widgets;

import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;

public class UiUtil {
  private UiUtil() {}

  public static Pane hspace() {
    Pane ui = new Pane();
    HBox.setHgrow(ui, Priority.ALWAYS);
    return ui;
  }
}

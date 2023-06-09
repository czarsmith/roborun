package hcs.roborun.ui.widgets;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;

public class SvgButton extends Button {
  public SvgButton(String uri) {
    this(uri, null);
  }
  
  public SvgButton(String uri, EventHandler<ActionEvent> handler) {
    super(null, new SvgImageView(uri));

    if (handler != null) {
      setOnAction(handler);
    }
  }
}

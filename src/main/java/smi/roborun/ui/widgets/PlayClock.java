package smi.roborun.ui.widgets;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.util.Duration;

public class PlayClock extends Label {
  private LongProperty time;
  private Timeline timer;
  private String label;

  public PlayClock() {
    time = new SimpleLongProperty();
    textProperty().bind(Bindings.createStringBinding(() -> (label == null ? "" : (label + ": "))
      + UiUtil.millisToMS(time.get(), true), time));
  }

  public void reset(long millis) {
    stop();
    time.set(millis);
  }

  public void start(String label, long millis, EventHandler<ActionEvent> onFinished) {
    this.label = label;
    reset(millis);
    timer = new Timeline(new KeyFrame(Duration.millis(millis), new KeyValue(time, 0, Interpolator.LINEAR)));
    if (onFinished != null) {
      timer.setOnFinished(onFinished);
    }
    timer.play();
  }

  public void stop() {
    if (timer != null) {
      timer.stop();
    }
  }
}

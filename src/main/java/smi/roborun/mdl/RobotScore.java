package smi.roborun.mdl;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class RobotScore {
  private DoubleProperty score;

  public RobotScore() {
    score = new SimpleDoubleProperty(0d);
  }

  @JsonIgnore
  public DoubleProperty getScoreProperty() {
    return score;
  }

  public double getScore() {
    return score.get();
  }

  public void setScore(double score) {
    this.score.set(score);
  }
}

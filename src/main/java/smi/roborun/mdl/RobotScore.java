package smi.roborun.mdl;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class RobotScore {
  private IntegerProperty rank;
  private DoubleProperty score;

  public RobotScore() {
    rank = new SimpleIntegerProperty(0);
    score = new SimpleDoubleProperty(0d);
    reset();
  }

  public void reset() {
    setRank(0);
    setScore(0);
  }

  @JsonIgnore
  public IntegerProperty getRankProperty() {
    return rank;
  }

  public double getRank() {
    return rank.get();
  }

  public void setRank(int rank) {
    this.rank.set(rank);
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

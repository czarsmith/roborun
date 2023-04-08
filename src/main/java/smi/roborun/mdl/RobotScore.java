package smi.roborun.mdl;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class RobotScore {
  private StringProperty robotName;
  private StringProperty author;
  private IntegerProperty rank;
  private DoubleProperty score;

  public RobotScore() {
    robotName = new SimpleStringProperty();
    author = new SimpleStringProperty();
    rank = new SimpleIntegerProperty(0);
    score = new SimpleDoubleProperty(0d);
    reset(true);
  }

  public void reset(boolean hard) {
    setRank(0);
    setScore(0);

    if (hard) {
      rank.unbind();
      score.unbind();
    }
  }

  public RobotScore copy() {
    RobotScore ret = new RobotScore();
    ret.setRobotName(getRobotName());
    ret.setAuthor(getAuthor());
    ret.setRank(getRank());
    ret.setScore(getScore());
    return ret;
  }

  @JsonIgnore
  public StringProperty getRobotNameProperty() {
    return robotName;
  }

  public String getRobotName() {
    return robotName.get();
  }

  public void setRobotName(String robotName) {
    this.robotName.set(robotName);
  }

  @JsonIgnore
  public StringProperty getAuthorProperty() {
    return author;
  }

  public String getAuthor() {
    return author.get();
  }

  public void setAuthor(String author) {
    this.author.set(author);
  }

  @JsonIgnore
  public IntegerProperty getRankProperty() {
    return rank;
  }

  public int getRank() {
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

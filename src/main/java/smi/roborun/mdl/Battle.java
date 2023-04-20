package smi.roborun.mdl;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

@JsonInclude(Include.NON_NULL)
public class Battle {
  public enum BattleType { MELEE, VS };

  private BattleType type;

  /** 1-Based round number */
  private Integer roundNumber;

  /** 1-Based battle number relative to the round. */
  private Integer battleNumber;

  private Integer numRounds;
  private Integer battlefieldWidth;
  private Integer battlefieldHeight;
  private Integer tps;

  /**
   * The number of robots in this battle.  This is not simply the size of the robots list because
   * we don't always know which robots will participate in a particular battle until previous
   * battles are completed.  This number tells us how many robots to expect even before we know
   * exactly which ones will be in this battle.
   */
  private IntegerProperty numRobots;
  private List<Robot> robots;
  private ObservableList<RobotScore> results;
  private Long desiredRuntimeMillis;

  /** The robocode battle round, not the tournament round. */
  private IntegerProperty battleRound;

  /** This can't be observable because it's not updated on the UI event thread */
  private long startTime;

  /** This can't be observable because it's not updated on the UI event thread */
  private long endTime;

  private Integer advanceToBattleNumber;

  /** True if this battle is part of a preliminary round which doesn't count toward the total score. */
  private boolean preliminary;

  public Battle() {
    numRobots = new SimpleIntegerProperty(0);
    robots = new ArrayList<>();
    results = FXCollections.observableArrayList();
    battleRound = new SimpleIntegerProperty();
    reset(true);
  }

  public void reset(boolean hard) {
    battleRound.set(0);
    startTime = 0;
    endTime = 0;
    results.clear();
    robots.forEach(robot -> robot.reset(hard));

    if (hard) {
      numRounds = 3;
      battlefieldWidth = 800;
      battlefieldHeight = 800;
      tps = 25;
      desiredRuntimeMillis = 30000L;
      robots.clear();
      advanceToBattleNumber = null;
      preliminary = false;
    } else if (roundNumber > 1 || type != BattleType.MELEE) {
      robots.clear(); // Because only the first melee round has predetermined participants
    }
  }

  @JsonIgnore
  public String getId() {
    return getType() + "-" + getRoundNumber() + "-" + getBattleNumber();
  }

  public BattleType getType() {
    return type;
  }
  public void setType(BattleType type) {
    this.type = type;
  }
  public Integer getRoundNumber() {
    return roundNumber;
  }
  public void setRoundNumber(Integer roundNumber) {
    this.roundNumber = roundNumber;
  }
  public Integer getBattleNumber() {
    return battleNumber;
  }
  public void setBattleNumber(Integer battleNumber) {
    this.battleNumber = battleNumber;
  }
  public Integer getNumRounds() {
    return numRounds;
  }

  public void setNumRounds(Integer numRounds) {
    this.numRounds = numRounds;
  }

  public Integer getBattlefieldWidth() {
    return battlefieldWidth;
  }

  public void setBattlefieldWidth(Integer battlefieldWidth) {
    this.battlefieldWidth = battlefieldWidth;
  }

  public Integer getBattlefieldHeight() {
    return battlefieldHeight;
  }

  public void setBattlefieldHeight(Integer battlefieldHeight) {
    this.battlefieldHeight = battlefieldHeight;
  }

  @JsonIgnore
  public IntegerProperty getNumRobotsProperty() {
    return numRobots;
  }

  public Integer getNumRobots() {
    return numRobots.get();
  }

  public void setNumRobots(Integer numRobots) {
    this.numRobots.set(numRobots);
  }
  
  public List<Robot> getRobots() {
    return robots;
  }
  public void setRobots(List<Robot> robots) {
    this.robots = robots;
  }

  public ObservableList<RobotScore> getResults() {
    return results;
  }

  public void setResults(List<RobotScore> results) {
    this.results.clear();
    this.results.addAll(results);
  }

  public Integer getTps() {
    return tps;
  }

  public void setTps(Integer tps) {
    this.tps = tps;
  }

  public Long getDesiredRuntimeMillis() {
    return desiredRuntimeMillis;
  }

  public void setDesiredRuntimeMillis(Long desiredRuntimeMillis) {
    this.desiredRuntimeMillis = desiredRuntimeMillis;
  }

  @JsonIgnore
  public IntegerProperty getBattleRoundProperty() {
    return battleRound;
  }

  public Integer getBattleRound() {
    return battleRound.get();
  }

  public void setBattleRound(Integer battleRound) {
    this.battleRound.set(battleRound);
  }

  public long getStartTime() {
    return startTime;
  }

  public void setStartTime(long startTime) {
    this.startTime = startTime;
  }

  public long getEndTime() {
    return endTime;
  }

  public void setEndTime(long endTime) {
    this.endTime = endTime;
  }

  public Integer getAdvanceToBattleNumber() {
    return advanceToBattleNumber;
  }

  public void setAdvanceToBattleNumber(Integer advanceToBattleNumber) {
    this.advanceToBattleNumber = advanceToBattleNumber;
  }

  public boolean isPreliminary() {
    return preliminary;
  }

  public void setPreliminary(boolean preliminary) {
    this.preliminary = preliminary;
  }
}

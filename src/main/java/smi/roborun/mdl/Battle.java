package smi.roborun.mdl;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import robocode.BattleResults;

public class Battle {
  public enum BattleType { MELEE, VS };

  private Long id;
  private BattleType type;
  private Integer roundNumber;
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
  private List<BattleResults> results;
  private Long desiredRuntimeMillis;

  /** The robocode battle round, not the tournament round. */
  private IntegerProperty battleRound;

  /** This can't be observable because it's not updated on the UI event thread */
  private long startTime;

  public Battle() {
    numRobots = new SimpleIntegerProperty(0);
    robots = new ArrayList<>();
    results = new ArrayList<>();
    battleRound = new SimpleIntegerProperty();
    reset(true);
  }

  public void reset(boolean hard) {
    numRobots.unbind();
    battleRound.unbind();
    battleRound.set(0);
    startTime = 0;
    results.clear();
    robots.forEach(robot -> robot.reset(hard));

    if (hard) {
      numRounds = 3;
      battlefieldWidth = 800;
      battlefieldHeight = 800;
      tps = 25;
      desiredRuntimeMillis = 30000L;
      robots.clear();
    }
  }

  public Long getId() {
    return id;
  }
  public void setId(Long id) {
    this.id = id;
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
  public List<BattleResults> getResults() {
    return results;
  }
  public void setResults(List<BattleResults> results) {
    this.results = results;
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
}

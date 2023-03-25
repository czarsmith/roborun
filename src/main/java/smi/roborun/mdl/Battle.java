package smi.roborun.mdl;

import java.util.ArrayList;
import java.util.List;

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
  private List<Robot> robots;
  private List<BattleResults> results;

  public Battle() {
    numRounds = 3;
    battlefieldWidth = 800;
    battlefieldHeight = 800;
    tps = 25;
    robots = new ArrayList<>();
    results = new ArrayList<>();
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
  
}

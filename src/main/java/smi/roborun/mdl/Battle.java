package smi.roborun.mdl;

import java.util.ArrayList;
import java.util.List;

public class Battle {
  public enum BattleType { MELEE, VS };

  private Long id;
  private BattleType type;
  private Integer round;
  private Integer match;
  private Integer rounds;
  private Integer battlefieldWidth;
  private Integer battlefieldHeight;
  private Integer tps;
  private List<String> robots;
  private List<BattleResult> results;

  public Battle() {
    rounds = 3;
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
  public Integer getRound() {
    return round;
  }
  public void setRound(Integer round) {
    this.round = round;
  }
  public Integer getMatch() {
    return match;
  }
  public void setMatch(Integer match) {
    this.match = match;
  }
  public Integer getRounds() {
    return rounds;
  }

  public void setRounds(Integer rounds) {
    this.rounds = rounds;
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

  public List<String> getRobots() {
    return robots;
  }
  public void setRobots(List<String> robots) {
    this.robots = robots;
  }
  public List<BattleResult> getResults() {
    return results;
  }
  public void setResults(List<BattleResult> results) {
    this.results = results;
  }

  public Integer getTps() {
    return tps;
  }

  public void setTps(Integer tps) {
    this.tps = tps;
  }
  
}

package hcs.roborun.mdl;

import com.fasterxml.jackson.annotation.JsonIgnore;

import hcs.roborun.mdl.Battle.BattleType;

public class Round {
  private BattleType type;
  private int roundNumber;
  private int numBattles;
  private int maxBattles;
  private boolean preliminary;

  public Round() {}

  public Round(BattleType type, int roundNumber, int numBattles, int maxBattles) {
    this.type = type;
    this.roundNumber = roundNumber;
    this.numBattles = numBattles;
    this.maxBattles = maxBattles;
    preliminary = numBattles < maxBattles;
  }

  @JsonIgnore
  public String getId() {
    return type + "-" + roundNumber;
  }

  public BattleType getType() {
    return type;
  }
  public void setType(BattleType type) {
    this.type = type;
  }
  public int getRoundNumber() {
    return roundNumber;
  }
  public void setRoundNumber(int roundNumber) {
    this.roundNumber = roundNumber;
  }
  public int getNumBattles() {
    return numBattles;
  }
  public void setNumBattles(int numBattles) {
    this.numBattles = numBattles;
  }

  public int getMaxBattles() {
    return maxBattles;
  }

  public void setMaxBattles(int maxBattles) {
    this.maxBattles = maxBattles;
  }

  public boolean isPreliminary() {
    return preliminary;
  }

  public void setPreliminary(boolean preliminary) {
    this.preliminary = preliminary;
  }
}

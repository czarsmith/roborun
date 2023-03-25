package smi.roborun.mdl;

import java.util.ArrayList;
import java.util.List;

public class Round {
  private Integer roundNumber;
  private Integer numBattles;
  private List<Battle> battles;
  
  public Round(Integer number, Integer numBattles) {
    this.roundNumber = number;
    this.numBattles = numBattles;
    battles = new ArrayList<>();
  }

  public Integer getRoundNumber() {
    return roundNumber;
  }
  public void setRoundNumber(Integer roundNumber) {
    this.roundNumber = roundNumber;
  }
  public Integer getNumBattles() {
    return numBattles;
  }

  public void setNumBattles(Integer numBattles) {
    this.numBattles = numBattles;
  }

  public List<Battle> getBattles() {
    return battles;
  }
  public void setBattles(List<Battle> battles) {
    this.battles = battles;
  }
}

package smi.roborun.mdl;

import java.util.ArrayList;
import java.util.List;

public class Tourney {
  private Long maxRuntimeMillis;
  private Integer maxMeleeSize;
  private Integer numMeleeRounds;
  private Integer meleeBattlefieldWidth;
  private Integer meleeBattlefieldHeight;
  private Integer vsBattlefieldWidth;
  private Integer vsBattlefieldHeight;
  private Integer desiredTps;
  private List<Battle> battles;
  private List<Robot> robots;

  public Tourney() {
    maxRuntimeMillis = 0L;
    maxMeleeSize = 16;
    meleeBattlefieldWidth = 800;
    meleeBattlefieldHeight = 800;
    vsBattlefieldWidth = 600;
    vsBattlefieldHeight = 600;
    desiredTps = 25;
    battles = new ArrayList<>();
    robots = new ArrayList<>();
  }
  
  public Long getMaxRuntimeMillis() {
    return maxRuntimeMillis;
  }
  public void setMaxRuntimeMillis(Long maxRuntimeMillis) {
    this.maxRuntimeMillis = maxRuntimeMillis;
  }
  public Integer getMaxMeleeSize() {
    return maxMeleeSize;
  }
  public void setMaxMeleeSize(Integer maxMeleeSize) {
    this.maxMeleeSize = maxMeleeSize;
  }
  public Integer getNumMeleeRounds() {
    return numMeleeRounds;
  }
  public void setNumMeleeRounds(Integer numMeleeRounds) {
    this.numMeleeRounds = numMeleeRounds;
  }
  public Integer getMeleeBattlefieldWidth() {
    return meleeBattlefieldWidth;
  }
  public void setMeleeBattlefieldWidth(Integer meleeBattlefieldWidth) {
    this.meleeBattlefieldWidth = meleeBattlefieldWidth;
  }
  public Integer getMeleeBattlefieldHeight() {
    return meleeBattlefieldHeight;
  }
  public void setMeleeBattlefieldHeight(Integer meleeBattlefieldHeight) {
    this.meleeBattlefieldHeight = meleeBattlefieldHeight;
  }
  public Integer getVsBattlefieldWidth() {
    return vsBattlefieldWidth;
  }
  public void setVsBattlefieldWidth(Integer vsBattlefieldWidth) {
    this.vsBattlefieldWidth = vsBattlefieldWidth;
  }
  public Integer getVsBattlefieldHeight() {
    return vsBattlefieldHeight;
  }
  public void setVsBattlefieldHeight(Integer vsBattlefieldHeight) {
    this.vsBattlefieldHeight = vsBattlefieldHeight;
  }
  public Integer getDesiredTps() {
    return desiredTps;
  }
  public void setDesiredTps(Integer desiredTps) {
    this.desiredTps = desiredTps;
  }
  public List<Battle> getBattles() {
    return battles;
  }
  public void setBattles(List<Battle> battles) {
    this.battles = battles;
  }

  public List<Robot> getRobots() {
    return robots;
  }

  public void setRobots(List<Robot> robots) {
    this.robots = robots;
  }
}

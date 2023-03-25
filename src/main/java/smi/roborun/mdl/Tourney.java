package smi.roborun.mdl;

import java.util.ArrayList;
import java.util.List;

public class Tourney {
  private Long maxRuntimeMillis;
  private Integer desiredTps;
  private List<Robot> robots;
  private Integer maxMeleeSize;
  private Integer numMeleeRoundsPerBattle;
  private Integer meleeBattlefieldWidth;
  private Integer meleeBattlefieldHeight;
  private List<Round> meleeRounds;
  private Integer numVsRoundsPerBattle;
  private Integer vsBattlefieldWidth;
  private Integer vsBattlefieldHeight;
  private List<Round> vsRounds;

  public Tourney() {
    maxRuntimeMillis = 0L;
    maxMeleeSize = 4;
    meleeBattlefieldWidth = 800;
    meleeBattlefieldHeight = 800;
    vsBattlefieldWidth = 600;
    vsBattlefieldHeight = 600;
    desiredTps = 50;
    numMeleeRoundsPerBattle = 1;
    numVsRoundsPerBattle = 1;
    meleeRounds = new ArrayList<>();
    vsRounds = new ArrayList<>();
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
  
  public Integer getNumMeleeRoundsPerBattle() {
    return numMeleeRoundsPerBattle;
  }

  public void setNumMeleeRoundsPerBattle(Integer numMeleeRoundsPerBattle) {
    this.numMeleeRoundsPerBattle = numMeleeRoundsPerBattle;
  }

  public Integer getNumVsRoundsPerBattle() {
    return numVsRoundsPerBattle;
  }

  public void setNumVsRoundsPerBattle(Integer numVsRoundsPerBattle) {
    this.numVsRoundsPerBattle = numVsRoundsPerBattle;
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
  public List<Round> getMeleeRounds() {
    return meleeRounds;
  }

  public void setMeleeRounds(List<Round> meleeRounds) {
    this.meleeRounds = meleeRounds;
  }

  public List<Round> getVsRounds() {
    return vsRounds;
  }

  public void setVsRounds(List<Round> vsRounds) {
    this.vsRounds = vsRounds;
  }

  public List<Robot> getRobots() {
    return robots;
  }

  public void setRobots(List<Robot> robots) {
    this.robots = robots;
  }
}

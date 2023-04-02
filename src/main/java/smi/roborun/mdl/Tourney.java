package smi.roborun.mdl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;

@JsonInclude(Include.NON_NULL)
public class Tourney {
  private LongProperty startTime;
  private LongProperty endTime;
  private Long desiredRuntimeMillis;
  private Integer desiredTps;
  private Map<String, Robot> robots;
  private Integer maxMeleeSize;
  private Integer numMeleeRoundsPerBattle;
  private Integer meleeBattlefieldWidth;
  private Integer meleeBattlefieldHeight;
  private List<Round> meleeRounds;
  private Integer numVsRoundsPerBattle;
  private Integer vsBattlefieldWidth;
  private Integer vsBattlefieldHeight;
  private List<Round> vsRounds;
  private int numBattles;

  /** The current round, if one is running. */
  private ObjectProperty<Round> round;

  /** The current battle if one is running. */
  private ObjectProperty<Battle> battle;

  public Tourney() {
    startTime = new SimpleLongProperty();
    endTime = new SimpleLongProperty();
    meleeRounds = new ArrayList<>();
    vsRounds = new ArrayList<>();
    robots = new HashMap<>();
    round = new SimpleObjectProperty<>();
    battle = new SimpleObjectProperty<>();

    reset(true);
  }
  
  /** Hard reset is for when the tournament settings change.  Soft reset is for running the same tournament again. */
  public void reset(boolean hard) {
    startTime.unbind();
    startTime.set(0L);
    endTime.unbind();
    endTime.set(0L);
    desiredRuntimeMillis = 30000L;
    maxMeleeSize = 4;
    meleeBattlefieldWidth = 800;
    meleeBattlefieldHeight = 800;
    vsBattlefieldWidth = 600;
    vsBattlefieldHeight = 600;
    desiredTps = 25;
    numMeleeRoundsPerBattle = 10;
    numVsRoundsPerBattle = 10;
    round.unbind();
    round.set(null);
    battle.unbind();
    battle.set(null);
    meleeRounds.forEach(round -> round.reset(hard));
    vsRounds.forEach(round -> round.reset(hard));
    
    if (hard) {
      meleeRounds.clear();
      vsRounds.clear();  
      robots.clear();
    }
  }

  public boolean hasBattles() {
    return !meleeRounds.isEmpty() && !meleeRounds.get(0).getBattles().isEmpty();
  }

  @JsonIgnore
  public LongProperty getStartTimeProperty() {
    return startTime;
  }

  public long getStartTime() {
    return startTime.get();
  }

  public void setStartTime(long startTime) {
    this.startTime.set(startTime);
  }

  @JsonIgnore
  public LongProperty getEndTimeProperty() {
    return endTime;
  }

  public long getEndTime() {
    return endTime.get();
  }

  public void setEndTime(long endTime) {
    this.endTime.set(endTime);
  }

  public Long getDesiredRuntimeMillis() {
    return desiredRuntimeMillis;
  }
  public void setDesiredRuntimeMillis(Long desiredRuntimeMillis) {
    this.desiredRuntimeMillis = desiredRuntimeMillis;
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

  @JsonIgnore
  public Robot getRobot(String name) {
    return robots.get(name);
  }

  public Collection<Robot> getRobots() {
    return robots.values();
  }

  public void setRobots(List<Robot> robots) {
    this.robots.clear();
    robots.forEach(robot -> this.robots.put(robot.getRobotName(), robot));
  }

  public int getNumBattles() {
    return numBattles;
  }

  public void setNumBattles(int numBattles) {
    this.numBattles = numBattles;
  }

  @JsonIgnore
  public ObjectProperty<Round> getRoundProperty() {
    return round;
  }

  public Round getRound() {
    return round.get();
  }

  public void setRound(Round round) {
    this.round.set(round);
  }

  @JsonIgnore
  public ObjectProperty<Battle> getBattleProperty() {
    return battle;
  }

  public Battle getBattle() {
    return battle.get();
  }

  public void setBattle(Battle battle) {
    this.battle.set(battle);
  }  
}

package smi.roborun.mdl;

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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

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
  private Integer numVsRoundsPerBattle;
  private Integer vsBattlefieldWidth;
  private Integer vsBattlefieldHeight;
  private ObservableList<Battle> battles;

  /** The current battle.  This is always non-null whenever there is at least one battle defined. */
  private ObjectProperty<Battle> battle;

  public Tourney() {
    startTime = new SimpleLongProperty();
    endTime = new SimpleLongProperty();
    robots = new HashMap<>();
    battles = FXCollections.observableArrayList();
    battle = new SimpleObjectProperty<>();

    reset(true);
  }
  
  /** Hard reset is for when the tournament settings change.  Soft reset is for running the same tournament again. */
  public void reset(boolean hard) {
    startTime.set(0L);
    endTime.set(0L);
    robots.values().forEach(robot -> robot.reset(hard));
    battles.forEach(battle -> battle.reset(hard));
    
    if (hard) {
      desiredRuntimeMillis = 30000L;
      maxMeleeSize = 4;
      meleeBattlefieldWidth = 800;
      meleeBattlefieldHeight = 800;
      vsBattlefieldWidth = 600;
      vsBattlefieldHeight = 600;
      desiredTps = 25;
      numMeleeRoundsPerBattle = 10;
      numVsRoundsPerBattle = 10;
      battles.clear();
      robots.clear();
      battle.set(null);
    } else {
      // TODO: set battle to first one
    }
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

  public ObservableList<Battle> getBattles() {
    return battles;
  }

  public void setBattles(List<Battle> battles) {
    this.battles.clear();
    this.battles.addAll(battles);
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

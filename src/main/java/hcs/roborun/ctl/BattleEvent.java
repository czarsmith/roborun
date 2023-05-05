package hcs.roborun.ctl;

import javafx.event.Event;
import javafx.event.EventType;
import hcs.roborun.mdl.Battle;
import hcs.roborun.mdl.Tourney;

public class BattleEvent extends Event {
  public static final EventType<BattleEvent> STARTED = new EventType<>(ANY, "STARTED");
  public static final EventType<BattleEvent> TURN_FINISHED = new EventType<>(ANY, "TURN_FINISHED");
  public static final EventType<BattleEvent> ROUND_STARTED = new EventType<>(ANY, "ROUND_STARTED");
  public static final EventType<BattleEvent> ROUND_FINISHED = new EventType<>(ANY, "ROUND_FINISHED");
  public static final EventType<BattleEvent> FINISHED = new EventType<>(ANY, "FINISHED");
  
  private Tourney tourney;
  private Battle battle;

  public BattleEvent(EventType<? extends Event> eventType) {
    this(eventType, null, null);
  }
  
  public BattleEvent(EventType<? extends Event> eventType, Tourney tourney, Battle battle) {
    super(eventType);
    this.tourney = tourney;
    this.battle = battle;
  }

  public static BattleEvent started(Tourney tourney, Battle battle) {
    return new BattleEvent(STARTED, tourney, battle);
  }

  public static BattleEvent finished(Tourney tourney, Battle battle) {
    return new BattleEvent(FINISHED, tourney, battle);
  }

  public static BattleEvent turnFinished(Tourney tourney, Battle battle) {
    return new BattleEvent(TURN_FINISHED, tourney, battle);
  }

  public static BattleEvent roundStarted() {
    return new BattleEvent(ROUND_STARTED);
  }

  public static BattleEvent roundFinished() {
    return new BattleEvent(ROUND_FINISHED);
  }

  public Tourney getTourney() {
    return tourney;
  }

  public void setTourney(Tourney tourney) {
    this.tourney = tourney;
  }

  public Battle getBattle() {
    return battle;
  }

  public void setBattle(Battle battle) {
    this.battle = battle;
  }
}

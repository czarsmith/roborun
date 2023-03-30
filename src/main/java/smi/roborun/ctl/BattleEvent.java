package smi.roborun.ctl;

import javafx.event.Event;
import javafx.event.EventType;
import smi.roborun.mdl.Battle;
import smi.roborun.mdl.Round;
import smi.roborun.mdl.Tourney;

public class BattleEvent extends Event {
  public static final EventType<BattleEvent> STARTED = new EventType<>(ANY, "STARTED");
  public static final EventType<BattleEvent> TURN_FINISHED = new EventType<>(ANY, "TURN_FINISHED");
  public static final EventType<BattleEvent> FINISHED = new EventType<>(ANY, "FINISHED");
  
  private Tourney tourney;
  private Round round;
  private Battle battle;
  
  public BattleEvent(EventType<? extends Event> eventType, Tourney tourney, Round round, Battle battle) {
    super(eventType);
    this.tourney = tourney;
    this.round = round;
    this.battle = battle;
  }

  public static BattleEvent started(Tourney tourney, Round round, Battle battle) {
    return new BattleEvent(STARTED, tourney, round, battle);
  }

  public static BattleEvent finished(Tourney tourney, Round round, Battle battle) {
    return new BattleEvent(FINISHED, tourney, round, battle);
  }

  public static BattleEvent turnFinished(Tourney tourney, Round round, Battle battle) {
    return new BattleEvent(TURN_FINISHED, tourney, round, battle);
  }

  public Tourney getTourney() {
    return tourney;
  }

  public void setTourney(Tourney tourney) {
    this.tourney = tourney;
  }

  public Round getRound() {
    return round;
  }

  public void setRound(Round round) {
    this.round = round;
  }

  public Battle getBattle() {
    return battle;
  }

  public void setBattle(Battle battle) {
    this.battle = battle;
  }
}

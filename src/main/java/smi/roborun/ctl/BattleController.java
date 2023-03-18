package smi.roborun.ctl;

import java.io.File;

import robocode.control.BattleSpecification;
import robocode.control.BattlefieldSpecification;
import robocode.control.RobocodeEngine;
import robocode.control.RobotSpecification;
import robocode.control.events.BattleAdaptor;
import robocode.control.events.BattleCompletedEvent;
import robocode.control.events.BattleErrorEvent;
import robocode.control.events.BattleFinishedEvent;
import robocode.control.events.BattleMessageEvent;
import smi.roborun.RobocodeConfig;

public class BattleController extends BattleAdaptor implements Runnable {
  private RobocodeEngine engine;

  public void execute() {
    new Thread(this).start();
  }

  public void run() {
    File robocodeDir = new File("/home/czarsmith/robocode");
    
    int tps = 300;
    int numberOfRounds = 3;
    int battlefieldWidth = 800;
    int battlefieldHeight = 800;

    var config = new RobocodeConfig(robocodeDir);
    config.setTps(tps);
    config.setVisibleGround(false);
    config.setVisibleScanArcs(false);
    config.setWindowSize(0, 0, 170 + battlefieldWidth, 140 + battlefieldHeight);
    config.apply();

    RobocodeEngine.setLogMessagesEnabled(false);
    engine = new RobocodeEngine(robocodeDir);
    engine.addBattleListener(this);
    engine.setVisible(true);

    BattlefieldSpecification battlefield = new BattlefieldSpecification(battlefieldWidth, battlefieldHeight);
    RobotSpecification[] selectedRobots = engine.getLocalRepository("sample.RamFire,sample.Corners");
    BattleSpecification battleSpec = new BattleSpecification(numberOfRounds, battlefield, selectedRobots);
    engine.runBattle(battleSpec, true);
    engine.close();
  }

  @Override
  public void onBattleFinished(BattleFinishedEvent e) {
  }

  @Override
  public void onBattleCompleted(BattleCompletedEvent e) {
    System.out.println("-- Battle has completed --");
    System.out.println("Battle results:");
    for (robocode.BattleResults result : e.getSortedResults()) {
      System.out.println("  " + result.getTeamLeaderName() + ": " + result.getScore());
    }
  }

  @Override
  public void onBattleMessage(BattleMessageEvent e) {
      System.out.println("Msg> " + e.getMessage());
  }

  @Override
  public void onBattleError(BattleErrorEvent e) {
      System.out.println("Err> " + e.getError());
  }
}

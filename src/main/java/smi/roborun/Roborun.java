package smi.roborun;

import java.io.File;

import robocode.control.BattleSpecification;
import robocode.control.BattlefieldSpecification;
import robocode.control.RobocodeEngine;
import robocode.control.RobotSpecification;

public class Roborun {
  public static final void main(String... args) {
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
    RobocodeEngine engine = new RobocodeEngine(robocodeDir);
    engine.addBattleListener(new BattleObserver());
    engine.setVisible(true);

    BattlefieldSpecification battlefield = new BattlefieldSpecification(battlefieldWidth, battlefieldHeight);
    RobotSpecification[] selectedRobots = engine.getLocalRepository("sample.RamFire,sample.Corners");
    BattleSpecification battleSpec = new BattleSpecification(numberOfRounds, battlefield, selectedRobots);
    engine.runBattle(battleSpec, true);
    engine.close();

    System.exit(0);
  }
}

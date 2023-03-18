package smi.roborun;

import robocode.control.BattleSpecification;
import robocode.control.BattlefieldSpecification;
import robocode.control.RobocodeEngine;
import robocode.control.RobotSpecification;

public class Roborun {
  public static final void main(String... args) {
    System.setProperty("java.security.manager", "allow");
    
    // Disable log messages from Robocode
    RobocodeEngine.setLogMessagesEnabled(false);

    // Create the RobocodeEngine
    //   RobocodeEngine engine = new RobocodeEngine(); // Run from current working directory
    RobocodeEngine engine = new RobocodeEngine(new java.io.File("/home/czarsmith/robocode"));

    // Add our own battle listener to the RobocodeEngine 
    engine.addBattleListener(new BattleObserver());

    // Show the Robocode battle view
    engine.setVisible(true);

    // Setup the battle specification
    int numberOfRounds = 5;
    BattlefieldSpecification battlefield = new BattlefieldSpecification(800, 600); // 800x600
    RobotSpecification[] selectedRobots = engine.getLocalRepository("sample.RamFire,sample.Corners");

    BattleSpecification battleSpec = new BattleSpecification(numberOfRounds, battlefield, selectedRobots);

    // Run our specified battle and let it run till it is over
    engine.runBattle(battleSpec, true); // waits till the battle finishes

    // Cleanup our RobocodeEngine
    engine.close();

    // Make sure that the Java VM is shut down properly
    System.exit(0);
  }
}

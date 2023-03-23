package smi.roborun.ctl;

import java.awt.Component;
import java.awt.Container;
import java.awt.Window;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSlider;

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
  private boolean running;
  private JSlider tpsSlider;
  private List<Integer> tpsTicks;
  private Window robocodeWin;

  public BattleController() {
    // The TPS slider isn't linear so we have to configure each of the individual tick marks
    // in order to determine which slider tick corresponds to each TPS value.
    tpsTicks = new ArrayList<>();
    IntStream.range(0, 30).forEach(tpsTicks::add);
    tpsTicks.addAll(List.of(30, 32, 34, 36, 38, 40, 42, 44, 46, 48, 50));
    tpsTicks.addAll(List.of(53, 56, 59, 62, 65, 70, 75, 80, 85, 90, 95, 100));
    tpsTicks.addAll(List.of(110, 130, 150, 200, 300, 500, 750, 1000, Integer.MAX_VALUE));
  }

  public void execute() {
    running = true;
    new Thread(this).start();

    // Find the robocode window
    robocodeWin = until(() -> Stream.of(Window.getWindows())
      .filter(w -> "net.sf.robocode.ui.dialog.RobocodeFrame".equals(w.getClass().getName()))
      .findAny().orElse(null));

    // Find the TPS Slider
    tpsSlider = until(() -> (JSlider)findComponent(robocodeWin, c -> c instanceof JSlider));

    System.out.println("Battle has started");
  }

  public void setTps(int tps) {
    System.out.println("Set TPS to " + tps);
    for (int i = 0; i < tpsTicks.size(); i++) {
      if (tps <= tpsTicks.get(i)) {
        tpsSlider.setValue(i);
        break;
      }
    }
  }

  public void run() {
    File robocodeDir = new File("/home/czarsmith/robocode");
    
    int tps = 25;
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
    running = false;
  }

  private <T> T until(Supplier<T> supplier) {
    T result = supplier.get();
    while (result == null && running) {
      try { Thread.sleep(200); } catch (Exception e) {}
      result = supplier.get();
    }
    return result;
  }

  private Component findComponent(Component comp, Predicate<Component> matcher) {
    if (matcher.test(comp)) {
      return comp;
    } else if (comp instanceof JMenu menu) {
      return IntStream.range(0, menu.getItemCount()).mapToObj(i -> menu.getItem(i)).filter(Objects::nonNull)
        .map(c -> findComponent(c, matcher)).filter(Objects::nonNull).findAny().orElse(null);
    } else if (comp instanceof JMenuItem menuitem) {
      return Stream.of(menuitem.getSubElements()).map(o -> o.getComponent()).filter(Objects::nonNull)
        .map(c -> findComponent(c, matcher)).filter(Objects::nonNull).findAny().orElse(null);
    } else if (comp instanceof java.awt.Container) {
      return Stream.of(((Container)comp).getComponents())
        .map(c -> findComponent(c, matcher)).filter(Objects::nonNull).findAny().orElse(null);
    } else {
      return null;
    }
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

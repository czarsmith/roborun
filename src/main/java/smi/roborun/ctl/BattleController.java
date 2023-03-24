package smi.roborun.ctl;

import java.awt.Component;
import java.awt.Container;
import java.awt.Window;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSlider;

import org.apache.commons.lang3.StringUtils;

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
import smi.roborun.mdl.Battle;
import smi.roborun.mdl.Tourney;

public class BattleController extends BattleAdaptor {
  private RobocodeEngine engine;
  private boolean running;
  private JSlider tpsSlider;
  private List<Integer> tpsTicks;
  private Window robocodeWin;
  File robocodeDir;

  public BattleController() {
    // The TPS slider isn't linear so we have to configure each of the individual tick marks
    // in order to determine which slider tick corresponds to each TPS value.
    tpsTicks = new ArrayList<>();
    IntStream.range(0, 30).forEach(tpsTicks::add);
    tpsTicks.addAll(List.of(30, 32, 34, 36, 38, 40, 42, 44, 46, 48, 50));
    tpsTicks.addAll(List.of(53, 56, 59, 62, 65, 70, 75, 80, 85, 90, 95, 100));
    tpsTicks.addAll(List.of(110, 130, 150, 200, 300, 500, 750, 1000, Integer.MAX_VALUE));

    robocodeDir = new File("/home/czarsmith/robocode");
    
    RobocodeEngine.setLogMessagesEnabled(false);
    engine = new RobocodeEngine(robocodeDir);
    engine.addBattleListener(this);
  }

  public List<RobotSpecification> getRobots() {
    return Arrays.asList(engine.getLocalRepository());
  }

  public void execute(Tourney tourney) {
    //new Thread(new TourneyThread(tourney)).start();
  }

  private class TourneyThread implements Runnable {
    private Tourney tourney;

    public TourneyThread(Tourney tourney) {
      this.tourney = tourney;
    }

    public void run() {
      running = true;
      tourney.getBattles().forEach(b -> executeBattle(tourney, b));
      running = false;
    }  
  }

  private void executeBattle(Tourney tourney, Battle battle) {
    Thread battleThread = new Thread(new BattleThread(tourney, battle));
    battleThread.start();

    // Find the robocode window
    robocodeWin = until(() -> Stream.of(Window.getWindows())
      .filter(w -> "net.sf.robocode.ui.dialog.RobocodeFrame".equals(w.getClass().getName()))
      .findAny().orElse(null));
    ((JFrame)robocodeWin).setUndecorated(true);
    
    until(() -> findComponents(robocodeWin, c -> c instanceof JMenu)).forEach(c -> ((JMenu)c).setVisible(false));
    
    // Find the TPS Slider
    tpsSlider = until(() -> (JSlider)findComponent(robocodeWin, c -> c instanceof JSlider));

    System.out.println("Battle has started");

    try {
      battleThread.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
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

  private class BattleThread implements Runnable {
    private Tourney tourney;
    private Battle battle;

    BattleThread(Tourney tourney, Battle battle) {
      this.tourney = tourney;
      this.battle = battle;
    }

    public void run() {
      var config = new RobocodeConfig(robocodeDir);
      config.setTps(battle.getTps());
      config.setVisibleGround(false);
      config.setVisibleScanArcs(false);
      config.setWindowSize(0, 0, 170 + battle.getBattlefieldWidth(), 140 + battle.getBattlefieldHeight());
      config.apply();

      BattlefieldSpecification battlefield = new BattlefieldSpecification(battle.getBattlefieldWidth(), battle.getBattlefieldHeight());
      RobotSpecification[] selectedRobots = engine.getLocalRepository(StringUtils.join(battle.getRobots(),","));
      BattleSpecification battleSpec = new BattleSpecification(battle.getRounds(), battlefield, selectedRobots);

      engine.setVisible(true);
      engine.runBattle(battleSpec, true);
      engine.close();
    }
  }

  private <T> T until(Supplier<T> supplier) {
    T result = supplier.get();
    while ((result == null || (result instanceof List && ((List<?>)result).isEmpty())) && running) {
      try {
        Thread.sleep(200);
        result = supplier.get();
      } catch (Exception e) {}
    }
    return result;
  }

  private Component findComponent(Component comp, Predicate<Component> matcher) {
    return findComponents(comp, matcher).get(0);
  }

  private List<Component> findComponents(Component comp, Predicate<Component> matcher) {
    if (matcher.test(comp)) {
      return Arrays.asList(comp);
    } else if (comp instanceof JMenu) {
      return IntStream.range(0, ((JMenu)comp).getItemCount()).mapToObj(i -> ((JMenu)comp).getItem(i)).filter(Objects::nonNull)
        .flatMap(c -> findComponents(c, matcher).stream()).filter(Objects::nonNull).collect(Collectors.toList());
    } else if (comp instanceof JMenuItem) {
      return Stream.of(((JMenuItem)comp).getSubElements()).map(o -> o.getComponent()).filter(Objects::nonNull)
        .flatMap(c -> findComponents(c, matcher).stream()).filter(Objects::nonNull).collect(Collectors.toList());
    } else if (comp instanceof java.awt.Container) {
      return Stream.of(((Container)comp).getComponents())
        .flatMap(c -> findComponents(c, matcher).stream()).filter(Objects::nonNull).collect(Collectors.toList());
    } else {
      return Collections.emptyList();
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

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

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSlider;

import org.apache.commons.lang3.StringUtils;

import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.stage.Stage;
import robocode.control.BattleSpecification;
import robocode.control.BattlefieldSpecification;
import robocode.control.RobocodeEngine;
import robocode.control.RobotSpecification;
import robocode.control.events.BattleAdaptor;
import robocode.control.events.BattleCompletedEvent;
import smi.roborun.RobocodeConfig;
import smi.roborun.mdl.Battle;
import smi.roborun.mdl.Robot;
import smi.roborun.mdl.Round;
import smi.roborun.mdl.Tourney;

public class BattleController extends BattleAdaptor {
  private RobocodeEngine engine;
  private boolean running;
  private JSlider tpsSlider;
  private List<Integer> tpsTicks;
  private Window robocodeWin;
  private File robocodeDir;
  private Stage stage;
  private Tourney tourney;
  private Round currRound;
  private Battle currBattle;

  public BattleController(Stage stage) {
    this.stage = stage;

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
    this.tourney = tourney;
    new Thread(new TourneyThread()).start();
  }

  private class TourneyThread implements Runnable {
    public void run() {
      running = true;
      // Execute melee battles
      tourney.getMeleeRounds().forEach(meleeRound ->
        meleeRound.getBattles().forEach(battle -> executeBattle(meleeRound, battle)));
      running = false;
    }  
  }

  private void executeBattle(Round round, Battle battle) {
    currRound = round;
    currBattle = battle;

    System.out.println("START Round " + battle.getRoundNumber() + " Battle " + battle.getBattleNumber());

    Thread battleThread = new Thread(new BattleThread());
    battleThread.start();

    // Find the robocode window
    if (robocodeWin == null) {
    robocodeWin = until(() -> Stream.of(Window.getWindows())
      .filter(w -> "net.sf.robocode.ui.dialog.RobocodeFrame".equals(w.getClass().getName()))
      .findAny().orElse(null));
//    ((JFrame)robocodeWin).setUndecorated(true);
    
//    until(() -> findComponents(robocodeWin, c -> c instanceof JMenu)).forEach(c -> ((JMenu)c).setVisible(false));
    
    // Find the TPS Slider
    tpsSlider = until(() -> (JSlider)findComponent(robocodeWin, c -> c instanceof JSlider));
    }
    setTps(currBattle.getTps());
    
    try {
      battleThread.join();
      System.out.println("DONE Round " + battle.getRoundNumber() + " Battle " + battle.getBattleNumber());
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
    public void run() {
      var config = new RobocodeConfig(robocodeDir);
      config.setTps(currBattle.getTps());
      config.setVisibleGround(false);
      config.setVisibleScanArcs(false);
      config.setWindowSize(0, 0, 170 + currBattle.getBattlefieldWidth(), 140 + currBattle.getBattlefieldHeight());
      config.apply();

      BattlefieldSpecification battlefield = new BattlefieldSpecification(
        currBattle.getBattlefieldWidth(), currBattle.getBattlefieldHeight());
      RobotSpecification[] selectedRobots = engine.getLocalRepository(
        StringUtils.join(currBattle.getRobots().stream().map(Robot::getRobotName).collect(Collectors.toList()), ","));
      BattleSpecification battleSpec = new BattleSpecification(currBattle.getNumRounds(), battlefield, selectedRobots);

      engine.setVisible(true);
      engine.runBattle(battleSpec, true);
      engine.setVisible(false);
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
    List<Component> comps = findComponents(comp, matcher);
    return comps.isEmpty() ? null : comps.get(0);
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
  public void onBattleCompleted(BattleCompletedEvent e) {
    currBattle.setResults(Arrays.asList(e.getSortedResults()));
    stage.fireEvent(BattleEvent.finished(tourney, currRound, currBattle));
  }

  public void addEventHandler(EventType<BattleEvent> e, EventHandler<BattleEvent> handler) {
    stage.addEventFilter(e, handler);
  }
}

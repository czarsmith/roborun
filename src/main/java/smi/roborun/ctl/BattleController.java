package smi.roborun.ctl;

import java.awt.Component;
import java.awt.Container;
import java.awt.Window;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSlider;
import javax.swing.JToolBar;

import org.apache.commons.lang3.StringUtils;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.stage.Stage;
import robocode.BattleResults;
import robocode.control.BattleSpecification;
import robocode.control.BattlefieldSpecification;
import robocode.control.RobocodeEngine;
import robocode.control.RobotSpecification;
import robocode.control.events.BattleAdaptor;
import robocode.control.events.BattleCompletedEvent;
import robocode.control.events.BattleStartedEvent;
import robocode.control.events.RoundEndedEvent;
import robocode.control.events.RoundStartedEvent;
import robocode.control.events.TurnEndedEvent;
import robocode.control.snapshot.IScoreSnapshot;
import smi.roborun.RobocodeConfig;
import smi.roborun.mdl.Battle;
import smi.roborun.mdl.Robot;
import smi.roborun.mdl.RobotScore;
import smi.roborun.mdl.Tourney;

public class BattleController extends BattleAdaptor {
  private static final int BATTLEFIELD_MARGIN = 4;
  
  private RobocodeEngine engine;
  private JSlider tpsSlider;
  private List<Integer> tpsTicks;
  private Window robocodeWin;
  private File robocodeDir;
  private Stage stage;
  private Tourney tourney;
  private Battle battle;
  private ScheduledExecutorService ses;
  private ScheduledFuture<?> tpsNext;
  private ScheduledFuture<?> tpsMax;
  private long nextEventTimeMillis = 0;
  private long eventRateMillis = 500;
  private long doubleSpeed;
  private long maxSpeed;
  private double maxRate;

  public BattleController(Stage stage, Tourney tourney, String robocodeHomePath) {
    this.stage = stage;
    this.tourney = tourney;
    robocodeDir = new File(robocodeHomePath);    
    ses = Executors.newSingleThreadScheduledExecutor();

    // The TPS slider isn't linear so we have to configure each of the individual tick marks
    // in order to determine which slider tick corresponds to each TPS value.
    tpsTicks = new ArrayList<>();
    IntStream.range(0, 30).forEach(tpsTicks::add);
    tpsTicks.addAll(List.of(30, 32, 34, 36, 38, 40, 42, 44, 46, 48, 50));
    tpsTicks.addAll(List.of(53, 56, 59, 62, 65, 70, 75, 80, 85, 90, 95, 100));
    tpsTicks.addAll(List.of(110, 130, 150, 200, 300, 500, 750, 1000, Integer.MAX_VALUE));

    RobocodeEngine.setLogMessagesEnabled(false);
    engine = new RobocodeEngine(robocodeDir);
    engine.addBattleListener(this);
  }

  public List<RobotSpecification> getRobots() {
    return Arrays.asList(engine.getLocalRepository());
  }

  public void playBattle() {
    battle = tourney.getBattle();

    if (battle.getRoundNumber() == 1 && battle.getBattleNumber() == 1) {
      maxRate = 0;
    }
    
    Thread battleThread = new Thread(new BattleThread());
    battleThread.start();

    prepUi();

    setTps(battle.getTps());

    System.out.println("Battle Started at: " + new Date());
    System.out.println("Max Rate: " + maxRate);
    maxSpeed = System.currentTimeMillis() + battle.getDesiredRuntimeMillis() * 2 / 3;
    if (maxRate > 0) {
      maxSpeed = (long)(System.currentTimeMillis() + battle.getDesiredRuntimeMillis() - maxRate * battle.getNumRobots() * battle.getNumRounds());
    }
    maxSpeed = Math.max(System.currentTimeMillis(), maxSpeed);
    doubleSpeed = System.currentTimeMillis() + (maxSpeed - System.currentTimeMillis()) / 2;
    doubleSpeed = Math.max(System.currentTimeMillis(), doubleSpeed);
    System.out.println("Double Speed: " + new Date(doubleSpeed));
    System.out.println("Max Speed: " + new Date(maxSpeed));
    if (doubleSpeed < maxSpeed) {
      tpsNext = ses.schedule(() -> setTps(battle.getTps() * 2), doubleSpeed - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
    }
    tpsMax = ses.schedule(() -> setTps(Integer.MAX_VALUE), maxSpeed - System.currentTimeMillis(), TimeUnit.MILLISECONDS);

    try {
      battleThread.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  private void prepUi() {
    if (robocodeWin == null) {
      // Find the robocode window
      robocodeWin = until(() -> Stream.of(Window.getWindows())
        .filter(w -> "net.sf.robocode.ui.dialog.RobocodeFrame".equals(w.getClass().getName()))
        .findAny().orElse(null), 10);
      try {
        ((JFrame)robocodeWin).setUndecorated(true);
      } catch (java.awt.IllegalComponentStateException e) {}

      tpsSlider = until(() -> (JSlider)findComponent(robocodeWin, c -> c instanceof JSlider));
      until(() -> findComponents(robocodeWin, c -> c instanceof JToolBar)).forEach(c -> ((JToolBar)c).setVisible(false));
      until(() -> findComponents(robocodeWin, c -> c instanceof JMenuBar)).forEach(c -> ((JMenuBar)c).setVisible(false));
      until(() -> (JButton)findComponent(robocodeWin, c -> c instanceof JButton
        && "Main battle log".equals(((JButton)c).getText()))).getParent().setVisible(false);
    }
  }

  public void setTps(int tps) {
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
      config.setTps(battle.getTps());
      config.setVisibleGround(false);
      config.setVisibleScanArcs(false);
      config.setWindowSize(0, 0, BATTLEFIELD_MARGIN + battle.getBattlefieldWidth(), BATTLEFIELD_MARGIN + battle.getBattlefieldHeight());
      config.setShowResults(false);
      config.apply();

      BattlefieldSpecification battlefield = new BattlefieldSpecification(
        battle.getBattlefieldWidth(), battle.getBattlefieldHeight());
      RobotSpecification[] selectedRobots = engine.getLocalRepository(
        StringUtils.join(battle.getRobots().stream().map(Robot::getRobotName).collect(Collectors.toList()), ","));
      BattleSpecification battleSpec = new BattleSpecification(battle.getNumRounds(), battlefield, selectedRobots);

      engine.setVisible(true);
      robocodeWin.setSize(BATTLEFIELD_MARGIN + battle.getBattlefieldWidth(), BATTLEFIELD_MARGIN + battle.getBattlefieldHeight());
      engine.runBattle(battleSpec, true);
      engine.setVisible(false);
    }
  }

  private <T> T until(Supplier<T> supplier) {
    return until(supplier, 200);
  }

  private <T> T until(Supplier<T> supplier, long interval) {
    T result = supplier.get();
    while (result == null || (result instanceof List && ((List<?>)result).isEmpty())) {
      try {
        Thread.sleep(interval);
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
  public void onBattleStarted(BattleStartedEvent e) {
    battle.setStartTime(System.currentTimeMillis());
  }

  @Override
  public void onBattleCompleted(BattleCompletedEvent e) {
    tpsNext.cancel(true);
    tpsMax.cancel(true);
    battle.setEndTime(System.currentTimeMillis());
    
    System.out.println("Battle Ended at: " + new Date());
    maxRate = (System.currentTimeMillis() - (double)maxSpeed) / (battle.getNumRobots() * battle.getNumRounds());
    Platform.runLater(() -> {
      updateScores(e.getSortedResults());
      battle.getResults().addAll(battle.getRobots().stream().map(Robot::getBattleScore)
        .map(RobotScore::copy).sorted(Comparator.comparing(RobotScore::getRank)).collect(Collectors.toList()));
      stage.fireEvent(BattleEvent.finished(tourney, battle));
    });
  }

  @Override
  public void onRoundStarted(RoundStartedEvent e) {
    Platform.runLater(() -> {
      battle.setBattleRound(e.getRound());
      stage.fireEvent(BattleEvent.roundStarted());
    });
  }

  @Override
  public void onRoundEnded(RoundEndedEvent e) {
    Platform.runLater(() -> stage.fireEvent(BattleEvent.roundFinished()));
  }

  @Override
  public void onTurnEnded(TurnEndedEvent e) {
    if (System.currentTimeMillis() >= nextEventTimeMillis) {
      nextEventTimeMillis = System.currentTimeMillis() + eventRateMillis;
      Platform.runLater(() -> {
        IScoreSnapshot[] scores = e.getTurnSnapshot().getSortedTeamScores();
        updateScores(scores);
        stage.fireEvent(BattleEvent.turnFinished(tourney, battle));
      });
    }
  }

  private void updateScores(BattleResults[] scores) {
    for (int i = 0; i < scores.length; i++) {
      BattleResults score = scores[i];
      RobotScore battleScore = tourney.getRobot(scores[i].getTeamLeaderName()).getBattleScore();
      RobotScore previousScore = battleScore.copy();
      battleScore.setRobotName(score.getTeamLeaderName());
      battleScore.setScore(score.getScore());
      battleScore.setRank(battleScore.getScore() == 0 ? 0 : i + 1);
      battleScore.setSurvival(score.getSurvival());
      battleScore.setLastSurvivorBonus(score.getLastSurvivorBonus());
      battleScore.setBulletDamage(score.getBulletDamage());
      battleScore.setBulletDamageBonus(score.getBulletDamageBonus());
      battleScore.setRamDamage(score.getRamDamage());
      battleScore.setRamDamageBonus(score.getRamDamageBonus());
      battleScore.setFirsts(score.getFirsts());
      battleScore.setSeconds(score.getSeconds());
      battleScore.setThirds(score.getThirds());

      if (!battle.isPreliminary()) {
        updateTotalScore(battleScore, previousScore);
      }
    }
    updateTotalRank();
  }

  private void updateScores(IScoreSnapshot[] scores) {
    for (int i = 0; i < scores.length; i++) {
      IScoreSnapshot score = scores[i];
      RobotScore battleScore = tourney.getRobot(scores[i].getName()).getBattleScore();
      RobotScore previousScore = battleScore.copy();
      battleScore.setScore((int)(score.getTotalScore() + score.getCurrentScore()));
      battleScore.setRank(battleScore.getScore() == 0 ? 0 : i + 1);
      battleScore.setRobotName(score.getName());
      battleScore.setSurvival((int)(score.getTotalSurvivalScore() + score.getCurrentSurvivalScore()));
      battleScore.setLastSurvivorBonus((int)(score.getTotalLastSurvivorBonus() + score.getCurrentSurvivalBonus()));
      battleScore.setBulletDamage((int)(score.getTotalBulletDamageScore() + score.getCurrentBulletDamageScore()));
      battleScore.setBulletDamageBonus((int)(score.getTotalBulletKillBonus() + score.getCurrentBulletKillBonus()));
      battleScore.setRamDamage((int)(score.getTotalRammingDamageScore() + score.getCurrentRammingDamageScore()));
      battleScore.setRamDamageBonus((int)(score.getTotalRammingKillBonus() + score.getCurrentRammingKillBonus()));
      battleScore.setFirsts(score.getTotalFirsts());
      battleScore.setSeconds(score.getTotalSeconds());
      battleScore.setThirds(score.getTotalThirds());

      if (!battle.isPreliminary()) {
        updateTotalScore(battleScore, previousScore);
      }
    }
    updateTotalRank();
  }

  private void updateTotalScore(RobotScore battleScore, RobotScore previousScore) {
    RobotScore totalScore = tourney.getRobot(battleScore.getRobotName()).getTotalScore();
    totalScore.setScore(totalScore.getScore() + battleScore.getScore() - previousScore.getScore());
    totalScore.setSurvival(totalScore.getSurvival() + battleScore.getSurvival() - previousScore.getSurvival());
    totalScore.setLastSurvivorBonus(totalScore.getLastSurvivorBonus() + battleScore.getLastSurvivorBonus() - previousScore.getLastSurvivorBonus());
    totalScore.setBulletDamage(totalScore.getBulletDamage() + battleScore.getBulletDamage() - previousScore.getBulletDamage());
    totalScore.setBulletDamageBonus(totalScore.getBulletDamageBonus() + battleScore.getBulletDamageBonus() - previousScore.getBulletDamageBonus());
    totalScore.setRamDamage(totalScore.getRamDamage() + battleScore.getRamDamage() - previousScore.getRamDamage());
    totalScore.setRamDamageBonus(totalScore.getRamDamageBonus() + battleScore.getRamDamageBonus() - previousScore.getRamDamageBonus());
    totalScore.setFirsts(totalScore.getFirsts() + battleScore.getFirsts() - previousScore.getFirsts());
    totalScore.setSeconds(totalScore.getSeconds() + battleScore.getSeconds() - previousScore.getSeconds());
    totalScore.setThirds(totalScore.getThirds() + battleScore.getThirds() - previousScore.getThirds());
  }

  private void updateTotalRank() {
    if (!battle.isPreliminary()) {
      List<RobotScore> sorted = tourney.getRobots().stream().map(Robot::getTotalScore)
        .sorted(Comparator.comparing(s -> s.getScore(), Comparator.reverseOrder())).collect(Collectors.toList());
      for (int i = 0; i < sorted.size(); i++) {
        sorted.get(i).setRank(sorted.get(i).getScore() == 0 ? 0 : i + 1);
      }
    }
  }

  public void addEventHandler(EventType<BattleEvent> e, EventHandler<BattleEvent> handler) {
    stage.addEventFilter(e, handler);
  }
}

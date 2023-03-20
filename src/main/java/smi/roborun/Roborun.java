package smi.roborun;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import smi.roborun.ctl.BattleController;
import smi.roborun.ui.MeleeBracket;
import smi.roborun.ui.NavButton;
import smi.roborun.ui.Participants;
import smi.roborun.ui.VsBracket;

public class Roborun extends JFrame {
  private static final String PARTICIPANTS_TAB = "participants-tab";
  private static final String MELEE_TAB = "melee-tab";
  private static final String VS_TAB = "vs-tab";

  private BattleController ctl;
  private CardLayout tabs;

  public Roborun() {
    ctl = new BattleController();
    
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setUndecorated(true);
    setLayout(new BorderLayout());
    setExtendedState(JFrame.MAXIMIZED_BOTH);

    tabs = new CardLayout();
    JPanel center = new JPanel();
    center.setLayout(tabs);
    center.add(new Participants(ctl), PARTICIPANTS_TAB);
    center.add(new MeleeBracket(ctl), MELEE_TAB);
    center.add(new VsBracket(ctl), VS_TAB);

    JPanel nav = new JPanel();
    nav.setBorder(new EmptyBorder(8, 8, 8, 8));
    nav.setLayout(new BoxLayout(nav, BoxLayout.Y_AXIS));
    nav.add(Box.createVerticalGlue());
    nav.add(new NavButton("Settings", "/icons/gear-solid.svg", e -> tabs.show(center, PARTICIPANTS_TAB)));
    nav.add(new NavButton("Start", "/icons/play-solid.svg", e -> {
      ctl.execute();
      CompletableFuture.delayedExecutor(10, TimeUnit.SECONDS).execute(() -> {
        ctl.setTps(50);
      });
    }));
    nav.add(new NavButton("Melee", "/icons/people-group-solid.svg", e -> tabs.show(center, MELEE_TAB)));
    nav.add(new NavButton("1v1", "/icons/people-arrows-solid.svg", e -> tabs.show(center, VS_TAB)));
    nav.add(Box.createVerticalGlue());
    nav.add(new NavButton("Close", "/icons/square-xmark-solid.svg", e -> this.exit()));
    
    getContentPane().add(nav, BorderLayout.WEST);
    getContentPane().add(center, BorderLayout.CENTER);
    setVisible(true);
  }

  private void exit() {
    dispose();
    System.exit(0);
  }

  public static final void main(String... args) throws Exception {
    RoborunLaf.configureLaf();
    new Roborun();
  }
}

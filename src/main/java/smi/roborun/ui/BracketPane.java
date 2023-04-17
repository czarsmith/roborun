package smi.roborun.ui;

import java.util.Comparator;

import javafx.collections.ListChangeListener;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import smi.roborun.mdl.Battle;
import smi.roborun.mdl.Battle.BattleType;
import smi.roborun.mdl.Tourney;

public class BracketPane extends ScrollPane implements ListChangeListener<Battle> {
  private Pane viewport;

  public BracketPane(Tourney tourney) {
    tourney.getBattles().addListener(this);

    setBorder(new Border(new BorderStroke(Color.BLUE, BorderStrokeStyle.SOLID, new CornerRadii(5),
      BorderWidths.DEFAULT)));

    viewport = new Pane();
    viewport.setPrefWidth(600);
    viewport.setMinWidth(600);
    viewport.setMaxWidth(600);
    setContent(viewport);
  }

  @Override
  public void onChanged(Change<? extends Battle> battles) {
    viewport.getChildren().clear();

    int numMeleeRounds = battles.getList().stream()
      .filter(b -> b.getType() == BattleType.MELEE)
      .map(b -> b.getRoundNumber())
      .max(Comparator.naturalOrder()).orElse(0);

    battles.getList().stream().filter(b -> b.getType() == BattleType.MELEE).forEach(battle -> {
      Label lbl = createBattle(battle);
      lbl.setTranslateX(100 * battle.getRoundNumber());
      lbl.setTranslateY(100 * battle.getBattleNumber());
      viewport.getChildren().add(lbl);
    });

    int vsOffsetX = numMeleeRounds * 100;
    battles.getList().stream().filter(b -> b.getType() == BattleType.VS).forEach(battle -> {
      Label lbl = createBattle(battle);
      lbl.setTranslateX(vsOffsetX + 100 * battle.getRoundNumber());
      lbl.setTranslateY(100 * battle.getBattleNumber());
      viewport.getChildren().add(lbl);
    });
  }

  private Label createBattle(Battle battle) {
    Label lbl = new Label("R" + battle.getRoundNumber() + " B" + battle.getBattleNumber());
    lbl.setPadding(new Insets(16));
    lbl.setFont(new Font("Arial", 18));
    lbl.setBorder(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, new CornerRadii(5),
      BorderWidths.DEFAULT)));
    return lbl;
  }
}

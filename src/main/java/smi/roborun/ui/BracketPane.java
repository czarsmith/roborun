package smi.roborun.ui;

import javafx.collections.ListChangeListener;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import smi.roborun.mdl.Battle;
import smi.roborun.mdl.Battle.BattleType;
import smi.roborun.mdl.Tourney;

public class BracketPane extends ScrollPane implements ListChangeListener<Battle> {
  private Pane viewport;

  public BracketPane(Tourney tourney) {
    tourney.getBattles().addListener(this);

    viewport = new Pane();
    viewport.setPrefWidth(600);
    viewport.setMinWidth(600);
    viewport.setMaxWidth(600);
    setContent(viewport);
  }

  @Override
  public void onChanged(Change<? extends Battle> battles) {
    viewport.getChildren().clear();

    int x = 0;
    int roundNumber = 1;
    BattleType battleType = BattleType.MELEE;
    for (Battle battle : battles.getList()) {
      if (battle.getRoundNumber() != roundNumber || battle.getType() != battleType) {
        x++;
        battleType = battle.getType();
        roundNumber = battle.getRoundNumber();
      }
      viewport.getChildren().add(new BracketBattle(battle, x, battle.getBattleNumber() - 1));
    }
  }
}

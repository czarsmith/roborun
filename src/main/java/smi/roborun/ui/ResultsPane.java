package smi.roborun.ui;

import javafx.collections.ListChangeListener.Change;
import javafx.scene.layout.VBox;
import smi.roborun.mdl.Battle;
import smi.roborun.mdl.Tourney;

public class ResultsPane extends VBox {
  public ResultsPane(Tourney tourney) {
    tourney.getBattles().addListener(this::onBattlesChanged);
    getChildren().add(new RankingsPane(tourney));
  }

  private void onBattlesChanged(Change<? extends Battle> bc) {
    getChildren().removeIf(c -> c instanceof BattleResultsPane);
    bc.getList().forEach(b -> getChildren().add(new BattleResultsPane(b)));
  }
}

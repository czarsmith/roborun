package smi.roborun.ui;

import javafx.collections.ListChangeListener;
import javafx.scene.layout.VBox;
import smi.roborun.mdl.Battle;
import smi.roborun.mdl.Tourney;

public class ResultsPane extends VBox implements ListChangeListener<Battle> {
  public ResultsPane(Tourney tourney) {
    tourney.getBattles().addListener(this);
  }

  @Override
  public void onChanged(Change<? extends Battle> c) {
    getChildren().clear();
    c.getList().forEach(b -> getChildren().add(new BattleResultsPane(b)));
  }
}

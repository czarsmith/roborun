package smi.roborun.ui;

import javafx.collections.ListChangeListener.Change;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import smi.roborun.mdl.Battle;
import smi.roborun.mdl.Tourney;

public class ResultsPane extends ScrollPane {
  private VBox vbox;
  private Tourney tourney;

  public ResultsPane(Tourney tourney) {
    this.tourney = tourney;
    tourney.getBattles().addListener(this::onBattlesChanged);

    setFitToWidth(true);

    vbox = new VBox();
    vbox.setFillWidth(false);
    vbox.setSpacing(8);
    vbox.setAlignment(Pos.CENTER);
    vbox.getChildren().add(new RankingsPane(tourney));

    setContent(vbox);
  }

  private void onBattlesChanged(Change<? extends Battle> bc) {
    vbox.getChildren().removeIf(c -> c instanceof BattleResultsPane);
    bc.getList().forEach(b -> vbox.getChildren().add(new BattleResultsPane(tourney, b)));
  }
}

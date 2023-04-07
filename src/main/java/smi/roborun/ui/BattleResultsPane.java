package smi.roborun.ui;

import javafx.collections.ListChangeListener;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import robocode.BattleResults;
import smi.roborun.mdl.Battle;

public class BattleResultsPane extends VBox implements ListChangeListener<BattleResults> {
  private GridPane grid;

  public BattleResultsPane(Battle battle) {
    battle.getResults().addListener(this);

    getChildren().add(new Label(battle.getType() + " Round " + battle.getRoundNumber() + " Battle " + battle.getBattleNumber()));

    grid = new GridPane();
    getChildren().add(grid);
  }

  @Override
  public void onChanged(Change<? extends BattleResults> c) {
    getChildren().remove(grid);
    grid = new GridPane();
    getChildren().add(grid);
    c.getList().forEach(br -> addRow(br));
  }

  private void addRow(BattleResults br) {
    int row = grid.getRowCount();
    grid.add(new Label(br.getTeamLeaderName()), 0, row);
    grid.add(new Label(Integer.toString(br.getScore())), 1, row);
  }
}

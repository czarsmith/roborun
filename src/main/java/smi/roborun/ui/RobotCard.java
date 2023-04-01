package smi.roborun.ui;

import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import smi.roborun.mdl.Robot;

public class RobotCard extends GridPane {
  private Robot robot;

  public RobotCard(Robot robot) {
    this.robot = robot;

    setBorder(new Border(new BorderStroke(Color.GRAY, BorderStrokeStyle.SOLID, new CornerRadii(5), BorderStroke.THIN)));
    setPadding(new Insets(8));

    //IntegerProperty battleRankProperty = robot.getBattleScore().getRankProperty();
    //Bindings.createStringBinding(() ->
    //  getRankDisplay((int)battleRankProperty.get()) + " " + robotDisplayName, battleRankProperty));
    Label displayNameLabel = new Label();
    displayNameLabel.setFont(new Font("Arial", 18));
    displayNameLabel.textProperty().bind(robot.getShortNameAndRankProperty());
    GridPane.setColumnSpan(displayNameLabel, 2);
    add(displayNameLabel, 0, 0);

    Label packageName = new Label("(" + robot.getRobotName().substring(robot.getPackageName().length() + 1) + ")");
    packageName.setFont(new Font("Arial", 10));
    GridPane.setColumnSpan(packageName, 2);
    add(packageName, 0, 1);

    DoubleProperty osp = robot.getOverallScore().getScoreProperty();
    Label overallScoreLabel = new Label();
    overallScoreLabel.textProperty().bind(Bindings.createStringBinding(() -> 
      Integer.toString((int)osp.get()), osp));
    overallScoreLabel.setFont(new Font("Arial", 14));
    add(fieldNameLabel("Overall Score: "), 0, 2);
    add(overallScoreLabel, 1, 2);

    DoubleProperty bsp = robot.getBattleScore().getScoreProperty();
    Label battleScoreLabel = new Label();
    battleScoreLabel.textProperty().bind(Bindings.createStringBinding(() -> 
      Integer.toString((int)bsp.get()), bsp));
    battleScoreLabel.setFont(new Font("Arial", 14));
    add(fieldNameLabel("Battle Score: "), 0, 3);
    add(battleScoreLabel, 1, 3);
  }

  private Label fieldNameLabel(String text) {
    Label label = new Label(text);
    label.setFont(Font.font("Aria", 14));
    return label;
  }

  public Robot getRobot() {
    return robot;
  }
}

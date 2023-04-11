package smi.roborun.ui;

import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
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
  private static final int PADDING = 8;
  private Robot robot;

  public RobotCard(Robot robot) {
    this.robot = robot;

    setBorder(new Border(new BorderStroke(Color.GRAY, BorderStrokeStyle.SOLID, new CornerRadii(5), BorderStroke.THIN)));
    setPadding(new Insets(PADDING));

    ReadOnlyDoubleProperty cardWidth = widthProperty();

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

    Label authorLabel = new Label("By: " + robot.getAuthor());
    authorLabel.setFont(new Font("Arial", 14));
    authorLabel.prefWidthProperty().bind(Bindings.createDoubleBinding(() -> cardWidth.get() - PADDING * 2 - 2, cardWidth));
    add(authorLabel, 0, 2);
    GridPane.setColumnSpan(authorLabel, 2);

    IntegerProperty tsp = robot.getTotalScore().getScoreProperty();
    Label totalScoreLabel = new Label();
    totalScoreLabel.textProperty().bind(Bindings.createStringBinding(() -> Integer.toString(tsp.get()), tsp));
    totalScoreLabel.setFont(new Font("Arial", 14));
    add(fieldNameLabel("Overall Score: "), 0, 3);
    add(totalScoreLabel, 1, 3);

    IntegerProperty bsp = robot.getBattleScore().getScoreProperty();
    Label battleScoreLabel = new Label();
    battleScoreLabel.textProperty().bind(Bindings.createStringBinding(() -> Integer.toString(bsp.get()), bsp));
    battleScoreLabel.setFont(new Font("Arial", 14));
    add(fieldNameLabel("Battle Score: "), 0, 4);
    add(battleScoreLabel, 1, 4);

    Label codeSizeLabel = new Label(Integer.toString(robot.getCodeSize()));
    codeSizeLabel.setFont(new Font("Arial", 14));
    add(fieldNameLabel("Weight: "), 0, 5);
    add(codeSizeLabel, 1, 5);
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

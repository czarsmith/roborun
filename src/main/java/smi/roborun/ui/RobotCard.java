package smi.roborun.ui;

import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import smi.roborun.mdl.Robot;

public class RobotCard extends VBox {
  private Robot robot;
  private Label battleScoreLabel;

  public RobotCard(Robot robot) {
    this.robot = robot;

    setBorder(new Border(new BorderStroke(Color.GRAY, BorderStrokeStyle.SOLID, new CornerRadii(5), BorderStroke.THIN)));
    setPadding(new Insets(8));

    Label displayName = new Label(getDisplayName());
    displayName.setFont(new Font("Arial", 18));
    getChildren().add(displayName);

    Label packageName = new Label("(" + getPackageName() + ")");
    packageName.setFont(new Font("Arial", 10));
    getChildren().add(packageName);

    DoubleProperty dp = robot.getBattleScore().getScoreProperty();

    battleScoreLabel = new Label();
    battleScoreLabel.textProperty().bind(Bindings.createStringBinding(() -> 
      "Battle Score: " + (int)dp.get(), dp));
    packageName.setFont(new Font("Arial", 14));
    getChildren().add(battleScoreLabel);
  }

  public Robot getRobot() {
    return robot;
  }
  
  private String getDisplayName() {
    String displayName = robot.getSpec().getClassName();
    displayName = displayName.substring(displayName.lastIndexOf(".") + 1);
    return displayName;
  }

  private String getPackageName() {
    String packageName = robot.getSpec().getClassName();
    packageName = packageName.substring(0, packageName.lastIndexOf("."));
    return packageName;
  }
}

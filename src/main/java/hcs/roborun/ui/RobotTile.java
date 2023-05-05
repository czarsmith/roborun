package hcs.roborun.ui;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import hcs.roborun.mdl.Robot;

public class RobotTile extends VBox {
  private Robot robot;

  public RobotTile() {
    this(null);
  }
  
  public RobotTile(Robot robot) {
    this.robot = robot;

    setBorder(new Border(new BorderStroke(Color.GRAY, BorderStrokeStyle.SOLID, new CornerRadii(5), BorderStroke.THIN)));
    setPadding(new Insets(8));

    Label displayName = new Label();
    if (robot != null) {
      displayName.textProperty().bind(robot.getShortNameAndRankProperty());
    } else {
      displayName.setText("?");
    }
    displayName.setFont(new Font("Arial", 18));
    getChildren().add(displayName);

    Label packageName = new Label();
    if (robot != null) {
      packageName.setText("(" + robot.getPackageName() + ")");
    } else {
      packageName.setText("...");
    }
    packageName.setFont(new Font("Arial", 10));
    getChildren().add(packageName);
  }

  public Robot getRobot() {
    return robot;
  }
}

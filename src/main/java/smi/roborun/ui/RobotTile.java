package smi.roborun.ui;

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

public class RobotTile extends VBox {
  private Robot robot;

  public RobotTile() {
    this(null);
  }
  
  public RobotTile(Robot robot) {
    this.robot = robot;

    setBorder(new Border(new BorderStroke(Color.GRAY, BorderStrokeStyle.SOLID, new CornerRadii(5), BorderStroke.THIN)));
    setPadding(new Insets(8));

    Label displayName = new Label(getDisplayName());
    displayName.setFont(new Font("Arial", 18));
    getChildren().add(displayName);

    Label packageName = new Label("(" + getPackageName() + ")");
    packageName.setFont(new Font("Arial", 10));
    getChildren().add(packageName);
  }

  public Robot getRobot() {
    return robot;
  }
  
  private String getDisplayName() {
    if (robot != null) {
      String displayName = robot.getSpec().getClassName();
      return displayName.substring(displayName.lastIndexOf(".") + 1);  
    } else {
      return "?";
    }
  }

  private String getPackageName() {
    if (robot != null) {
      String packageName = robot.getSpec().getClassName();
      return packageName.substring(0, packageName.lastIndexOf("."));  
    } else {
      return "-";
    }
  }
}

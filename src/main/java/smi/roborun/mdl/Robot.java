package smi.roborun.mdl;

import org.apache.commons.lang3.StringUtils;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import robocode.control.RobotSpecification;

public class Robot {
  private BooleanProperty selected;
  private StringProperty author;
  private StringProperty robotName;
  private ObjectProperty<Long> codeSize;

  private RobotSpecification spec;

  public Robot(RobotSpecification spec) {
    this.spec = spec;
    selected = new SimpleBooleanProperty();
    author = new SimpleStringProperty(StringUtils.defaultIfBlank(spec.getAuthorName(), "Unknown"));
    robotName = new SimpleStringProperty(spec.getName());
    codeSize = new SimpleObjectProperty<>(0L);
  }

  public BooleanProperty getSelectedProperty() {
    return selected;
  }

  public boolean getSelected() {
    return selected.get();
  }

  public void setSelected(boolean selected) {
    this.selected.set(selected);
  }

  public StringProperty getAuthorProperty() {
    return author;
  }

  public StringProperty getRobotNameProperty() {
    return robotName;
  }

  public String getRobotName() {
    return robotName.get();
  }

  public void setRobotName(String robotName) {
    this.robotName.set(robotName);
  }
  
  public ObjectProperty<Long> getCodeSizeProperty() {
    return codeSize;
  }
}

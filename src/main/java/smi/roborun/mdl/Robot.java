package smi.roborun.mdl;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
  private ObjectProperty<Integer> meleeSeed;
  
  private RobotSpecification spec;

  public Robot(RobotSpecification spec) {
    this.spec = spec;
    selected = new SimpleBooleanProperty();
    author = new SimpleStringProperty(StringUtils.defaultIfBlank(spec.getAuthorName(), "Unknown"));
    robotName = new SimpleStringProperty(spec.getName());
    codeSize = new SimpleObjectProperty<>(0L);
    meleeSeed = new SimpleObjectProperty<>(0);
  }

  @JsonIgnore
  public BooleanProperty getSelectedProperty() {
    return selected;
  }

  public boolean getSelected() {
    return selected.get();
  }

  public void setSelected(boolean selected) {
    this.selected.set(selected);
  }

  @JsonIgnore
  public StringProperty getAuthorProperty() {
    return author;
  }

  @JsonIgnore
  public StringProperty getRobotNameProperty() {
    return robotName;
  }

  public String getRobotName() {
    return robotName.get();
  }

  public void setRobotName(String robotName) {
    this.robotName.set(robotName);
  }
  
  @JsonIgnore
  public ObjectProperty<Long> getCodeSizeProperty() {
    return codeSize;
  }

  @JsonIgnore
  public ObjectProperty<Integer> getMeleeSeedProperty() {
    return meleeSeed;
  }

  public Integer getMeleeSeed() {
    return meleeSeed.get();
  }

  public void setMeleeSeed(Integer meleeSeed) {
    this.meleeSeed.set(meleeSeed);
  }
}

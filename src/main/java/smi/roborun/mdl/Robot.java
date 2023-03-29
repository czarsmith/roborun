package smi.roborun.mdl;

import java.io.File;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import net.sf.robocode.repository.CodeSizeCalculator;
import robocode.control.RobotSpecification;

public class Robot {
  private BooleanProperty selected;
  private StringProperty author;
  private StringProperty robotName;
  private ObjectProperty<Integer> codeSize;
  private ObjectProperty<Integer> meleeSeed;
  
  private RobotSpecification spec;

  public Robot(RobotSpecification spec) {
    this.spec = spec;
    selected = new SimpleBooleanProperty();
    author = new SimpleStringProperty(StringUtils.defaultIfBlank(spec.getAuthorName(), "Unknown"));
    robotName = new SimpleStringProperty(spec.getName());
    codeSize = new SimpleObjectProperty<>(0);
    meleeSeed = new SimpleObjectProperty<>(0);

    try {
      // Weird Robocode stuff apparently
      String path = spec.getJarFile().getPath();
      path = path.substring(path.lastIndexOf(":") + 1);
      path = path.replace("!", "");
      File file = new File(path);
      if (file.isFile()) {
        codeSize.set(CodeSizeCalculator.getJarFileCodeSize(file));
      }
    } catch (Exception e) {
      System.out.println("Unknown robot path: " + spec.getJarFile());
    }
  }

  public RobotSpecification getSpec() {
    return spec;
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
  public ObjectProperty<Integer> getCodeSizeProperty() {
    return codeSize;
  }

  public Integer getCodeSize() {
    return codeSize.get();
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

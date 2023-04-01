package smi.roborun.mdl;

import java.io.File;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import net.sf.robocode.repository.CodeSizeCalculator;
import robocode.control.RobotSpecification;

@JsonInclude(Include.NON_NULL)
public class Robot {
  private StringProperty shortName;
  private StringProperty packageName;
  private StringProperty shortNameAndRank; // overall rank
  private StringProperty shortNameAndBattleRank;
  private BooleanProperty selected;
  private StringProperty author;
  private StringProperty robotName;
  private ObjectProperty<Integer> codeSize;
  private ObjectProperty<Integer> meleeSeed;
  private ObjectProperty<RobotScore> overallScore;
  private ObjectProperty<RobotScore> battleScore;

  private RobotSpecification spec;

  public Robot(RobotSpecification spec) {
    this.spec = spec;
    shortName = new SimpleStringProperty(spec.getClassName().substring(spec.getClassName().lastIndexOf(".") + 1));
    packageName = new SimpleStringProperty(spec.getClassName().substring(0, spec.getClassName().lastIndexOf(".")));
    shortNameAndRank = new SimpleStringProperty("- " + shortName.get());
    shortNameAndBattleRank = new SimpleStringProperty("- " + shortName.get());
    selected = new SimpleBooleanProperty();
    author = new SimpleStringProperty(StringUtils.defaultIfBlank(spec.getAuthorName(), "Unknown"));
    robotName = new SimpleStringProperty(spec.getNameAndVersion());
    codeSize = new SimpleObjectProperty<>(0);
    meleeSeed = new SimpleObjectProperty<>(0);
    overallScore = new SimpleObjectProperty<>(new RobotScore());
    battleScore = new SimpleObjectProperty<>(new RobotScore());

    IntegerProperty orp = overallScore.get().getRankProperty();
    shortNameAndRank.bind(Bindings.createStringBinding(() ->
      (orp.get() == 0 ? "-" : orp.get()) + " " + shortName.get(), orp));

    IntegerProperty brp = battleScore.get().getRankProperty();
    shortNameAndBattleRank.bind(Bindings.createStringBinding(() ->
      (brp.get() == 0 ? "-" : brp.get()) + " " + shortName.get(), brp));
  
    // Calculate code size
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

  @JsonIgnore
  public StringProperty getShortNameProperty() {
    return shortName;
  }

  public String getShortName() {
    return shortName.get();
  }

  public void setShortName(String shortName) {
    this.shortName.set(shortName);
  }

  @JsonIgnore
  public StringProperty getPackageNameProperty() {
    return packageName;
  }

  public String getPackageName() {
    return packageName.get();
  }

  public void setPackageName(String packageName) {
    this.packageName.set(packageName);
  }
  
  @JsonIgnore
  public StringProperty getShortNameAndRankProperty() {
    return shortNameAndRank;
  }

  public String getShortNameAndRank() {
    return shortNameAndRank.get();
  }

  public void setShortNameAndRank(String shortNameAndRank) {
    this.shortNameAndRank.set(shortNameAndRank);
  }
  
  @JsonIgnore
  public StringProperty getShortNameAndBattleRankProperty() {
    return shortNameAndBattleRank;
  }

  public String getShortNameAndBattleRank() {
    return shortNameAndBattleRank.get();
  }

  public void setShortNameAndBattleRank(String shortNameAndBattleRank) {
    this.shortNameAndBattleRank.set(shortNameAndBattleRank);
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

  public ObjectProperty<RobotScore> getOverallScoreProperty() {
    return overallScore;
  }

  public RobotScore getOverallScore() {
    return overallScore.get();
  }

  public ObjectProperty<RobotScore> getBattleScoreProperty() {
    return battleScore;
  }

  public RobotScore getBattleScore() {
    return battleScore.get();
  }
}

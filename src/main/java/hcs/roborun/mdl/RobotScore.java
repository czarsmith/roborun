package smi.roborun.mdl;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class RobotScore {
  private StringProperty robotName;
  private StringProperty author;
  private IntegerProperty rank;
  private IntegerProperty score;
  private IntegerProperty survival;
  private IntegerProperty lastSurvivorBonus;
  private IntegerProperty bulletDamage;
  private IntegerProperty bulletDamageBonus;
  private IntegerProperty ramDamage;
  private IntegerProperty ramDamageBonus;
  private IntegerProperty firsts;
  private IntegerProperty seconds;
  private IntegerProperty thirds;

  public RobotScore() {
    robotName = new SimpleStringProperty();
    author = new SimpleStringProperty();
    rank = new SimpleIntegerProperty(0);
    score = new SimpleIntegerProperty(0);
    survival = new SimpleIntegerProperty(0);
    lastSurvivorBonus = new SimpleIntegerProperty(0);
    bulletDamage = new SimpleIntegerProperty(0);
    bulletDamageBonus = new SimpleIntegerProperty(0);
    ramDamage = new SimpleIntegerProperty(0);
    ramDamageBonus = new SimpleIntegerProperty(0);
    firsts = new SimpleIntegerProperty(0);
    seconds = new SimpleIntegerProperty(0);
    thirds = new SimpleIntegerProperty(0);
    reset(true);
  }

  public void reset(boolean hard) {
    setRank(0);
    setScore(0);
    setSurvival(0);
    setLastSurvivorBonus(0);
    setBulletDamage(0);
    setBulletDamageBonus(0);
    setRamDamage(0);
    setRamDamageBonus(0);
    setFirsts(0);
    setSeconds(0);
    setThirds(0);

    if (hard) {
      rank.unbind();
      score.unbind();
      survival.unbind();
      lastSurvivorBonus.unbind();
      bulletDamage.unbind();
      bulletDamageBonus.unbind();
      ramDamage.unbind();
      ramDamageBonus.unbind();
      firsts.unbind();
      seconds.unbind();
      thirds.unbind();
    }
  }

  public RobotScore copy() {
    RobotScore ret = new RobotScore();
    ret.setRobotName(getRobotName());
    ret.setAuthor(getAuthor());
    ret.setRank(getRank());
    ret.setScore(getScore());
    ret.setBulletDamage(getBulletDamage());
    ret.setBulletDamageBonus(getBulletDamageBonus());
    ret.setFirsts(getFirsts());
    ret.setLastSurvivorBonus(getLastSurvivorBonus());
    ret.setRamDamage(getRamDamage());
    ret.setRamDamageBonus(getRamDamageBonus());
    ret.setSeconds(getSeconds());
    ret.setSurvival(getSurvival());
    ret.setThirds(getThirds());
    return ret;
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
  public StringProperty getAuthorProperty() {
    return author;
  }

  public String getAuthor() {
    return author.get();
  }

  public void setAuthor(String author) {
    this.author.set(author);
  }

  @JsonIgnore
  public IntegerProperty getRankProperty() {
    return rank;
  }

  public int getRank() {
    return rank.get();
  }

  public void setRank(int rank) {
    this.rank.set(rank);
  }

  @JsonIgnore
  public IntegerProperty getScoreProperty() {
    return score;
  }

  public int getScore() {
    return score.get();
  }

  public void setScore(int score) {
    this.score.set(score);
  }

  @JsonIgnore
  public IntegerProperty getSurvivalProperty() {
    return survival;
  }

  public int getSurvival() {
    return survival.get();
  }

  public void setSurvival(int survival) {
    this.survival.set(survival);
  }

  @JsonIgnore
  public IntegerProperty getLastSurvivorBonusProperty() {
    return lastSurvivorBonus;
  }

  public int getLastSurvivorBonus() {
    return lastSurvivorBonus.get();
  }

  public void setLastSurvivorBonus(int lastSurvivorBonus) {
    this.lastSurvivorBonus.set(lastSurvivorBonus);
  }

  @JsonIgnore
  public IntegerProperty getBulletDamageProperty() {
    return bulletDamage;
  }

  public int getBulletDamage() {
    return bulletDamage.get();
  }

  public void setBulletDamage(int bulletDamage) {
    this.bulletDamage.set(bulletDamage);
  }

  @JsonIgnore
  public IntegerProperty getBulletDamageBonusProperty() {
    return bulletDamageBonus;
  }

  public int getBulletDamageBonus() {
    return bulletDamageBonus.get();
  }

  public void setBulletDamageBonus(int bulletDamageBonus) {
    this.bulletDamageBonus.set(bulletDamageBonus);
  }

  @JsonIgnore
  public IntegerProperty getRamDamageProperty() {
    return ramDamage;
  }

  public int getRamDamage() {
    return ramDamage.get();
  }

  public void setRamDamage(int ramDamage) {
    this.ramDamage.set(ramDamage);
  }

  @JsonIgnore
  public IntegerProperty getRamDamageBonusProperty() {
    return ramDamageBonus;
  }

  public int getRamDamageBonus() {
    return ramDamageBonus.get();
  }

  public void setRamDamageBonus(int ramDamageBonus) {
    this.ramDamageBonus.set(ramDamageBonus);
  }

  @JsonIgnore
  public IntegerProperty getFirstsProperty() {
    return firsts;
  }

  public int getFirsts() {
    return firsts.get();
  }

  public void setFirsts(int firsts) {
    this.firsts.set(firsts);
  }

  @JsonIgnore
  public IntegerProperty getSecondsProperty() {
    return seconds;
  }

  public int getSeconds() {
    return seconds.get();
  }

  public void setSeconds(int seconds) {
    this.seconds.set(seconds);
  }

  @JsonIgnore
  public IntegerProperty getThirdsProperty() {
    return thirds;
  }

  public int getThirds() {
    return thirds.get();
  }

  public void setThirds(int thirds) {
    this.thirds.set(thirds);
  }
}

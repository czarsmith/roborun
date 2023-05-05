package smi.roborun;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

public class RobocodeConfig {
  private File robocodeFile;
  private File windowFile;
  private Properties robocodeProps;
  private Properties windowProps;

  public RobocodeConfig(File robocodeDir) {
    this.robocodeFile = new File(robocodeDir, "config/robocode.properties");
    this.windowFile = new File(robocodeDir, "config/window.properties");
    robocodeProps = new Properties();
    windowProps = new Properties();
    try (InputStream is = new FileInputStream(robocodeFile)) {
      robocodeProps.load(is);
    } catch (IOException e) {
      e.printStackTrace();
    }
    try (InputStream is = new FileInputStream(windowFile)) {
      windowProps.load(is);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void setTps(int tps) {
    robocodeProps.setProperty("robocode.options.battle.desiredTPS", Objects.toString(tps));
  }

  public void setVisibleGround(boolean visible) {
    robocodeProps.setProperty("robocode.options.view.ground", Objects.toString(visible));
  }

  public void setVisibleScanArcs(boolean visible) {
    robocodeProps.setProperty("robocode.options.view.scanArcs", Objects.toString(visible));
  }
  
  public void setWindowSize(int x, int y, int width, int height) {
    windowProps.setProperty("net.sf.robocode.ui.dialog.RobocodeFrame", StringUtils.join(List.of(x, y, width, height),","));
  }

  public void setShowResults(boolean show) {
    robocodeProps.setProperty("robocode.options.common.showResults", Objects.toString(show));
  }

  public void apply() {
    try (OutputStream os = new FileOutputStream(robocodeFile)) {
      robocodeProps.store(os, null);
    } catch (IOException e) {
      e.printStackTrace();
    }
    try (OutputStream os = new FileOutputStream(windowFile)) {
      windowProps.store(os, null);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}

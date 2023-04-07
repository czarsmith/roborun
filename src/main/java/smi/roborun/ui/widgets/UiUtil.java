package smi.roborun.ui.widgets;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.util.Callback;

public class UiUtil {
  private UiUtil() {}

  public static Pane hspace() {
    Pane ui = new Pane();
    HBox.setHgrow(ui, Priority.SOMETIMES);
    return ui;
  }

  public static Pane hspace(int width) {
    Pane ui = new Pane();
    ui.setMinWidth(width);
    ui.setMaxWidth(width);
    ui.setPrefWidth(width);
    return ui;
  }

  public static <T, F> TableColumn<T, F> tableCol(String title,
      Callback<CellDataFeatures<T, F>, ObservableValue<F>> callback) {
    TableColumn<T, F> col = new TableColumn<>(title);
    col.setCellValueFactory(callback);
    return col;
  }

  public static void error(String message) {
    new Alert(AlertType.ERROR, message).showAndWait();
  }

  public static String millisToMS(long millis, boolean down) {
    long minutes = millis / 60000;
    double seconds = millis % 60000 / 1000d;
    return String.format("%02d:%02d", minutes, (long)(down ? Math.ceil(seconds) : Math.floor(seconds)));
  }

  public static String millisToHMS(long millis) {
    long hours = millis / 3600000;
    long minutes = millis % 3600000 / 60000;
    long seconds = millis % 60000 / 1000;
    return String.format("%02d:%02d:%02d", hours, minutes, seconds);
  }

  public static long hmsToMillis(String hms) {
    Pattern p = Pattern.compile("(\\d\\d):(\\d\\d):(\\d\\d)");
    Matcher m = p.matcher(hms);
    long millis = 0;
    if (m.matches()) {
      millis = Integer.parseInt(m.group(1)) * 3600000
        + Integer.parseInt(m.group(2)) * 60000
        + Integer.parseInt(m.group(3)) * 1000;
    }
    return millis;
  }
}

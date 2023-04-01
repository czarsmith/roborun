package smi.roborun.ui.widgets;

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
}

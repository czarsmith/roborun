package smi.roborun.ui.widgets;

import javafx.beans.value.ObservableValue;
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
    HBox.setHgrow(ui, Priority.ALWAYS);
    return ui;
  }

  public static <T, F> TableColumn<T, F> tableCol(String title,
      Callback<CellDataFeatures<T, F>, ObservableValue<F>> callback) {
    TableColumn<T, F> col = new TableColumn<>(title);
    col.setCellValueFactory(callback);
    return col;
  }
}

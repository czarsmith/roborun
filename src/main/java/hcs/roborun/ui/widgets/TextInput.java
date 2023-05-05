package hcs.roborun.ui.widgets;

import java.util.regex.Pattern;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;

public class TextInput extends TextField implements ChangeListener<String> {
  private static final String INVALID_CLS = "invalid";

  private Pattern pattern;
  private ChangeListener<String> otherListener;

  public TextInput(String value, String pattern, ChangeListener<String> otherListener) {
    super(value);
    this.pattern = Pattern.compile(pattern);
    this.otherListener = otherListener;
    textProperty().addListener(this);
  }

  @Override
  public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
    boolean valid = isValid();
    if (!valid && !getStyleClass().contains(INVALID_CLS)) {
      getStyleClass().add(INVALID_CLS);
    } else if (valid && getStyleClass().contains(INVALID_CLS)) {
      getStyleClass().remove(INVALID_CLS);
    }
    if (valid) {
      otherListener.changed(observable, oldValue, newValue);
    }
  }

  public boolean isValid() {
    return pattern.matcher(getText()).matches();
  }
}

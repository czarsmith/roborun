package hcs.roborun.ui;

import javafx.beans.property.StringProperty;

public interface TitledNode {
  StringProperty getTitleProperty();
  String getTitle();
}

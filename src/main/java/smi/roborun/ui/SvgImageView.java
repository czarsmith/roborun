package smi.roborun.ui;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.ImageView;

public class SvgImageView extends ImageView {
  public SvgImageView(String uri) {
    super(SwingFXUtils.toFXImage(new SvgImage(uri, 24, 24).getImage(), null));
  }
}

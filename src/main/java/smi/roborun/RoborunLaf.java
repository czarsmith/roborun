package smi.roborun;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.plaf.FontUIResource;

import org.apache.commons.lang3.StringUtils;

public class RoborunLaf {
  public static final void configureLaf() throws Exception {
    for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
      if ("Nimbus".equals(info.getName())) {
        UIManager.setLookAndFeel(info.getClassName());
        break;
      }
    }

    FontUIResource font = new FontUIResource("Arial", Font.PLAIN, 24);
    UIManager.put("control", Color.BLACK);
    UIManager.put("text", Color.WHITE);
    UIManager.put("textHighlight", Color.WHITE);
    UIManager.put("nimbusBase", Color.BLACK);
    UIManager.put("nimbusFocus", Color.WHITE);
    UIManager.put("nimbusBlueGrey", Color.BLUE);
    List<Object> fontKeys = new ArrayList<>();
    for (Map.Entry<Object, Object> entry : UIManager.getLookAndFeelDefaults().entrySet()) {
      Object key = entry.getKey();
      if (StringUtils.containsIgnoreCase(Objects.toString(key), "font")) {
        fontKeys.add(key);
      }
    }
//    fontKeys.forEach(key -> UIManager.put(key, font));
  }
}

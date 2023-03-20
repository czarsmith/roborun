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

    Color black = new Color(25, 25, 25);
    Color gray = new Color(50, 50, 50);

    FontUIResource font = new FontUIResource("Arial", Font.PLAIN, 24);
    UIManager.put("control", black);
    UIManager.put("text", Color.WHITE);
    UIManager.put("textHighlight", Color.WHITE);
    UIManager.put("ToolTip.background", black);
    UIManager.put("ToolTip.foreground", Color.WHITE);
    UIManager.put("ToolTip.font", new Font("Arial", Font.BOLD, 24));
    UIManager.put("nimbusBase", black);
    UIManager.put("nimbusFocus", Color.WHITE);
    UIManager.put("nimbusBlueGrey", gray);
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

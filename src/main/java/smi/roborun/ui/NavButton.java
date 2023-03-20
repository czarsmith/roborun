package smi.roborun.ui;

import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public class NavButton extends JButton {
  public NavButton(String tooltip, String uri, ActionListener al) {
    setToolTipText(tooltip);
    addActionListener(al);
    setFont(new Font("Arial", Font.PLAIN, 24));
    setMargin(new Insets(16, 16, 16, 16));
    setIcon(new ImageIcon(new SvgImage(uri, 32, 32).getImage()));
  }
}

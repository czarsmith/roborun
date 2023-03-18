package smi.roborun.ui;

import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionListener;

import javax.swing.JButton;

public class NavButton extends JButton {
  public NavButton(String text, ActionListener al) {
    super(text);
    this.addActionListener(al);
    this.setFont(new Font("Arial", Font.PLAIN, 24));
    this.setMargin(new Insets(20, 20, 20, 20));
  }
}

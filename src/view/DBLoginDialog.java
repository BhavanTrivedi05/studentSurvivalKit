package view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class DBLoginDialog extends JDialog {

  private JTextField userField;
  private JPasswordField passField;
  private JTextField urlField;
  private boolean confirmed = false;

  public DBLoginDialog() {
    setTitle("StudentSurvivalKit — Connect to Database");
    setModal(true);
    setSize(420, 320);
    setLocationRelativeTo(null);
    setResizable(false);
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);

    setContentPane(buildPanel());
  }

  private JPanel buildPanel() {
    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    panel.setBackground(Color.WHITE);
    panel.setBorder(new EmptyBorder(32, 40, 32, 40));

    // Title
    JLabel title = new JLabel("Database Login");
    title.setFont(new Font("SansSerif", Font.BOLD, 20));
    title.setForeground(new Color(30, 42, 58));
    title.setAlignmentX(Component.CENTER_ALIGNMENT);

    JLabel subtitle = new JLabel("Enter your MySQL credentials to continue");
    subtitle.setFont(new Font("SansSerif", Font.PLAIN, 12));
    subtitle.setForeground(new Color(120, 130, 150));
    subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

    // URL field
    urlField = new JTextField("jdbc:mysql://localhost:3306/studentsurvivalkit");
    urlField.setFont(new Font("SansSerif", Font.PLAIN, 13));
    urlField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
    urlField.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(new Color(210, 215, 225), 1, true),
        BorderFactory.createEmptyBorder(4, 10, 4, 10)
    ));

    // Username field
    userField = new JTextField("root");
    userField.setFont(new Font("SansSerif", Font.PLAIN, 13));
    userField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
    userField.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(new Color(210, 215, 225), 1, true),
        BorderFactory.createEmptyBorder(4, 10, 4, 10)
    ));

    // Password field
    passField = new JPasswordField();
    passField.setFont(new Font("SansSerif", Font.PLAIN, 13));
    passField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
    passField.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(new Color(210, 215, 225), 1, true),
        BorderFactory.createEmptyBorder(4, 10, 4, 10)
    ));

    // Press Enter on password to connect
    passField.addActionListener(e -> onConnect());

    // Connect button
    JButton connectBtn = new JButton("Connect");
    connectBtn.setBackground(new Color(37, 99, 235));
    connectBtn.setForeground(Color.WHITE);
    connectBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
    connectBtn.setFocusPainted(false);
    connectBtn.setBorderPainted(false);
    connectBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    connectBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
    connectBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
    connectBtn.addActionListener(e -> onConnect());

    // Layout
    panel.add(title);
    panel.add(Box.createVerticalStrut(4));
    panel.add(subtitle);
    panel.add(Box.createVerticalStrut(24));
    panel.add(makeLabel("Database URL"));
    panel.add(Box.createVerticalStrut(4));
    panel.add(urlField);
    panel.add(Box.createVerticalStrut(12));
    panel.add(makeLabel("Username"));
    panel.add(Box.createVerticalStrut(4));
    panel.add(userField);
    panel.add(Box.createVerticalStrut(12));
    panel.add(makeLabel("Password"));
    panel.add(Box.createVerticalStrut(4));
    panel.add(passField);
    panel.add(Box.createVerticalStrut(20));
    panel.add(connectBtn);

    return panel;
  }

  private JLabel makeLabel(String text) {
    JLabel lbl = new JLabel(text);
    lbl.setFont(new Font("SansSerif", Font.PLAIN, 12));
    lbl.setForeground(new Color(80, 90, 110));
    lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
    return lbl;
  }

  private void onConnect() {
    if (getPassword().isEmpty()) {
      JOptionPane.showMessageDialog(this,
          "Password cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
      return;
    }
    confirmed = true;
    dispose();
  }

  // ── Getters ──────────────────────────────────────────
  public boolean isConfirmed()  { return confirmed; }
  public String getUrl()        { return urlField.getText().trim(); }
  public String getUsername()   { return userField.getText().trim(); }
  public String getPassword()   { return new String(passField.getPassword()); }
}
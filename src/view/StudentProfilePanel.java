package view;

import controller.StudentController;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class StudentProfilePanel extends JFrame {

  private StudentController controller;

  private JTextField emailField       = new JTextField();
  private JTextField firstNameField   = new JTextField();
  private JTextField lastNameField    = new JTextField();
  private JTextField nationalityField = new JTextField();
  private JTextField countryField     = new JTextField();
  private JTextField programField     = new JTextField();
  private JTextField visaField        = new JTextField();
  private JTextField gradYearField    = new JTextField();

  public StudentProfilePanel(StudentController controller) {
    this.controller = controller;
    setTitle("StudentSurvivalKit — Create Profile");
    setSize(480, 580);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setContentPane(buildPanel());
  }

  private JPanel buildPanel() {
    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    panel.setBackground(Color.WHITE);
    panel.setBorder(new EmptyBorder(32, 40, 32, 40));

    JLabel title = new JLabel("Create Your Profile");
    title.setFont(new Font("SansSerif", Font.BOLD, 22));
    title.setForeground(new Color(30, 42, 58));
    title.setAlignmentX(Component.CENTER_ALIGNMENT);

    JLabel subtitle = new JLabel("Set up your StudentSurvivalKit account");
    subtitle.setFont(new Font("SansSerif", Font.PLAIN, 13));
    subtitle.setForeground(new Color(120, 130, 150));
    subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

    panel.add(title);
    panel.add(Box.createVerticalStrut(4));
    panel.add(subtitle);
    panel.add(Box.createVerticalStrut(24));

    panel.add(makeRow("Email",               emailField));
    panel.add(makeRow("First Name",          firstNameField));
    panel.add(makeRow("Last Name",           lastNameField));
    panel.add(makeRow("Nationality",         nationalityField));
    panel.add(makeRow("Home Country",        countryField));
    panel.add(makeRow("Program",             programField));
    panel.add(makeRow("Visa Type (e.g. F-1)",visaField));
    panel.add(makeRow("Graduation Year",     gradYearField));
    panel.add(Box.createVerticalStrut(20));

    JButton saveBtn = new JButton("Save & Continue");
    saveBtn.setBackground(new Color(37, 99, 235));
    saveBtn.setForeground(Color.WHITE);
    saveBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
    saveBtn.setFocusPainted(false);
    saveBtn.setBorderPainted(false);
    saveBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
    saveBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    saveBtn.addActionListener(e -> onSave());
    panel.add(saveBtn);

    return panel;
  }

  private JPanel makeRow(String label, JTextField field) {
    JPanel row = new JPanel();
    row.setLayout(new BoxLayout(row, BoxLayout.Y_AXIS));
    row.setOpaque(false);
    row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

    JLabel lbl = new JLabel(label);
    lbl.setFont(new Font("SansSerif", Font.PLAIN, 12));
    lbl.setForeground(new Color(80, 90, 110));

    field.setFont(new Font("SansSerif", Font.PLAIN, 13));
    field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
    field.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(new Color(210, 215, 225), 1, true),
        BorderFactory.createEmptyBorder(4, 10, 4, 10)
    ));

    row.add(lbl);
    row.add(Box.createVerticalStrut(3));
    row.add(field);
    row.add(Box.createVerticalStrut(10));
    return row;
  }

  private void onSave() {
    try {
      int gradYear = Integer.parseInt(gradYearField.getText().trim());
      boolean success = controller.saveProfile(
          emailField.getText().trim(),
          firstNameField.getText().trim(),
          lastNameField.getText().trim(),
          nationalityField.getText().trim(),
          countryField.getText().trim(),
          programField.getText().trim(),
          visaField.getText().trim(),
          gradYear
      );
      if (success) {
        dispose();
        int id = controller.getStudent().getStudentId();
        new MainDashboard(id, firstNameField.getText().trim()).setVisible(true);
      } else {
        JOptionPane.showMessageDialog(this,
            "Could not save profile. Check all fields.",
            "Error", JOptionPane.ERROR_MESSAGE);
      }
    } catch (NumberFormatException e) {
      JOptionPane.showMessageDialog(this,
          "Graduation year must be a number (e.g. 2027).",
          "Invalid Input", JOptionPane.ERROR_MESSAGE);
    }
  }
}
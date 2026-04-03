package view;

import controller.StudentController;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class StudentProfilePanel extends JFrame {
  private final StudentController ctrl;
  private final JTextField    email    = new JTextField(20);
  private final JTextField    first    = new JTextField(20);
  private final JTextField    last     = new JTextField(20);
  private final JTextField    nat      = new JTextField(20);
  private final JTextField    country  = new JTextField(20);
  private final JTextField    program  = new JTextField(20);
  private final JTextField    visa     = new JTextField(20);
  private final JTextField    gradYear = new JTextField(20);
  private final JPasswordField pass    = new JPasswordField(20);
  private final JPasswordField confirm = new JPasswordField(20);

  public StudentProfilePanel(StudentController ctrl) {
    this.ctrl = ctrl;
    setTitle("StudentSurvivalKit — Create Account");
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setResizable(false);
    setContentPane(build());
    pack();
    setLocationRelativeTo(null);
  }

  private JPanel build() {
    JPanel outer = new JPanel(new BorderLayout());
    outer.setBackground(Color.WHITE);

    JPanel header = new JPanel();
    header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
    header.setBackground(new Color(30, 42, 58));
    header.setBorder(new EmptyBorder(28, 48, 22, 48));
    JLabel t = new JLabel("Create Your Account");
    t.setFont(new Font("SansSerif", Font.BOLD, 22));
    t.setForeground(Color.WHITE);
    t.setAlignmentX(Component.CENTER_ALIGNMENT);
    header.add(t);

    JPanel form = new JPanel(new GridBagLayout());
    form.setBackground(Color.WHITE);
    form.setBorder(new EmptyBorder(20, 48, 8, 48));

    GridBagConstraints lc = new GridBagConstraints();
    lc.gridx = 0; lc.anchor = GridBagConstraints.WEST;
    lc.insets = new Insets(8, 0, 2, 16);
    GridBagConstraints fc = new GridBagConstraints();
    fc.gridx = 1; fc.fill = GridBagConstraints.HORIZONTAL;
    fc.weightx = 1.0; fc.insets = new Insets(8, 0, 2, 0);

    String[] labels = {"Email","First Name","Last Name","Nationality","Home Country","Program","Visa Type (e.g. F-1)","Graduation Year","Password","Confirm Password"};
    JTextField[] fields = {email,first,last,nat,country,program,visa,gradYear,pass,confirm};

    for (int i = 0; i < labels.length; i++) {
      lc.gridy = fc.gridy = i;
      JLabel lbl = new JLabel(labels[i]);
      lbl.setFont(new Font("SansSerif", Font.PLAIN, 12));
      lbl.setForeground(new Color(80, 90, 110));
      form.add(lbl, lc);
      fields[i].setFont(new Font("SansSerif", Font.PLAIN, 13));
      fields[i].setPreferredSize(new Dimension(260, 34));
      fields[i].setBorder(BorderFactory.createCompoundBorder(
          BorderFactory.createLineBorder(new Color(210, 215, 225), 1, true),
          BorderFactory.createEmptyBorder(4, 10, 4, 10)));
      form.add(fields[i], fc);
    }

    JPanel btnPanel = new JPanel(new BorderLayout());
    btnPanel.setBackground(Color.WHITE);
    btnPanel.setBorder(new EmptyBorder(16, 48, 28, 48));
    JButton save = new JButton("Create Account");
    save.setBackground(new Color(37, 99, 235)); save.setForeground(Color.WHITE);
    save.setOpaque(true); save.setContentAreaFilled(true);
    save.setBorderPainted(false); save.setFocusPainted(false);
    save.setFont(new Font("SansSerif", Font.BOLD, 14));
    save.setPreferredSize(new Dimension(400, 42));
    save.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    save.addActionListener(e -> onSave());
    btnPanel.add(save, BorderLayout.CENTER);

    outer.add(header,   BorderLayout.NORTH);
    outer.add(form,     BorderLayout.CENTER);
    outer.add(btnPanel, BorderLayout.SOUTH);
    return outer;
  }

  private void onSave() {
    String p = new String(pass.getPassword()).trim();
    String c = new String(confirm.getPassword()).trim();
    if (p.isEmpty()) { err("Password is required."); return; }
    if (!p.equals(c)) { err("Passwords do not match."); return; }
    try {
      int yr = Integer.parseInt(gradYear.getText().trim());
      if (ctrl.saveProfile(email.getText().trim(), first.getText().trim(),
          last.getText().trim(), nat.getText().trim(), country.getText().trim(),
          program.getText().trim(), visa.getText().trim(), yr, p)) {
        dispose();
        new MainDashboard(ctrl.getStudent().getStudentId(), first.getText().trim()).setVisible(true);
      } else err("Could not create account. Email may already be registered.");
    } catch (NumberFormatException x) { err("Graduation year must be a number (e.g. 2027)."); }
  }

  private void err(String m) { JOptionPane.showMessageDialog(this, m, "Error", JOptionPane.ERROR_MESSAGE); }
}

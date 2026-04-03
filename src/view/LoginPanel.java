package view;

import controller.StudentController;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class LoginPanel extends JFrame {
  private final StudentController ctrl;
  private final JTextField    emailField = new JTextField(24);
  private final JPasswordField passField = new JPasswordField(24);
  private final JLabel        errorLabel = new JLabel(" ");

  public LoginPanel(StudentController ctrl) {
    this.ctrl = ctrl;
    setTitle("StudentSurvivalKit");
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setResizable(false);
    setContentPane(build());
    pack();
    setLocationRelativeTo(null);
  }

  private JPanel build() {
    JPanel outer = new JPanel(new BorderLayout());
    outer.setBackground(Color.WHITE);

    // ── Dark header ──────────────────────────────
    JPanel header = new JPanel();
    header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
    header.setBackground(new Color(30, 42, 58));
    header.setBorder(new EmptyBorder(36, 48, 30, 48));

    JLabel app = new JLabel("StudentSurvivalKit");
    app.setFont(new Font("SansSerif", Font.BOLD, 24));
    app.setForeground(Color.WHITE);
    app.setAlignmentX(Component.CENTER_ALIGNMENT);

    JLabel tag = new JLabel("Your all-in-one international student manager");
    tag.setFont(new Font("SansSerif", Font.PLAIN, 13));
    tag.setForeground(new Color(160, 175, 200));
    tag.setAlignmentX(Component.CENTER_ALIGNMENT);

    header.add(app);
    header.add(Box.createVerticalStrut(6));
    header.add(tag);

    // ── Form ────────────────────────────────────
    JPanel form = new JPanel(new GridBagLayout());
    form.setBackground(Color.WHITE);
    form.setBorder(new EmptyBorder(28, 48, 8, 48));

    GridBagConstraints lc = new GridBagConstraints();
    lc.gridx = 0; lc.anchor = GridBagConstraints.WEST;
    lc.insets = new Insets(10, 0, 2, 0);

    GridBagConstraints fc = new GridBagConstraints();
    fc.gridx = 0; fc.fill = GridBagConstraints.HORIZONTAL;
    fc.weightx = 1.0; fc.insets = new Insets(0, 0, 4, 0);

    styleField(emailField);
    styleField(passField);
    passField.addActionListener(e -> signIn());

    lc.gridy = 0; form.add(lbl("Email Address"), lc);
    fc.gridy = 1; form.add(emailField, fc);
    lc.gridy = 2; form.add(lbl("Password"), lc);
    fc.gridy = 3; form.add(passField, fc);

    errorLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
    errorLabel.setForeground(new Color(220, 38, 38));
    lc.gridy = 4; lc.insets = new Insets(6, 0, 0, 0);
    form.add(errorLabel, lc);

    // ── Buttons ─────────────────────────────────
    JPanel btnRow = new JPanel(new GridLayout(1, 2, 12, 0));
    btnRow.setBackground(Color.WHITE);
    btnRow.setBorder(new EmptyBorder(12, 48, 32, 48));

    JButton signIn  = styledBtn("Sign In",       new Color(37, 99, 235));
    JButton signUp  = styledBtn("Create Account", new Color(30, 42, 58));
    signIn.addActionListener(e -> signIn());
    signUp.addActionListener(e -> { dispose(); new StudentProfilePanel(ctrl).setVisible(true); });

    btnRow.add(signIn);
    btnRow.add(signUp);

    outer.add(header,  BorderLayout.NORTH);
    outer.add(form,    BorderLayout.CENTER);
    outer.add(btnRow,  BorderLayout.SOUTH);
    return outer;
  }

  private void signIn() {
    String email = emailField.getText().trim();
    String pass  = new String(passField.getPassword()).trim();
    if (email.isEmpty() || pass.isEmpty()) { errorLabel.setText("Email and password are required."); return; }
    int id = ctrl.signIn(email, pass);
    if (id > 0) {
      dispose();
      new MainDashboard(id, ctrl.getStudent().getFirstName()).setVisible(true);
    } else {
      errorLabel.setText("Invalid email or password.");
      passField.setText("");
    }
  }

  private JLabel lbl(String t) {
    JLabel l = new JLabel(t);
    l.setFont(new Font("SansSerif", Font.PLAIN, 12));
    l.setForeground(new Color(80, 90, 110));
    return l;
  }
  private void styleField(JTextField f) {
    f.setFont(new Font("SansSerif", Font.PLAIN, 13));
    f.setPreferredSize(new Dimension(320, 38));
    f.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(new Color(210, 215, 225), 1, true),
        BorderFactory.createEmptyBorder(4, 10, 4, 10)));
  }
  private JButton styledBtn(String l, Color bg) {
    JButton b = new JButton(l);
    b.setBackground(bg); b.setForeground(Color.WHITE);
    b.setOpaque(true); b.setContentAreaFilled(true);
    b.setBorderPainted(false); b.setFocusPainted(false);
    b.setFont(new Font("SansSerif", Font.BOLD, 13));
    b.setPreferredSize(new Dimension(140, 42));
    b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    return b;
  }
}
package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public abstract class BaseFormDialog extends JDialog {

  protected boolean confirmed = false;

  public BaseFormDialog(Frame parent, String title) {
    super(parent, title, true);
    setResizable(false);
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
  }

  protected void finishBuild(JPanel form) {
    form.setBackground(Color.WHITE);
    form.setBorder(new EmptyBorder(20, 28, 8, 28));

    JPanel header = new JPanel(new BorderLayout());
    header.setBackground(new Color(30, 42, 58));
    header.setBorder(new EmptyBorder(16, 28, 14, 28));
    JLabel t = new JLabel(getTitle());
    t.setFont(new Font("SansSerif", Font.BOLD, 17));
    t.setForeground(Color.WHITE);
    header.add(t);

    JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
    footer.setBackground(Color.WHITE);
    footer.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(220, 223, 235)));
    JButton cancel = new JButton("Cancel");
    cancel.addActionListener(e -> dispose());
    JButton save = new JButton("Save");
    save.setBackground(new Color(37, 99, 235));
    save.setForeground(Color.WHITE);
    save.setOpaque(true); save.setBorderPainted(false); save.setFocusPainted(false);
    save.addActionListener(e -> onSave());
    footer.add(cancel); footer.add(save);

    JPanel root = new JPanel(new BorderLayout());
    root.add(header, BorderLayout.NORTH);
    root.add(new JScrollPane(form), BorderLayout.CENTER);
    root.add(footer, BorderLayout.SOUTH);
    setContentPane(root);
    pack();
    setLocationRelativeTo(getParent());
  }

  protected abstract void onSave();
  public boolean isConfirmed() { return confirmed; }

  protected JTextField field(String val) {
    JTextField f = new JTextField(val, 22);
    f.setFont(new Font("SansSerif", Font.PLAIN, 13));
    f.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(new Color(210, 215, 225), 1, true),
        BorderFactory.createEmptyBorder(4, 8, 4, 8)));
    return f;
  }
  protected JTextField field() { return field(""); }

  protected JComboBox<String> combo(String... items) {
    JComboBox<String> c = new JComboBox<>(items);
    c.setFont(new Font("SansSerif", Font.PLAIN, 13));
    return c;
  }

  protected JTextArea area(String val) {
    JTextArea a = new JTextArea(val, 3, 22);
    a.setFont(new Font("SansSerif", Font.PLAIN, 13));
    a.setLineWrap(true); a.setWrapStyleWord(true);
    a.setBorder(BorderFactory.createLineBorder(new Color(210, 215, 225)));
    return a;
  }

  protected void row(JPanel p, GridBagConstraints lc, GridBagConstraints fc,
                     int i, String label, JComponent comp) {
    lc.gridy = fc.gridy = i;
    JLabel l = new JLabel(label);
    l.setFont(new Font("SansSerif", Font.PLAIN, 12));
    l.setForeground(new Color(80, 90, 110));
    p.add(l, lc); p.add(comp, fc);
  }

  protected void err(String msg) {
    JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
  }

  protected GridBagConstraints lc() {
    GridBagConstraints c = new GridBagConstraints();
    c.gridx = 0; c.anchor = GridBagConstraints.WEST;
    c.insets = new Insets(8, 0, 4, 16);
    return c;
  }
  protected GridBagConstraints fc() {
    GridBagConstraints c = new GridBagConstraints();
    c.gridx = 1; c.fill = GridBagConstraints.HORIZONTAL;
    c.weightx = 1.0; c.insets = new Insets(8, 0, 4, 0);
    return c;
  }
}
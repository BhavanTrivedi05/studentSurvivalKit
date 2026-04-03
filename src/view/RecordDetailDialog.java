package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.LinkedHashMap;

public class RecordDetailDialog extends JDialog {

  public RecordDetailDialog(Frame parent, String title, LinkedHashMap<String, String> fields) {
    super(parent, title, true);
    setResizable(false);

    JPanel header = new JPanel(new BorderLayout());
    header.setBackground(new Color(30, 42, 58));
    header.setBorder(new EmptyBorder(14, 24, 12, 24));
    JLabel t = new JLabel(title);
    t.setFont(new Font("SansSerif", Font.BOLD, 16));
    t.setForeground(Color.WHITE);
    header.add(t);

    JPanel body = new JPanel(new GridBagLayout());
    body.setBackground(Color.WHITE);
    body.setBorder(new EmptyBorder(16, 24, 8, 24));
    GridBagConstraints lc = new GridBagConstraints();
    lc.gridx = 0; lc.anchor = GridBagConstraints.WEST; lc.insets = new Insets(5, 0, 5, 20);
    GridBagConstraints vc = new GridBagConstraints();
    vc.gridx = 1; vc.fill = GridBagConstraints.HORIZONTAL; vc.weightx = 1.0; vc.insets = new Insets(5, 0, 5, 0);
    int i = 0;
    for (var e : fields.entrySet()) {
      lc.gridy = vc.gridy = i++;
      JLabel lbl = new JLabel(e.getKey());
      lbl.setFont(new Font("SansSerif", Font.BOLD, 12));
      lbl.setForeground(new Color(100, 110, 130));
      JLabel val = new JLabel(e.getValue() == null || e.getValue().isBlank() ? "—" : e.getValue());
      val.setFont(new Font("SansSerif", Font.PLAIN, 13));
      val.setForeground(new Color(30, 42, 58));
      body.add(lbl, lc); body.add(val, vc);
    }

    JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 14, 10));
    footer.setBackground(Color.WHITE);
    footer.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(220, 223, 235)));
    JButton close = new JButton("Close");
    close.setBackground(new Color(30, 42, 58)); close.setForeground(Color.WHITE);
    close.setOpaque(true); close.setBorderPainted(false); close.setFocusPainted(false);
    close.addActionListener(e -> dispose());
    footer.add(close);

    JPanel root = new JPanel(new BorderLayout());
    root.add(header, BorderLayout.NORTH);
    root.add(body, BorderLayout.CENTER);
    root.add(footer, BorderLayout.SOUTH);
    setContentPane(root);
    setMinimumSize(new Dimension(400, 180));
    pack();
    setLocationRelativeTo(parent);
  }
}
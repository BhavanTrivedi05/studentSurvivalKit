package view;

import controller.ContactController;
import model.Contact;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ContactPanel extends JPanel {
  private ContactController controller;
  private JTable table;
  private DefaultTableModel tableModel;

  public ContactPanel(int studentId) {
    controller = new ContactController(studentId);
    setLayout(new BorderLayout());
    setBackground(new Color(245, 246, 250));
    setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));
    add(buildHeader(), BorderLayout.NORTH);
    add(buildTable(), BorderLayout.CENTER);
    add(buildActionBar(), BorderLayout.SOUTH);
    loadData();
  }

  private void loadData() {
    tableModel.setRowCount(0);
    for (Contact c : controller.getAll()) {
      tableModel.addRow(new Object[]{
          c.getContactId(), c.getFullName(), c.getRole(),
          c.getCategory(), c.getEmail(), c.getPhone(),
          c.getOrganization(), c.getNotes()
      });
    }
  }

  private void showAddDialog() {
    JTextField nameF   = new JTextField();
    JTextField roleF   = new JTextField();
    JComboBox<String> catBox = new JComboBox<>(new String[]{"DSO Advisor","Academic Advisor","Professor","Embassy","Healthcare","Legal","Other"});
    JTextField emailF  = new JTextField();
    JTextField phoneF  = new JTextField();
    JTextField orgF    = new JTextField();
    JTextField notesF  = new JTextField();

    Object[] fields = {"Full Name:", nameF, "Role:", roleF, "Category:", catBox,
        "Email:", emailF, "Phone:", phoneF, "Organization:", orgF, "Notes:", notesF};
    int r = JOptionPane.showConfirmDialog(this, fields, "Add Contact", JOptionPane.OK_CANCEL_OPTION);
    if (r == JOptionPane.OK_OPTION) {
      boolean ok = controller.add(
          nameF.getText().trim(), roleF.getText().trim(),
          (String) catBox.getSelectedItem(),
          emailF.getText().trim(), phoneF.getText().trim(),
          orgF.getText().trim(), notesF.getText().trim()
      );
      if (ok) { loadData(); JOptionPane.showMessageDialog(this, "Contact added!"); }
      else JOptionPane.showMessageDialog(this, "Error adding contact.", "Error", JOptionPane.ERROR_MESSAGE);
    }
  }

  private void deleteSelected() {
    int row = table.getSelectedRow();
    if (row < 0) { JOptionPane.showMessageDialog(this, "Select a row first."); return; }
    int id = (int) tableModel.getValueAt(row, 0);
    if (JOptionPane.showConfirmDialog(this, "Delete this contact?", "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
      if (controller.delete(id)) loadData();
    }
  }

  private JPanel buildHeader() {
    JPanel h = new JPanel(new BorderLayout()); h.setOpaque(false);
    JLabel t = new JLabel("Contacts"); t.setFont(new Font("SansSerif", Font.BOLD, 22));
    h.add(t, BorderLayout.WEST);
    JButton add = new JButton("+ Add New"); styleBtn(add); add.addActionListener(e -> showAddDialog());
    h.add(add, BorderLayout.EAST); return h;
  }

  private JScrollPane buildTable() {
    tableModel = new DefaultTableModel(new String[]{"ID","Name","Role","Category","Email","Phone","Organization","Notes"}, 0) {
      public boolean isCellEditable(int r, int c) { return false; }
    };
    table = new JTable(tableModel); table.setRowHeight(36);
    table.setFont(new Font("SansSerif", Font.PLAIN, 13));
    table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
    JScrollPane s = new JScrollPane(table); s.setBorder(BorderFactory.createEmptyBorder(16,0,0,0)); return s;
  }

  private JPanel buildActionBar() {
    JPanel bar = new JPanel(new FlowLayout(FlowLayout.RIGHT)); bar.setOpaque(false);
    JButton del = new JButton("Delete Selected"); del.setForeground(new Color(200,40,40)); del.setFocusPainted(false);
    del.addActionListener(e -> deleteSelected()); bar.add(del); return bar;
  }

  private void styleBtn(JButton b) {
    b.setBackground(new Color(37,99,235)); b.setForeground(Color.WHITE);
    b.setFocusPainted(false); b.setBorderPainted(false);
    b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
  }
}

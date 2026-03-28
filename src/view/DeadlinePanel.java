package view;

import controller.DeadlineController;
import model.Deadline;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;

public class DeadlinePanel extends JPanel {
  private DeadlineController controller;
  private JTable table;
  private DefaultTableModel tableModel;

  public DeadlinePanel(int studentId) {
    controller = new DeadlineController(studentId);
    setLayout(new BorderLayout());
    setBackground(new Color(245, 246, 250));
    setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));
    add(buildHeader("Deadlines"), BorderLayout.NORTH);
    add(buildTable(new String[]{"ID", "Title", "Category", "Due Date", "Reminder", "Status", "Days Left", "Notes"}), BorderLayout.CENTER);
    add(buildActionBar(), BorderLayout.SOUTH);
    loadData();
  }

  private void loadData() {
    tableModel.setRowCount(0);
    for (Deadline d : controller.getAll()) {
      long daysLeft = java.time.temporal.ChronoUnit.DAYS.between(LocalDate.now(), d.getDueDate());
      tableModel.addRow(new Object[]{
          d.getDeadlineId(), d.getTitle(), d.getCategory(),
          d.getDueDate(), d.getReminderDate(), d.getStatus(), daysLeft, d.getNotes()
      });
    }
  }

  private void showAddDialog() {
    JTextField titleF    = new JTextField();
    JTextField categoryF = new JTextField();
    JTextField dueDateF  = new JTextField("YYYY-MM-DD");
    JTextField reminderF = new JTextField("YYYY-MM-DD");
    JComboBox<String> statusBox = new JComboBox<>(new String[]{"Pending", "Completed", "Missed"});
    JTextField notesF    = new JTextField();

    Object[] fields = {
        "Title:", titleF, "Category:", categoryF,
        "Due Date:", dueDateF, "Reminder Date:", reminderF,
        "Status:", statusBox, "Notes:", notesF
    };
    int r = JOptionPane.showConfirmDialog(this, fields, "Add Deadline", JOptionPane.OK_CANCEL_OPTION);
    if (r == JOptionPane.OK_OPTION) {
      boolean ok = controller.add(
          titleF.getText().trim(), categoryF.getText().trim(),
          LocalDate.parse(dueDateF.getText().trim()),
          reminderF.getText().trim().isEmpty() ? null : LocalDate.parse(reminderF.getText().trim()),
          (String) statusBox.getSelectedItem(),
          notesF.getText().trim()
      );
      if (ok) { loadData(); JOptionPane.showMessageDialog(this, "Deadline added!"); }
      else JOptionPane.showMessageDialog(this, "Error adding deadline.", "Error", JOptionPane.ERROR_MESSAGE);
    }
  }

  private void deleteSelected() {
    int row = table.getSelectedRow();
    if (row < 0) { JOptionPane.showMessageDialog(this, "Select a row first."); return; }
    int id = (int) tableModel.getValueAt(row, 0);
    int confirm = JOptionPane.showConfirmDialog(this, "Delete this deadline?", "Confirm", JOptionPane.YES_NO_OPTION);
    if (confirm == JOptionPane.YES_OPTION) {
      if (controller.delete(id)) { loadData(); }
    }
  }

  // ── Shared helpers ─────────────────────────────────────
  private JPanel buildHeader(String name) {
    JPanel h = new JPanel(new BorderLayout());
    h.setOpaque(false);
    JLabel title = new JLabel(name);
    title.setFont(new Font("SansSerif", Font.BOLD, 22));
    h.add(title, BorderLayout.WEST);
    JButton add = new JButton("+ Add New");
    styleBtn(add); add.addActionListener(e -> showAddDialog());
    h.add(add, BorderLayout.EAST);
    return h;
  }

  private JScrollPane buildTable(String[] cols) {
    tableModel = new DefaultTableModel(cols, 0) {
      public boolean isCellEditable(int r, int c) { return false; }
    };
    table = new JTable(tableModel);
    table.setRowHeight(36);
    table.setFont(new Font("SansSerif", Font.PLAIN, 13));
    table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
    table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    JScrollPane s = new JScrollPane(table);
    s.setBorder(BorderFactory.createEmptyBorder(16, 0, 0, 0));
    return s;
  }

  private JPanel buildActionBar() {
    JPanel bar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    bar.setOpaque(false);
    JButton del = new JButton("Delete Selected");
    del.setForeground(new Color(200, 40, 40));
    del.setFocusPainted(false);
    del.addActionListener(e -> deleteSelected());
    bar.add(del);
    return bar;
  }

  private void styleBtn(JButton b) {
    b.setBackground(new Color(37, 99, 235)); b.setForeground(Color.WHITE);
    b.setFocusPainted(false); b.setBorderPainted(false);
    b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
  }
}



// ============================================================
// FILE: src/view/HealthRecordPanel.java
// ============================================================
package view;

import controller.HealthRecordController;
import model.HealthRecord;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;

public class HealthRecordPanel extends JPanel {
  private HealthRecordController controller;
  private JTable table;
  private DefaultTableModel tableModel;

  public HealthRecordPanel(int studentId) {
    controller = new HealthRecordController(studentId);
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
    for (HealthRecord h : controller.getAll()) {
      tableModel.addRow(new Object[]{
          h.getHealthId(), h.getRecordType(), h.getProviderName(),
          h.getVisitDate(), h.getNextDueDate(), h.getInsuranceProvider(),
          h.getInsuranceId(), h.getDescription(), h.getNotes()
      });
    }
  }

  private void showAddDialog() {
    JComboBox<String> typeBox = new JComboBox<>(new String[]{"Doctor Visit","Vaccination","Dental","Vision","Insurance","Prescription","Other"});
    JTextField providerF  = new JTextField();
    JTextField visitF     = new JTextField("YYYY-MM-DD");
    JTextField nextDueF   = new JTextField("YYYY-MM-DD or leave blank");
    JTextField insProvF   = new JTextField();
    JTextField insIdF     = new JTextField();
    JTextField descF      = new JTextField();
    JTextField notesF     = new JTextField();

    Object[] fields = {"Type:", typeBox, "Provider:", providerF, "Visit Date:", visitF,
        "Next Due Date:", nextDueF, "Insurance Provider:", insProvF,
        "Insurance ID:", insIdF, "Description:", descF, "Notes:", notesF};
    int r = JOptionPane.showConfirmDialog(this, fields, "Add Health Record", JOptionPane.OK_CANCEL_OPTION);
    if (r == JOptionPane.OK_OPTION) {
      try {
        String nextDue = nextDueF.getText().trim();
        boolean ok = controller.add(
            (String) typeBox.getSelectedItem(), providerF.getText().trim(),
            LocalDate.parse(visitF.getText().trim()),
            nextDue.isEmpty() || nextDue.startsWith("YYYY") ? null : LocalDate.parse(nextDue),
            insProvF.getText().trim(), insIdF.getText().trim(),
            descF.getText().trim(), notesF.getText().trim()
        );
        if (ok) { loadData(); JOptionPane.showMessageDialog(this, "Health record added!"); }
      } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "Check date formats.", "Error", JOptionPane.ERROR_MESSAGE);
      }
    }
  }

  private void deleteSelected() {
    int row = table.getSelectedRow();
    if (row < 0) { JOptionPane.showMessageDialog(this, "Select a row first."); return; }
    int id = (int) tableModel.getValueAt(row, 0);
    if (JOptionPane.showConfirmDialog(this, "Delete this record?", "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
      if (controller.delete(id)) loadData();
    }
  }

  private JPanel buildHeader() {
    JPanel h = new JPanel(new BorderLayout()); h.setOpaque(false);
    JLabel t = new JLabel("Health Records"); t.setFont(new Font("SansSerif", Font.BOLD, 22));
    h.add(t, BorderLayout.WEST);
    JButton add = new JButton("+ Add New"); styleBtn(add); add.addActionListener(e -> showAddDialog());
    h.add(add, BorderLayout.EAST); return h;
  }

  private JScrollPane buildTable() {
    tableModel = new DefaultTableModel(new String[]{"ID","Type","Provider","Visit Date","Next Due","Insurance","Ins. ID","Description","Notes"}, 0) {
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


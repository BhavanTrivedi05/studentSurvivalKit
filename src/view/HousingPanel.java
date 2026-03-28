package view;

import controller.HousingController;
import model.Housing;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class HousingPanel extends JPanel {
  private HousingController controller;
  private JTable table;
  private DefaultTableModel tableModel;

  public HousingPanel(int studentId) {
    controller = new HousingController(studentId);
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
    for (Housing h : controller.getAll()) {
      long daysLeft = ChronoUnit.DAYS.between(LocalDate.now(), h.getLeaseEndDate());
      tableModel.addRow(new Object[]{
          h.getHousingId(), h.getAddress(), h.getLeaseStartDate(),
          h.getLeaseEndDate(), h.getLandlordName(), h.getLandlordContact(),
          h.getMonthlyRent(), daysLeft, h.getNotes()
      });
    }
  }

  private void showAddDialog() {
    JTextField addressF   = new JTextField();
    JTextField startF     = new JTextField("YYYY-MM-DD");
    JTextField endF       = new JTextField("YYYY-MM-DD");
    JTextField landlordF  = new JTextField();
    JTextField contactF   = new JTextField();
    JTextField rentF      = new JTextField();
    JTextField notesF     = new JTextField();

    Object[] fields = {"Address:", addressF, "Lease Start:", startF, "Lease End:", endF,
        "Landlord Name:", landlordF, "Landlord Contact:", contactF, "Monthly Rent:", rentF, "Notes:", notesF};
    int r = JOptionPane.showConfirmDialog(this, fields, "Add Housing", JOptionPane.OK_CANCEL_OPTION);
    if (r == JOptionPane.OK_OPTION) {
      try {
        boolean ok = controller.add(
            addressF.getText().trim(),
            LocalDate.parse(startF.getText().trim()),
            LocalDate.parse(endF.getText().trim()),
            landlordF.getText().trim(), contactF.getText().trim(),
            Double.parseDouble(rentF.getText().trim()), notesF.getText().trim()
        );
        if (ok) { loadData(); JOptionPane.showMessageDialog(this, "Housing added!"); }
      } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "Check inputs.", "Error", JOptionPane.ERROR_MESSAGE);
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
    JLabel t = new JLabel("Housing"); t.setFont(new Font("SansSerif", Font.BOLD, 22));
    h.add(t, BorderLayout.WEST);
    JButton add = new JButton("+ Add New"); styleBtn(add); add.addActionListener(e -> showAddDialog());
    h.add(add, BorderLayout.EAST); return h;
  }

  private JScrollPane buildTable() {
    tableModel = new DefaultTableModel(new String[]{"ID","Address","Lease Start","Lease End","Landlord","Contact","Rent","Days Left","Notes"}, 0) {
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
package view;

import controller.ExpenseController;
import model.Expense;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;

public class ExpensePanel extends JPanel {
  private ExpenseController controller;
  private JTable table;
  private DefaultTableModel tableModel;

  public ExpensePanel(int studentId) {
    controller = new ExpenseController(studentId);
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
    for (Expense e : controller.getAll()) {
      tableModel.addRow(new Object[]{
          e.getExpenseId(), e.getAmount(), e.getCategory(),
          e.getExpenseDate(), e.getDescription(), e.getCurrency()
      });
    }
  }

  private void showAddDialog() {
    JTextField amountF   = new JTextField();
    JComboBox<String> catBox = new JComboBox<>(new String[]{"Rent","Food","Transport","Tuition","Utilities","Health","Entertainment","Other"});
    JTextField dateF     = new JTextField("YYYY-MM-DD");
    JTextField descF     = new JTextField();
    JTextField currencyF = new JTextField("USD");

    Object[] fields = {"Amount:", amountF, "Category:", catBox, "Date:", dateF, "Description:", descF, "Currency:", currencyF};
    int r = JOptionPane.showConfirmDialog(this, fields, "Add Expense", JOptionPane.OK_CANCEL_OPTION);
    if (r == JOptionPane.OK_OPTION) {
      try {
        boolean ok = controller.add(
            Double.parseDouble(amountF.getText().trim()),
            (String) catBox.getSelectedItem(),
            LocalDate.parse(dateF.getText().trim()),
            descF.getText().trim(), currencyF.getText().trim()
        );
        if (ok) { loadData(); JOptionPane.showMessageDialog(this, "Expense added!"); }
      } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "Check amount format.", "Error", JOptionPane.ERROR_MESSAGE);
      }
    }
  }

  private void deleteSelected() {
    int row = table.getSelectedRow();
    if (row < 0) { JOptionPane.showMessageDialog(this, "Select a row first."); return; }
    int id = (int) tableModel.getValueAt(row, 0);
    if (JOptionPane.showConfirmDialog(this, "Delete this expense?", "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
      if (controller.delete(id)) loadData();
    }
  }

  private JPanel buildHeader() {
    JPanel h = new JPanel(new BorderLayout()); h.setOpaque(false);
    JLabel t = new JLabel("Expenses"); t.setFont(new Font("SansSerif", Font.BOLD, 22));
    h.add(t, BorderLayout.WEST);
    JButton add = new JButton("+ Add New"); styleBtn(add); add.addActionListener(e -> showAddDialog());
    h.add(add, BorderLayout.EAST); return h;
  }

  private JScrollPane buildTable() {
    tableModel = new DefaultTableModel(new String[]{"ID","Amount","Category","Date","Description","Currency"}, 0) {
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

package view;

import controller.JobApplicationController;
import model.JobApplication;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;

public class JobApplicationPanel extends JPanel {
  private JobApplicationController controller;
  private JTable table;
  private DefaultTableModel tableModel;

  public JobApplicationPanel(int studentId) {
    controller = new JobApplicationController(studentId);
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
    for (JobApplication j : controller.getAll()) {
      tableModel.addRow(new Object[]{
          j.getApplicationId(), j.getCompanyName(), j.getRole(),
          j.getLocation(), j.getAppliedDate(), j.getStatus(),
          j.getJobType(), j.isReferral(), j.getNotes()
      });
    }
  }

  private void showAddDialog() {
    JTextField companyF  = new JTextField();
    JTextField roleF     = new JTextField();
    JTextField locationF = new JTextField();
    JTextField dateF     = new JTextField("YYYY-MM-DD");
    JComboBox<String> statusBox  = new JComboBox<>(new String[]{"Applied","OA","Phone Screen","Interview","Offer","Rejected","Withdrawn"});
    JComboBox<String> typeBox    = new JComboBox<>(new String[]{"Internship","Co-op","Full-time","Part-time"});
    JCheckBox referralBox = new JCheckBox("Referred");
    JTextField notesF    = new JTextField();

    Object[] fields = {
        "Company:", companyF, "Role:", roleF, "Location:", locationF,
        "Applied Date:", dateF, "Status:", statusBox, "Job Type:", typeBox,
        referralBox, "Notes:", notesF
    };
    int r = JOptionPane.showConfirmDialog(this, fields, "Add Job Application", JOptionPane.OK_CANCEL_OPTION);
    if (r == JOptionPane.OK_OPTION) {
      boolean ok = controller.add(
          companyF.getText().trim(), roleF.getText().trim(),
          locationF.getText().trim(), LocalDate.parse(dateF.getText().trim()),
          (String) statusBox.getSelectedItem(), (String) typeBox.getSelectedItem(),
          referralBox.isSelected(), notesF.getText().trim()
      );
      if (ok) { loadData(); JOptionPane.showMessageDialog(this, "Application added!"); }
      else JOptionPane.showMessageDialog(this, "Error adding application.", "Error", JOptionPane.ERROR_MESSAGE);
    }
  }

  private void deleteSelected() {
    int row = table.getSelectedRow();
    if (row < 0) { JOptionPane.showMessageDialog(this, "Select a row first."); return; }
    int id = (int) tableModel.getValueAt(row, 0);
    if (JOptionPane.showConfirmDialog(this, "Delete this application?", "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
      if (controller.delete(id)) loadData();
    }
  }

  private JPanel buildHeader() {
    JPanel h = new JPanel(new BorderLayout()); h.setOpaque(false);
    JLabel t = new JLabel("Job Applications"); t.setFont(new Font("SansSerif", Font.BOLD, 22));
    h.add(t, BorderLayout.WEST);
    JButton add = new JButton("+ Add New"); styleBtn(add); add.addActionListener(e -> showAddDialog());
    h.add(add, BorderLayout.EAST); return h;
  }

  private JScrollPane buildTable() {
    tableModel = new DefaultTableModel(new String[]{"ID","Company","Role","Location","Applied","Status","Type","Referral","Notes"}, 0) {
      public boolean isCellEditable(int r, int c) { return false; }
    };
    table = new JTable(tableModel); table.setRowHeight(36);
    table.setFont(new Font("SansSerif", Font.PLAIN, 13));
    table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
    table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    JScrollPane s = new JScrollPane(table); s.setBorder(BorderFactory.createEmptyBorder(16,0,0,0));
    return s;
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

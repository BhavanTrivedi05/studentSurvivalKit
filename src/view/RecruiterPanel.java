package view;

import controller.JobApplicationController;
import controller.RecruiterController;
import model.JobApplication;
import model.Recruiter;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class RecruiterPanel extends JPanel {
  private RecruiterController controller;
  private JobApplicationController jobController;
  private JTable table;
  private DefaultTableModel tableModel;
  private int studentId;

  public RecruiterPanel(int studentId) {
    this.studentId  = studentId;
    controller      = new RecruiterController();
    jobController   = new JobApplicationController(studentId);
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
    for (Recruiter r : controller.getAll(studentId)) {
      tableModel.addRow(new Object[]{
          r.getRecruiterId(), r.getApplicationId(), r.getRecruiterName(),
          r.getRecruiterEmail(), r.getCompanyName(), r.getPhone(),
          r.getLinkedin(), r.getNotes()
      });
    }
  }

  private void showAddDialog() {
    // Build application dropdown
    List<JobApplication> jobs = jobController.getAll();
    if (jobs.isEmpty()) {
      JOptionPane.showMessageDialog(this, "Add a Job Application first before adding a Recruiter.");
      return;
    }
    String[] jobOptions = jobs.stream()
        .map(j -> j.getApplicationId() + " — " + j.getCompanyName() + " | " + j.getRole())
        .toArray(String[]::new);
    JComboBox<String> jobBox = new JComboBox<>(jobOptions);

    JTextField nameF    = new JTextField();
    JTextField emailF   = new JTextField();
    JTextField companyF = new JTextField();
    JTextField phoneF   = new JTextField();
    JTextField linkedinF= new JTextField();
    JTextField notesF   = new JTextField();

    Object[] fields = {"Job Application:", jobBox, "Recruiter Name:", nameF,
        "Recruiter Email:", emailF, "Company:", companyF,
        "Phone:", phoneF, "LinkedIn:", linkedinF, "Notes:", notesF};
    int r = JOptionPane.showConfirmDialog(this, fields, "Add Recruiter", JOptionPane.OK_CANCEL_OPTION);
    if (r == JOptionPane.OK_OPTION) {
      int appId = jobs.get(jobBox.getSelectedIndex()).getApplicationId();
      boolean ok = controller.add(
          appId, nameF.getText().trim(), emailF.getText().trim(),
          companyF.getText().trim(), phoneF.getText().trim(),
          linkedinF.getText().trim(), notesF.getText().trim()
      );
      if (ok) { loadData(); JOptionPane.showMessageDialog(this, "Recruiter added!"); }
      else JOptionPane.showMessageDialog(this, "Error adding recruiter.", "Error", JOptionPane.ERROR_MESSAGE);
    }
  }

  private void deleteSelected() {
    int row = table.getSelectedRow();
    if (row < 0) { JOptionPane.showMessageDialog(this, "Select a row first."); return; }
    int id = (int) tableModel.getValueAt(row, 0);
    if (JOptionPane.showConfirmDialog(this, "Delete this recruiter?", "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
      if (controller.delete(id)) loadData();
    }
  }

  private JPanel buildHeader() {
    JPanel h = new JPanel(new BorderLayout()); h.setOpaque(false);
    JLabel t = new JLabel("Recruiters"); t.setFont(new Font("SansSerif", Font.BOLD, 22));
    h.add(t, BorderLayout.WEST);
    JButton add = new JButton("+ Add New"); styleBtn(add); add.addActionListener(e -> showAddDialog());
    h.add(add, BorderLayout.EAST); return h;
  }

  private JScrollPane buildTable() {
    tableModel = new DefaultTableModel(new String[]{"ID","App ID","Name","Email","Company","Phone","LinkedIn","Notes"}, 0) {
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
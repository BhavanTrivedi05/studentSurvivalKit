package view;

import controller.CourseController;
import model.Course;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class CoursePanel extends JPanel {
  private CourseController controller;
  private JTable table;
  private DefaultTableModel tableModel;

  public CoursePanel(int studentId) {
    controller = new CourseController(studentId);
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
    for (Course c : controller.getAll()) {
      tableModel.addRow(new Object[]{
          c.getCourseId(), c.getCourseCode(), c.getCourseName(),
          c.getCredits(), c.getProfessor(), c.getSemester(),
          c.getStatus(), c.getGrade(), c.getNotes()
      });
    }
  }

  private void showAddDialog() {
    JTextField codeF      = new JTextField();
    JTextField nameF      = new JTextField();
    JTextField creditsF   = new JTextField();
    JTextField profF      = new JTextField();
    JTextField semesterF  = new JTextField();
    JComboBox<String> statusBox = new JComboBox<>(new String[]{"In Progress","Completed","Dropped"});
    JTextField gradeF     = new JTextField();
    JTextField notesF     = new JTextField();

    Object[] fields = {"Code:", codeF, "Course Name:", nameF, "Credits:", creditsF,
        "Professor:", profF, "Semester:", semesterF, "Status:", statusBox,
        "Grade:", gradeF, "Notes:", notesF};
    int r = JOptionPane.showConfirmDialog(this, fields, "Add Course", JOptionPane.OK_CANCEL_OPTION);
    if (r == JOptionPane.OK_OPTION) {
      try {
        boolean ok = controller.add(
            codeF.getText().trim(), nameF.getText().trim(),
            Integer.parseInt(creditsF.getText().trim()),
            profF.getText().trim(), semesterF.getText().trim(),
            (String) statusBox.getSelectedItem(),
            gradeF.getText().trim().isEmpty() ? null : gradeF.getText().trim(),
            notesF.getText().trim()
        );
        if (ok) { loadData(); JOptionPane.showMessageDialog(this, "Course added!"); }
      } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "Check credits field.", "Error", JOptionPane.ERROR_MESSAGE);
      }
    }
  }

  private void deleteSelected() {
    int row = table.getSelectedRow();
    if (row < 0) { JOptionPane.showMessageDialog(this, "Select a row first."); return; }
    int id = (int) tableModel.getValueAt(row, 0);
    if (JOptionPane.showConfirmDialog(this, "Delete this course?", "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
      if (controller.delete(id)) loadData();
    }
  }

  private JPanel buildHeader() {
    JPanel h = new JPanel(new BorderLayout()); h.setOpaque(false);
    JLabel t = new JLabel("Courses"); t.setFont(new Font("SansSerif", Font.BOLD, 22));
    h.add(t, BorderLayout.WEST);
    JButton add = new JButton("+ Add New"); styleBtn(add); add.addActionListener(e -> showAddDialog());
    h.add(add, BorderLayout.EAST); return h;
  }

  private JScrollPane buildTable() {
    tableModel = new DefaultTableModel(new String[]{"ID","Code","Name","Credits","Professor","Semester","Status","Grade","Notes"}, 0) {
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
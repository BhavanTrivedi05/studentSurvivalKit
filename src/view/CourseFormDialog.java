package view;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import model.Course;
import java.awt.*;

public class CourseFormDialog extends BaseFormDialog {

  private final JTextField codeF    = field();
  private final JTextField nameF    = field();
  private final JTextField creditsF = field();
  private final JTextField profF    = field();
  private final JTextField semF     = field();
  private final JComboBox<String> statusC = combo("In Progress", "Completed", "Dropped");
  private final JTextField gradeF   = field();
  private final JTextArea notesA   = area("");

  private String code, name, professor, semester, status, grade, notes;
  private int credits;

  public CourseFormDialog(Frame parent, Course c) {
    super(parent, c == null ? "Add Course" : "Edit Course");
    if (c != null) {
      codeF.setText(c.getCourseCode());
      nameF.setText(c.getCourseName());
      creditsF.setText(String.valueOf(c.getCredits()));
      profF.setText(c.getProfessor());
      semF.setText(c.getSemester());
      statusC.setSelectedItem(c.getStatus());
      gradeF.setText(c.getGrade() != null ? c.getGrade() : "");
      notesA.setText(c.getNotes() != null ? c.getNotes() : "");
    }
    JPanel p = new JPanel(new GridBagLayout());
    var lc = lc(); var fc = fc();
    row(p, lc, fc, 0, "Course Code *", codeF);
    row(p, lc, fc, 1, "Course Name *", nameF);
    row(p, lc, fc, 2, "Credits *",     creditsF);
    row(p, lc, fc, 3, "Professor",     profF);
    row(p, lc, fc, 4, "Semester",      semF);
    row(p, lc, fc, 5, "Status",        statusC);
    row(p, lc, fc, 6, "Grade",         gradeF);
    row(p, lc, fc, 7, "Notes",         notesA);
    finishBuild(p);
  }

  @Override
  protected void onSave() {
    code      = codeF.getText().trim();
    name      = nameF.getText().trim();
    professor = profF.getText().trim();
    semester  = semF.getText().trim();
    status    = (String) statusC.getSelectedItem();
    grade     = gradeF.getText().trim().isEmpty() ? null : gradeF.getText().trim();
    notes     = notesA.getText().trim();
    if (code.isEmpty()) { err("Course code is required."); return; }
    if (name.isEmpty()) { err("Course name is required."); return; }
    try { credits = Integer.parseInt(creditsF.getText().trim()); }
    catch (Exception e) { err("Credits must be a number."); return; }
    if (credits < 1 || credits > 6) { err("Credits must be between 1 and 6."); return; }
    confirmed = true; dispose();
  }

  public String getCode()      { return code; }
  public String getName()      { return name; }
  public int    getCredits()   { return credits; }
  public String getProfessor() { return professor; }
  public String getSemester()  { return semester; }
  public String getStatus()    { return status; }
  public String getGrade()     { return grade; }
  public String getNotes()     { return notes; }
}
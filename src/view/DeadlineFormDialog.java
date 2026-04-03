package view;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import model.Deadline;
import java.awt.*;
import java.time.LocalDate;

public class DeadlineFormDialog extends BaseFormDialog {

  private final JTextField titleF    = field();
  private final JTextField categoryF = field();
  private final JTextField dueF      = field("YYYY-MM-DD");
  private final JTextField remF      = field("YYYY-MM-DD or blank");
  private final JComboBox<String> statusC = combo("Pending", "Completed", "Missed");
  private final JTextArea notesA     = area("");

  private String title2, category, status, notes;
  private LocalDate dueDate, reminderDate;

  public DeadlineFormDialog(Frame parent, Deadline d) {
    super(parent, d == null ? "Add Deadline" : "Edit Deadline");
    if (d != null) {
      titleF.setText(d.getTitle());
      categoryF.setText(d.getCategory());
      dueF.setText(d.getDueDate().toString());
      remF.setText(d.getReminderDate() != null ? d.getReminderDate().toString() : "");
      statusC.setSelectedItem(d.getStatus());
      notesA.setText(d.getNotes() != null ? d.getNotes() : "");
    }
    JPanel p = new JPanel(new GridBagLayout());
    var lc = lc(); var fc = fc();
    row(p, lc, fc, 0, "Title *",    titleF);
    row(p, lc, fc, 1, "Category *", categoryF);
    row(p, lc, fc, 2, "Due Date *", dueF);
    row(p, lc, fc, 3, "Reminder",   remF);
    row(p, lc, fc, 4, "Status",     statusC);
    row(p, lc, fc, 5, "Notes",      notesA);
    finishBuild(p);
  }

  @Override
  protected void onSave() {
    title2   = titleF.getText().trim();
    category = categoryF.getText().trim();
    status   = (String) statusC.getSelectedItem();
    notes    = notesA.getText().trim();
    if (title2.isEmpty())   { err("Title is required."); return; }
    if (category.isEmpty()) { err("Category is required."); return; }
    try { dueDate = LocalDate.parse(dueF.getText().trim()); }
    catch (Exception e) { err("Use YYYY-MM-DD for due date."); return; }
    String r = remF.getText().trim();
    if (!r.isEmpty() && !r.startsWith("Y")) {
      try { reminderDate = LocalDate.parse(r); }
      catch (Exception e) { err("Use YYYY-MM-DD for reminder."); return; }
    }
    confirmed = true; dispose();
  }

  public String    getTitle()        { return title2; }
  public String    getCategory()     { return category; }
  public LocalDate getDueDate()      { return dueDate; }
  public LocalDate getReminderDate() { return reminderDate; }
  public String    getStatus()       { return status; }
  public String    getNotes()        { return notes; }
}
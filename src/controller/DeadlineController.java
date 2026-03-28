package controller;

import model.Deadline;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DeadlineController {
  private int studentId;

  public DeadlineController(int studentId) {
    this.studentId = studentId;
  }

  public List<Deadline> getAll() {
    try { return Deadline.getAll(studentId); }
    catch (SQLException e) { e.printStackTrace(); return new ArrayList<>(); }
  }

  public boolean add(String title, String category, LocalDate dueDate,
                     LocalDate reminderDate, String status, String notes) {
    if (title.isEmpty() || category.isEmpty()) return false;
    try {
      new Deadline(studentId, title, category, dueDate, reminderDate, status, notes).save();
      return true;
    } catch (SQLException e) { e.printStackTrace(); return false; }
  }

  public boolean delete(int deadlineId) {
    try {
      Deadline d = Deadline.getById(deadlineId);
      if (d != null) { d.delete(); return true; }
      return false;
    } catch (SQLException e) { e.printStackTrace(); return false; }
  }
}


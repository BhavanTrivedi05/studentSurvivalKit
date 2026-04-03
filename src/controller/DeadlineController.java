package controller;

import db.DBConnection;
import model.Deadline;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DeadlineController {
  private final int studentId;
  public DeadlineController(int studentId) { this.studentId = studentId; }

  public List<Deadline> getAll() {
    try { return Deadline.getAll(studentId); }
    catch (SQLException e) { e.printStackTrace(); return new ArrayList<>(); }
  }
  public boolean add(String title, String category, LocalDate dueDate, LocalDate reminderDate, String status, String notes) {
    if (title.isEmpty() || category.isEmpty()) return false;
    try { new Deadline(studentId,title,category,dueDate,reminderDate,status,notes).save(); return true; }
    catch (SQLException e) { e.printStackTrace(); return false; }
  }
  public boolean update(int id, String title, String category, LocalDate dueDate, LocalDate reminderDate, String status, String notes) {
    try {
      PreparedStatement ps = DBConnection.getConnection().prepareStatement(
          "UPDATE Deadline SET title=?,category=?,due_date=?,reminder_date=?,status=?,notes=? WHERE deadline_id=?");
      ps.setString(1,title); ps.setString(2,category); ps.setDate(3,Date.valueOf(dueDate));
      if (reminderDate!=null) ps.setDate(4,Date.valueOf(reminderDate)); else ps.setNull(4,Types.DATE);
      ps.setString(5,status); ps.setString(6,notes); ps.setInt(7,id);
      ps.executeUpdate(); return true;
    } catch (SQLException e) { e.printStackTrace(); return false; }
  }
  public boolean delete(int id)     { return deleteById(id); }
  public boolean deleteById(int id) {
    try {
      PreparedStatement ps = DBConnection.getConnection().prepareStatement("DELETE FROM Deadline WHERE deadline_id=?");
      ps.setInt(1,id); ps.executeUpdate(); return true;
    } catch (SQLException e) { e.printStackTrace(); return false; }
  }
}
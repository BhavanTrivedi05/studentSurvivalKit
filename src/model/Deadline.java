package model;

import db.DBConnection;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Deadline {
  private int deadlineId;
  private int studentId;
  private String title;
  private String category;
  private LocalDate dueDate;
  private LocalDate reminderDate;
  private String status;
  private String notes;

  public Deadline(int studentId, String title, String category,
                  LocalDate dueDate, LocalDate reminderDate,
                  String status, String notes) {
    this.studentId    = studentId;
    this.title        = title;
    this.category     = category;
    this.dueDate      = dueDate;
    this.reminderDate = reminderDate;
    this.status       = status;
    this.notes        = notes;
  }

  public void save() throws SQLException {
    String sql = "INSERT INTO Deadline (student_id,title,category,due_date,reminder_date,status,notes) VALUES (?,?,?,?,?,?,?)";
    PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
    ps.setInt(1, studentId);
    ps.setString(2, title);
    ps.setString(3, category);
    ps.setDate(4, Date.valueOf(dueDate));
    ps.setDate(5, reminderDate != null ? Date.valueOf(reminderDate) : null);
    ps.setString(6, status);
    ps.setString(7, notes);
    ps.executeUpdate();
  }

  public static List<Deadline> getAll(int studentId) throws SQLException {
    List<Deadline> list = new ArrayList<>();
    String sql = "SELECT * FROM Deadline WHERE student_id=? ORDER BY due_date ASC";
    PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
    ps.setInt(1, studentId);
    ResultSet rs = ps.executeQuery();
    while (rs.next()) {
      Deadline d = new Deadline(
          rs.getInt("student_id"), rs.getString("title"),
          rs.getString("category"), rs.getDate("due_date").toLocalDate(),
          rs.getDate("reminder_date") != null ? rs.getDate("reminder_date").toLocalDate() : null,
          rs.getString("status"), rs.getString("notes")
      );
      d.deadlineId = rs.getInt("deadline_id");
      list.add(d);
    }
    return list;
  }

  public void delete() throws SQLException {
    String sql = "DELETE FROM Deadline WHERE deadline_id=?";
    PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
    ps.setInt(1, deadlineId);
    ps.executeUpdate();
  }

  public static Deadline getById(int id) throws SQLException {
    String sql = "SELECT * FROM Deadline WHERE deadline_id=?";
    PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
    ps.setInt(1, id);
    ResultSet rs = ps.executeQuery();
    if (rs.next()) {
      Deadline d = new Deadline(
          rs.getInt("student_id"), rs.getString("title"),
          rs.getString("category"), rs.getDate("due_date").toLocalDate(),
          rs.getDate("reminder_date") != null ? rs.getDate("reminder_date").toLocalDate() : null,
          rs.getString("status"), rs.getString("notes")
      );
      d.deadlineId = rs.getInt("deadline_id");
      return d;
    }
    return null;
  }

  public int getDeadlineId()        { return deadlineId; }
  public int getStudentId()         { return studentId; }
  public String getTitle()          { return title; }
  public String getCategory()       { return category; }
  public LocalDate getDueDate()     { return dueDate; }
  public LocalDate getReminderDate(){ return reminderDate; }
  public String getStatus()         { return status; }
  public String getNotes()          { return notes; }
}


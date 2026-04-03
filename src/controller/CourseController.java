package controller;

import db.DBConnection;
import model.Course;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class CourseController {
  private final int studentId;
  public CourseController(int studentId) { this.studentId = studentId; }

  public List<Course> getAll() {
    try { return Course.getAll(studentId); }
    catch (SQLException e) { e.printStackTrace(); return new ArrayList<>(); }
  }
  public boolean add(String code, String name, int credits, String prof, String sem, String status, String grade, String notes) {
    if (code.isEmpty() || name.isEmpty() || credits<1 || credits>6) return false;
    try { new Course(studentId,code,name,credits,prof,sem,status,grade,notes).save(); return true; }
    catch (SQLException e) { e.printStackTrace(); return false; }
  }
  public boolean update(int id, String code, String name, int credits, String prof, String sem, String status, String grade, String notes) {
    try {
      PreparedStatement ps = DBConnection.getConnection().prepareStatement(
          "UPDATE Course SET course_code=?,course_name=?,credits=?,professor=?,semester=?,status=?,grade=?,notes=? WHERE course_id=?");
      ps.setString(1,code); ps.setString(2,name); ps.setInt(3,credits);
      ps.setString(4,prof); ps.setString(5,sem); ps.setString(6,status);
      if (grade!=null && !grade.isEmpty()) ps.setString(7,grade); else ps.setNull(7,Types.VARCHAR);
      ps.setString(8,notes); ps.setInt(9,id);
      ps.executeUpdate(); return true;
    } catch (SQLException e) { e.printStackTrace(); return false; }
  }
  public boolean delete(int id)     { return deleteById(id); }
  public boolean deleteById(int id) {
    try {
      PreparedStatement ps = DBConnection.getConnection().prepareStatement("DELETE FROM Course WHERE course_id=?");
      ps.setInt(1,id); ps.executeUpdate(); return true;
    } catch (SQLException e) { e.printStackTrace(); return false; }
  }
}

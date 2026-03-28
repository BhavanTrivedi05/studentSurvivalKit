package model;

import db.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Course {
  private int courseId;
  private int studentId;
  private String courseCode;
  private String courseName;
  private int credits;
  private String professor;
  private String semester;
  private String status;
  private String grade;
  private String notes;

  public Course(int studentId, String courseCode, String courseName,
                int credits, String professor, String semester,
                String status, String grade, String notes) {
    this.studentId  = studentId;
    this.courseCode = courseCode;
    this.courseName = courseName;
    this.credits    = credits;
    this.professor  = professor;
    this.semester   = semester;
    this.status     = status;
    this.grade      = grade;
    this.notes      = notes;
  }

  public void save() throws SQLException {
    String sql = "INSERT INTO Course (student_id,course_code,course_name,credits,professor,semester,status,grade,notes) VALUES (?,?,?,?,?,?,?,?,?)";
    PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
    ps.setInt(1, studentId);
    ps.setString(2, courseCode);
    ps.setString(3, courseName);
    ps.setInt(4, credits);
    ps.setString(5, professor);
    ps.setString(6, semester);
    ps.setString(7, status);
    ps.setString(8, grade);
    ps.setString(9, notes);
    ps.executeUpdate();
  }

  public static List<Course> getAll(int studentId) throws SQLException {
    List<Course> list = new ArrayList<>();
    String sql = "SELECT * FROM Course WHERE student_id=? ORDER BY semester DESC";
    PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
    ps.setInt(1, studentId);
    ResultSet rs = ps.executeQuery();
    while (rs.next()) {
      Course c = new Course(
          rs.getInt("student_id"), rs.getString("course_code"),
          rs.getString("course_name"), rs.getInt("credits"),
          rs.getString("professor"), rs.getString("semester"),
          rs.getString("status"), rs.getString("grade"),
          rs.getString("notes")
      );
      c.courseId = rs.getInt("course_id");
      list.add(c);
    }
    return list;
  }

  public void delete() throws SQLException {
    String sql = "DELETE FROM Course WHERE course_id=?";
    PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
    ps.setInt(1, courseId);
    ps.executeUpdate();
  }

  public int getCourseId()      { return courseId; }
  public int getStudentId()     { return studentId; }
  public String getCourseCode() { return courseCode; }
  public String getCourseName() { return courseName; }
  public int getCredits()       { return credits; }
  public String getProfessor()  { return professor; }
  public String getSemester()   { return semester; }
  public String getStatus()     { return status; }
  public String getGrade()      { return grade; }
  public String getNotes()      { return notes; }
}

package controller;

import model.Course;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CourseController {
  private int studentId;

  public CourseController(int studentId) {
    this.studentId = studentId;
  }

  public List<Course> getAll() {
    try { return Course.getAll(studentId); }
    catch (SQLException e) { e.printStackTrace(); return new ArrayList<>(); }
  }

  public boolean add(String courseCode, String courseName, int credits,
                     String professor, String semester, String status,
                     String grade, String notes) {
    if (courseCode.isEmpty() || courseName.isEmpty()) return false;
    if (credits < 1 || credits > 6) return false;
    try {
      new Course(studentId, courseCode, courseName, credits,
          professor, semester, status, grade, notes).save();
      return true;
    } catch (SQLException e) { e.printStackTrace(); return false; }
  }

  public boolean delete(int courseId) {
    try {
      List<Course> all = Course.getAll(studentId);
      for (Course c : all) {
        if (c.getCourseId() == courseId) {
          c.delete(); return true;
        }
      }
      return false;
    } catch (SQLException e) { e.printStackTrace(); return false; }
  }
}


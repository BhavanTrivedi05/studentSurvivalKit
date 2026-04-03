package controller;

import db.DBConnection;
import model.Student;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StudentController {
  private Student student;

  public int signIn(String email, String password) {
    try {
      PreparedStatement ps = DBConnection.getConnection().prepareStatement(
          "SELECT * FROM Student WHERE email=? AND password=?");
      ps.setString(1,email); ps.setString(2,password);
      ResultSet rs = ps.executeQuery();
      if (rs.next()) {
        student = new Student(
            rs.getString("email"), rs.getString("first_name"),
            rs.getString("last_name"), rs.getString("nationality"),
            rs.getString("home_country"), rs.getString("program"),
            rs.getString("visa_type"), rs.getInt("graduation_year"));
        student.setStudentId(rs.getInt("student_id"));
        return student.getStudentId();
      }
      return -1;
    } catch (SQLException e) { e.printStackTrace(); return -1; }
  }

  public boolean profileExists() {
    try { student = Student.get(); return student != null; }
    catch (SQLException e) { e.printStackTrace(); return false; }
  }

  public Student getStudent() { return student; }

  public boolean saveProfile(String email, String firstName, String lastName,
                             String nationality, String homeCountry,
                             String program, String visaType,
                             int gradYear, String password) {
    try {
      student = new Student(email,firstName,lastName,nationality,
          homeCountry,program,visaType,gradYear);
      student.saveWithPassword(password);
      return true;
    } catch (SQLException e) { e.printStackTrace(); return false; }
  }

  public boolean saveProfile(String email, String firstName, String lastName,
                             String nationality, String homeCountry,
                             String program, String visaType, int gradYear) {
    return saveProfile(email,firstName,lastName,nationality,
        homeCountry,program,visaType,gradYear,"changeme");
  }

  public boolean updateProfile(Student s) {
    try { s.update(); return true; }
    catch (SQLException e) { e.printStackTrace(); return false; }
  }
}

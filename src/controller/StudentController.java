package controller;

import java.sql.SQLException;
import model.Student;

public class StudentController {
  private Student student;

  public boolean profileExists() {
    try {
      student = Student.get();
      return student != null;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  public Student getStudent() { return student; }

  public boolean saveProfile(String email, String firstName, String lastName,
                             String nationality, String homeCountry,
                             String program, String visaType, int gradYear) {
    try {
      student = new Student(email, firstName, lastName,
          nationality, homeCountry, program, visaType, gradYear);
      student.save();
      return true;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  public boolean updateProfile(Student s) {
    try {
      s.update();
      return true;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }
}

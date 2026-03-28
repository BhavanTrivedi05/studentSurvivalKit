package controller;

import model.Recruiter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RecruiterController {

  public List<Recruiter> getAll(int studentId) {
    try { return Recruiter.getAll(studentId); }
    catch (SQLException e) { e.printStackTrace(); return new ArrayList<>(); }
  }

  public boolean add(int applicationId, String name, String email,
                     String company, String phone, String linkedin, String notes) {
    if (name.isEmpty() || email.isEmpty()) return false;
    try {
      new Recruiter(applicationId, name, email, company, phone, linkedin, notes).save();
      return true;
    } catch (SQLException e) { e.printStackTrace(); return false; }
  }

  public boolean delete(int recruiterId) {
    try {
      List<Recruiter> all = Recruiter.getAll(0);
      for (Recruiter r : all) {
        if (r.getRecruiterId() == recruiterId) {
          r.delete(); return true;
        }
      }
      return false;
    } catch (SQLException e) { e.printStackTrace(); return false; }
  }
}
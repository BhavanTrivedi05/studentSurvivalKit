package controller;

import model.JobApplication;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class JobApplicationController {
  private int studentId;

  public JobApplicationController(int studentId) {
    this.studentId = studentId;
  }

  public List<JobApplication> getAll() {
    try { return JobApplication.getAll(studentId); }
    catch (SQLException e) { e.printStackTrace(); return new ArrayList<>(); }
  }

  public boolean add(String companyName, String role, String location,
                     LocalDate appliedDate, String status, String jobType,
                     boolean referral, String notes) {
    if (companyName.isEmpty() || role.isEmpty()) return false;
    try {
      new JobApplication(studentId, companyName, role, location,
          appliedDate, status, jobType, referral, notes).save();
      return true;
    } catch (SQLException e) { e.printStackTrace(); return false; }
  }

  public boolean delete(int applicationId) {
    try {
      List<JobApplication> all = JobApplication.getAll(studentId);
      for (JobApplication j : all) {
        if (j.getApplicationId() == applicationId) {
          j.delete(); return true;
        }
      }
      return false;
    } catch (SQLException e) { e.printStackTrace(); return false; }
  }
}

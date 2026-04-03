package controller;

import db.DBConnection;
import model.JobApplication;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class JobApplicationController {
  private final int studentId;
  public JobApplicationController(int studentId) { this.studentId = studentId; }

  public List<JobApplication> getAll() {
    try { return JobApplication.getAll(studentId); }
    catch (SQLException e) { e.printStackTrace(); return new ArrayList<>(); }
  }
  public boolean add(String company, String role, String location, LocalDate applied, String status, String jobType, boolean referral, String notes) {
    if (company.isEmpty() || role.isEmpty()) return false;
    try { new JobApplication(studentId,company,role,location,applied,status,jobType,referral,notes).save(); return true; }
    catch (SQLException e) { e.printStackTrace(); return false; }
  }
  public boolean update(int id, String company, String role, String location, LocalDate applied, String status, String jobType, boolean referral, String notes) {
    try {
      PreparedStatement ps = DBConnection.getConnection().prepareStatement(
          "UPDATE JobApplication SET company_name=?,role=?,location=?,applied_date=?,status=?,job_type=?,referrals=?,notes=? WHERE application_id=?");
      ps.setString(1,company); ps.setString(2,role); ps.setString(3,location);
      ps.setDate(4,Date.valueOf(applied)); ps.setString(5,status);
      ps.setString(6,jobType); ps.setBoolean(7,referral); ps.setString(8,notes); ps.setInt(9,id);
      ps.executeUpdate(); return true;
    } catch (SQLException e) { e.printStackTrace(); return false; }
  }
  public boolean delete(int id)     { return deleteById(id); }
  public boolean deleteById(int id) {
    try {
      PreparedStatement ps = DBConnection.getConnection().prepareStatement("DELETE FROM JobApplication WHERE application_id=?");
      ps.setInt(1,id); ps.executeUpdate(); return true;
    } catch (SQLException e) { e.printStackTrace(); return false; }
  }
}

package model;

import db.DBConnection;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class JobApplication {
  private int applicationId;
  private int studentId;
  private String companyName;
  private String role;
  private String location;
  private LocalDate appliedDate;
  private String status;
  private String jobType;
  private boolean referral;
  private String notes;

  public JobApplication(int studentId, String companyName, String role,
                        String location, LocalDate appliedDate,
                        String status, String jobType,
                        boolean referral, String notes) {
    this.studentId   = studentId;
    this.companyName = companyName;
    this.role        = role;
    this.location    = location;
    this.appliedDate = appliedDate;
    this.status      = status;
    this.jobType     = jobType;
    this.referral    = referral;
    this.notes       = notes;
  }

  public void save() throws SQLException {
    String sql = "INSERT INTO JobApplication (student_id,company_name,role,location,applied_date,status,job_type,referrals,notes) VALUES (?,?,?,?,?,?,?,?,?)";
    PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
    ps.setInt(1, studentId);
    ps.setString(2, companyName);
    ps.setString(3, role);
    ps.setString(4, location);
    ps.setDate(5, Date.valueOf(appliedDate));
    ps.setString(6, status);
    ps.setString(7, jobType);
    ps.setBoolean(8, referral);
    ps.setString(9, notes);
    ps.executeUpdate();
    ResultSet keys = ps.getGeneratedKeys();
    if (keys.next()) applicationId = keys.getInt(1);
  }

  public static List<JobApplication> getAll(int studentId) throws SQLException {
    List<JobApplication> list = new ArrayList<>();
    String sql = "SELECT * FROM JobApplication WHERE student_id=? ORDER BY applied_date DESC";
    PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
    ps.setInt(1, studentId);
    ResultSet rs = ps.executeQuery();
    while (rs.next()) {
      JobApplication j = new JobApplication(
          rs.getInt("student_id"), rs.getString("company_name"),
          rs.getString("role"), rs.getString("location"),
          rs.getDate("applied_date").toLocalDate(),
          rs.getString("status"), rs.getString("job_type"),
          rs.getBoolean("referrals"), rs.getString("notes")
      );
      j.applicationId = rs.getInt("application_id");
      list.add(j);
    }
    return list;
  }

  public void delete() throws SQLException {
    String sql = "DELETE FROM JobApplication WHERE application_id=?";
    PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
    ps.setInt(1, applicationId);
    ps.executeUpdate();
  }

  public int getApplicationId()     { return applicationId; }
  public int getStudentId()         { return studentId; }
  public String getCompanyName()    { return companyName; }
  public String getRole()           { return role; }
  public String getLocation()       { return location; }
  public LocalDate getAppliedDate() { return appliedDate; }
  public String getStatus()         { return status; }
  public String getJobType()        { return jobType; }
  public boolean isReferral()       { return referral; }
  public String getNotes()          { return notes; }
}

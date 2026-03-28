package model;

import db.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Recruiter {
  private int recruiterId;
  private int applicationId;
  private String recruiterName;
  private String recruiterEmail;
  private String companyName;
  private String phone;
  private String linkedin;
  private String notes;

  public Recruiter(int applicationId, String recruiterName, String recruiterEmail,
                   String companyName, String phone, String linkedin, String notes) {
    this.applicationId  = applicationId;
    this.recruiterName  = recruiterName;
    this.recruiterEmail = recruiterEmail;
    this.companyName    = companyName;
    this.phone          = phone;
    this.linkedin       = linkedin;
    this.notes          = notes;
  }

  public void save() throws SQLException {
    String sql = "INSERT INTO Recruiter (application_id,recruiter_name,recruiter_email,company_name,phone,linkedin,notes) VALUES (?,?,?,?,?,?,?)";
    PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
    ps.setInt(1, applicationId);
    ps.setString(2, recruiterName);
    ps.setString(3, recruiterEmail);
    ps.setString(4, companyName);
    ps.setString(5, phone);
    ps.setString(6, linkedin);
    ps.setString(7, notes);
    ps.executeUpdate();
  }

  // Get all recruiters for a student via JOIN
  public static List<Recruiter> getAll(int studentId) throws SQLException {
    List<Recruiter> list = new ArrayList<>();
    String sql = "SELECT r.* FROM Recruiter r " +
        "JOIN JobApplication j ON r.application_id = j.application_id " +
        "WHERE j.student_id=? ORDER BY r.recruiter_name ASC";
    PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
    ps.setInt(1, studentId);
    ResultSet rs = ps.executeQuery();
    while (rs.next()) {
      Recruiter r = new Recruiter(
          rs.getInt("application_id"), rs.getString("recruiter_name"),
          rs.getString("recruiter_email"), rs.getString("company_name"),
          rs.getString("phone"), rs.getString("linkedin"), rs.getString("notes")
      );
      r.recruiterId = rs.getInt("recruiter_id");
      list.add(r);
    }
    return list;
  }

  public void delete() throws SQLException {
    String sql = "DELETE FROM Recruiter WHERE recruiter_id=?";
    PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
    ps.setInt(1, recruiterId);
    ps.executeUpdate();
  }

  public int getRecruiterId()       { return recruiterId; }
  public int getApplicationId()     { return applicationId; }
  public String getRecruiterName()  { return recruiterName; }
  public String getRecruiterEmail() { return recruiterEmail; }
  public String getCompanyName()    { return companyName; }
  public String getPhone()          { return phone; }
  public String getLinkedin()       { return linkedin; }
  public String getNotes()          { return notes; }
}
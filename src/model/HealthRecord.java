package model;

import db.DBConnection;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class HealthRecord {
  private int healthId;
  private int studentId;
  private String recordType;
  private String providerName;
  private LocalDate visitDate;
  private LocalDate nextDueDate;
  private String insuranceProvider;
  private String insuranceId;
  private String description;
  private String notes;

  public HealthRecord(int studentId, String recordType, String providerName,
                      LocalDate visitDate, LocalDate nextDueDate,
                      String insuranceProvider, String insuranceId,
                      String description, String notes) {
    this.studentId         = studentId;
    this.recordType        = recordType;
    this.providerName      = providerName;
    this.visitDate         = visitDate;
    this.nextDueDate       = nextDueDate;
    this.insuranceProvider = insuranceProvider;
    this.insuranceId       = insuranceId;
    this.description       = description;
    this.notes             = notes;
  }

  public void save() throws SQLException {
    String sql = "INSERT INTO HealthRecord (student_id,record_type,provider_name,visit_date,next_due_date,insurance_provider,insurance_id,description,notes) VALUES (?,?,?,?,?,?,?,?,?)";
    PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
    ps.setInt(1, studentId);
    ps.setString(2, recordType);
    ps.setString(3, providerName);
    ps.setDate(4, Date.valueOf(visitDate));
    ps.setDate(5, nextDueDate != null ? Date.valueOf(nextDueDate) : null);
    ps.setString(6, insuranceProvider);
    ps.setString(7, insuranceId);
    ps.setString(8, description);
    ps.setString(9, notes);
    ps.executeUpdate();
  }

  public static List<HealthRecord> getAll(int studentId) throws SQLException {
    List<HealthRecord> list = new ArrayList<>();
    String sql = "SELECT * FROM HealthRecord WHERE student_id=? ORDER BY visit_date DESC";
    PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
    ps.setInt(1, studentId);
    ResultSet rs = ps.executeQuery();
    while (rs.next()) {
      HealthRecord h = new HealthRecord(
          rs.getInt("student_id"), rs.getString("record_type"),
          rs.getString("provider_name"), rs.getDate("visit_date").toLocalDate(),
          rs.getDate("next_due_date") != null ? rs.getDate("next_due_date").toLocalDate() : null,
          rs.getString("insurance_provider"), rs.getString("insurance_id"),
          rs.getString("description"), rs.getString("notes")
      );
      h.healthId = rs.getInt("health_id");
      list.add(h);
    }
    return list;
  }

  public void delete() throws SQLException {
    String sql = "DELETE FROM HealthRecord WHERE health_id=?";
    PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
    ps.setInt(1, healthId);
    ps.executeUpdate();
  }

  public int getHealthId()               { return healthId; }
  public int getStudentId()              { return studentId; }
  public String getRecordType()          { return recordType; }
  public String getProviderName()        { return providerName; }
  public LocalDate getVisitDate()        { return visitDate; }
  public LocalDate getNextDueDate()      { return nextDueDate; }
  public String getInsuranceProvider()   { return insuranceProvider; }
  public String getInsuranceId()         { return insuranceId; }
  public String getDescription()         { return description; }
  public String getNotes()               { return notes; }
}

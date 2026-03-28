package controller;

import model.HealthRecord;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class HealthRecordController {
  private int studentId;

  public HealthRecordController(int studentId) {
    this.studentId = studentId;
  }

  public List<HealthRecord> getAll() {
    try { return HealthRecord.getAll(studentId); }
    catch (SQLException e) { e.printStackTrace(); return new ArrayList<>(); }
  }

  public boolean add(String recordType, String providerName, LocalDate visitDate,
                     LocalDate nextDueDate, String insuranceProvider,
                     String insuranceId, String description, String notes) {
    if (providerName.isEmpty() || recordType.isEmpty()) return false;
    try {
      new HealthRecord(studentId, recordType, providerName, visitDate,
          nextDueDate, insuranceProvider, insuranceId, description, notes).save();
      return true;
    } catch (SQLException e) { e.printStackTrace(); return false; }
  }

  public boolean delete(int healthId) {
    try {
      List<HealthRecord> all = HealthRecord.getAll(studentId);
      for (HealthRecord h : all) {
        if (h.getHealthId() == healthId) {
          h.delete(); return true;
        }
      }
      return false;
    } catch (SQLException e) { e.printStackTrace(); return false; }
  }
}

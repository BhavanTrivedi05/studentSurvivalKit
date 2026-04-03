package controller;

import db.DBConnection;
import model.HealthRecord;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class HealthRecordController {
  private final int studentId;
  public HealthRecordController(int studentId) { this.studentId = studentId; }

  public List<HealthRecord> getAll() {
    try { return HealthRecord.getAll(studentId); }
    catch (SQLException e) { e.printStackTrace(); return new ArrayList<>(); }
  }
  public boolean add(String type, String provider, LocalDate visit, LocalDate nextDue, String insProv, String insId, String desc, String notes) {
    if (type.isEmpty() || provider.isEmpty()) return false;
    try { new HealthRecord(studentId,type,provider,visit,nextDue,insProv,insId,desc,notes).save(); return true; }
    catch (SQLException e) { e.printStackTrace(); return false; }
  }
  public boolean update(int id, String type, String provider, LocalDate visit, LocalDate nextDue, String insProv, String insId, String desc, String notes) {
    try {
      PreparedStatement ps = DBConnection.getConnection().prepareStatement(
          "UPDATE HealthRecord SET record_type=?,provider_name=?,visit_date=?,next_due_date=?,insurance_provider=?,insurance_id=?,description=?,notes=? WHERE health_id=?");
      ps.setString(1,type); ps.setString(2,provider); ps.setDate(3,Date.valueOf(visit));
      if (nextDue!=null) ps.setDate(4,Date.valueOf(nextDue)); else ps.setNull(4,Types.DATE);
      ps.setString(5,insProv); ps.setString(6,insId); ps.setString(7,desc); ps.setString(8,notes); ps.setInt(9,id);
      ps.executeUpdate(); return true;
    } catch (SQLException e) { e.printStackTrace(); return false; }
  }
  public boolean delete(int id)     { return deleteById(id); }
  public boolean deleteById(int id) {
    try {
      PreparedStatement ps = DBConnection.getConnection().prepareStatement("DELETE FROM HealthRecord WHERE health_id=?");
      ps.setInt(1,id); ps.executeUpdate(); return true;
    } catch (SQLException e) { e.printStackTrace(); return false; }
  }
}
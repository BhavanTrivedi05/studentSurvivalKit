package controller;

import db.DBConnection;
import model.Document;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DocumentController {
  private final int studentId;
  public DocumentController(int studentId) { this.studentId = studentId; }

  public List<Document> getAllDocuments() {
    try { return Document.getAll(studentId); }
    catch (SQLException e) { e.printStackTrace(); return new ArrayList<>(); }
  }
  public boolean addDocument(String type, LocalDate issue, LocalDate expiry, String auth, String notes) {
    if (type.isEmpty() || auth.isEmpty() || expiry.isBefore(issue)) return false;
    try { new Document(studentId,type,issue,expiry,auth,notes).save(); return true; }
    catch (SQLException e) { e.printStackTrace(); return false; }
  }
  public boolean update(int id, String type, LocalDate issue, LocalDate expiry, String auth, String notes) {
    try {
      PreparedStatement ps = DBConnection.getConnection().prepareStatement(
          "UPDATE Document SET doc_type=?,issue_date=?,expiry_date=?,issuing_authority=?,notes=? WHERE document_id=?");
      ps.setString(1,type); ps.setDate(2,Date.valueOf(issue)); ps.setDate(3,Date.valueOf(expiry));
      ps.setString(4,auth); ps.setString(5,notes); ps.setInt(6,id);
      ps.executeUpdate(); return true;
    } catch (SQLException e) { e.printStackTrace(); return false; }
  }
  public boolean delete(int id)     { return deleteById(id); }
  public boolean deleteById(int id) {
    try {
      PreparedStatement ps = DBConnection.getConnection().prepareStatement("DELETE FROM Document WHERE document_id=?");
      ps.setInt(1,id); ps.executeUpdate(); return true;
    } catch (SQLException e) { e.printStackTrace(); return false; }
  }
}

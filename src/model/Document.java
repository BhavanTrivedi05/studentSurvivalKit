package model;

import db.DBConnection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Document {
  private int documentId;
  private int studentId;
  private String docType;
  private LocalDate issueDate;
  private LocalDate expiryDate;
  private String issuingAuthority;
  private String notes;

  public Document(int studentId, String docType, LocalDate issueDate,
                  LocalDate expiryDate, String issuingAuthority, String notes) {
    this.studentId        = studentId;
    this.docType          = docType;
    this.issueDate        = issueDate;
    this.expiryDate       = expiryDate;
    this.issuingAuthority = issuingAuthority;
    this.notes            = notes;
  }

  // DB Methods
  public void save() throws SQLException {
    String sql = "INSERT INTO Document (student_id, doc_type, issue_date, expiry_date, issuing_authority, notes) " +
        "VALUES (?,?,?,?,?,?)";
    PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
    ps.setInt(1, studentId);
    ps.setString(2, docType);
    ps.setDate(3, Date.valueOf(issueDate));
    ps.setDate(4, Date.valueOf(expiryDate));
    ps.setString(5, issuingAuthority);
    ps.setString(6, notes);
    ps.executeUpdate();
  }

  public static List<Document> getAll(int studentId) throws SQLException {
    List<Document> list = new ArrayList<>();
    String sql = "SELECT *, days_until_expiry(expiry_date) AS days_left FROM Document WHERE student_id=?";
    PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
    ps.setInt(1, studentId);
    ResultSet rs = ps.executeQuery();
    while (rs.next()) {
      Document d = new Document(
          rs.getInt("student_id"),
          rs.getString("doc_type"),
          rs.getDate("issue_date").toLocalDate(),
          rs.getDate("expiry_date").toLocalDate(),
          rs.getString("issuing_authority"),
          rs.getString("notes")
      );
      d.documentId = rs.getInt("document_id");
      list.add(d);
    }
    return list;
  }

  public void update() throws SQLException {
    String sql = "UPDATE Document SET doc_type=?, issue_date=?, expiry_date=?, " +
        "issuing_authority=?, notes=? WHERE document_id=?";
    PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
    ps.setString(1, docType);
    ps.setDate(2, Date.valueOf(issueDate));
    ps.setDate(3, Date.valueOf(expiryDate));
    ps.setString(4, issuingAuthority);
    ps.setString(5, notes);
    ps.setInt(6, documentId);
    ps.executeUpdate();
  }

  public void delete() throws SQLException {
    String sql = "DELETE FROM Document WHERE document_id=?";
    PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
    ps.setInt(1, documentId);
    ps.executeUpdate();
  }

  // Getters & Setters
  public int getDocumentId()          { return documentId; }
  public int getStudentId()           { return studentId; }
  public String getDocType()          { return docType; }
  public LocalDate getIssueDate()     { return issueDate; }
  public LocalDate getExpiryDate()    { return expiryDate; }
  public String getIssuingAuthority() { return issuingAuthority; }
  public String getNotes()            { return notes; }
  public void setDocType(String v)          { docType = v; }
  public void setIssueDate(LocalDate v)     { issueDate = v; }
  public void setExpiryDate(LocalDate v)    { expiryDate = v; }
  public void setIssuingAuthority(String v) { issuingAuthority = v; }
  public void setNotes(String v)            { notes = v; }
}

// NOTE: All other model classes (Deadline, JobApplication, Expense,
// Housing, Course, HealthRecord, Contact, Recruiter) follow the
// EXACT same pattern as Document.java above:
//   - Fields matching the DB columns
//   - Constructor
//   - save(), getAll(studentId), update(), delete()
//   - Getters and Setters

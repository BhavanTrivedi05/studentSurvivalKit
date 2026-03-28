package controller;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import model.Document;

public class DocumentController {
  private int studentId;

  public DocumentController(int studentId) {
    this.studentId = studentId;
  }

  public List<Document> getAllDocuments() {
    try {
      return Document.getAll(studentId);
    } catch (SQLException e) {
      e.printStackTrace();
      return new ArrayList<>();
    }
  }

  public boolean addDocument(String docType, LocalDate issueDate, LocalDate expiryDate,
                             String issuingAuthority, String notes) {
    if (docType.isEmpty() || issuingAuthority.isEmpty()) return false;
    if (expiryDate.isBefore(issueDate)) return false;
    try {
      new Document(studentId, docType, issueDate, expiryDate, issuingAuthority, notes).save();
      return true;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  public boolean updateDocument(Document d) {
    try {
      d.update();
      return true;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  public boolean deleteDocument(Document d) {
    try {
      d.delete();
      return true;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }
}

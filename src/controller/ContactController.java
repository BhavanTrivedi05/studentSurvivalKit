package controller;

import model.Contact;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ContactController {
  private int studentId;

  public ContactController(int studentId) {
    this.studentId = studentId;
  }

  public List<Contact> getAll() {
    try { return Contact.getAll(studentId); }
    catch (SQLException e) { e.printStackTrace(); return new ArrayList<>(); }
  }

  public boolean add(String fullName, String role, String category,
                     String email, String phone, String organization, String notes) {
    if (fullName.isEmpty() || category.isEmpty()) return false;
    try {
      new Contact(studentId, fullName, role, category,
          email, phone, organization, notes).save();
      return true;
    } catch (SQLException e) { e.printStackTrace(); return false; }
  }

  public boolean delete(int contactId) {
    try {
      List<Contact> all = Contact.getAll(studentId);
      for (Contact c : all) {
        if (c.getContactId() == contactId) {
          c.delete(); return true;
        }
      }
      return false;
    } catch (SQLException e) { e.printStackTrace(); return false; }
  }
}

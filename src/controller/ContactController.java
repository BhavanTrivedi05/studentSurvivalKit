package controller;

import db.DBConnection;
import model.Contact;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ContactController {
  private final int studentId;
  public ContactController(int studentId) { this.studentId = studentId; }

  public List<Contact> getAll() {
    try { return Contact.getAll(studentId); }
    catch (SQLException e) { e.printStackTrace(); return new ArrayList<>(); }
  }
  public boolean add(String name, String role, String category, String email, String phone, String org, String notes) {
    if (name.isEmpty() || category.isEmpty()) return false;
    try { new Contact(studentId,name,role,category,email,phone,org,notes).save(); return true; }
    catch (SQLException e) { e.printStackTrace(); return false; }
  }
  public boolean update(int id, String name, String role, String category, String email, String phone, String org, String notes) {
    try {
      PreparedStatement ps = DBConnection.getConnection().prepareStatement(
          "UPDATE Contact SET full_name=?,role=?,category=?,email=?,phone=?,organization=?,notes=? WHERE contact_id=?");
      ps.setString(1,name); ps.setString(2,role); ps.setString(3,category);
      ps.setString(4,email); ps.setString(5,phone); ps.setString(6,org);
      ps.setString(7,notes); ps.setInt(8,id);
      ps.executeUpdate(); return true;
    } catch (SQLException e) { e.printStackTrace(); return false; }
  }
  public boolean delete(int id)     { return deleteById(id); }
  public boolean deleteById(int id) {
    try {
      PreparedStatement ps = DBConnection.getConnection().prepareStatement("DELETE FROM Contact WHERE contact_id=?");
      ps.setInt(1,id); ps.executeUpdate(); return true;
    } catch (SQLException e) { e.printStackTrace(); return false; }
  }
}
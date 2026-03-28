package model;

import db.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Contact {
  private int contactId;
  private int studentId;
  private String fullName;
  private String role;
  private String category;
  private String email;
  private String phone;
  private String organization;
  private String notes;

  public Contact(int studentId, String fullName, String role,
                 String category, String email, String phone,
                 String organization, String notes) {
    this.studentId    = studentId;
    this.fullName     = fullName;
    this.role         = role;
    this.category     = category;
    this.email        = email;
    this.phone        = phone;
    this.organization = organization;
    this.notes        = notes;
  }

  public void save() throws SQLException {
    String sql = "INSERT INTO Contact (student_id,full_name,role,category,email,phone,organization,notes) VALUES (?,?,?,?,?,?,?,?)";
    PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
    ps.setInt(1, studentId);
    ps.setString(2, fullName);
    ps.setString(3, role);
    ps.setString(4, category);
    ps.setString(5, email);
    ps.setString(6, phone);
    ps.setString(7, organization);
    ps.setString(8, notes);
    ps.executeUpdate();
  }

  public static List<Contact> getAll(int studentId) throws SQLException {
    List<Contact> list = new ArrayList<>();
    String sql = "SELECT * FROM Contact WHERE student_id=? ORDER BY full_name ASC";
    PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
    ps.setInt(1, studentId);
    ResultSet rs = ps.executeQuery();
    while (rs.next()) {
      Contact c = new Contact(
          rs.getInt("student_id"), rs.getString("full_name"),
          rs.getString("role"), rs.getString("category"),
          rs.getString("email"), rs.getString("phone"),
          rs.getString("organization"), rs.getString("notes")
      );
      c.contactId = rs.getInt("contact_id");
      list.add(c);
    }
    return list;
  }

  public void delete() throws SQLException {
    String sql = "DELETE FROM Contact WHERE contact_id=?";
    PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
    ps.setInt(1, contactId);
    ps.executeUpdate();
  }

  public int getContactId()       { return contactId; }
  public int getStudentId()       { return studentId; }
  public String getFullName()     { return fullName; }
  public String getRole()         { return role; }
  public String getCategory()     { return category; }
  public String getEmail()        { return email; }
  public String getPhone()        { return phone; }
  public String getOrganization() { return organization; }
  public String getNotes()        { return notes; }
}

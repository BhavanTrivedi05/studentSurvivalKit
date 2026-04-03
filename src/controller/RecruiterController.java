package controller;

import db.DBConnection;
import model.Recruiter;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RecruiterController {

  public List<Recruiter> getAll(int studentId) {
    try { return Recruiter.getAll(studentId); }
    catch (SQLException e) { e.printStackTrace(); return new ArrayList<>(); }
  }
  public boolean add(int appId, String name, String email, String company, String phone, String linkedin, String notes) {
    if (name.isEmpty() || email.isEmpty()) return false;
    try { new Recruiter(appId,name,email,company,phone,linkedin,notes).save(); return true; }
    catch (SQLException e) { e.printStackTrace(); return false; }
  }
  public boolean update(int id, String name, String email, String company, String phone, String linkedin, String notes) {
    try {
      PreparedStatement ps = DBConnection.getConnection().prepareStatement(
          "UPDATE Recruiter SET recruiter_name=?,recruiter_email=?,company_name=?,phone=?,linkedin=?,notes=? WHERE recruiter_id=?");
      ps.setString(1,name); ps.setString(2,email); ps.setString(3,company);
      ps.setString(4,phone); ps.setString(5,linkedin); ps.setString(6,notes); ps.setInt(7,id);
      ps.executeUpdate(); return true;
    } catch (SQLException e) { e.printStackTrace(); return false; }
  }
  public boolean delete(int id)     { return deleteById(id); }
  public boolean deleteById(int id) {
    try {
      PreparedStatement ps = DBConnection.getConnection().prepareStatement("DELETE FROM Recruiter WHERE recruiter_id=?");
      ps.setInt(1,id); ps.executeUpdate(); return true;
    } catch (SQLException e) { e.printStackTrace(); return false; }
  }
}
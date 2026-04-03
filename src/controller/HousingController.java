package controller;

import db.DBConnection;
import model.Housing;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class HousingController {
  private final int studentId;
  public HousingController(int studentId) { this.studentId = studentId; }

  public List<Housing> getAll() {
    try { return Housing.getAll(studentId); }
    catch (SQLException e) { e.printStackTrace(); return new ArrayList<>(); }
  }
  public boolean add(String address, LocalDate start, LocalDate end, String landlord, String contact, double rent, String notes) {
    if (address.isEmpty() || landlord.isEmpty() || end.isBefore(start)) return false;
    try { new Housing(studentId,address,start,end,landlord,contact,rent,notes).save(); return true; }
    catch (SQLException e) { e.printStackTrace(); return false; }
  }
  public boolean update(int id, String address, LocalDate start, LocalDate end, String landlord, String contact, double rent, String notes) {
    try {
      PreparedStatement ps = DBConnection.getConnection().prepareStatement(
          "UPDATE Housing SET address=?,lease_start_date=?,lease_end_date=?,landlord_name=?,landlord_contact=?,monthly_rent=?,notes=? WHERE housing_id=?");
      ps.setString(1,address); ps.setDate(2,Date.valueOf(start)); ps.setDate(3,Date.valueOf(end));
      ps.setString(4,landlord); ps.setString(5,contact); ps.setDouble(6,rent);
      ps.setString(7,notes); ps.setInt(8,id);
      ps.executeUpdate(); return true;
    } catch (SQLException e) { e.printStackTrace(); return false; }
  }
  public boolean delete(int id)     { return deleteById(id); }
  public boolean deleteById(int id) {
    try {
      PreparedStatement ps = DBConnection.getConnection().prepareStatement("DELETE FROM Housing WHERE housing_id=?");
      ps.setInt(1,id); ps.executeUpdate(); return true;
    } catch (SQLException e) { e.printStackTrace(); return false; }
  }
}
package controller;

import model.Housing;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class HousingController {
  private int studentId;

  public HousingController(int studentId) {
    this.studentId = studentId;
  }

  public List<Housing> getAll() {
    try { return Housing.getAll(studentId); }
    catch (SQLException e) { e.printStackTrace(); return new ArrayList<>(); }
  }

  public boolean add(String address, LocalDate leaseStart, LocalDate leaseEnd,
                     String landlordName, String landlordContact,
                     double monthlyRent, String notes) {
    if (address.isEmpty() || landlordName.isEmpty()) return false;
    if (leaseEnd.isBefore(leaseStart)) return false;
    try {
      new Housing(studentId, address, leaseStart, leaseEnd,
          landlordName, landlordContact, monthlyRent, notes).save();
      return true;
    } catch (SQLException e) { e.printStackTrace(); return false; }
  }

  public boolean delete(int housingId) {
    try {
      List<Housing> all = Housing.getAll(studentId);
      for (Housing h : all) {
        if (h.getHousingId() == housingId) {
          h.delete(); return true;
        }
      }
      return false;
    } catch (SQLException e) { e.printStackTrace(); return false; }
  }
}

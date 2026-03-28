package model;

import db.DBConnection;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Housing {
  private int housingId;
  private int studentId;
  private String address;
  private LocalDate leaseStartDate;
  private LocalDate leaseEndDate;
  private String landlordName;
  private String landlordContact;
  private double monthlyRent;
  private String notes;

  public Housing(int studentId, String address, LocalDate leaseStartDate,
                 LocalDate leaseEndDate, String landlordName,
                 String landlordContact, double monthlyRent, String notes) {
    this.studentId      = studentId;
    this.address        = address;
    this.leaseStartDate = leaseStartDate;
    this.leaseEndDate   = leaseEndDate;
    this.landlordName   = landlordName;
    this.landlordContact= landlordContact;
    this.monthlyRent    = monthlyRent;
    this.notes          = notes;
  }

  public void save() throws SQLException {
    String sql = "INSERT INTO Housing (student_id,address,lease_start_date,lease_end_date,landlord_name,landlord_contact,monthly_rent,notes) VALUES (?,?,?,?,?,?,?,?)";
    PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
    ps.setInt(1, studentId);
    ps.setString(2, address);
    ps.setDate(3, Date.valueOf(leaseStartDate));
    ps.setDate(4, Date.valueOf(leaseEndDate));
    ps.setString(5, landlordName);
    ps.setString(6, landlordContact);
    ps.setDouble(7, monthlyRent);
    ps.setString(8, notes);
    ps.executeUpdate();
  }

  public static List<Housing> getAll(int studentId) throws SQLException {
    List<Housing> list = new ArrayList<>();
    String sql = "SELECT * FROM Housing WHERE student_id=? ORDER BY lease_end_date ASC";
    PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
    ps.setInt(1, studentId);
    ResultSet rs = ps.executeQuery();
    while (rs.next()) {
      Housing h = new Housing(
          rs.getInt("student_id"), rs.getString("address"),
          rs.getDate("lease_start_date").toLocalDate(),
          rs.getDate("lease_end_date").toLocalDate(),
          rs.getString("landlord_name"), rs.getString("landlord_contact"),
          rs.getDouble("monthly_rent"), rs.getString("notes")
      );
      h.housingId = rs.getInt("housing_id");
      list.add(h);
    }
    return list;
  }

  public void delete() throws SQLException {
    String sql = "DELETE FROM Housing WHERE housing_id=?";
    PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
    ps.setInt(1, housingId);
    ps.executeUpdate();
  }

  public int getHousingId()              { return housingId; }
  public int getStudentId()              { return studentId; }
  public String getAddress()             { return address; }
  public LocalDate getLeaseStartDate()   { return leaseStartDate; }
  public LocalDate getLeaseEndDate()     { return leaseEndDate; }
  public String getLandlordName()        { return landlordName; }
  public String getLandlordContact()     { return landlordContact; }
  public double getMonthlyRent()         { return monthlyRent; }
  public String getNotes()               { return notes; }
}

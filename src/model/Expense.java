package model;

import db.DBConnection;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Expense {
  private int expenseId;
  private int studentId;
  private double amount;
  private String category;
  private LocalDate expenseDate;
  private String description;
  private String currency;

  public Expense(int studentId, double amount, String category,
                 LocalDate expenseDate, String description, String currency) {
    this.studentId   = studentId;
    this.amount      = amount;
    this.category    = category;
    this.expenseDate = expenseDate;
    this.description = description;
    this.currency    = currency;
  }

  public void save() throws SQLException {
    String sql = "INSERT INTO Expense (student_id,amount,category,expense_date,description,currency) VALUES (?,?,?,?,?,?)";
    PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
    ps.setInt(1, studentId);
    ps.setDouble(2, amount);
    ps.setString(3, category);
    ps.setDate(4, Date.valueOf(expenseDate));
    ps.setString(5, description);
    ps.setString(6, currency);
    ps.executeUpdate();
  }

  public static List<Expense> getAll(int studentId) throws SQLException {
    List<Expense> list = new ArrayList<>();
    String sql = "SELECT * FROM Expense WHERE student_id=? ORDER BY expense_date DESC";
    PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
    ps.setInt(1, studentId);
    ResultSet rs = ps.executeQuery();
    while (rs.next()) {
      Expense e = new Expense(
          rs.getInt("student_id"), rs.getDouble("amount"),
          rs.getString("category"), rs.getDate("expense_date").toLocalDate(),
          rs.getString("description"), rs.getString("currency")
      );
      e.expenseId = rs.getInt("expense_id");
      list.add(e);
    }
    return list;
  }

  public void delete() throws SQLException {
    String sql = "DELETE FROM Expense WHERE expense_id=?";
    PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
    ps.setInt(1, expenseId);
    ps.executeUpdate();
  }

  public int getExpenseId()         { return expenseId; }
  public int getStudentId()         { return studentId; }
  public double getAmount()         { return amount; }
  public String getCategory()       { return category; }
  public LocalDate getExpenseDate() { return expenseDate; }
  public String getDescription()    { return description; }
  public String getCurrency()       { return currency; }
}

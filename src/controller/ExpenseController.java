package controller;

import db.DBConnection;
import model.Expense;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ExpenseController {
  private final int studentId;
  public ExpenseController(int studentId) { this.studentId = studentId; }

  public List<Expense> getAll() {
    try { return Expense.getAll(studentId); }
    catch (SQLException e) { e.printStackTrace(); return new ArrayList<>(); }
  }
  public boolean add(double amount, String category, LocalDate date, String description, String currency) {
    if (amount<=0 || description.isEmpty()) return false;
    try { new Expense(studentId,amount,category,date,description,currency).save(); return true; }
    catch (SQLException e) { e.printStackTrace(); return false; }
  }
  public boolean update(int id, double amount, String category, LocalDate date, String description, String currency) {
    try {
      PreparedStatement ps = DBConnection.getConnection().prepareStatement(
          "UPDATE Expense SET amount=?,category=?,expense_date=?,description=?,currency=? WHERE expense_id=?");
      ps.setDouble(1,amount); ps.setString(2,category); ps.setDate(3,Date.valueOf(date));
      ps.setString(4,description); ps.setString(5,currency); ps.setInt(6,id);
      ps.executeUpdate(); return true;
    } catch (SQLException e) { e.printStackTrace(); return false; }
  }
  public boolean delete(int id)     { return deleteById(id); }
  public boolean deleteById(int id) {
    try {
      PreparedStatement ps = DBConnection.getConnection().prepareStatement("DELETE FROM Expense WHERE expense_id=?");
      ps.setInt(1,id); ps.executeUpdate(); return true;
    } catch (SQLException e) { e.printStackTrace(); return false; }
  }
}
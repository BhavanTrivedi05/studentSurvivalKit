package controller;

import model.Expense;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ExpenseController {
  private int studentId;

  public ExpenseController(int studentId) {
    this.studentId = studentId;
  }

  public List<Expense> getAll() {
    try { return Expense.getAll(studentId); }
    catch (SQLException e) { e.printStackTrace(); return new ArrayList<>(); }
  }

  public boolean add(double amount, String category, LocalDate date,
                     String description, String currency) {
    if (amount <= 0 || description.isEmpty()) return false;
    try {
      new Expense(studentId, amount, category, date, description, currency).save();
      return true;
    } catch (SQLException e) { e.printStackTrace(); return false; }
  }

  public boolean delete(int expenseId) {
    try {
      List<Expense> all = Expense.getAll(studentId);
      for (Expense ex : all) {
        if (ex.getExpenseId() == expenseId) {
          ex.delete(); return true;
        }
      }
      return false;
    } catch (SQLException e) { e.printStackTrace(); return false; }
  }
}

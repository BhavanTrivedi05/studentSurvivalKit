package controller;

import db.DBConnection;
import java.sql.*;
import java.time.LocalDate;

public class DashboardController {

  private final int studentId;

  public DashboardController(int studentId) {
    this.studentId = studentId;
  }

  // ── Calls VIEW: student_dashboard_summary ────────────
  public int getExpiringDocCount() {
    try {
      PreparedStatement ps = DBConnection.getConnection().prepareStatement(
          "SELECT docs_expiring_soon FROM student_dashboard_summary WHERE student_id = ?");
      ps.setInt(1, studentId);
      ResultSet rs = ps.executeQuery();
      if (rs.next()) return rs.getInt(1);
    } catch (SQLException e) { e.printStackTrace(); }
    return 0;
  }

  public int getDeadlinesThisWeek() {
    try {
      PreparedStatement ps = DBConnection.getConnection().prepareStatement(
          "SELECT deadlines_this_week FROM student_dashboard_summary WHERE student_id = ?");
      ps.setInt(1, studentId);
      ResultSet rs = ps.executeQuery();
      if (rs.next()) return rs.getInt(1);
    } catch (SQLException e) { e.printStackTrace(); }
    return 0;
  }

  public int getTotalJobApplications() {
    try {
      PreparedStatement ps = DBConnection.getConnection().prepareStatement(
          "SELECT total_job_applications FROM student_dashboard_summary WHERE student_id = ?");
      ps.setInt(1, studentId);
      ResultSet rs = ps.executeQuery();
      if (rs.next()) return rs.getInt(1);
    } catch (SQLException e) { e.printStackTrace(); }
    return 0;
  }

  public double getMonthlyExpenses() {
    try {
      PreparedStatement ps = DBConnection.getConnection().prepareStatement(
          "SELECT monthly_expenses FROM student_dashboard_summary WHERE student_id = ?");
      ps.setInt(1, studentId);
      ResultSet rs = ps.executeQuery();
      if (rs.next()) return rs.getDouble(1);
    } catch (SQLException e) { e.printStackTrace(); }
    return 0.0;
  }

  // ── SP 1: get_expiring_documents ─────────────────────
  public ResultSet getExpiringDocuments(int days) {
    try {
      CallableStatement cs = DBConnection.getConnection()
          .prepareCall("{CALL get_expiring_documents(?, ?)}");
      cs.setInt(1, studentId);
      cs.setInt(2, days);
      return cs.executeQuery();
    } catch (SQLException e) { e.printStackTrace(); return null; }
  }

  // ── SP 2: get_upcoming_deadlines ─────────────────────
  public ResultSet getUpcomingDeadlines(int days) {
    try {
      CallableStatement cs = DBConnection.getConnection()
          .prepareCall("{CALL get_upcoming_deadlines(?, ?)}");
      cs.setInt(1, studentId);
      cs.setInt(2, days);
      return cs.executeQuery();
    } catch (SQLException e) { e.printStackTrace(); return null; }
  }

  // ── SP 3: get_monthly_expense_total ──────────────────
  public double getMonthlyExpenseTotal() {
    try {
      CallableStatement cs = DBConnection.getConnection()
          .prepareCall("{CALL get_monthly_expense_total(?, ?, ?, ?)}");
      cs.setInt(1, studentId);
      cs.setInt(2, LocalDate.now().getYear());
      cs.setInt(3, LocalDate.now().getMonthValue());
      cs.registerOutParameter(4, Types.DECIMAL);
      cs.execute();
      return cs.getDouble(4);
    } catch (SQLException e) { e.printStackTrace(); return 0.0; }
  }
}
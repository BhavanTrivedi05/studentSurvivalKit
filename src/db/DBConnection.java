package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

  private static Connection conn;

  private DBConnection() {}

  // Called once on startup with credentials from the login dialog
  public static void init(String url, String user, String password) throws SQLException {
    conn = DriverManager.getConnection(url, user, password);
    System.out.println("DB connected successfully.");
  }

  public static Connection getConnection() throws SQLException {
    if (conn == null || conn.isClosed()) {
      throw new SQLException("DB not initialized. Call DBConnection.init() first.");
    }
    return conn;
  }

  public static void closeConnection() {
    try {
      if (conn != null && !conn.isClosed()) {
        conn.close();
        System.out.println("DB connection closed.");
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
import db.DBConnection;
import controller.StudentController;
import view.DBLoginDialog;
import view.MainDashboard;
import view.StudentProfilePanel;

import javax.swing.*;
import java.sql.SQLException;

public class Main {
  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {

      // Step 1: Show DB login dialog
      DBLoginDialog loginDialog = new DBLoginDialog();
      loginDialog.setVisible(true);

      if (!loginDialog.isConfirmed()) {
        System.exit(0);
      }

      // Step 2: Connect to DB
      try {
        DBConnection.init(
            loginDialog.getUrl(),
            loginDialog.getUsername(),
            loginDialog.getPassword()
        );
      } catch (SQLException e) {
        JOptionPane.showMessageDialog(null,
            "Could not connect to database.\n\n" +
                "Check that:\n" +
                "  1. MySQL is running\n" +
                "  2. Your password is correct\n" +
                "  3. The database 'studentsurvivalkit' exists\n\n" +
                e.getMessage(),
            "Connection Failed", JOptionPane.ERROR_MESSAGE);
        System.exit(0);
      }

      // Step 3: Check if student profile exists
      StudentController sc = new StudentController();

      if (sc.profileExists()) {
        // Profile found — go straight to dashboard
        int id      = sc.getStudent().getStudentId();
        String name = sc.getStudent().getFirstName();
        new MainDashboard(id, name).setVisible(true);
      } else {
        // No profile — show setup screen
        new StudentProfilePanel(sc).setVisible(true);
      }
    });
  }
}
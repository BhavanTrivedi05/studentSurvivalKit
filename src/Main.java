import controller.StudentController;
import db.DBConnection;
import view.DBLoginDialog;
import view.LoginPanel;

import javax.swing.*;
import java.sql.SQLException;

public class Main {
  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {

      // Step 1 — DB connection dialog
      DBLoginDialog dlg = new DBLoginDialog();
      dlg.setVisible(true);
      if (!dlg.isConfirmed()) System.exit(0);

      // Step 2 — Connect
      try {
        DBConnection.init(dlg.getUrl(), dlg.getUsername(), dlg.getPassword());
      } catch (SQLException e) {
        JOptionPane.showMessageDialog(null,
            "Could not connect to database.\n\nCheck that:\n" +
                "  1. MySQL is running\n  2. Password is correct\n" +
                "  3. Database 'studentsurvivalkit' exists\n\n" + e.getMessage(),
            "Connection Failed", JOptionPane.ERROR_MESSAGE);
        System.exit(0);
      }

      // Step 3 — Show login screen
      new LoginPanel(new StudentController()).setVisible(true);
    });
  }
}
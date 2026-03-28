package view;

import controller.DocumentController;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import model.Document;

public class DocumentPanel extends JPanel {
  private DocumentController controller;
  private JTable table;
  private DefaultTableModel tableModel;

  public DocumentPanel(int studentId) {
    controller = new DocumentController(studentId);
    setLayout(new BorderLayout());
    setBackground(new Color(245, 246, 250));
    setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));

    add(buildHeader(), BorderLayout.NORTH);
    add(buildTable(), BorderLayout.CENTER);

    loadData();
  }

  private JPanel buildHeader() {
    JPanel header = new JPanel(new BorderLayout());
    header.setOpaque(false);

    JLabel title = new JLabel("Documents");
    title.setFont(new Font("Inter", Font.BOLD, 22));
    header.add(title, BorderLayout.WEST);

    JButton addBtn = new JButton("+ Add New");
    addBtn.setBackground(new Color(37, 99, 235));
    addBtn.setForeground(Color.WHITE);
    addBtn.setFocusPainted(false);
    addBtn.setBorderPainted(false);
    addBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    addBtn.addActionListener(e -> showAddDialog());
    header.add(addBtn, BorderLayout.EAST);

    return header;
  }

  private JScrollPane buildTable() {
    String[] cols = {"ID", "Type", "Issue Date", "Expiry Date", "Issuing Authority", "Notes", "Days Left"};
    tableModel = new DefaultTableModel(cols, 0) {
      public boolean isCellEditable(int r, int c) { return false; }
    };
    table = new JTable(tableModel);
    table.setRowHeight(36);
    table.setFont(new Font("Inter", Font.PLAIN, 13));
    table.getTableHeader().setFont(new Font("Inter", Font.BOLD, 13));
    table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

    JScrollPane scroll = new JScrollPane(table);
    scroll.setBorder(BorderFactory.createEmptyBorder(16, 0, 0, 0));
    return scroll;
  }

  private void loadData() {
    tableModel.setRowCount(0);
    for (Document d : controller.getAllDocuments()) {
      long daysLeft = ChronoUnit.DAYS.between(LocalDate.now(), d.getExpiryDate());
      tableModel.addRow(new Object[]{
          d.getDocumentId(), d.getDocType(), d.getIssueDate(),
          d.getExpiryDate(), d.getIssuingAuthority(), d.getNotes(), daysLeft
      });
    }
  }

  private void showAddDialog() {
    // Build input dialog
    JTextField docTypeField       = new JTextField();
    JTextField issueDateField     = new JTextField("YYYY-MM-DD");
    JTextField expiryDateField    = new JTextField("YYYY-MM-DD");
    JTextField authorityField     = new JTextField();
    JTextField notesField         = new JTextField();

    Object[] fields = {
        "Document Type:", docTypeField,
        "Issue Date:",    issueDateField,
        "Expiry Date:",   expiryDateField,
        "Authority:",     authorityField,
        "Notes:",         notesField
    };

    int result = JOptionPane.showConfirmDialog(this, fields, "Add Document",
        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

    if (result == JOptionPane.OK_OPTION) {
      boolean success = controller.addDocument(
          docTypeField.getText().trim(),
          LocalDate.parse(issueDateField.getText().trim()),
          LocalDate.parse(expiryDateField.getText().trim()),
          authorityField.getText().trim(),
          notesField.getText().trim()
      );
      if (success) {
        loadData();
        JOptionPane.showMessageDialog(this, "Document added successfully!");
      } else {
        JOptionPane.showMessageDialog(this, "Error adding document. Check inputs.",
            "Error", JOptionPane.ERROR_MESSAGE);
      }
    }
  }

  // TODO: Add showEditDialog() and deleteSelected() following the same pattern
}

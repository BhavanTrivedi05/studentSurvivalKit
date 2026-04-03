package view;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import model.Contact;
import java.awt.*;

public class ContactFormDialog extends BaseFormDialog {

  private final JTextField nameF  = field();
  private final JTextField roleF  = field();
  private final JComboBox<String>
      catC = combo("DSO Advisor","Academic Advisor","Professor","Embassy","Healthcare","Legal","Other");
  private final JTextField emailF = field();
  private final JTextField phoneF = field();
  private final JTextField orgF   = field();
  private final JTextArea notesA = area("");

  private String name, role, category, email, phone, org, notes;

  public ContactFormDialog(Frame parent, Contact c) {
    super(parent, c == null ? "Add Contact" : "Edit Contact");
    if (c != null) {
      nameF.setText(c.getFullName());
      roleF.setText(c.getRole());
      catC.setSelectedItem(c.getCategory());
      emailF.setText(c.getEmail() != null ? c.getEmail() : "");
      phoneF.setText(c.getPhone() != null ? c.getPhone() : "");
      orgF.setText(c.getOrganization() != null ? c.getOrganization() : "");
      notesA.setText(c.getNotes() != null ? c.getNotes() : "");
    }
    JPanel p = new JPanel(new GridBagLayout());
    var lc = lc(); var fc = fc();
    row(p, lc, fc, 0, "Full Name *",   nameF);
    row(p, lc, fc, 1, "Role",          roleF);
    row(p, lc, fc, 2, "Category",      catC);
    row(p, lc, fc, 3, "Email",         emailF);
    row(p, lc, fc, 4, "Phone",         phoneF);
    row(p, lc, fc, 5, "Organization",  orgF);
    row(p, lc, fc, 6, "Notes",         notesA);
    finishBuild(p);
  }

  @Override
  protected void onSave() {
    name     = nameF.getText().trim();
    role     = roleF.getText().trim();
    category = (String) catC.getSelectedItem();
    email    = emailF.getText().trim();
    phone    = phoneF.getText().trim();
    org      = orgF.getText().trim();
    notes    = notesA.getText().trim();
    if (name.isEmpty()) { err("Full name is required."); return; }
    confirmed = true; dispose();
  }

  public String getName()     { return name; }
  public String getRole()     { return role; }
  public String getCategory() { return category; }
  public String getEmail()    { return email; }
  public String getPhone()    { return phone; }
  public String getOrg()      { return org; }
  public String getNotes()    { return notes; }
}
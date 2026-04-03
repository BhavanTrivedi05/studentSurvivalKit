package view;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import model.JobApplication;
import java.awt.*;
import java.time.LocalDate;

public class JobApplicationFormDialog extends BaseFormDialog {

  private final JTextField companyF  = field();
  private final JTextField roleF     = field();
  private final JTextField locationF = field();
  private final JTextField dateF     = field("YYYY-MM-DD");
  private final JComboBox<String>
      statusC  = combo("Applied","OA","Phone Screen","Interview","Offer","Rejected","Withdrawn");
  private final JComboBox<String> typeC    = combo("Internship","Co-op","Full-time","Part-time");
  private final JCheckBox referralCB      = new JCheckBox("Referred by someone");
  private final JTextArea notesA          = area("");

  private String company, role, location, status, jobType, notes;
  private LocalDate appliedDate;
  private boolean referral;

  public JobApplicationFormDialog(Frame parent, JobApplication j) {
    super(parent, j == null ? "Add Application" : "Edit Application");
    if (j != null) {
      companyF.setText(j.getCompanyName());
      roleF.setText(j.getRole());
      locationF.setText(j.getLocation() != null ? j.getLocation() : "");
      dateF.setText(j.getAppliedDate().toString());
      statusC.setSelectedItem(j.getStatus());
      typeC.setSelectedItem(j.getJobType());
      referralCB.setSelected(j.isReferral());
      notesA.setText(j.getNotes() != null ? j.getNotes() : "");
    }
    JPanel p = new JPanel(new GridBagLayout());
    var lc = lc(); var fc = fc();
    row(p, lc, fc, 0, "Company *",      companyF);
    row(p, lc, fc, 1, "Role *",         roleF);
    row(p, lc, fc, 2, "Location",       locationF);
    row(p, lc, fc, 3, "Applied Date *", dateF);
    row(p, lc, fc, 4, "Status",         statusC);
    row(p, lc, fc, 5, "Job Type",       typeC);
    row(p, lc, fc, 6, "",               referralCB);
    row(p, lc, fc, 7, "Notes",          notesA);
    finishBuild(p);
  }

  @Override
  protected void onSave() {
    company  = companyF.getText().trim();
    role     = roleF.getText().trim();
    location = locationF.getText().trim();
    status   = (String) statusC.getSelectedItem();
    jobType  = (String) typeC.getSelectedItem();
    referral = referralCB.isSelected();
    notes    = notesA.getText().trim();
    if (company.isEmpty()) { err("Company is required."); return; }
    if (role.isEmpty())    { err("Role is required."); return; }
    try { appliedDate = LocalDate.parse(dateF.getText().trim()); }
    catch (Exception e) { err("Use YYYY-MM-DD for date."); return; }
    confirmed = true; dispose();
  }

  public String    getCompany()     { return company; }
  public String    getRole()        { return role; }
  public String    getLocation()    { return location; }
  public LocalDate getAppliedDate() { return appliedDate; }
  public String    getStatus()      { return status; }
  public String    getJobType()     { return jobType; }
  public boolean   isReferral()     { return referral; }
  public String    getNotes()       { return notes; }
}
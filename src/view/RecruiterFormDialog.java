package view;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import model.JobApplication;
import model.Recruiter;
import java.awt.*;
import java.util.List;

public class RecruiterFormDialog extends BaseFormDialog {

  private final JComboBox<String> jobC;
  private final List<JobApplication> jobs;
  private final JTextField nameF    = field();
  private final JTextField emailF   = field();
  private final JTextField companyF = field();
  private final JTextField phoneF   = field();
  private final JTextField linkedinF= field();
  private final JTextArea notesA   = area("");

  private String name, email, company, phone, linkedin, notes;
  private int applicationId;

  // Add mode — needs job list
  public RecruiterFormDialog(Frame parent, List<JobApplication> jobs) {
    this(parent, jobs, null);
  }

  // Edit mode — job combo hidden, only contact fields shown
  public RecruiterFormDialog(Frame parent, List<JobApplication> jobs, Recruiter r) {
    super(parent, r == null ? "Add Recruiter" : "Edit Recruiter");
    this.jobs = jobs;
    String[] opts = jobs.stream()
        .map(j -> j.getApplicationId() + " — " + j.getCompanyName() + " | " + j.getRole())
        .toArray(String[]::new);
    jobC = new JComboBox<>(opts);

    if (r != null) {
      nameF.setText(r.getRecruiterName());
      emailF.setText(r.getRecruiterEmail());
      companyF.setText(r.getCompanyName() != null ? r.getCompanyName() : "");
      phoneF.setText(r.getPhone() != null ? r.getPhone() : "");
      linkedinF.setText(r.getLinkedin() != null ? r.getLinkedin() : "");
      notesA.setText(r.getNotes() != null ? r.getNotes() : "");
    }

    JPanel p = new JPanel(new GridBagLayout());
    var lc = lc(); var fc = fc();
    int i = 0;
    if (r == null) row(p, lc, fc, i++, "Job Application *", jobC);
    row(p, lc, fc, i++, "Name *",    nameF);
    row(p, lc, fc, i++, "Email *",   emailF);
    row(p, lc, fc, i++, "Company",   companyF);
    row(p, lc, fc, i++, "Phone",     phoneF);
    row(p, lc, fc, i++, "LinkedIn",  linkedinF);
    row(p, lc, fc, i,   "Notes",     notesA);
    finishBuild(p);
  }

  @Override
  protected void onSave() {
    name     = nameF.getText().trim();
    email    = emailF.getText().trim();
    company  = companyF.getText().trim();
    phone    = phoneF.getText().trim();
    linkedin = linkedinF.getText().trim();
    notes    = notesA.getText().trim();
    if (name.isEmpty())  { err("Name is required."); return; }
    if (email.isEmpty()) { err("Email is required."); return; }
    if (!jobs.isEmpty()) applicationId = jobs.get(jobC.getSelectedIndex()).getApplicationId();
    confirmed = true; dispose();
  }

  public int    getApplicationId() { return applicationId; }
  public String getName()          { return name; }
  public String getEmail()         { return email; }
  public String getCompany()       { return company; }
  public String getPhone()         { return phone; }
  public String getLinkedin()      { return linkedin; }
  public String getNotes()         { return notes; }
}
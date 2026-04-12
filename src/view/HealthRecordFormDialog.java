package view;


import java.awt.Frame;
import java.awt.GridBagLayout;
import java.time.LocalDate;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import model.HealthRecord;

public class HealthRecordFormDialog extends BaseFormDialog {

  private final JComboBox<String>
      typeC = combo("Doctor Visit","Vaccination","Dental","Vision","Insurance","Prescription","Other");
  private final JTextField providerF   = field();
  private final JTextField visitF      = field("YYYY-MM-DD");
  private final JTextField nextDueF    = field("YYYY-MM-DD or blank");
  private final JTextField insProvF    = field();
  private final JTextField insIdF      = field();
  private final JTextField descF       = field();
  private final JTextArea notesA      = area("");

  private String type, provider, insProvider, insId, description, notes;
  private LocalDate visitDate, nextDue;

  public HealthRecordFormDialog(Frame parent, HealthRecord h) {
    super(parent, h == null ? "Add Health Record" : "Edit Health Record");
    if (h != null) {
      typeC.setSelectedItem(h.getRecordType());
      providerF.setText(h.getProviderName());
      visitF.setText(h.getVisitDate().toString());
      nextDueF.setText(h.getNextDueDate() != null ? h.getNextDueDate().toString() : "");
      insProvF.setText(h.getInsuranceProvider() != null ? h.getInsuranceProvider() : "");
      insIdF.setText(h.getInsuranceId() != null ? h.getInsuranceId() : "");
      descF.setText(h.getDescription() != null ? h.getDescription() : "");
      notesA.setText(h.getNotes() != null ? h.getNotes() : "");
    }
    JPanel p = new JPanel(new GridBagLayout());
    var lc = lc(); var fc = fc();
    row(p, lc, fc, 0, "Type",               typeC);
    row(p, lc, fc, 1, "Provider *",          providerF);
    row(p, lc, fc, 2, "Visit Date *",        visitF);
    row(p, lc, fc, 3, "Next Due",            nextDueF);
    row(p, lc, fc, 4, "Insurance Provider",  insProvF);
    row(p, lc, fc, 5, "Insurance ID",        insIdF);
    row(p, lc, fc, 6, "Description",         descF);
    row(p, lc, fc, 7, "Notes",               notesA);
    finishBuild(p);
  }

  @Override
  protected void onSave() {
    type        = (String) typeC.getSelectedItem();
    provider    = providerF.getText().trim();
    insProvider = insProvF.getText().trim();
    insId       = insIdF.getText().trim();
    description = descF.getText().trim();
    notes       = notesA.getText().trim();
    if (provider.isEmpty()) { err("Provider is required."); return; }
    try { visitDate = LocalDate.parse(visitF.getText().trim()); }
    catch (Exception e) { err("Use YYYY-MM-DD for visit date."); return; }
    String nd = nextDueF.getText().trim();
    if (!nd.isEmpty() && !nd.startsWith("Y")) {
      try { nextDue = LocalDate.parse(nd); }
      catch (Exception e) { err("Use YYYY-MM-DD for next due date."); return; }
    }
    confirmed = true; dispose();
  }


  // AFTER:
  public String getRecordType() { return type; }
  public String    getProvider()    { return provider; }
  public LocalDate getVisitDate()   { return visitDate; }
  public LocalDate getNextDue()     { return nextDue; }
  public String    getInsProvider() { return insProvider; }
  public String    getInsId()       { return insId; }
  public String    getDescription() { return description; }
  public String    getNotes()       { return notes; }
}
package view;

import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import model.Housing;
import java.awt.*;
import java.time.LocalDate;

public class HousingFormDialog extends BaseFormDialog {

  private final JTextField addressF = field();
  private final JTextField startF   = field("YYYY-MM-DD");
  private final JTextField endF     = field("YYYY-MM-DD");
  private final JTextField landlordF= field();
  private final JTextField contactF = field();
  private final JTextField rentF    = field();
  private final JTextArea notesA   = area("");

  private String address, landlord, contact, notes;
  private LocalDate start, end;
  private double rent;

  public HousingFormDialog(Frame parent, Housing h) {
    super(parent, h == null ? "Add Housing" : "Edit Housing");
    if (h != null) {
      addressF.setText(h.getAddress());
      startF.setText(h.getLeaseStartDate().toString());
      endF.setText(h.getLeaseEndDate().toString());
      landlordF.setText(h.getLandlordName());
      contactF.setText(h.getLandlordContact());
      rentF.setText(String.valueOf(h.getMonthlyRent()));
      notesA.setText(h.getNotes() != null ? h.getNotes() : "");
    }
    JPanel p = new JPanel(new GridBagLayout());
    var lc = lc(); var fc = fc();
    row(p, lc, fc, 0, "Address *",      addressF);
    row(p, lc, fc, 1, "Lease Start *",  startF);
    row(p, lc, fc, 2, "Lease End *",    endF);
    row(p, lc, fc, 3, "Landlord *",     landlordF);
    row(p, lc, fc, 4, "Contact",        contactF);
    row(p, lc, fc, 5, "Monthly Rent *", rentF);
    row(p, lc, fc, 6, "Notes",          notesA);
    finishBuild(p);
  }

  @Override
  protected void onSave() {
    address  = addressF.getText().trim();
    landlord = landlordF.getText().trim();
    contact  = contactF.getText().trim();
    notes    = notesA.getText().trim();
    if (address.isEmpty())  { err("Address is required."); return; }
    if (landlord.isEmpty()) { err("Landlord name is required."); return; }
    try { start = LocalDate.parse(startF.getText().trim()); end = LocalDate.parse(endF.getText().trim()); }
    catch (Exception e) { err("Use YYYY-MM-DD for dates."); return; }
    if (!end.isAfter(start)) { err("Lease end must be after lease start."); return; }
    try { rent = Double.parseDouble(rentF.getText().trim()); }
    catch (Exception e) { err("Rent must be a number."); return; }
    if (rent <= 0) { err("Rent must be greater than 0."); return; }
    confirmed = true; dispose();
  }

  public String    getAddress()  { return address; }
  public LocalDate getStart()    { return start; }
  public LocalDate getEnd()      { return end; }
  public String    getLandlord() { return landlord; }
  public String    getContact()  { return contact; }
  public double    getRent()     { return rent; }
  public String    getNotes()    { return notes; }
}
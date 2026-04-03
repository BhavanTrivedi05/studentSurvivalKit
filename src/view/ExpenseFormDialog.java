package view;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;
import model.Expense;
import java.awt.*;
import java.time.LocalDate;

public class ExpenseFormDialog extends BaseFormDialog {

  private final JTextField amountF = field();
  private final JComboBox<String>
      categoryC = combo("Rent","Food","Transport","Tuition","Utilities","Health","Entertainment","Other");
  private final JTextField dateF   = field("YYYY-MM-DD");
  private final JTextField descF   = field();
  private final JTextField currF   = field("USD");

  private double amount;
  private String category, description, currency;
  private LocalDate date;

  public ExpenseFormDialog(Frame parent, Expense e) {
    super(parent, e == null ? "Add Expense" : "Edit Expense");
    if (e != null) {
      amountF.setText(String.valueOf(e.getAmount()));
      categoryC.setSelectedItem(e.getCategory());
      dateF.setText(e.getExpenseDate().toString());
      descF.setText(e.getDescription());
      currF.setText(e.getCurrency());
    }
    JPanel p = new JPanel(new GridBagLayout());
    var lc = lc(); var fc = fc();
    row(p, lc, fc, 0, "Amount *",     amountF);
    row(p, lc, fc, 1, "Category",     categoryC);
    row(p, lc, fc, 2, "Date *",       dateF);
    row(p, lc, fc, 3, "Description *",descF);
    row(p, lc, fc, 4, "Currency",     currF);
    finishBuild(p);
  }

  @Override
  protected void onSave() {
    category    = (String) categoryC.getSelectedItem();
    description = descF.getText().trim();
    currency    = currF.getText().trim();
    if (description.isEmpty()) { err("Description is required."); return; }
    try { amount = Double.parseDouble(amountF.getText().trim()); }
    catch (Exception e) { err("Amount must be a number."); return; }
    if (amount <= 0) { err("Amount must be greater than 0."); return; }
    try { date = LocalDate.parse(dateF.getText().trim()); }
    catch (Exception e) { err("Use YYYY-MM-DD for date."); return; }
    confirmed = true; dispose();
  }

  public double    getAmount()      { return amount; }
  public String    getCategory()    { return category; }
  public LocalDate getDate()        { return date; }
  public String    getDescription() { return description; }
  public String    getCurrency()    { return currency; }
}
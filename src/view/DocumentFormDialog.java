package view;

import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import model.Document;
import java.awt.*;
import java.time.LocalDate;

public class DocumentFormDialog extends BaseFormDialog {

  private final JTextField typeF   = field();
  private final JTextField issueF  = field("YYYY-MM-DD");
  private final JTextField expiryF = field("YYYY-MM-DD");
  private final JTextField authF   = field();
  private final JTextArea notesA  = area("");

  private String docType, authority, notes;
  private LocalDate issueDate, expiryDate;

  public DocumentFormDialog(Frame parent, Document doc) {
    super(parent, doc == null ? "Add Document" : "Edit Document");
    if (doc != null) {
      typeF.setText(doc.getDocType());
      issueF.setText(doc.getIssueDate().toString());
      expiryF.setText(doc.getExpiryDate().toString());
      authF.setText(doc.getIssuingAuthority());
      notesA.setText(doc.getNotes() != null ? doc.getNotes() : "");
    }
    JPanel p = new JPanel(new GridBagLayout());
    var lc = lc(); var fc = fc();
    row(p, lc, fc, 0, "Document Type *", typeF);
    row(p, lc, fc, 1, "Issue Date *",    issueF);
    row(p, lc, fc, 2, "Expiry Date *",   expiryF);
    row(p, lc, fc, 3, "Authority *",     authF);
    row(p, lc, fc, 4, "Notes",           notesA);
    finishBuild(p);
  }

  @Override
  protected void onSave() {
    docType   = typeF.getText().trim();
    authority = authF.getText().trim();
    notes     = notesA.getText().trim();
    if (docType.isEmpty())   { err("Document type is required."); return; }
    if (authority.isEmpty()) { err("Issuing authority is required."); return; }
    try {
      issueDate  = LocalDate.parse(issueF.getText().trim());
      expiryDate = LocalDate.parse(expiryF.getText().trim());
    } catch (Exception e) { err("Use YYYY-MM-DD for dates."); return; }
    if (!expiryDate.isAfter(issueDate)) { err("Expiry must be after issue date."); return; }
    confirmed = true; dispose();
  }

  public String    getDocType()    { return docType; }
  public LocalDate getIssueDate()  { return issueDate; }
  public LocalDate getExpiryDate() { return expiryDate; }
  public String    getAuthority()  { return authority; }
  public String    getNotes()      { return notes; }
}
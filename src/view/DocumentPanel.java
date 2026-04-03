package view;

import controller.DocumentController;
import javax.swing.*; import javax.swing.table.DefaultTableModel;
import java.awt.*; import java.time.LocalDate; import java.time.temporal.ChronoUnit;

public class DocumentPanel extends JPanel {
  private final DocumentController ctrl;
  private JTable table; private DefaultTableModel mdl;

  public DocumentPanel(int sid) {
    ctrl = new DocumentController(sid);
    init(); reload();
  }
  private void reload() {
    mdl.setRowCount(0);
    for (var d : ctrl.getAllDocuments()) {
      mdl.addRow(new Object[]{d.getDocumentId(), d.getDocType(), d.getIssueDate(),
          d.getExpiryDate(), d.getIssuingAuthority(), d.getNotes(),
          ChronoUnit.DAYS.between(LocalDate.now(), d.getExpiryDate())});
    }
  }
  private void showAdd() {
    JTextField t=f(), i=f("YYYY-MM-DD"), ex=f("YYYY-MM-DD"), a=f(), n=f();
    if (dlg("Add Document", "Type:",t, "Issue Date:",i, "Expiry Date:",ex, "Authority:",a, "Notes:",n)) {
      try { if(ctrl.addDocument(t.getText().trim(),LocalDate.parse(i.getText().trim()),LocalDate.parse(ex.getText().trim()),a.getText().trim(),n.getText().trim())){reload();ok("Added!");}else err("Check fields."); }
      catch(Exception x){err("Use YYYY-MM-DD.");}
    }
  }
  private void showEdit() {
    int row=sel(); if(row<0) return;
    int id=(int)mdl.getValueAt(row,0);
    JTextField t=f(s(row,1)),i=f(s(row,2)),ex=f(s(row,3)),a=f(s(row,4)),n=f(s(row,5));
    if (dlg("Edit Document","Type:",t,"Issue Date:",i,"Expiry Date:",ex,"Authority:",a,"Notes:",n)) {
      try { if(ctrl.update(id,t.getText().trim(),LocalDate.parse(i.getText().trim()),LocalDate.parse(ex.getText().trim()),a.getText().trim(),n.getText().trim())){reload();ok("Updated!");}else err("Update failed."); }
      catch(Exception x){err("Use YYYY-MM-DD.");}
    }
  }
  private void init() {
    setLayout(new BorderLayout()); setBackground(BG);
    setBorder(BorderFactory.createEmptyBorder(24,24,24,24));
    mdl=new DefaultTableModel(new String[]{"ID","Type","Issue Date","Expiry Date","Authority","Notes","Days Left"},0){public boolean isCellEditable(int r,int c){return false;}};
    table=makeTable(); hideCol(0);
    add(hdr("Documents",e->showAdd()),BorderLayout.NORTH);
    add(new JScrollPane(table){{setBorder(BorderFactory.createLineBorder(new Color(220,223,235)));}},BorderLayout.CENTER);
    add(bar(),BorderLayout.SOUTH);
  }
  private JPanel bar(){JPanel p=new JPanel(new FlowLayout(FlowLayout.RIGHT,8,10));p.setOpaque(false);JButton e=btn("✎ Edit",DARK),d=btn("✕ Delete",RED);e.addActionListener(x->showEdit());d.addActionListener(x->del());p.add(e);p.add(d);return p;}
  private void del(){int row=sel();if(row<0)return;if(JOptionPane.showConfirmDialog(this,"Delete?","Confirm",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION){if(ctrl.deleteById((int)mdl.getValueAt(row,0))){reload();ok("Deleted.");}else err("Delete failed.");}}
  private int sel(){int r=table.getSelectedRow();if(r<0)info("Select a row first.");return r;}
  private boolean dlg(String title,Object...items){return JOptionPane.showConfirmDialog(this,items,title,JOptionPane.OK_CANCEL_OPTION,JOptionPane.PLAIN_MESSAGE)==JOptionPane.OK_OPTION;}
  private JPanel hdr(String title,java.awt.event.ActionListener a){JPanel h=new JPanel(new BorderLayout());h.setOpaque(false);h.setBorder(BorderFactory.createEmptyBorder(0,0,16,0));JLabel l=new JLabel(title);l.setFont(new Font("SansSerif",Font.BOLD,24));l.setForeground(DARK);h.add(l,BorderLayout.WEST);JButton b=btn("+ Add New",BLUE);b.addActionListener(a);h.add(b,BorderLayout.EAST);return h;}
  private JTable makeTable(){JTable t=new JTable(mdl);t.setRowHeight(38);t.setFont(new Font("SansSerif",Font.PLAIN,13));t.getTableHeader().setFont(new Font("SansSerif",Font.BOLD,13));t.getTableHeader().setBackground(new Color(237,239,245));t.getTableHeader().setForeground(new Color(50,60,80));t.setGridColor(new Color(230,232,240));t.setSelectionBackground(new Color(219,234,254));t.setSelectionForeground(DARK);t.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);t.setShowVerticalLines(false);return t;}
  private void hideCol(int...cols){for(int c:cols){table.getColumnModel().getColumn(c).setMinWidth(0);table.getColumnModel().getColumn(c).setMaxWidth(0);}}
  static final Color BG=new Color(245,246,250),BLUE=new Color(37,99,235),DARK=new Color(30,42,58),RED=new Color(200,40,40);
  static JButton btn(String l,Color bg){JButton b=new JButton(l);b.setBackground(bg);b.setForeground(Color.WHITE);b.setOpaque(true);b.setContentAreaFilled(true);b.setBorderPainted(false);b.setFocusPainted(false);b.setFont(new Font("SansSerif",Font.PLAIN,13));b.setBorder(BorderFactory.createEmptyBorder(8,14,8,14));b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));return b;}
  static JTextField f(){return f("");}
  static JTextField f(String v){JTextField t=new JTextField(v,22);t.setFont(new Font("SansSerif",Font.PLAIN,13));t.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(210,215,225),1,true),BorderFactory.createEmptyBorder(4,8,4,8)));return t;}
  String s(int r,int c){Object v=mdl.getValueAt(r,c);return v!=null?v.toString():"";}
  void ok(String m){JOptionPane.showMessageDialog(this,m,"Success",JOptionPane.INFORMATION_MESSAGE);}
  void err(String m){JOptionPane.showMessageDialog(this,m,"Error",JOptionPane.ERROR_MESSAGE);}
  void info(String m){JOptionPane.showMessageDialog(this,m,"Info",JOptionPane.INFORMATION_MESSAGE);}
}

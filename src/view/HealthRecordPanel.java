package view;

import controller.HealthRecordController;
import javax.swing.*; import javax.swing.table.DefaultTableModel;
import java.awt.*; import java.time.LocalDate;
import static view.DocumentPanel.*;

public class HealthRecordPanel extends JPanel {
  private final HealthRecordController ctrl;
  private JTable table; private DefaultTableModel mdl;

  public HealthRecordPanel(int sid){ctrl=new HealthRecordController(sid);init();reload();}
  private void reload(){
    mdl.setRowCount(0);
    for(var h:ctrl.getAll())
      mdl.addRow(new Object[]{h.getHealthId(),h.getRecordType(),h.getProviderName(),h.getVisitDate(),h.getNextDueDate(),h.getInsuranceProvider(),h.getInsuranceId(),h.getDescription(),h.getNotes()});
  }
  private void showAdd(){
    JComboBox<String> ty=combo("Doctor Visit","Vaccination","Dental","Vision","Insurance","Prescription","Other");
    JTextField pr=f(),vi=f("YYYY-MM-DD"),nd=f("YYYY-MM-DD or blank"),ip=f(),ii=f(),de=f(),no=f();
    if(dlg("Add Health Record","Type:",ty,"Provider:",pr,"Visit Date:",vi,"Next Due:",nd,"Insurance Provider:",ip,"Insurance ID:",ii,"Description:",de,"Notes:",no)){
      try{String n=nd.getText().trim();LocalDate ndd=n.isEmpty()||n.startsWith("Y")?null:LocalDate.parse(n);
        if(ctrl.add((String)ty.getSelectedItem(),pr.getText().trim(),LocalDate.parse(vi.getText().trim()),ndd,ip.getText().trim(),ii.getText().trim(),de.getText().trim(),no.getText().trim())){reload();ok("Added!");}else err("Type and provider required.");}
      catch(Exception x){err("Use YYYY-MM-DD for dates.");}
    }
  }
  private void showEdit(){
    int row=sel();if(row<0)return; int id=(int)mdl.getValueAt(row,0);
    JComboBox<String> ty=combo("Doctor Visit","Vaccination","Dental","Vision","Insurance","Prescription","Other"); ty.setSelectedItem(s(row,1));
    JTextField pr=f(s(row,2)),vi=f(s(row,3)),nd=f(s(row,4)),ip=f(s(row,5)),ii=f(s(row,6)),de=f(s(row,7)),no=f(s(row,8));
    if(dlg("Edit Health Record","Type:",ty,"Provider:",pr,"Visit Date:",vi,"Next Due:",nd,"Insurance Provider:",ip,"Insurance ID:",ii,"Description:",de,"Notes:",no)){
      try{String n=nd.getText().trim();LocalDate ndd=n.isEmpty()||n.equals("null")?null:LocalDate.parse(n);
        if(ctrl.update(id,(String)ty.getSelectedItem(),pr.getText().trim(),LocalDate.parse(vi.getText().trim()),ndd,ip.getText().trim(),ii.getText().trim(),de.getText().trim(),no.getText().trim())){reload();ok("Updated!");}else err("Update failed.");}
      catch(Exception x){err("Use YYYY-MM-DD for dates.");}
    }
  }
  private void init(){
    setLayout(new BorderLayout());setBackground(BG);setBorder(BorderFactory.createEmptyBorder(24,24,24,24));
    mdl=new DefaultTableModel(new String[]{"ID","Type","Provider","Visit Date","Next Due","Insurance","Ins. ID","Description","Notes"},0){public boolean isCellEditable(int r,int c){return false;}};
    table=makeTable(mdl);hideCol(0);
    add(hdr("Health Records",e->showAdd()),BorderLayout.NORTH);
    add(new JScrollPane(table){{setBorder(BorderFactory.createLineBorder(new Color(220,223,235)));}},BorderLayout.CENTER);
    add(bar(),BorderLayout.SOUTH);
  }
  private JPanel bar(){JPanel p=new JPanel(new FlowLayout(FlowLayout.RIGHT,8,10));p.setOpaque(false);JButton e=btn("✎ Edit",DARK),d=btn("✕ Delete",RED);e.addActionListener(x->showEdit());d.addActionListener(x->del());p.add(e);p.add(d);return p;}
  private void del(){int row=sel();if(row<0)return;if(JOptionPane.showConfirmDialog(this,"Delete?","Confirm",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION){if(ctrl.deleteById((int)mdl.getValueAt(row,0))){reload();ok("Deleted.");}}}
  private int sel(){int r=table.getSelectedRow();if(r<0)info("Select a row first.");return r;}
  private boolean dlg(String t,Object...items){return JOptionPane.showConfirmDialog(this,items,t,JOptionPane.OK_CANCEL_OPTION,JOptionPane.PLAIN_MESSAGE)==JOptionPane.OK_OPTION;}
  private JPanel hdr(String t,java.awt.event.ActionListener a){JPanel h=new JPanel(new BorderLayout());h.setOpaque(false);h.setBorder(BorderFactory.createEmptyBorder(0,0,16,0));JLabel l=new JLabel(t);l.setFont(new Font("SansSerif",Font.BOLD,24));l.setForeground(DARK);h.add(l,BorderLayout.WEST);JButton b=btn("+ Add New",BLUE);b.addActionListener(a);h.add(b,BorderLayout.EAST);return h;}
  private JTable makeTable(DefaultTableModel m){JTable t=new JTable(m);t.setRowHeight(38);t.setFont(new Font("SansSerif",Font.PLAIN,13));t.getTableHeader().setFont(new Font("SansSerif",Font.BOLD,13));t.getTableHeader().setBackground(new Color(237,239,245));t.setGridColor(new Color(230,232,240));t.setSelectionBackground(new Color(219,234,254));t.setSelectionForeground(DARK);t.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);t.setShowVerticalLines(false);return t;}
  private void hideCol(int...cols){for(int c:cols){table.getColumnModel().getColumn(c).setMinWidth(0);table.getColumnModel().getColumn(c).setMaxWidth(0);}}
  private JComboBox<String> combo(String...items){return new JComboBox<>(items);}
  String s(int r,int c){Object v=mdl.getValueAt(r,c);return v!=null?v.toString():"";}
  void ok(String m){JOptionPane.showMessageDialog(this,m,"Success",JOptionPane.INFORMATION_MESSAGE);}
  void err(String m){JOptionPane.showMessageDialog(this,m,"Error",JOptionPane.ERROR_MESSAGE);}
  void info(String m){JOptionPane.showMessageDialog(this,m,"Info",JOptionPane.INFORMATION_MESSAGE);}
}
package view;

import controller.ExpenseController;
import javax.swing.*; import javax.swing.table.DefaultTableModel;
import java.awt.*; import java.time.LocalDate;
import static view.DocumentPanel.*;

public class ExpensePanel extends JPanel {
  private final ExpenseController ctrl;
  private JTable table; private DefaultTableModel mdl;

  public ExpensePanel(int sid){ctrl=new ExpenseController(sid);init();reload();}
  private void reload(){
    mdl.setRowCount(0);
    for(var e:ctrl.getAll())
      mdl.addRow(new Object[]{e.getExpenseId(),e.getAmount(),e.getCategory(),e.getExpenseDate(),e.getDescription(),e.getCurrency()});
  }
  private void showAdd(){
    JTextField am=f(),da=f("YYYY-MM-DD"),de=f(),cu=f("USD");
    JComboBox<String> ca=combo("Rent","Food","Transport","Tuition","Utilities","Health","Entertainment","Other");
    if(dlg("Add Expense","Amount:",am,"Category:",ca,"Date:",da,"Description:",de,"Currency:",cu)){
      try{if(ctrl.add(Double.parseDouble(am.getText().trim()),(String)ca.getSelectedItem(),LocalDate.parse(da.getText().trim()),de.getText().trim(),cu.getText().trim())){reload();ok("Added!");}else err("Amount must be > 0.");}
      catch(Exception x){err("Check amount and date format.");}
    }
  }
  private void showEdit(){
    int row=sel();if(row<0)return; int id=(int)mdl.getValueAt(row,0);
    JTextField am=f(s(row,1)),da=f(s(row,3)),de=f(s(row,4)),cu=f(s(row,5));
    JComboBox<String> ca=combo("Rent","Food","Transport","Tuition","Utilities","Health","Entertainment","Other"); ca.setSelectedItem(s(row,2));
    if(dlg("Edit Expense","Amount:",am,"Category:",ca,"Date:",da,"Description:",de,"Currency:",cu)){
      try{if(ctrl.update(id,Double.parseDouble(am.getText().trim()),(String)ca.getSelectedItem(),LocalDate.parse(da.getText().trim()),de.getText().trim(),cu.getText().trim())){reload();ok("Updated!");}else err("Update failed.");}
      catch(Exception x){err("Check amount and date format.");}
    }
  }
  private void init(){
    setLayout(new BorderLayout());setBackground(BG);setBorder(BorderFactory.createEmptyBorder(24,24,24,24));
    mdl=new DefaultTableModel(new String[]{"ID","Amount","Category","Date","Description","Currency"},0){public boolean isCellEditable(int r,int c){return false;}};
    table=makeTable(mdl);hideCol(0);
    add(hdr("Expenses",e->showAdd()),BorderLayout.NORTH);
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


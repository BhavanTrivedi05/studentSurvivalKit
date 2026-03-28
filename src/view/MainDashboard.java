package view;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class MainDashboard extends JFrame {

  private int studentId;
  private String studentName;
  private JPanel contentPanel;
  private CardLayout cardLayout;
  private JButton activeButton = null;

  // Accept studentId + name — no Student object needed here
  public MainDashboard(int studentId, String studentName) {
    this.studentId   = studentId;
    this.studentName = studentName;

    setTitle("StudentSurvivalKit");
    setSize(1280, 800);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLayout(new BorderLayout());
    setLocationRelativeTo(null);

    add(buildSidebar(), BorderLayout.WEST);
    add(buildContentArea(), BorderLayout.CENTER);
  }

  // ── Sidebar ────────────────────────────────────────────
  private JPanel buildSidebar() {
    JPanel sidebar = new JPanel();
    sidebar.setPreferredSize(new Dimension(220, 800));
    sidebar.setBackground(new Color(30, 42, 58));
    sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));

    JLabel title = new JLabel("  StudentSurvivalKit");
    title.setForeground(Color.WHITE);
    title.setFont(new Font("SansSerif", Font.BOLD, 14));
    title.setBorder(BorderFactory.createEmptyBorder(24, 0, 8, 0));
    title.setAlignmentX(Component.LEFT_ALIGNMENT);
    sidebar.add(title);

    JLabel nameLabel = new JLabel("  " + studentName);
    nameLabel.setForeground(new Color(160, 175, 200));
    nameLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
    nameLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 16, 0));
    nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
    sidebar.add(nameLabel);

    sidebar.add(buildDivider());

    String[] modules = {
        "Dashboard", "Documents", "Deadlines", "Job Applications",
        "Expenses", "Housing", "Courses", "Health Records",
        "Contacts", "Recruiters"
    };
    for (String m : modules) {
      sidebar.add(buildNavButton(m));
    }
    return sidebar;
  }

  private JPanel buildDivider() {
    JPanel d = new JPanel();
    d.setMaximumSize(new Dimension(220, 1));
    d.setBackground(new Color(50, 65, 85));
    return d;
  }

  private JButton buildNavButton(String label) {
    JButton btn = new JButton("  " + label);
    btn.setForeground(new Color(190, 200, 220));
    btn.setBackground(new Color(30, 42, 58));
    btn.setBorderPainted(false);
    btn.setFocusPainted(false);
    btn.setHorizontalAlignment(SwingConstants.LEFT);
    btn.setMaximumSize(new Dimension(220, 44));
    btn.setPreferredSize(new Dimension(220, 44));
    btn.setFont(new Font("SansSerif", Font.PLAIN, 13));
    btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

    btn.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseEntered(java.awt.event.MouseEvent e) {
        if (btn != activeButton)
          btn.setBackground(new Color(45, 58, 78));
      }
      public void mouseExited(java.awt.event.MouseEvent e) {
        if (btn != activeButton)
          btn.setBackground(new Color(30, 42, 58));
      }
    });

    btn.addActionListener(e -> {
      switchPanel(label);
      setActiveButton(btn);
    });

    return btn;
  }

  private void setActiveButton(JButton btn) {
    if (activeButton != null) {
      activeButton.setBackground(new Color(30, 42, 58));
      activeButton.setForeground(new Color(190, 200, 220));
    }
    activeButton = btn;
    activeButton.setBackground(new Color(37, 99, 235));
    activeButton.setForeground(Color.WHITE);
  }

  // ── Content Area ───────────────────────────────────────
  private JPanel buildContentArea() {
    cardLayout   = new CardLayout();
    contentPanel = new JPanel(cardLayout);
    contentPanel.setBackground(new Color(245, 246, 250));

    // Dashboard home panel
    contentPanel.add(buildDashboardHome(), "Dashboard");

    // All module panels wired with studentId
    contentPanel.add(new DocumentPanel(studentId),       "Documents");
    contentPanel.add(new DeadlinePanel(studentId),       "Deadlines");
    contentPanel.add(new JobApplicationPanel(studentId), "Job Applications");
    contentPanel.add(new ExpensePanel(studentId),        "Expenses");
    contentPanel.add(new HousingPanel(studentId),        "Housing");
    contentPanel.add(new CoursePanel(studentId),         "Courses");
    contentPanel.add(new HealthRecordPanel(studentId),   "Health Records");
    contentPanel.add(new ContactPanel(studentId),        "Contacts");
    contentPanel.add(new RecruiterPanel(studentId),      "Recruiters");

    return contentPanel;
  }

  private void switchPanel(String name) {
    cardLayout.show(contentPanel, name);
  }

  // ── Dashboard Home ─────────────────────────────────────
  private JPanel buildDashboardHome() {
    JPanel panel = new JPanel(new BorderLayout());
    panel.setBackground(new Color(245, 246, 250));
    panel.setBorder(BorderFactory.createEmptyBorder(32, 32, 32, 32));

    JLabel welcome = new JLabel("Good morning, " + studentName + "!");
    welcome.setFont(new Font("SansSerif", Font.BOLD, 26));
    welcome.setForeground(new Color(30, 42, 58));

    JLabel subtitle = new JLabel("Here's your summary for today");
    subtitle.setFont(new Font("SansSerif", Font.PLAIN, 14));
    subtitle.setForeground(new Color(100, 110, 130));

    JPanel header = new JPanel();
    header.setOpaque(false);
    header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
    header.add(welcome);
    header.add(Box.createVerticalStrut(6));
    header.add(subtitle);

    panel.add(header, BorderLayout.NORTH);
    panel.add(buildSummaryCards(), BorderLayout.CENTER);
    return panel;
  }

  private JPanel buildSummaryCards() {
    JPanel cards = new JPanel(new GridLayout(1, 4, 16, 0));
    cards.setOpaque(false);
    cards.setBorder(BorderFactory.createEmptyBorder(32, 0, 0, 0));

    cards.add(buildCard("Documents Expiring", "—", new Color(220, 38, 38)));
    cards.add(buildCard("Deadlines This Week", "—", new Color(234, 88, 12)));
    cards.add(buildCard("Job Applications",    "—", new Color(37, 99, 235)));
    cards.add(buildCard("Monthly Expenses",    "—", new Color(22, 163, 74)));

    return cards;
  }

  private JPanel buildCard(String title, String value, Color accent) {
    JPanel card = new JPanel();
    card.setBackground(Color.WHITE);
    card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
    card.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(new Color(225, 228, 235), 1, true),
        BorderFactory.createEmptyBorder(20, 20, 20, 20)
    ));

    JLabel titleLabel = new JLabel(title);
    titleLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
    titleLabel.setForeground(new Color(100, 110, 130));

    JLabel valueLabel = new JLabel(value);
    valueLabel.setFont(new Font("SansSerif", Font.BOLD, 28));
    valueLabel.setForeground(accent);

    card.add(titleLabel);
    card.add(Box.createVerticalStrut(8));
    card.add(valueLabel);
    return card;
  }

  // ── Placeholder for unbuilt modules ───────────────────
  private JPanel placeholder(String name) {
    JPanel p = new JPanel(new BorderLayout());
    p.setBackground(new Color(245, 246, 250));
    JLabel lbl = new JLabel(name + " — coming soon", SwingConstants.CENTER);
    lbl.setFont(new Font("SansSerif", Font.PLAIN, 18));
    lbl.setForeground(new Color(150, 160, 180));
    p.add(lbl, BorderLayout.CENTER);
    return p;
  }
}
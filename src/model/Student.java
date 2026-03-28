package model;

import db.DBConnection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Student {
  private int studentId;
  private String email;
  private String firstName;
  private String lastName;
  private String nationality;
  private String homeCountry;
  private String program;
  private String visaType;
  private int graduationYear;

  // Constructor
  public Student(String email, String firstName, String lastName,
                 String nationality, String homeCountry,
                 String program, String visaType, int graduationYear) {
    this.email          = email;
    this.firstName      = firstName;
    this.lastName       = lastName;
    this.nationality    = nationality;
    this.homeCountry    = homeCountry;
    this.program        = program;
    this.visaType       = visaType;
    this.graduationYear = graduationYear;
  }

  // DB Methods
  public void save() throws SQLException {
    String sql = "INSERT INTO Student (email, first_name, last_name, nationality, " +
        "home_country, program, visa_type, graduation_year) VALUES (?,?,?,?,?,?,?,?)";
    PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
    ps.setString(1, email);
    ps.setString(2, firstName);
    ps.setString(3, lastName);
    ps.setString(4, nationality);
    ps.setString(5, homeCountry);
    ps.setString(6, program);
    ps.setString(7, visaType);
    ps.setInt(8, graduationYear);
    ps.executeUpdate();
  }

  public static Student get() throws SQLException {
    String sql = "SELECT * FROM Student LIMIT 1";
    PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
    ResultSet rs = ps.executeQuery();
    if (rs.next()) {
      Student s = new Student(
          rs.getString("email"),
          rs.getString("first_name"),
          rs.getString("last_name"),
          rs.getString("nationality"),
          rs.getString("home_country"),
          rs.getString("program"),
          rs.getString("visa_type"),
          rs.getInt("graduation_year")
      );
      s.studentId = rs.getInt("student_id");
      return s;
    }
    return null;
  }

  public void update() throws SQLException {
    String sql = "UPDATE Student SET email=?, first_name=?, last_name=?, nationality=?, " +
        "home_country=?, program=?, visa_type=?, graduation_year=? WHERE student_id=?";
    PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
    ps.setString(1, email);
    ps.setString(2, firstName);
    ps.setString(3, lastName);
    ps.setString(4, nationality);
    ps.setString(5, homeCountry);
    ps.setString(6, program);
    ps.setString(7, visaType);
    ps.setInt(8, graduationYear);
    ps.setInt(9, studentId);
    ps.executeUpdate();
  }

  // Getters & Setters
  public int getStudentId()       { return studentId; }
  public String getEmail()        { return email; }
  public String getFirstName()    { return firstName; }
  public String getLastName()     { return lastName; }
  public String getNationality()  { return nationality; }
  public String getHomeCountry()  { return homeCountry; }
  public String getProgram()      { return program; }
  public String getVisaType()     { return visaType; }
  public int getGraduationYear()  { return graduationYear; }
  public void setEmail(String v)        { email = v; }
  public void setFirstName(String v)    { firstName = v; }
  public void setLastName(String v)     { lastName = v; }
  public void setNationality(String v)  { nationality = v; }
  public void setHomeCountry(String v)  { homeCountry = v; }
  public void setProgram(String v)      { program = v; }
  public void setVisaType(String v)     { visaType = v; }
  public void setGraduationYear(int v)  { graduationYear = v; }
}


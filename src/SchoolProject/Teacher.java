package SchoolProject;

import Database.ConnectDatabase;
import java.sql.*;
import java.util.*;
import javax.swing.JOptionPane;
import javax.swing.table.*;

/**
 *
 * @author NMixalis
 */
public class Teacher extends User {

    public Teacher (ConnectDatabase dbConnect) {
        super(dbConnect);
    }

    public void addTeacher(String idTeacher, String specialty, String interests, String phone) {
        try {
            usersRs.moveToInsertRow();
            usersRs.updateString("idteacher", idTeacher.trim());
            usersRs.updateString("eidikotita", specialty.trim());
            usersRs.updateString("endiaferonta", interests.trim());
            usersRs.updateString("tilefono", phone.trim());
            usersRs.insertRow();
            calculateUsersLength();
            reloadData();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
            System.exit(1);
        }
    }

    @Override
    public void delete() {
        try {
            Statement stmt = dbConnect.getConnection().createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            String query = "delete from teacher where idteacher = " + usersRs.getInt("idteacher");
            String query2 = "delete from teaches where teacher_idteacher = " + usersRs.getInt("idteacher");

            stmt.executeUpdate(query2);
            stmt.executeUpdate(query);

            usersRs.deleteRow();
            if (usersRs.isBeforeFirst()) {
                usersRs.first();
            }
            calculateUsersLength();
            reloadData();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
            System.exit(1);
        }
    }

    public void modifyTeacher(String idTeacher, String specialty, String interests, String phone, String newIdTeacher) {
        try {
            String query = "update teacher set idteacher= ? ,eidikotita= ? ,endiaferonta= ? ,tilefono= ? "
                    + "where idteacher = " + Integer.parseInt(idTeacher);
            PreparedStatement stmt = dbConnect.getConnection().prepareStatement(query);

            stmt.setString(1, newIdTeacher.trim());
            stmt.setString(2, specialty.trim());
            stmt.setString(3, interests.trim());
            stmt.setString(4, phone.trim());

            stmt.executeUpdate();
            stmt.close();
            initUsersData();
            reloadData();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
            System.exit(1);
        }
    }

    @Override
    public TableModel table() {

        tableModel = null;

        if (getUsersLength() != 0) {
            try {
                String query = "SELECT * FROM teacher t join users u on t.idteacher = u.idusers";
                Statement stmt = dbConnect.getConnection().createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                stmt.executeQuery(query);

                List<Object> usersList = new ArrayList<>();

                while (stmt.getResultSet().next()) {
                    int idteacher = stmt.getResultSet().getInt("idteacher");
                    String eidikotita = stmt.getResultSet().getString("eidikotita");
                    String endiaferonta = stmt.getResultSet().getString("endiaferonta");
                    String tilefwno = stmt.getResultSet().getString("tilefono");
                    String lastname = stmt.getResultSet().getString("lastname");
                    String firstname = stmt.getResultSet().getString("firstname");

                    List<Object> rowData = new ArrayList<>();

                    rowData.add(idteacher);
                    rowData.add(lastname);
                    rowData.add(firstname);
                    rowData.add(eidikotita);
                    rowData.add(endiaferonta);
                    rowData.add(tilefwno);

                    usersList.add(rowData);
                }

                Object[] columnNames = new String[]{
                    "Serial",
                    "Lastname",
                    "Firstname",
                    "Specialty",
                    "Interests",
                    "Phone"
                };

                Object[][] data = new Object[usersList.size()][];

                for (int i = 0; i < usersList.size(); i++) {
                    ArrayList<Object> rowList = (ArrayList<Object>) usersList.get(i);
                    data[i] = rowList.toArray();
                }

                tableModel = new DefaultTableModel(data, columnNames);

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
                System.exit(1);
            }
        } else {
            JOptionPane.showMessageDialog(null,
                    "There's no User records in the database", "Message", JOptionPane.INFORMATION_MESSAGE);
        }
        return tableModel;
    }
    
    
    @Override
    protected void initUsersData() {
        try {
            statement = dbConnect.getConnection().createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            String query = "select * from teacher";
            usersRs = statement.executeQuery(query);
            usersRs.first();
            calculateUsersLength();
            reloadData();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
            System.exit(1);
        }

    }

    public void reloadData() {
        try {
            if (getUsersLength() == 0) {
                idTeacher = 0;
                specialty = "";
                interests = "";
                phone = "";
            } else {
                idTeacher = usersRs.getInt("idteacher");
                specialty = usersRs.getString("eidikotita");
                interests = usersRs.getString("endiaferonta");
                phone = usersRs.getString("tilefono");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
            System.exit(1);
        }
    }

    @Override
    public void calculateUsersLength() {
        try {
            Statement stmt = dbConnect.getConnection().createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            String query = "select count(*) as cnt from teacher";
            ResultSet countRs = stmt.executeQuery(query);
            countRs.first();
            teachersLength = countRs.getInt("cnt");
            countRs.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
            System.exit(1);
        }
    }

    @Override
    public int getUsersLength() {
        return teachersLength;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public String getInterests() {
        return interests;
    }

    public void setInterests(String interests) {
        this.interests = interests;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getTeachersLength() {
        return teachersLength;
    }

    public void setTeachersLength(int teachersLength) {
        this.teachersLength = teachersLength;
    }

    public int getIdTeacher() {
        return idTeacher;
    }

    public void setIdTeacher(int idTeacher) {
        this.idTeacher = idTeacher;
    }
    
    public void addTeaching(String idLesson, String idTeacher) {
        try {
            Statement stmt = dbConnect.getConnection().createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            String query = "insert into teaches (lesson_idlesson,teacher_idteacher) values "
                    + "(" + idLesson + "," + idTeacher + ")";
            stmt.execute(query);
            stmt.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
            System.exit(1);
        }
    }
    
        public void deleteTeaching(String idLesson, String idTeacher) {
        try {
            String query = "delete from teaches where teacher_idteacher = " + idTeacher
                    + " and lesson_idlesson = " + idLesson;
            PreparedStatement stmt = dbConnect.getConnection().prepareStatement(query);

            stmt.execute(query);
            stmt.close();
            reloadData();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
            System.exit(1);
        }
    }
        
    public TableModel teachingTable() {

        tableModel = null;

        if (getUsersLength() != 0) {
            try {
                String query = "SELECT * FROM teaches t "
                        + "join users u on t.teacher_idteacher = u.idusers "
                        + "join teacher tt on tt.idteacher = u.idusers "
                        + "join lesson l on t.lesson_idlesson = l.idlesson";
                Statement stmt = dbConnect.getConnection().createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                stmt.executeQuery(query);

                List<Object> usersList = new ArrayList<>();

                while (stmt.getResultSet().next()) {
                    int iduser = stmt.getResultSet().getInt("idusers");
                    String lastname = stmt.getResultSet().getString("lastname");
                    String firstname = stmt.getResultSet().getString("firstname");
                    String lessonname = stmt.getResultSet().getString("name");

                    List<Object> rowData = new ArrayList<>();

                    rowData.add(iduser);
                    rowData.add(lastname);
                    rowData.add(firstname);
                    rowData.add(lessonname);

                    usersList.add(rowData);
                }

                Object[] columnNames = new String[]{
                    "Serial",
                    "Lastname",
                    "Firstname",
                    "Lesson"
                };

                Object[][] data = new Object[usersList.size()][];

                for (int i = 0; i < usersList.size(); i++) {
                    ArrayList<Object> rowList = (ArrayList<Object>) usersList.get(i);
                    data[i] = rowList.toArray();
                }

                tableModel = new DefaultTableModel(data, columnNames);

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
                System.exit(1);
            }
        } else {
            JOptionPane.showMessageDialog(null,
                    "There's no User records in the database", "Message", JOptionPane.INFORMATION_MESSAGE);
        }
        return tableModel;
    }
        
    @Override
    public String getFirstName() {
        String firstname = "";
        ResultSet countRs;

        try {
            Statement stmt = dbConnect.getConnection().createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            String query = "select firstname from users where idusers = " + idTeacher;
            countRs = stmt.executeQuery(query);
            countRs.first();
            firstname = countRs.getString("firstname");
            countRs.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
            System.exit(1);
        }
        return firstname;
    }

    @Override
    public String getLastName() {
        String lastname = "";
        ResultSet countRs;

        try {
            Statement stmt = dbConnect.getConnection().createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            String query = "select lastname from users where idusers = " + idTeacher;
            countRs = stmt.executeQuery(query);
            countRs.first();
            lastname = countRs.getString("lastname");
            countRs.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
            System.exit(1);
        }
        return lastname;
    }
    
    // Declaration of variables
    private String specialty, interests, phone;
    private int teachersLength, idTeacher;
}
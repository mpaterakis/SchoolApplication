package SchoolProject;

import Database.ConnectDatabase;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.*;

/**
 *
 * @author mpaterakis, erald
 */
public class Student extends User {

    public Student(ConnectDatabase dbConnect) {
        super(dbConnect);
    }

    public void addStudent(String idStudent, String am, String examino) {
        try {
            if (examino.isEmpty()) {
                examino = "0";
            }

            usersRs.moveToInsertRow();
            usersRs.updateString("idstudent", idStudent.trim());
            usersRs.updateString("am", am.trim());
            usersRs.updateString("examino", examino.trim());
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
            String query = "delete from student where idstudent = " + usersRs.getInt("idstudent");
            String query2 = "delete from enrollment where student_idstudent = " + usersRs.getInt("idstudent");

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

    public void modifyStudent(String idStudent, String am, String examino, String newIdStudent) {
        try {
            if (examino.isEmpty()) {
                examino = "0";
            }

            String query = "update student set idstudent= ? ,am= ? ,examino= ?  "
                    + "where idstudent = " + Integer.parseInt(idStudent);
            PreparedStatement stmt = dbConnect.getConnection().prepareStatement(query);

            stmt.setString(1, newIdStudent.trim());
            stmt.setString(2, am.trim());
            stmt.setString(3, examino.trim());

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
    protected void initUsersData() {
        try {
            statement = dbConnect.getConnection().createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            String query = "select * from student";
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
                idStudent = 0;
                am = "";
                examino = 0;
            } else {
                idStudent = usersRs.getInt("idstudent");
                am = usersRs.getString("am");
                examino = usersRs.getInt("examino");
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
            String query = "select count(*) as cnt from student";
            ResultSet countRs = stmt.executeQuery(query);
            countRs.first();
            studentsLength = countRs.getInt("cnt");
            countRs.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
            System.exit(1);
        }
    }

    public int getUsersLength() {
        return studentsLength;
    }

    public String getAm() {
        return am;
    }

    public void setAm(String am) {
        this.am = am;
    }

    public int getExamino() {
        return examino;
    }

    public void setExamino(int examino) {
        this.examino = examino;
    }

    public int getStudentsLength() {
        return studentsLength;
    }

    public void setStudentsLength(int studentsLength) {
        this.studentsLength = studentsLength;
    }

    public int getIdStudent() {
        return idStudent;
    }

    public void setIdStudent(int idStudent) {
        this.idStudent = idStudent;
    }
    
    @Override
    public TableModel table() {

        tableModel = null;

        if (getUsersLength() != 0) {
            try {
                String query = "SELECT * FROM student s join users u on u.idusers = s.idstudent";
                Statement stmt = dbConnect.getConnection().createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                stmt.executeQuery(query);

                List<Object> usersList = new ArrayList<>();

                while (stmt.getResultSet().next()) {
                    int iduser = stmt.getResultSet().getInt("idusers");
                    String am_ = stmt.getResultSet().getString("am");
                    String eksamino = stmt.getResultSet().getString("examino");
                    String useremail = stmt.getResultSet().getString("email");
                    String lastname = stmt.getResultSet().getString("lastname");
                    String firstname = stmt.getResultSet().getString("firstname");

                    List<Object> rowData = new ArrayList<>();

                    rowData.add(iduser);
                    rowData.add(lastname);
                    rowData.add(firstname);
                    rowData.add(useremail);
                    rowData.add(am_);
                    rowData.add(eksamino);

                    usersList.add(rowData);
                }

                Object[] columnNames = new String[]{
                    "Serial",
                    "Lastname",
                    "Firstname",
                    "Email",
                    "AM",
                    "Semester"
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

    public void addEnrollment(String idlesson, String idStudent, String grade, String status) {
        if (grade.isEmpty()) {
            grade = "0";
        }
        if (status.isEmpty()) {
            status = "0";
        }
        try {
            Statement stmt = dbConnect.getConnection().createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            String query = "insert into enrollment (lesson_idlesson,student_idstudent,grade,status) values "
                    + "(" + idlesson + "," + idStudent + "," + grade + ",'" + status + "')";
            stmt.execute(query);
            stmt.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
            System.exit(1);
        }
    }

    public void modifyEnrollment(String idlesson, String idStudent, String grade, String status) {
        String idEnrollment = "";
        ResultSet countRs;

        try {
            Statement pre_stmt = dbConnect.getConnection().createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            String pre_query = "select idenrollment from enrollment where lesson_idlesson = "
                    + idlesson + " and student_idstudent = " + idStudent;
            countRs = pre_stmt.executeQuery(pre_query);
            countRs.first();
            idEnrollment = countRs.getString("idenrollment");

            String query = "update enrollment set idenrollment = ?, lesson_idlesson= ? , student_idstudent= ?, "
                    + "grade=?, status=? where idenrollment = " + idEnrollment;
            PreparedStatement stmt = dbConnect.getConnection().prepareStatement(query);;

            stmt.setString(1, idEnrollment);
            stmt.setString(2, idlesson.trim());
            stmt.setString(3, idStudent.trim());
            stmt.setString(4, grade.trim());
            stmt.setString(5, status.trim());

            stmt.executeUpdate();
            stmt.close();
            reloadData();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
            System.exit(1);
        }
    }

    public void deleteEnrollment(String idLesson, String idStudent) {
        try {
            String query = "delete from enrollment where student_idstudent = " + idStudent
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

    public TableModel enrollmentTable() {

        tableModel = null;

        if (getUsersLength() != 0) {
            try {
                String query = "SELECT * FROM enrollment e join users u on e.student_idstudent = u.idusers "
                        + "join student s on s.idstudent = u.idusers "
                        + "join lesson l on e.lesson_idlesson = l.idlesson";
                Statement stmt = dbConnect.getConnection().createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                stmt.executeQuery(query);

                List<Object> usersList = new ArrayList<>();

                while (stmt.getResultSet().next()) {
                    int iduser = stmt.getResultSet().getInt("idusers");
                    String am_ = stmt.getResultSet().getString("am");
                    String lastname = stmt.getResultSet().getString("lastname");
                    String firstname = stmt.getResultSet().getString("firstname");
                    String lessonname = stmt.getResultSet().getString("name");
                    String lessondesc = stmt.getResultSet().getString("description");
                    String grade = stmt.getResultSet().getString("grade");
                    String status = stmt.getResultSet().getString("status");

                    List<Object> rowData = new ArrayList<>();

                    rowData.add(iduser);
                    rowData.add(lastname);
                    rowData.add(firstname);
                    rowData.add(am_);
                    rowData.add(lessonname);
                    rowData.add(lessondesc);
                    rowData.add(grade);
                    rowData.add(status);

                    usersList.add(rowData);
                }

                Object[] columnNames = new String[]{
                    "Serial",
                    "Lastname",
                    "Firstname",
                    "AM",
                    "Lesson",
                    "Description",
                    "Grade",
                    "Status"
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

    public int getGrade(String lessonId) {
        int grade = 0;
        ResultSet countRs;

        try {
            Statement stmt = dbConnect.getConnection().createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            String query = "select grade from enrollment where lesson_idlesson = "
                    + lessonId + " and student_idstudent = " + idStudent;
            countRs = stmt.executeQuery(query);
            countRs.first();
            grade = countRs.getInt("grade");
            countRs.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
            System.exit(1);
        }
        return grade;
    }

    public String getStatus(String lessonId) {
        String status = "";
        ResultSet countRs;

        try {
            Statement stmt = dbConnect.getConnection().createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            String query = "select status from enrollment where lesson_idlesson = "
                    + lessonId + " and student_idstudent = " + idStudent;
            countRs = stmt.executeQuery(query);
            countRs.first();
            status = countRs.getString("status");
            countRs.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
            System.exit(1);
        }
        return status;
    }

    @Override
    public String getFirstName() {
        String firstname = "";
        ResultSet countRs;

        try {
            Statement stmt = dbConnect.getConnection().createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            String query = "select firstname from users where idusers = " + idStudent;
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
            String query = "select lastname from users where idusers = " + idStudent;
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
    
    private String am;
    private int studentsLength, idStudent, examino;
}

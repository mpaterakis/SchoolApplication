package Model;

import Database.ConnectDatabase;
import java.sql.*;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;

/**
 *
 * @author mpaterakis
 */
public class EnrollmentCbModel extends DefaultComboBoxModel {

    public EnrollmentCbModel(ConnectDatabase dbConnect, int studentId, boolean isAddOrModify) {
        this.dbConnect = dbConnect;
        try {
            statement = dbConnect.getConnection().createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            String query = "";
            if (!isAddOrModify) {
                query = "select * from enrollment e "
                        + "join users u on u.idusers = e.student_idstudent "
                        + "join lesson l on e.lesson_idlesson = l.idlesson "
                        + "where e.student_idstudent = " + studentId;
            } else {
                query = "select * from lesson l where l.idlesson not in " 
                        + "(select lesson_idlesson  from enrollment where student_idstudent = " + studentId + ") "
                        + "and l.idlesson not in " 
                        + "(select pq.lesson_idlesson1 from prerequisite pq "
                        + "left join enrollment e on e.lesson_idlesson = pq.lesson_idlesson and e.student_idstudent = " + studentId + " "
                        + "where e.idenrollment is null or e.grade < 5)";
            }
            enrollmentRs = statement.executeQuery(query);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
            System.exit(1);
        }
    }

    @Override
    public Object getElementAt(int index) {
        String line = null;
        try {
            enrollmentRs.absolute(index + 1);
            line = enrollmentRs.getInt("l.idlesson") + ", " + enrollmentRs.getString("l.name");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
            System.exit(1);
        }
        return line;
    }

    @Override
    public int getSize() {
        int size = 0;
        int currentIndex;
        try {
            currentIndex = enrollmentRs.getRow();
            enrollmentRs.last();
            size = enrollmentRs.getRow();
            enrollmentRs.absolute(currentIndex+1);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
            System.exit(1);
        }
        return size;
    }
    
    private final ConnectDatabase dbConnect;
    private ResultSet enrollmentRs;
    private Statement statement;

}

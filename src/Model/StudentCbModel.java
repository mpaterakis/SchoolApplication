package Model;

import Database.ConnectDatabase;
import java.sql.*;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;

/**
 *
 * @author mpaterakis
 */
public class StudentCbModel extends DefaultComboBoxModel {

    public StudentCbModel(ConnectDatabase dbConnect, int studentId, boolean isAddOrModify) {
        this.dbConnect = dbConnect;
        try {
            statement = dbConnect.getConnection().createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            String query = "";
            if (!isAddOrModify) {
                query = "select * from users u "
                        + "join student s on u.idusers = s.idstudent "
                        + "where s.idstudent = " + studentId;
            } else {
                query = "(select * from users u where u.idusers not in "
                        + "(select s.idstudent from student s) and "
                        + "u.idusers not in (select idteacher from teacher))";
            }
            studentRs = statement.executeQuery(query);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
            System.exit(1);
        }
    }

    @Override
    public Object getElementAt(int index) {
        String line = null;
        try {
            studentRs.absolute(index + 1);
            line = studentRs.getInt("idusers") + ", " + studentRs.getString("firstname")
                    + " " + studentRs.getString("lastname");
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
            currentIndex = studentRs.getRow();
            studentRs.last();
            size = studentRs.getRow();
            studentRs.absolute(currentIndex+1);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
            System.exit(1);
        }
        return size;
    }
    
    private final ConnectDatabase dbConnect;
    private ResultSet studentRs;
    private Statement statement;
}

package Model;

import Database.ConnectDatabase;
import java.sql.*;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;

/**
 *
 * @author mpaterakis
 */
public class TeacherCbModel extends DefaultComboBoxModel {

    public TeacherCbModel(ConnectDatabase dbConnect, int teacherId, boolean isAddOrModify) {
        this.dbConnect = dbConnect;
        try {
            statement = dbConnect.getConnection().createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            String query = "";
            if (!isAddOrModify) {
                query = "select * from users u "
                        + "join teacher t on u.idusers = t.idteacher "
                        + "where t.idteacher = " + teacherId;
            } else {
                query = "(select * from users u where u.idusers not in "
                        + "(select t.idteacher from teacher t) and "
                        + "u.idusers not in (select idstudent from student))";
            }
            prereqRs = statement.executeQuery(query);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
            System.exit(1);
        }
    }

    @Override
    public Object getElementAt(int index) {
        String line = null;
        try {
            prereqRs.absolute(index + 1);
            line = prereqRs.getInt("idusers") + ", " + prereqRs.getString("firstname")
                    + " " + prereqRs.getString("lastname");
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
            currentIndex = prereqRs.getRow();
            prereqRs.last();
            size = prereqRs.getRow();
            prereqRs.absolute(currentIndex+1);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
            System.exit(1);
        }
        return size;
    }
    
    private final ConnectDatabase dbConnect;
    private ResultSet prereqRs;
    private Statement statement;
}

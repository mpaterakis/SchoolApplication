package Model;

import Database.ConnectDatabase;
import java.sql.*;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;

/**
 *
 * @author mpaterakis, erald
 */
public class TeachingCbModel extends DefaultComboBoxModel {

    public TeachingCbModel(ConnectDatabase dbConnect, int teacherId, boolean isAddOrModify) {
        this.dbConnect = dbConnect; 
        try {
            statement = dbConnect.getConnection().createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            String query = "";
            if (!isAddOrModify) {
                query = "select * from teaches t "
                        + "join users u on t.teacher_idteacher = u.idusers "
                        + "join lesson l on t.lesson_idlesson = l.idlesson "
                        + "where t.teacher_idteacher = " + teacherId;

            } else {
                query = "select * from lesson where idlesson not in " 
                        + "(select lesson_idlesson from teaches where teacher_idteacher = " + teacherId + ") ";
            }
            teachesRs = statement.executeQuery(query);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
            System.exit(1);
        }
    }

    @Override
    public Object getElementAt(int index) {
        String line = null;
        try {
            teachesRs.absolute(index + 1);
            line = teachesRs.getInt("idlesson") + ", " + teachesRs.getString("name");
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
            currentIndex = teachesRs.getRow();
            teachesRs.last();
            size = teachesRs.getRow();
            teachesRs.absolute(currentIndex+1);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
            System.exit(1);
        }
        return size;
    }
    
    private final ConnectDatabase dbConnect;
    private ResultSet teachesRs;
    private Statement statement;
}
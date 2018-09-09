package Model;

import Database.ConnectDatabase;
import java.sql.*;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;

/**
 *
 * @author mpaterakis
 */
public class PrerequisiteCbModel extends DefaultComboBoxModel {

    public PrerequisiteCbModel(ConnectDatabase dbConnect, int lessonId, boolean isAddOrModify) {
        this.dbConnect = dbConnect;
        try {
            statement = dbConnect.getConnection().createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            String query = "";
            if (!isAddOrModify) {
                query = "select * from prerequisite pr "
                        + "join lesson l1 on pr.lesson_idlesson = l1.idlesson "
                        + "join lesson l2 on pr.lesson_idlesson1 = l2.idlesson "
                        + "where pr.lesson_idlesson1 = " + lessonId;
            } else {
                query = "select * from lesson l1 where l1.idlesson <> " + lessonId
                        + " and l1.idlesson not in (select lesson_idlesson1 "
                        + "from prerequisite where lesson_idlesson = " + lessonId + ")";
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
            line = prereqRs.getInt("l1.idlesson") + ", " + prereqRs.getString("l1.name");
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

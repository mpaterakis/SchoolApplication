package SchoolProject;

import Database.ConnectDatabase;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 *
 * @author mpaterakis
 */
public class Lesson {

    public Lesson(ConnectDatabase dbConnect) {
        this.dbConnect = dbConnect;
        initLessonData();
    }


    public void next() {
        try {
            lessonRs.next();
            reloadData();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
            System.exit(1);
        }
    }

    public void previous() {
        try {
            lessonRs.previous();
            reloadData();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
            System.exit(1);
        }
    }

    public void last() {
        try {
            lessonRs.last();
            reloadData();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
            System.exit(1);
        }
    }

    public void first() {
        try {
            lessonRs.first();
            reloadData();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
            System.exit(1);;
        }
    }
    
     public void add(String name, String description) {
        try {
            lessonRs.moveToInsertRow();
            lessonRs.updateString("Name", name.trim());
            lessonRs.updateString("Description", description.trim());
            lessonRs.insertRow();
            calculateLessonLength();
            reloadData();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
            System.exit(1);
        }
    }

    public void delete() {
        try {
            Statement stmt = dbConnect.getConnection().createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            String query1 = "delete from prerequisite where lesson_idlesson = " + lessonRs.getInt("idlesson")
                    + " or lesson_idlesson1 = " + lessonRs.getInt("idlesson");
            String query2 = "delete from enrollment where lesson_idlesson = " + lessonRs.getInt("idlesson");
            String query3 = "delete from teaches where lesson_idlesson = " + lessonRs.getInt("idlesson");
            
            stmt.executeUpdate(query1);
            stmt.executeUpdate(query2);
            stmt.executeUpdate(query3);

            lessonRs.deleteRow();
            if (lessonRs.isBeforeFirst()) {
                lessonRs.first();
            }
            calculateLessonLength();
            reloadData();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
            System.exit(1);
        }
    }

    public void modify(String idlesson, String name, String description) {
        try {
            String query = "update lesson set idlesson= ? ,name= ? ,description= ? "
                    + "where idlesson = " + Integer.parseInt(idlesson);
            PreparedStatement stmt = dbConnect.getConnection().prepareStatement(query);

            stmt.setString(1, idlesson.trim());
            stmt.setString(2, name.trim());
            stmt.setString(3, description.trim());

            stmt.executeUpdate();
            stmt.close();
            lessonRs.refreshRow();
            reloadData();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
            System.exit(1);
        }
    }

    public TableModel table() {

        tableModel = null;

        if (getLessonLength() != 0) {
            try {
                String query = "select * from lesson";
                Statement stmt = dbConnect.getConnection().createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                stmt.executeQuery(query);

                List<Object> lessonsList = new ArrayList<>();

                while (stmt.getResultSet().next()) {
                    int idlesson = stmt.getResultSet().getInt("idlesson");
                    String name = stmt.getResultSet().getString("name");
                    String description = stmt.getResultSet().getString("description");

                    List<Object> rowData = new ArrayList<>();

                    rowData.add(idlesson);
                    rowData.add(name);
                    rowData.add(description);

                    lessonsList.add(rowData);
                }

                Object[] columnNames = new String[]{
                    "Serial",
                    "Name",
                    "Description",};

                Object[][] data = new Object[lessonsList.size()][];

                for (int i = 0; i < lessonsList.size(); i++) {
                    ArrayList<Object> rowList = (ArrayList<Object>) lessonsList.get(i);
                    data[i] = rowList.toArray();
                }

                tableModel = new DefaultTableModel(data, columnNames);

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
                System.exit(1);
            }
        } else {
            JOptionPane.showMessageDialog(null,
                    "There's no Lessons records in the database", "Message", JOptionPane.INFORMATION_MESSAGE);
        }
        return tableModel;
    }

    public TableModel prerequisiteTable() {

        tableModel = null;

        if (getLessonLength() != 0) {
            try {
                String query = "select * from prerequisite pr "
                        + "join lesson l1 on pr.lesson_idlesson = l1.idlesson "
                        + "join lesson l2 on pr.lesson_idlesson1 = l2.idlesson";
                Statement stmt = dbConnect.getConnection().createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                stmt.executeQuery(query);

                List<Object> lessonsList = new ArrayList<>();

                while (stmt.getResultSet().next()) {
                    int idlesson1 = stmt.getResultSet().getInt("l1.idlesson");
                    String name1 = stmt.getResultSet().getString("l1.name");
                    String description1 = stmt.getResultSet().getString("l1.description");
                    int idlesson2 = stmt.getResultSet().getInt("l2.idlesson");
                    String name2 = stmt.getResultSet().getString("l2.name");
                    String description2 = stmt.getResultSet().getString("l2.description");

                    List<Object> rowData = new ArrayList<>();

                    rowData.add(idlesson1);
                    rowData.add(name1);
                    rowData.add(description1);
                    rowData.add(idlesson2);
                    rowData.add(name2);
                    rowData.add(description2);

                    lessonsList.add(rowData);
                }

                Object[] columnNames = new String[]{
                    "Lesson Serial",
                    "Lesson Name",
                    "Lesson Description",
                    "Prerequisite Serial",
                    "Prerequisite Name",
                    "Prerequisite Description",};

                Object[][] data = new Object[lessonsList.size()][];

                for (int i = 0; i < lessonsList.size(); i++) {
                    ArrayList<Object> rowList = (ArrayList<Object>) lessonsList.get(i);
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getIdLesson() {
        return idLesson;
    }

    public void setIdLesson(int idLesson) {
        this.idLesson = idLesson;
    }

    private void initLessonData() {
        try {
            statement = dbConnect.getConnection().createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            String query = "select * from lesson";
            lessonRs = statement.executeQuery(query);
            lessonRs.first();
            calculateLessonLength();
            reloadData();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
            System.exit(1);
        }
    }

    public void reloadData() {
        try {
            if (getLessonLength() == 0) {
                idLesson = 0;
                name = "";
                description = "";
            } else {
                idLesson = lessonRs.getInt("idlesson");
                description = lessonRs.getString("description");
                name = lessonRs.getString("name");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
            System.exit(1);
        }
    }

    public void calculateLessonLength() {
        try {
            Statement stmt = dbConnect.getConnection().createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            String query = "select count(*) as cnt from lesson";
            ResultSet countRs = stmt.executeQuery(query);
            countRs.first();
            lessonLength = countRs.getInt("cnt");
            countRs.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
            System.exit(1);
        }
    }

    public int getLessonLength() {
        return lessonLength;
    }

    public void addPrerequisite(String idLesson, String prerequisiteId) {
        try {
            Statement stmt = dbConnect.getConnection().createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            String query = "insert into prerequisite values "
                    + "(" + prerequisiteId + "," + idLesson + ")";
            stmt.execute(query);
            stmt.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
            System.exit(1);
        }
    }
    
    public void modifyPrerequisite(String idlesson, String idlesson1) {
        try {
            String query = "update prerequisite set lesson_idlesson= ? ,lesson_idlesson1= ? "
                    + "where lesson_idlesson1 = " + Integer.parseInt(idlesson);
            PreparedStatement stmt = dbConnect.getConnection().prepareStatement(query);;

            stmt.setString(1, idlesson1.trim());
            stmt.setString(2, idlesson.trim());

            stmt.executeUpdate();
            stmt.close();
            reloadData();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
            System.exit(1);
        }
    }

    public void deletePrerequisite(String idlesson, String idlesson1) {
        try {
            String query = "delete from prerequisite where lesson_idlesson1 = " + idlesson
                    + " and lesson_idlesson = " + idlesson1;
            PreparedStatement stmt = dbConnect.getConnection().prepareStatement(query);

            stmt.execute(query);
            stmt.close();
            reloadData();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
            System.exit(1);
        }
    }
    
    // Declaration of variables
    private String name, description;
    private int idLesson, lessonLength;
    private final ConnectDatabase dbConnect;
    private ResultSet lessonRs;
    private Statement statement;
    private TableModel tableModel;
}

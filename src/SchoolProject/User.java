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
public class User {

    public User(ConnectDatabase dbConnect) {
        this.dbConnect = dbConnect;
        initUsersData();
    }

    public void first() {
        try {
            usersRs.first();
            reloadData();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    public void previous() {
        try {
            usersRs.previous();
            reloadData();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
            System.exit(1);
        }
    }

    public void next() {
        try {
            usersRs.next();
            reloadData();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
            System.exit(1);
        }
    }

    public void last() {
        try {
            usersRs.last();
            reloadData();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
            System.exit(1);
        }
    }

    public void add(String lastname, String firstname, String email, String login, String password) {
        try {
            usersRs.moveToInsertRow();
            usersRs.updateString("lastname", lastname.trim());
            usersRs.updateString("firstname", firstname.trim());
            usersRs.updateString("email", email.trim());
            usersRs.updateString("login", login.trim());
            usersRs.updateString("password", password.trim());
            usersRs.insertRow();
            calculateUsersLength();
            reloadData();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
            System.exit(1);
        }
    }

    public void delete() {
        try {
            Statement stmt = dbConnect.getConnection().createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            String query1 = "delete from student where idstudent =" + usersRs.getInt("idusers");
            String query2 = "delete from teacher where idteacher =" + usersRs.getInt("idusers");
            String query3 = "delete from teaches where teacher_idteacher =" + usersRs.getInt("idusers");
            String query4 = "delete from enrollment where student_idstudent=" + usersRs.getInt("idusers");

            stmt.executeUpdate(query4);
            stmt.executeUpdate(query3);
            stmt.executeUpdate(query1);
            stmt.executeUpdate(query2);

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

    public void modify(String iduser, String lastname, String firstname, String email, String login, String password) {
        try {
            String query = "update users set login= ? ,password= ? ,email= ? ,lastname= ? ,firstname= ?"
                    + "where idusers = " + Integer.parseInt(iduser);
            PreparedStatement stmt = dbConnect.getConnection().prepareStatement(query);

            stmt.setString(1, login.trim());
            stmt.setString(2, password.trim());
            stmt.setString(3, email.trim());
            stmt.setString(4, lastname.trim());
            stmt.setString(5, firstname.trim());

            stmt.executeUpdate();
            stmt.close();
            usersRs.refreshRow();
            reloadData();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
            System.exit(1);
        }
    }

    public TableModel table() {

        tableModel = null;

        if (getUsersLength() != 0) {
            try {
                String query = "SELECT * FROM users";
                Statement stmt = dbConnect.getConnection().createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                stmt.executeQuery(query);

                List<Object> usersList = new ArrayList<>();

                while (stmt.getResultSet().next()) {
                    int iduser = stmt.getResultSet().getInt("idusers");
                    String userlogin = stmt.getResultSet().getString("login");
                    String userpassword = stmt.getResultSet().getString("password");
                    String useremail = stmt.getResultSet().getString("email");
                    String lastname = stmt.getResultSet().getString("lastname");
                    String firstname = stmt.getResultSet().getString("firstname");

                    List<Object> rowData = new ArrayList<>();

                    rowData.add(iduser);
                    rowData.add(lastname);
                    rowData.add(firstname);
                    rowData.add(useremail);
                    rowData.add(userlogin);
                    rowData.add(userpassword);

                    usersList.add(rowData);
                }

                Object[] columnNames = new String[]{
                    "Serial",
                    "Lastname",
                    "Firstname",
                    "Email",
                    "Login",
                    "Password"
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

    public void setId(int id) {
        this.idUser = id;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getEmail() {
        return email;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public int getId() {
        return idUser;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ResultSet getUsersRs() {
        return usersRs;
    }

    protected void initUsersData() {
        try {
            statement = dbConnect.getConnection().createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            String query = "select * from users";
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
                idUser = 0;
                login = "";
                password = "";
                email = "";
                lastName = "";
                firstName = "";
            } else {
                idUser = usersRs.getInt("idusers");
                login = usersRs.getString("login");
                password = usersRs.getString("password");
                email = usersRs.getString("email");
                lastName = usersRs.getString("lastname");
                firstName = usersRs.getString("firstname");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
            System.exit(1);
        }
    }

    public void calculateUsersLength() {
        try {
            Statement stmt = dbConnect.getConnection().createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            String query = "select count(*) as cnt from users";
            ResultSet countRs = stmt.executeQuery(query);
            countRs.first();
            usersLength = countRs.getInt("cnt");
            countRs.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
            System.exit(1);
        }
    }

    public int getUsersLength() {
        return usersLength;
    }

    // Declaration of variables
    private String lastName, firstName, email, login, password;
    private int idUser, usersLength;
    protected final ConnectDatabase dbConnect;
    protected ResultSet usersRs;
    protected Statement statement;
    protected TableModel tableModel;
}

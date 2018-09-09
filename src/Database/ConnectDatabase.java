/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Database;

import java.sql.*;
import javax.swing.JOptionPane;

/**
 *
 * @author mpaterakis
 */
public final class ConnectDatabase {    
    
    public ConnectDatabase(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
        connectToDB();
    }
    
    // Establish conenction to DB
    private void connectToDB() {
        // Check for the driver
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            String msg = "The com.mysql.jdbc.Driver is missing\n"
                    + "install and rerun the application";
            JOptionPane.showMessageDialog(null, msg, "SQL Driver Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        // Connect to db
        try {
            connection = DriverManager.getConnection("jdbc:mysql://" + url + "?enableSSL=true", username, password);
        } catch (SQLException e) {
            String msg = "Error Connecting to Database:\n"
                    + e.getMessage() + "\n";
            JOptionPane.showMessageDialog(null, msg, "SQL Connection Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }
    
    // Get Connection object
    public Connection getConnection() {
        return connection;
    }

    // Close connection
    public void closeConnection() {
        try {
            connection.close();
        } catch (SQLException e) {
            String msg = "Error Closing connection to Database:\n"
                    + e.getMessage() + "\n";
            JOptionPane.showMessageDialog(null, msg, "SQL Connection Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }
    
    private Connection connection;
    private final String url;
    private final String username;
    private final String password;
}

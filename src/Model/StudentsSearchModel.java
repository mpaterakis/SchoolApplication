package Model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.table.AbstractTableModel;



public class StudentsSearchModel extends AbstractTableModel {
    
     public StudentsSearchModel(Connection con, String srch, boolean all) {
        String queryStr;
        try {
            if(!all) 
                queryStr=QUERY_SET + srch +"%'" + ORDER_SUFFIX;
            else
                queryStr=QUERY_TABLE + ORDER_SUFFIX;
            stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rs = stmt.executeQuery(queryStr);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }        
    }
     
    
    @Override
    public int getRowCount() {
        int cnt=0;
        try {
            int pos=rs.getRow();
            rs.last();
            cnt=rs.getRow();
            if(pos>0)
                rs.absolute(pos);
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        return(cnt);
    }

    @Override
    public int getColumnCount() {
        return 6;
    }

    @Override
    public Object getValueAt(int row, int col) {
        try{
            rs.absolute(row + 1);
            
            switch(col) {
                case 0:
                    return rs.getInt("idusers");
                case 1:
                    return rs.getString("email");
                case 2:
                    return rs.getString("lastname");
                case 3:
                    return rs.getString("firstname");
                case 4:
                    return rs.getString("am");
                case 5:
                    return rs.getString("examino");
            }           
        } catch(SQLException e) {
            System.out.println("getValueAt: " + e.getMessage());
        }
        return null;
    }
    
    private static final String QUERY_TABLE="select * from users u join student s on u.idusers = s.idstudent";
    private static final String ORDER_KEY = "lastname";
    private static String QUERY_SET = QUERY_TABLE + " where " 
                                            + ORDER_KEY + " like '%";
    private String ORDER_SUFFIX = " order by " + ORDER_KEY;
    private ResultSet rs;
    private Statement stmt;
}

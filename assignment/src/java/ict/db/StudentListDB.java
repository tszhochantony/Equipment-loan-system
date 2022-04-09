package ict.db;

import ict.bean.StudentList;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class StudentListDB {
    private String dburl;
    private String dbUser;
    private String dbPassword;
    private EquipmentDB eDB;
    
    public StudentListDB(String dburl, String dbUser, String dbPassword) {
        this.dburl = dburl;
        this.dbUser = dbUser;
        this.dbPassword = dbPassword;
        eDB = new EquipmentDB(dburl, dbUser, dbPassword);
    }
    
    public Connection getConnection() throws SQLException, IOException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return DriverManager.getConnection(dburl, dbUser, dbPassword);
    }
    public void createStudentListTable() {
        Statement statement = null;
        Connection connection  = null;
        try {
            connection = getConnection();
            statement = connection.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS StudentList (" 
                       + "id varchar(25) NOT NULL,"
                       + "eType varchar(25) NOT NULL,"
                       + "count int(5) NOT NULL,"
                       + "PRIMARY KEY (id,eType),"
                       + "FOREIGN KEY (id) REFERENCES User (id)"
                       + ")";
            statement.execute(sql);
            statement.close();
            connection.close();
        } catch (SQLException ex) {
            while (ex != null) {
                ex.printStackTrace();
                ex = ex.getNextException();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }
    
    public boolean addItem(String id,String eType,int count) { 
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        boolean isSuccess = false;
        int listCount = checkStudentList(id,eType);
        int totalCount = listCount+count;
        if(totalCount<=5){
            eDB.editEquipmentStatus(eType,count,"available","keep");
        try {
            connection = getConnection();
            if(listCount==0){
                String preQueryStatement = "INSERT INTO StudentList VALUES (?, ?, ?)";
                preparedStatement = connection.prepareStatement(preQueryStatement);
                preparedStatement.setString(1, id);
                preparedStatement.setString(2, eType);
                preparedStatement.setInt(3, count);
            }
            else{
                String preQueryStatement = "UPDATE StudentList SET count=? WHERE id=? AND eType=?";
                preparedStatement = connection.prepareStatement(preQueryStatement);
                preparedStatement.setString(1, String.valueOf(totalCount));
                preparedStatement.setString(2, id);
                preparedStatement.setString(3, eType);
                
            }
            int rowCount = preparedStatement.executeUpdate();
            if (rowCount >= 1) {
                isSuccess = true;
            }
            preparedStatement.close();
            connection.close();
        } catch (SQLException ex) {
            while (ex != null) {
                ex.printStackTrace();
                ex = ex.getNextException();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        }
        return isSuccess;
    }
     
     public ArrayList queryStudentList(String id) {
        Connection cnnct = null;
        PreparedStatement pStmnt = null;
        try {
            cnnct = getConnection();
            String preQueryStatement = "SELECT * FROM `StudentList` WHERE id=?";
            pStmnt = cnnct.prepareStatement(preQueryStatement);
            pStmnt.setString(1, id);
            ResultSet rs = pStmnt.executeQuery();
            ArrayList list = new ArrayList();
            while (rs.next()) {
                StudentList sc = new StudentList();
                sc.setId(rs.getString(1));
                sc.setEType(rs.getString(2));
                sc.setCount(rs.getInt(3));
                list.add(sc);
            }
            return list;
        } catch (SQLException ex) {
            while (ex != null) {
                ex.printStackTrace();
                ex = ex.getNextException();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (pStmnt != null) {
                try {
                    pStmnt.close();
                } catch (SQLException e) {
                }
            }
            if (cnnct != null) {
                try {
                    cnnct.close();
                } catch (SQLException sqlEx) {
                }
            }
        }
        return null;
    }
     
     public int checkStudentList(String id,String eType) {
        Connection cnnct = null;
        PreparedStatement pStmnt = null;
        int count = 0;
        boolean check = true;
        try {
            cnnct = getConnection();
            String preQueryStatement = "SELECT count FROM `StudentList` WHERE id=? AND eType=?";
            pStmnt = cnnct.prepareStatement(preQueryStatement);
            pStmnt.setString(1, id);
            pStmnt.setString(2, eType);
            ResultSet rs = pStmnt.executeQuery();
            if(rs.next()){
                count+=Integer.parseInt(rs.getString(1));
            }
            
        } catch (SQLException ex) {
            while (ex != null) {
                ex.printStackTrace();
                ex = ex.getNextException();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (pStmnt != null) {
                try {
                    pStmnt.close();
                } catch (SQLException e) {
                }
            }
            if (cnnct != null) {
                try {
                    cnnct.close();
                } catch (SQLException sqlEx) {
                }
            }
        }
        return count;
    }
     
    public void removeFromList(String id,String eType,int count) {
        Connection cnnct = null;
        PreparedStatement pStmnt = null;
        eDB.editEquipmentStatus(eType,count,"keep","available");
        try {
            cnnct = getConnection();
            String preQueryStatement = "DELETE FROM StudentList WHERE id=? AND eType=?";
            pStmnt = cnnct.prepareStatement(preQueryStatement);
            pStmnt.setString(1, id);
            pStmnt.setString(2, eType);
            pStmnt.executeUpdate();
        } catch (SQLException ex) {
            while (ex != null) {
                ex.printStackTrace();
                ex = ex.getNextException();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (pStmnt != null) {
                try {
                    pStmnt.close();
                } catch (SQLException e) {
                }
            }
            if (cnnct != null) {
                try {
                    cnnct.close();
                } catch (SQLException sqlEx) {
                }
            }
        }
    }
    public void removeAllFromList(String id) {
        Connection cnnct = null;
        PreparedStatement pStmnt = null;
        try {
            cnnct = getConnection();
            String preQueryStatement = "SELECT * FROM `StudentList` WHERE id=?";
            pStmnt = cnnct.prepareStatement(preQueryStatement);
            pStmnt.setString(1, id);
            ResultSet rs = pStmnt.executeQuery();
            while(rs.next()){
                eDB.editEquipmentStatus(rs.getString(2),Integer.parseInt(rs.getString(3)),"keep","pending");
            }
            preQueryStatement = "DELETE FROM StudentList WHERE id=?";
            pStmnt = cnnct.prepareStatement(preQueryStatement);
            pStmnt.setString(1, id);
            pStmnt.executeUpdate();
        } catch (SQLException ex) {
            while (ex != null) {
                ex.printStackTrace();
                ex = ex.getNextException();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (pStmnt != null) {
                try {
                    pStmnt.close();
                } catch (SQLException e) {
                }
            }
            if (cnnct != null) {
                try {
                    cnnct.close();
                } catch (SQLException sqlEx) {
                }
            }
        }
    }
}

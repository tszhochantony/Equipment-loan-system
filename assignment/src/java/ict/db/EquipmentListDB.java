package ict.db;

import ict.bean.EquipmentList;
import ict.bean.StudentList;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


public class EquipmentListDB {
    private String dburl;
    private String dbUser;
    private String dbPassword;
    
    public EquipmentListDB(String dburl, String dbUser, String dbPassword) {
        this.dburl = dburl;
        this.dbUser = dbUser;
        this.dbPassword = dbPassword;
    }
    
    public Connection getConnection() throws SQLException, IOException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return DriverManager.getConnection(dburl, dbUser, dbPassword);
    }
    public void createEquipmentListTable() {
        Statement statement = null;
        Connection connection  = null;

        try {
            connection = getConnection();
            statement = connection.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS EquipmentList (" 
                       + "rId varchar(25) NOT NULL,"
                       + "equipment varchar(25) NOT NULL,"
                       + "count int(5) NOT NULL,"
                       + "PRIMARY KEY (rId,equipment),"
                       + "FOREIGN KEY (rId) REFERENCES CheckOutRecord (rId),"
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
    
        public boolean addEquipmentList(ArrayList<StudentList> equipment,String rId) { 
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        boolean isSuccess = false;
        try {
            connection = getConnection();
            for(int count=0;equipment.size()>count;count++){
                String preQueryStatement = "INSERT INTO EquipmentList VALUES (?,?,?)";
                preparedStatement = connection.prepareStatement(preQueryStatement);
                preparedStatement.setString(1, rId);
                StudentList sc = equipment.get(count);
                preparedStatement.setString(2, sc.getEType());
                preparedStatement.setInt(3, sc.getCount());
                preparedStatement.executeUpdate();
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
        return isSuccess;
    }
    
     public ArrayList<EquipmentList> queryEquipmentListById(String rId) {
        Connection cnnct = null;
        PreparedStatement pStmnt = null;
        try {
            cnnct = getConnection();
            String preQueryStatement = "SELECT * FROM `EquipmentList` WHERE rId=?";
            pStmnt = cnnct.prepareStatement(preQueryStatement);
            pStmnt.setString(1,rId) ;
            ResultSet rs = pStmnt.executeQuery();
            ArrayList<EquipmentList> list = new ArrayList<EquipmentList>();
            while (rs.next()) {
                EquipmentList el = new EquipmentList();
                el.setRId(rs.getString(1));
                el.setEquipment(rs.getString(2));
                el.setCount(Integer.parseInt(rs.getString(3)));
                list.add(el);
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
     public ArrayList<EquipmentList> queryEquipmentByRId(String rId) {
        Connection cnnct = null;
        PreparedStatement pStmnt = null;
       
        try {
            cnnct = getConnection();
            String preQueryStatement = "SELECT equipment,count FROM `equipmentlist` WHERE rId = ?";
            pStmnt = cnnct.prepareStatement(preQueryStatement);
            pStmnt.setString(1,rId) ;
            //Statement s = cnnct.createStatement();
            ResultSet rs = pStmnt.executeQuery();
            ArrayList<EquipmentList> list = new ArrayList<EquipmentList>();
            while (rs.next()) {             
                EquipmentList el = new EquipmentList();
                el.setEquipment(rs.getString(1));
                el.setCount(Integer.parseInt(rs.getString(2)));
                list.add(el);
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
}

package ict.db;

import ict.bean.Equipment;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class EquipmentDB {
    private String dburl;
    private String dbUser;
    private String dbPassword;
    
    public EquipmentDB(String dburl, String dbUser, String dbPassword) {
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
    public void createEquipmentTable() {
        Statement statement = null;
        Connection connection  = null;

        try {
            connection = getConnection();
            statement = connection.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS Equipment (" 
                       + "eId varchar(25) NOT NULL,"
                       + "eType varchar(25) NOT NULL,"
                       + "eStatus varchar(25) NOT NULL,"
                       + "PRIMARY KEY (eId)"
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
    
        public boolean addEquipment(String eType, String eStatus) { 
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        boolean isSuccess = false;
        String eId = (eType.substring(0, Math.min(3,eType.length()))).toUpperCase();
        try {
            connection = getConnection();
            int quantity = queryEquipmentQuantity(eType);
            if(quantity>0){
                String number = String.valueOf(quantity+1);
                int integerNumber = quantity+1;
                while(quantity>0&&integerNumber<100000){
                    integerNumber*=10;
                    eId+="0";
                }
                eId+=number;
            }else{eId += "000001";}
            String preQueryStatement = "INSERT INTO Equipment VALUES (?, ?, ?)";
            preparedStatement = connection.prepareStatement(preQueryStatement);
            preparedStatement.setString(1, eId);
            preparedStatement.setString(2, eType);
            preparedStatement.setString(3, eStatus);
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
        return isSuccess;
    }
    
     public void editEquipmentStatus(String eType,int count,String oldStatus,String newStatus) {
        Connection cnnct = null;
        PreparedStatement pStmnt = null;
        int num=0;
        try {
            cnnct = getConnection();
            String preQueryStatement = "SELECT eId FROM `equipment` WHERE eType=? AND eStatus=? ORDER BY eId";
            pStmnt = cnnct.prepareStatement(preQueryStatement);
            pStmnt.setString(1,eType) ;
            pStmnt.setString(2,oldStatus) ;
            ResultSet rs = pStmnt.executeQuery();
            while (rs.next()&&count>0) {
                updateEquipment(rs.getString(1),newStatus);
                count--;
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
    }
    public void updateEquipment(String eId,String eStatus) {
        Connection cnnct = null;
        PreparedStatement pStmnt = null;
        int num=0;
        try {
            cnnct = getConnection();
            String preQueryStatement = "UPDATE Equipment SET eStatus=? WHERE eId=?";
                pStmnt = cnnct.prepareStatement(preQueryStatement);
                pStmnt.setString(1,eStatus) ;
                pStmnt.setString(2,eId) ;
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
    
     public ArrayList queryEquipmentByType() {
        Connection cnnct = null;
        PreparedStatement pStmnt = null;
        try {
            cnnct = getConnection();
            String preQueryStatement = "SELECT eType,COUNT(*) FROM `Equipment` WHERE eStatus='available' GROUP BY eType";
            pStmnt = cnnct.prepareStatement(preQueryStatement);      
            //Statement s = cnnct.createStatement();
            ResultSet rs = pStmnt.executeQuery();

            ArrayList list = new ArrayList();

            while (rs.next()) {
                Equipment eq = new Equipment();
                eq.setEType(rs.getString(1));
                eq.setCount(rs.getInt(2));
                list.add(eq);
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
     public int queryEquipmentQuantity(String eType) {
        Connection cnnct = null;
        PreparedStatement pStmnt = null;
        int quantity =0;
        try {
            cnnct = getConnection();
            String preQueryStatement = "SELECT COUNT(*) FROM `Equipment` WHERE eType=?";
            pStmnt = cnnct.prepareStatement(preQueryStatement);
            pStmnt.setString(1,eType) ;
            //Statement s = cnnct.createStatement();
            ResultSet rs = pStmnt.executeQuery();
            while (rs.next()) {
                quantity = rs.getInt(1);
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
        return quantity;
    }

     public ArrayList queryEquipment() {
         Connection cnnct = null;
        PreparedStatement pStmnt = null;
        try {
            cnnct = getConnection();
            String preQueryStatement = "SELECT * FROM  EQUIPMENT";
            pStmnt = cnnct.prepareStatement(preQueryStatement);
            //Statement s = cnnct.createStatement();
            ResultSet rs = pStmnt.executeQuery();

            ArrayList list = new ArrayList();

            while (rs.next()) {
                Equipment e = new Equipment();
                e.setEId(rs.getString(1));
                e.setEType(rs.getString(2));
                e.setEStatus(rs.getString(3));
                list.add(e);
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
     public ArrayList queryEquipmentType() {
         Connection cnnct = null;
        PreparedStatement pStmnt = null;
        try {
            cnnct = getConnection();
            String preQueryStatement = "SELECT eType FROM EQUIPMENT GROUP BY eType";
            pStmnt = cnnct.prepareStatement(preQueryStatement);
            //Statement s = cnnct.createStatement();
            ResultSet rs = pStmnt.executeQuery();
            ArrayList list = new ArrayList();
            while (rs.next()) {
                Equipment e = new Equipment();
                e.setEType(rs.getString(1));
                list.add(e);
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
     public void queryEquipmentByBorrowNum(String eType , int count,String eStatus) {
        Connection cnnct = null;
        PreparedStatement pStmnt = null;
        String status =eStatus;
        try {
            cnnct = getConnection();
            String preQueryStatement = "SELECT eId FROM EQUIPMENT WHERE eType=? AND eStatus='pending'";
            pStmnt = cnnct.prepareStatement(preQueryStatement);    
            pStmnt.setString(1,eType) ;
            ResultSet rs = pStmnt.executeQuery();
            while (rs.next()&&count>0) {
                String eId = rs.getString(1);
                if(status.equals("rejected")){
                    eStatus = "available";
                }
                preQueryStatement = "UPDATE EQUIPMENT set eStatus=? WHERE eId=?";
                pStmnt = cnnct.prepareStatement(preQueryStatement);
                pStmnt.setString(1,eStatus) ;
                pStmnt.setString(2,eId) ;
                pStmnt.executeUpdate();
                count--;
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

    }
}

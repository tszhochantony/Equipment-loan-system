package ict.db;

import ict.bean.User;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class UserDB {
    private String dburl;
    private String dbUser;
    private String dbPassword;
    
    public UserDB(String dburl, String dbUser, String dbPassword) {
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
    public void createUserTable() {
        Statement statement = null;
        Connection connection  = null;

        try {
            connection = getConnection();
            statement = connection.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS User (" 
                       + "id varchar(20) NOT NULL,"
                       + "type varchar(25) NOT NULL,"
                       + "email varchar(50) NOT NULL,"
                       + "username varchar(25) NOT NULL,"
                       + "password varchar(25) NOT NULL,"
                       + "status varchar(25) NOT NULL,"
                       + "PRIMARY KEY (id)"
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
    public String isValidUser(String type,String email, String password) throws SQLException, IOException {
        String id="";
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = getConnection();
            String preQueryStatement = "SELECT id FROM User WHERE email=? AND password=? AND type=? AND status=?";
            preparedStatement = connection.prepareStatement(preQueryStatement);
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, type);
            preparedStatement.setString(4, "Available");
            ResultSet result = preparedStatement.executeQuery();
            if (result.next()){
                id =  result.getString(1);
            }
        } catch (SQLException ex) {
            while (ex != null) {
                ex.printStackTrace();
                ex = ex.getNextException();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return id; 
    }
    
    public boolean addUser(String type, String email,String user, String password) { 
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        String id = (type.substring(0, Math.min(3,type.length()))).toUpperCase();
        boolean isSuccess = false;
        try {
            connection = getConnection();
            String preQueryStatement = "SELECT count(*) FROM `User` WHERE type=?";
            preparedStatement = connection.prepareStatement(preQueryStatement);
            preparedStatement.setString(1, type);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                if(count>0){
                    String number = String.valueOf(count+1);
                    while(count>0&&count<100000){
                        count*=10;
                        id+="0";
                    }
                    id+=number;
                }
                else{id += "000001";}
            }
            preQueryStatement = "INSERT INTO User VALUES (?, ?, ?, ?, ?, ?)";
            preparedStatement = connection.prepareStatement(preQueryStatement);
            preparedStatement.setString(1, id);
            preparedStatement.setString(2, type);
            preparedStatement.setString(3, email);
            preparedStatement.setString(4, user);
            preparedStatement.setString(5, password);
            preparedStatement.setString(6, "Available");
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
    
     public boolean deactivateUser(String id,String status) {
        Connection cnnct = null;
        PreparedStatement pStmnt = null;
        int num=0;
        try {
            cnnct = getConnection();
            String preQueryStatement = "UPDATE User SET status=? WHERE id=?";
            pStmnt = cnnct.prepareStatement(preQueryStatement);
            pStmnt.setString(1, status);
            pStmnt.setString(2, id);
            //Statement s = cnnct.createStatement();
            num= pStmnt.executeUpdate();
          
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
         return (num == 1) ? true : false;   
    }
     
     public ArrayList queryUser(String selfId) {
        Connection cnnct = null;
        PreparedStatement pStmnt = null;
        try {
            cnnct = getConnection();
            String preQueryStatement = "SELECT * FROM  User";
            pStmnt = cnnct.prepareStatement(preQueryStatement);
            //Statement s = cnnct.createStatement();
            ResultSet rs = pStmnt.executeQuery();

            ArrayList list = new ArrayList();

            while (rs.next()) {
                if(!rs.getString(1).equals(selfId)){
                User u = new User();
                u.setUserId(rs.getString(1));
                u.setUserType(rs.getString(2));
                u.setUserEmail(rs.getString(3));
                u.setUsername(rs.getString(4));
                u.setPassword(rs.getString(5));
                u.setUserStatus(rs.getString(6));
                list.add(u);
                }
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
     public User queryTargetUser(String id) {
        Connection cnnct = null;
        PreparedStatement pStmnt = null;
        try {
            cnnct = getConnection();
            String preQueryStatement = "SELECT email,username,password,id FROM  User WHERE id=?";
            pStmnt = cnnct.prepareStatement(preQueryStatement);
            pStmnt.setString(1, id);
            ResultSet rs = pStmnt.executeQuery();

            User u = new User();
            if (rs.next()) {
                u.setUserEmail(rs.getString(1));
                u.setUsername(rs.getString(2));
                u.setPassword(rs.getString(3));
                u.setUserId(rs.getNString(4));
            }
            return u;
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
     public void updateUser(String id,String name,String email,String password) {
        Connection cnnct = null;
        PreparedStatement pStmnt = null;
        try {
            cnnct = getConnection();
            String preQueryStatement = "UPDATE User SET email=?,username=?,password=? WHERE id=?";
            pStmnt = cnnct.prepareStatement(preQueryStatement);
            pStmnt.setString(1, email);
            pStmnt.setString(2, name);
            pStmnt.setString(3, password);
            pStmnt.setString(4, id);
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

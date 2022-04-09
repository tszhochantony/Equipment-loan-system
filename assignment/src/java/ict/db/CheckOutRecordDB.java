package ict.db;

import ict.bean.CheckOutRecord;
import ict.bean.Equipment;
import ict.bean.Report;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import static java.util.Date.from;
import java.util.concurrent.TimeUnit;

public class CheckOutRecordDB {
    private String dburl;
    private String dbUser;
    private String dbPassword;
    
    public CheckOutRecordDB(String dburl, String dbUser, String dbPassword) {
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
    public void createCheckOutRecordTable() {
        Statement statement = null;
        Connection connection  = null;

        try {
            connection = getConnection();
            statement = connection.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS CheckOutRecord (" 
                       + "rId varchar(25) NOT NULL,"
                       + "sId varchar(25) NOT NULL,"
                       + "reservationDate date NOT NULL,"
                       + "status varchar(25) NOT NULL,"
                       + "tId varchar(25) NULL,"
                       + "handleDate date NULL,"
                       + "returnDate date NULL,"
                       + "PRIMARY KEY (rId),"
                       + "FOREIGN KEY (sId) REFERENCES User (id),"
                       + "FOREIGN KEY (tId) REFERENCES User (id)"
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
    
        public String addCheckOutRecord(String sId) { 
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        String rId = "R";
        try {
            connection = getConnection();
            String preQueryStatement = "SELECT count(*) FROM `CheckOutRecord`";
            preparedStatement = connection.prepareStatement(preQueryStatement);
            ResultSet rs = preparedStatement.executeQuery();
            Date toDate = java.sql.Date.valueOf(java.time.LocalDate.now());
            if (rs.next()) {
                int count = rs.getInt(1);
                if(count>0){
                    String number = String.valueOf(count+1);
                    while(count>0&&count<100000){
                        count*=10;
                        rId+="0";
                    }
                    rId+=number;
                }
                else{rId += "000001";}
            }
            preQueryStatement = "INSERT INTO CheckOutRecord(`rId`, `sId`, `reservationDate`, `status`) VALUES (?, ?, ?, ?)";
            preparedStatement = connection.prepareStatement(preQueryStatement);
            preparedStatement.setString(1, rId);
            preparedStatement.setString(2, sId);
            preparedStatement.setDate(3,toDate);
            preparedStatement.setString(4,"pending");
            int rowCount = preparedStatement.executeUpdate();
            if (rowCount >= 1) {
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
        return rId;
    }
        public String isValidRid(String sId) throws SQLException, IOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        String rId="";
        try {
            connection = getConnection();
            String preQueryStatement = "SELECT rId FROM `CheckOutRecord` WHERE sId=? AND status='borrowed'";
            preparedStatement = connection.prepareStatement(preQueryStatement);
            preparedStatement.setString(1, sId);
            ResultSet result = preparedStatement.executeQuery();
            if (result.next()){
                rId = result.getString(1);
            }
        } catch (SQLException ex) {
            while (ex != null) {
                ex.printStackTrace();
                ex = ex.getNextException();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return rId; 
    }
    public boolean queryValidId(String sId) {
        Connection cnnct = null;
        PreparedStatement pStmnt = null;
        boolean check =false;
        try {
            cnnct = getConnection();
            String preQueryStatement = "SELECT rId FROM `CheckOutRecord` WHERE sId=? AND (status='pending' OR status='borrowed') ";
            pStmnt = cnnct.prepareStatement(preQueryStatement);  
            pStmnt.setString(1, sId);
            ResultSet rs = pStmnt.executeQuery();
            if(rs.next()){
                check=true;
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
        return check;
    }
    public String queryStatusById(String sId) {
        Connection cnnct = null;
        PreparedStatement pStmnt = null;
        String message = "";
        boolean checked=false;
        Date oldDate =null;
        try {
            cnnct = getConnection();
            String preQueryStatement = "SELECT status,handleDate FROM `CheckOutRecord` WHERE sId=?";
            pStmnt = cnnct.prepareStatement(preQueryStatement);  
            pStmnt.setString(1, sId);
            ResultSet rs = pStmnt.executeQuery();
            while(rs.next()&&checked==false){
                String status= rs.getString(1);
                    if(status.equals("pending")){
                        message = "You reservation is waiting accepted.Please wait. ";
                        checked=true;
                        break;
                    }else if(status.equals("borrowed")){
                        message = "You reservation was accepted.Please come to equipment room to pick up. ";
                        checked=true;
                        break;
                    }else if(status.equals("rejected")){
                        Date toDate = java.sql.Date.valueOf(java.time.LocalDate.now());
                        oldDate = rs.getDate(2);
                        long diffInMillies = Math.abs(toDate.getTime() - oldDate.getTime());
                        int count = (int) (diffInMillies / (1000*60*60*24));
                        if(count<10){
                            message = "You reservation was rejected.";
                        }
                    }
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
        return message;
    }
    public ArrayList<CheckOutRecord> queryPendingReservation() {
        Connection cnnct = null;
        PreparedStatement pStmnt = null;
        boolean check =false;
        ArrayList<CheckOutRecord> list = new ArrayList<CheckOutRecord>();
        try {
            cnnct = getConnection();
            String preQueryStatement = "SELECT * FROM `CheckOutRecord` WHERE status='pending'";
            pStmnt = cnnct.prepareStatement(preQueryStatement);  
            ResultSet rs = pStmnt.executeQuery();
            while(rs.next()){
                CheckOutRecord cor = new CheckOutRecord();
                cor.setRId(rs.getString(1));
                cor.setSId(rs.getString(2));
                cor.setResDate(rs.getDate(3));
                cor.setStatus(rs.getString(4));     
                list.add(cor);
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
        return list;
    }
    public ArrayList<CheckOutRecord> queryReservation() {
        Connection cnnct = null;
        PreparedStatement pStmnt = null;
        ArrayList<CheckOutRecord> list = new ArrayList<CheckOutRecord>();
        try {
            cnnct = getConnection();
            String preQueryStatement = "SELECT * FROM `CheckOutRecord`";
            pStmnt = cnnct.prepareStatement(preQueryStatement);  
            ResultSet rs = pStmnt.executeQuery();
            while(rs.next()){
                CheckOutRecord cor = new CheckOutRecord();
                cor.setRId(rs.getString(1));
                cor.setSId(rs.getString(2));
                cor.setResDate(rs.getDate(3));
                cor.setStatus(rs.getString(4));  
                cor.setTId(rs.getString(5)); 
                cor.setHanDate(rs.getDate(6));  
                cor.setRetDate(rs.getDate(7));
                list.add(cor);
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
        return list;
    }
    public ArrayList<CheckOutRecord> queryStudentReservation(String sId) {
        Connection cnnct = null;
        PreparedStatement pStmnt = null;
        ArrayList<CheckOutRecord> list = new ArrayList<CheckOutRecord>();
        try {
            cnnct = getConnection();
            String preQueryStatement = "SELECT * FROM `CheckOutRecord` WHERE sId=?";
            pStmnt = cnnct.prepareStatement(preQueryStatement);  
            pStmnt.setString(1, sId);
            ResultSet rs = pStmnt.executeQuery();
            while(rs.next()){
                CheckOutRecord cor = new CheckOutRecord();
                cor.setRId(rs.getString(1));
                cor.setSId(rs.getString(2));
                cor.setResDate(rs.getDate(3));
                cor.setStatus(rs.getString(4));  
                cor.setTId(rs.getString(5)); 
                cor.setHanDate(rs.getDate(6));  
                cor.setRetDate(rs.getDate(7));
                list.add(cor);
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
        return list;
    }
    public ArrayList<CheckOutRecord> queryOverdueReservation(String sId) {
        Connection cnnct = null;
        PreparedStatement pStmnt = null;
        ArrayList<CheckOutRecord> list = new ArrayList<CheckOutRecord>();
        try {
            cnnct = getConnection();
            String preQueryStatement = "SELECT rId,handleDate FROM `CheckOutRecord` WHERE status='borrowed' AND sId =?";
            pStmnt = cnnct.prepareStatement(preQueryStatement);  
            pStmnt.setString(1, sId);
            ResultSet rs = pStmnt.executeQuery();
            while(rs.next()){
                Date handleDate = rs.getDate(2);
                Date toDate = java.sql.Date.valueOf(java.time.LocalDate.now());
                long diffInMillies = Math.abs(toDate.getTime() - handleDate.getTime());
                int count = (int) (diffInMillies / (1000*60*60*24));
                if(count>14){
                    CheckOutRecord cor = new CheckOutRecord();
                    cor.setRId(rs.getString(1));
                    cor.setHanDate(rs.getDate(2));
                    list.add(cor);
                }               
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
        return list;
    }
    public ArrayList<CheckOutRecord> queryAllOverdueReservation() {
        Connection cnnct = null;
        PreparedStatement pStmnt = null;
        ArrayList<CheckOutRecord> list = new ArrayList<CheckOutRecord>();
        try {
            cnnct = getConnection();
            String preQueryStatement = "SELECT rId,sId,handleDate FROM `CheckOutRecord` WHERE status='borrowed'";
            pStmnt = cnnct.prepareStatement(preQueryStatement);  
            ResultSet rs = pStmnt.executeQuery();
            while(rs.next()){
                Date handleDate = rs.getDate(3);
                Date toDate = java.sql.Date.valueOf(java.time.LocalDate.now());
                long diffInMillies = Math.abs(toDate.getTime() - handleDate.getTime());
                int count = (int) (diffInMillies / (1000*60*60*24));
                if(count>14){
                    CheckOutRecord cor = new CheckOutRecord();
                    cor.setRId(rs.getString(1));
                    cor.setSId(rs.getString(2));
                    cor.setHanDate(rs.getDate(3));
                    list.add(cor);
                }               
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
        return list;
    }
    public ArrayList<String> queryEquipment(String year) {
        Connection cnnct = null;
        PreparedStatement pStmnt = null;
        ArrayList<String> equipments = new ArrayList<String>();
        String equipment;
        try {
            cnnct = getConnection();
            String preQueryStatement = "SELECT equipment\n" +
                                        "FROM checkoutrecord\n" +
                                        "INNER JOIN equipmentlist\n" +
                                        "ON checkoutrecord.rId=equipmentlist.rId\n" +
                                        "WHERE YEAR(handleDate)=? AND (status='borrowed' OR status='finish')\n" +
                                        "GROUP BY equipment;";
            pStmnt = cnnct.prepareStatement(preQueryStatement);  
            pStmnt.setString(1, year);
            ResultSet rs = pStmnt.executeQuery();
            while(rs.next()){
                equipment = rs.getString(1);
                equipments.add(equipment);
            }
            return equipments;
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
    public ArrayList<String> queryRecordYear() {
        Connection cnnct = null;
        PreparedStatement pStmnt = null;
        ArrayList<String> years = new ArrayList<String>();
        String year;
        try {
            cnnct = getConnection();
            String preQueryStatement = "SELECT YEAR(`handleDate`) FROM `checkoutrecord` WHERE handleDate IS NOT NULL GROUP BY YEAR(`handleDate`) DESC;";
            pStmnt = cnnct.prepareStatement(preQueryStatement); 
            ResultSet rs = pStmnt.executeQuery();
            while(rs.next()){
                year = rs.getString(1);
                years.add(year);
            }
            return years;
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
    public Report queryEquipmentCountEachMonth(String equipment,String year) {
        Connection cnnct = null;
        PreparedStatement pStmnt = null;
        ArrayList<Integer> months = new ArrayList<Integer>();
        ArrayList<Integer> counts = new ArrayList<Integer>();
        Report r = new Report();
        boolean include=false;
        try {
            cnnct = getConnection();
            String preQueryStatement = "SELECT handleDate,`count`\n" +
                                        "FROM checkoutrecord\n" +
                                        "INNER JOIN equipmentlist\n" +
                                        "ON checkoutrecord.rId=equipmentlist.rId\n" +
                                        "WHERE equipment=? AND YEAR(handleDate)=? AND (status ='finish' OR status = 'borrowed' )\n" +
                                        "ORDER BY handleDate ASC;";
            pStmnt = cnnct.prepareStatement(preQueryStatement);  
            pStmnt.setString(1, equipment);
            pStmnt.setString(2, year);
            ResultSet rs = pStmnt.executeQuery();
            while(rs.next()){
                int index=0;
                Date handleDate = rs.getDate(1);
                Calendar cal = Calendar.getInstance();
                cal.setTime(handleDate);
                int thisMonth = cal.get(Calendar.MONTH);
                for(int i=0;i<months.size();i++){
                    if(thisMonth==months.get(i)){
                        include=true;
                        index=i;
                        break;
                    }
                }
                if(include==false){
                    months.add(thisMonth);
                    counts.add(rs.getInt(2));
                }
                else{
                    int oldCount = counts.get(index);
                    int newCount = rs.getInt(2);
                    counts.set(index, (oldCount+newCount));
                }
            }
            r.setEquipmentType(equipment);
            r.setMonth(months);
            r.setEquipmentCount(counts);
            return r;
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
    public ArrayList<String> queryRIdByMonth(int year,int month) {
        Connection cnnct = null;
        PreparedStatement pStmnt = null;
        Date handleDate =null;
        ArrayList<String> rId = new ArrayList<String>();
        try {
            cnnct = getConnection();
            String preQueryStatement = "SELECT rId,handleDate FROM `CheckOutRecord` WHERE status='finish' OR status='borrowed' GROUP BY handleDate";
            pStmnt = cnnct.prepareStatement(preQueryStatement);  
            ResultSet rs = pStmnt.executeQuery();
            while(rs.next()){
                handleDate = rs.getDate(2);
                Calendar cal = Calendar.getInstance();
                cal.setTime(handleDate);
                int thisYear = cal.get(Calendar.YEAR);
                int thisMonth = cal.get(Calendar.MONTH);
                if(thisYear==year&&thisMonth==month){
                    String id = rs.getString(1);
                    rId.add(id);
                }
            }
            return rId;
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
    public void UpdateRecord(String rId,String judgement,String tId) {
         Connection cnnct = null;
        PreparedStatement pStmnt = null;     
        Date toDate = java.sql.Date.valueOf(java.time.LocalDate.now());
        try {
            cnnct = getConnection();
            String preQueryStatement = "UPDATE checkoutrecord SET status=?,tId=?, handleDate"
                    + "=? WHERE rId=?";
            pStmnt = cnnct.prepareStatement(preQueryStatement);
            pStmnt.setString(1,judgement);
            pStmnt.setString(2, tId);
            pStmnt.setDate(3,toDate);
            pStmnt.setString(4, rId);
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
    public void FinishRecord(String rId) {
        Connection cnnct = null;
        PreparedStatement pStmnt = null;     
        Date toDate = java.sql.Date.valueOf(java.time.LocalDate.now());
        try {
            cnnct = getConnection();
            String preQueryStatement = "UPDATE checkoutrecord SET status='finish',returnDate=? WHERE rId=?";
            pStmnt = cnnct.prepareStatement(preQueryStatement);
            pStmnt.setDate(1,toDate);
            pStmnt.setString(2, rId);
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

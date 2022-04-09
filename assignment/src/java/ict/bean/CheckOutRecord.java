package ict.bean;
import java.io.Serializable;
import java.sql.Date;

public class CheckOutRecord implements Serializable {
    private String rId;
    private String sId;
    private String elId;
    private Date resDate;
    private String status;
    private String tId;
    private Date hanDate;
    private Date retDate;
    
    public CheckOutRecord() {  }
    
    public void setRId(String rId) { this.rId = rId; }
    public void setSId(String sId) { this.sId = sId; }
    public void setResDate(Date resDate) { this.resDate = resDate;}
    public void setStatus(String status) { this.status = status; }
    public void setTId(String tId) { this.tId = tId;}
    public void setHanDate(Date hanDate) { this.hanDate = hanDate;}
    public void setRetDate(Date retDate) { this.retDate = retDate;}
    
    public String getRId() { return rId; }
    public String getSId() { return sId; }
    public Date getResDate() { return resDate;}
    public String getStatus() { return status; }
    public String getTId() { return tId; }
    public Date getHanDate() { return hanDate;}
    public Date getRetDate() { return retDate;}
}

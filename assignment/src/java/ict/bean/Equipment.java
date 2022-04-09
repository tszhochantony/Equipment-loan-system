package ict.bean;

import java.io.Serializable;

public class Equipment implements Serializable {
    private String eId;
    private String eType;
    private String eStatus;
    private int count;
    
    public Equipment() {  }
    
    public void setEId(String eId) { this.eId = eId; }
    public void setEType(String eType) { this.eType = eType; }
    public void setEStatus(String eStatus) { this.eStatus = eStatus; }
    public void setCount(int count) { this.count = count; }
    public String getEId() { return eId; }
    public int getCount() { return count; }
    public String getEType() { return eType; }
    public String getEStatus() { return eStatus; }
}

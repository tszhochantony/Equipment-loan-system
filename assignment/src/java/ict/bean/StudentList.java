package ict.bean;

import java.io.Serializable;

public class StudentList implements Serializable {
    private String id;
    private String eType;
    private int count;
        
    
    public void setId(String id) { this.id = id; }
    public void setEType(String eType) { this.eType = eType; }
    public void setCount(int count) { this.count = count; }
    public String getId() { return id; }
    public String getEType() { return eType; }
    public int getCount() { return count; }
}

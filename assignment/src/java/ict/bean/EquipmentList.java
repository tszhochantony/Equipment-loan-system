package ict.bean;

import java.io.Serializable;

public class EquipmentList implements Serializable {
    private String rId;
    private String equipment;
    private int count;
    
    public EquipmentList() {  }
    
    public void setRId(String rId) { this.rId = rId; }
    public void setEquipment(String equipment) { this.equipment = equipment; }
    public void setCount(int count) { this.count = count; }
    public String getRId() { return rId; }
    public String getEquipment() { return equipment; }
    public int getCount() { return count; }
}

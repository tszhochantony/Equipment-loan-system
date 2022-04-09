package ict.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class Report implements Serializable {
    private String equipmentType;
    private ArrayList<Integer> month = new ArrayList<Integer>();;
    private ArrayList<Integer> equipmentCount= new ArrayList<Integer>();
    
    public Report() {  }
    
    public void setEquipmentType(String equipmentType) { this.equipmentType = equipmentType; }
    public void setMonth(ArrayList<Integer> month) { this.month = month; }
    public void setEquipmentCount(ArrayList<Integer> equipmentCount) { this.equipmentCount = equipmentCount; }
    public String getEquipmentType() { return equipmentType; }
    public ArrayList<Integer> getMonth() { return month; }
    public ArrayList<Integer> getEquipmenCount() { return equipmentCount; }
}

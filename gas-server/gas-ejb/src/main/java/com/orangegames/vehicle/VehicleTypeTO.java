package com.orangegames.vehicle;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlType;

/**
 *  VehicleTypeTO
 */
@XmlType( name="VehicleTypeTO", namespace="com.orangegames.vehicle" )
public class VehicleTypeTO implements Serializable {

    /**
     * Data
     */
    
    private int id;
    private int modelYear = 0;
    private String make = "";
    private String model = "";
    private boolean deleted;
    
    /**
     * Getters/Setters
     */
    
    public int getId() { return id; }
    public void setId(int value) { this.id = value; }
    
    public int getModelYear() { return modelYear; }
    public void setModelYear(int value) { this.modelYear = value; }
    
    public String getMake() { return make; }
    public void setMake(String value) { this.make = value; }

    public String getModel() { return model; }
    public void setModel(String value) { this.model = value; }
    
    public boolean isDeleted() { return deleted; }
    public void setDeleted(boolean value) { this.deleted = value; }
}

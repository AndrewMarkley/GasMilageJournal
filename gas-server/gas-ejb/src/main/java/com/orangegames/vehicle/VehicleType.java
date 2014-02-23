package com.orangegames.vehicle;

import com.orangegames.BaseEntity;
import javax.persistence.*;

/**
 *  VehicleType
 */
@Entity
@Table(name = "VehicleType")
public class VehicleType extends BaseEntity {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * Data
     */
    private int modelYear = 0;
    private String make = "";
    private String model = "";
    private boolean deleted;
    
    /**
     * Getters/Setters
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getId() { return id; }
    public void setId(int value) { this.id = value; }
    
    @Column(name = "modelYear")
    public int getModelYear() { return modelYear; }
    public void setModelYear(int value) { this.modelYear = value; }
    
    @Column(name = "make")
    public String getMake() { return make; }
    public void setMake(String value) { this.make = value; }

    @Column(name = "model")
    public String getModel() { return model; }
    public void setModel(String value) { this.model = value; }
    
    @Column(name = "deleted")
    public boolean isDeleted() { return deleted; }
    public void setDeleted(boolean value) { this.deleted = value; }
}

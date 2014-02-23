package com.orangegames.car;

import com.orangegames.BaseEntity;
import com.orangegames.fillups.FillUp;
import com.orangegames.vehicle.VehicleType;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

/**
 * Car
 */
@Entity
@Table(name = "Car")
public class Car extends BaseEntity
{

    private static final long serialVersionUID = 1L;

    /**
     * Data
     */
    private VehicleType vehicleType;
    private Double engineSize;
    private Double milage;
    private String name;
    private List<FillUp> fillUps = new ArrayList<>();
    private boolean deleted;

    /**
     * Getters/Setters
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getId() { return id; }
    public void setId(int value) { this.id = value; }
    
    @JoinColumn(name = "vehicleType")
    public VehicleType getVehicleType() { return vehicleType; }
    public void setVehicleType(VehicleType value) { this.vehicleType = value; }
    
    @JoinColumn(name = "fillUps")
    public List<FillUp> getFillUps() { return fillUps; }
    public void setFillUps(List<FillUp> value) { this.fillUps = value; }
    
    @Column(name = "name")
    public String getName() { return name; }
    public void setName(String value) { this.name = value; }
    
    @Column(name = "engineSize")
    public Double getEngineSize() { return engineSize; }
    public void setEngineSize(Double value) { this.engineSize = value; }
    
    @Column(name = "milage")
    public Double getMilage() { return milage; }
    public void setMilage(Double value) { this.milage = value; }
    
    @Column(name = "deleted")
    public boolean isDeleted() { return deleted; }
    public void setDeleted(boolean value) { this.deleted = value; }
}

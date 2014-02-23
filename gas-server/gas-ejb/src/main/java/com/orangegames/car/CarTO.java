package com.orangegames.car;

import com.orangegames.fillups.FillUpTO;
import com.orangegames.vehicle.VehicleTypeTO;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlType;

/**
 *  CarTO
 */
@XmlType( name="CarTO", namespace="com.orangegames.car" )
public class CarTO implements Serializable {
    
    /**
     * Data
     */
    
    private int id;
    private VehicleTypeTO vehicleType;
    private double engineSize = 0.0;
    private double milage = 0;
    private String name = "";
    private List<FillUpTO> fillUps = new ArrayList<>();
    private boolean deleted;

    /**
     * Getters/Setters
     */
    
    public int getId() { return id; }
    public void setId(int value) { this.id = value; }
    
    public VehicleTypeTO getVehicleType() { return vehicleType; }
    public void setVehicleType(VehicleTypeTO value) { this.vehicleType = value; }
    
    public List<FillUpTO> getFillUps() { return fillUps; }
    public void setFillUps(List<FillUpTO> value) { this.fillUps = value; }
    
    public String getName() { return name; }
    public void setName(String value) { this.name = value; }
    
    public Double getEngineSize() { return engineSize; }
    public void setEngineSize(Double value) { this.engineSize = value; }
    
    public Double getMilage() { return milage; }
    public void setMilage(Double value) { this.milage = value; }
    
    public boolean isDeleted() { return deleted; }
    public void setDeleted(boolean value) { this.deleted = value; }
}

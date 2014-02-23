package com.orangegames.fillups;

import com.orangegames.BaseEntity;
import java.util.Date;
import javax.persistence.*;

/**
 * FillUp
 */
@Entity
@Table(name = "FillUp")
public class FillUp extends BaseEntity
{

    /**
     * Data
     */
    private double distance;
    private double gas;
    private double price;
    private double totalCost;
    private double mpg;
    private String comments = "";
    private Date fillUpDate = null;
    private boolean deleted;

    /**
     * Getters/Setters
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getId() { return id; }
    public void setId(int value) { this.id = value; }
    
    @Column(name = "distance")
    public double getDistance() { return distance; }
    public void setDistance(double value) { this.distance = value; }
    
    @Column(name = "gas")
    public double getGas() { return gas; }
    public void setGas(double value) { this.gas = value; }
    
    @Column(name = "price")
    public double getPrice() { return price; }
    public void setPrice(double value) { this.price = value; }
    
    @Column(name = "totalCost")
    public double getTotalCost() { return totalCost; }
    public void setTotalCost(double value) { this.totalCost = value; }
    
    @Column(name = "mpg")
    public double getMpg() { return mpg; }
    public void setMpg(double value) { this.mpg = value; }

    @Column(name = "comments")
    public String getComments() { return comments; }
    public void setComments(String value) { this.comments = value; }
    
    @Column(name = "fillUpDate")
    @Temporal(javax.persistence.TemporalType.DATE)
    public Date getFillUpDate() { return fillUpDate; }
    public void setFillUpDate(Date value) { this.fillUpDate = value; }
    
    @Column(name = "deleted")
    public boolean isDeleted() { return deleted; }
    public void setDeleted(boolean value) { this.deleted = value; }
    
}

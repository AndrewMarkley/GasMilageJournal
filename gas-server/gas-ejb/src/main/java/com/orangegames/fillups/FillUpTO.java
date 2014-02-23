package com.orangegames.fillups;

import java.util.Date;

/**
 *  FillUpTO
 */
public class FillUpTO {

    /**
     * Data
     */
    private int id;
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
    public int getId() { return id; }
    public void setId(int value) { this.id = value; }
    
    public double getDistance() { return distance; }
    public void setDistance(double value) { this.distance = value; }
    
    public double getGas() { return gas; }
    public void setGas(double value) { this.gas = value; }
    
    public double getPrice() { return price; }
    public void setPrice(double value) { this.price = value; }
    
    public double getTotalCost() { return totalCost; }
    public void setTotalCost(double value) { this.totalCost = value; }
    
    public double getMpg() { return mpg; }
    public void setMpg(double value) { this.mpg = value; }

    public String getComments() { return comments; }
    public void setComments(String value) { this.comments = value; }
    
    public Date getFillUpDate() { return fillUpDate; }
    public void setFillUpDate(Date value) { this.fillUpDate = value; }
    
    public boolean isDeleted() { return deleted; }
    public void setDeleted(boolean value) { this.deleted = value; }
    
}

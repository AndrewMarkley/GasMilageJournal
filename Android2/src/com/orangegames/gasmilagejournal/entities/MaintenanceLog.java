package com.orangegames.gasmilagejournal.entities;

import java.io.Serializable;
import java.util.Date;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "MaintenanceLog")
public class MaintenanceLog implements Serializable
{
	private static final long serialVersionUID = 1L;

	@DatabaseField(generatedId = true)
	private int id = 0;
	
	@DatabaseField(canBeNull = false)
	private int carId = 0;

	@DatabaseField(canBeNull = false)
	private Date date = new Date();

	@DatabaseField(canBeNull = false)
	private Double cost = 0.0;

	@DatabaseField(canBeNull = false)
	private Double odometer = 0.0;

	@DatabaseField(canBeNull = false)
	private String title = "";

	@DatabaseField(canBeNull = false)
	private String description = "";

	@DatabaseField(canBeNull = false)
	private String location = "";

	@DatabaseField(dataType = DataType.BYTE_ARRAY)
	byte[] receipt;

	public MaintenanceLog() {}

	public MaintenanceLog(int carId, Date date, double cost, double odometer, String title, String description, String location, byte[] receipt) 
	{
		this.carId = carId;
		this.date = date;
		this.cost = cost;
		this.odometer = odometer;
		this.title = title;
		this.setDescription(description);
		this.location = location;
		this.receipt = receipt;
	}

	public int getId() { return id; }
	public void setId(int value) { this.id = value; }
	
	public int getCarId() { return carId; }
	public void setCarId(int value) { this.carId = value; }
	
	public String getDescription() { return description; }
	public void setDescription(String value) { this.description = value; }
	
	public String getTitle() { return title; }
	public void setTitle(String value) { this.description = title; }
	
	public String getLocation() { return location; }
	public void setLocation(String value) { this.location = value; }
	
	public Double getCost() { return cost; }
	public void setCost(Double value) { this.cost = value; }
	
	public Double getOdometer() { return odometer; }
	public void setOdometer(Double value) { this.odometer = value; }
	
	public Date getDate() { return date; }
	public void setDate(Date value) { this.date = value; }
	
	public byte[] getReceipt() { return receipt; }
	public void setReceipt(byte[] value) { this.receipt = value; }
	
	public String toString()
	{
		return getTitle() + " " + getDescription() + " " + getCost() + " " + getDate();   
	}
}

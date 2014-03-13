package com.orangegames.gasmilagejournal.entities;

import java.io.Serializable;
import java.util.Date;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "FillUps")
public class FillUp implements Serializable
{
	private static final long serialVersionUID = 1L;
	public static final String COLUMN_ID = "id";
	public static final String COLUMN_DISTANCE = "distance";
	public static final String COLUMN_GAS = "gas";
	public static final String COLUMN_PRICE = "price";
	public static final String COLUMN_TOTAL_COST = "totalCost";
	public static final String COLUMN_MPG = "mpg";
	public static final String COLUMN_COMMENTS = "comments";
	public static final String COLUMN_DATE = "date";
	public static final String COLUMN_CAR_ID = "carid";
	public static final String COLUMN_RECEIPT = "receipt";

	@DatabaseField(generatedId = true)
	private int id = 0;
	
	@DatabaseField(canBeNull = false)
	private double distance;
	
	@DatabaseField(canBeNull = false)
	double gas;
	
	@DatabaseField(canBeNull = false)
	double price;
	
	@DatabaseField(canBeNull = false)
	double totalCost;
	
	@DatabaseField(canBeNull = false)
	double mpg;
	
	@DatabaseField(canBeNull = false)
	String comments = "";
	
	@DatabaseField(canBeNull = false)
	Date date = new Date();
	
	@DatabaseField(canBeNull = false)
	int carId = 0;
	
	@DatabaseField(dataType = DataType.BYTE_ARRAY)
	byte[] receipt;
	
	public FillUp() {

	}

	public FillUp(int carId, double distance, double gas, double price, Date date, String comments, byte[] receipt) {
		this.distance = distance;
		this.gas = gas;
		this.price = round(price, 2);
		this.comments = comments;
		this.mpg = round(distance / gas, 3);
		this.totalCost = gas * price;
		this.date = date;
		this.carId = carId;
		this.receipt = receipt;
	}

	public int getId() { return id; }
	public void setId(int value) { this.id = value; }
	
	public int getCarId() { return carId; }
	public void setCarId(int value) { this.carId = value; }

	public Date getDate() { return date; }
	public void setDate(Date value) { this.date = value; } 
	
	public double getDistance() { return distance; }
	public void setDistance(double value) { this.distance = value; }

	public double getGas() { return gas; }
	public void setGas(double value) { this.gas = value; }

	public double getPrice() { return price; }
	public void setPrice(double value) { this.price = value; }

	public double getTotalCost() { return totalCost; }
	public void setTotalCost(double value) { this.totalCost = value; }

	public String getComments() { return comments; }
	public void setComments(String value) { this.comments = value; }

	public double getMPG() { return mpg; }
	public void setMPG(double value) { this.mpg = value; }
	
	public byte[] getReceipt() { return receipt; }
	public void setReceipt(byte[] value) { this.receipt = value; }

	public double round(double value, int places)
	{
		if ( places < 0 ) {
			return value;
		}
		long factor = (long) Math.pow(10, places);
		value = value * factor;
		long tmp = Math.round(value);
		return (double) tmp / factor;
	}

	@Override
	public String toString()
	{
		return "Car: " + carId + ", Distance: " + distance + ", Gas: " + gas + ", Price per Gallon: $" + price + ", Total Cost of Fill Up: $" + totalCost + ", MPG: " + mpg + ", Date: " + date
				+ ", Comments: " + comments;
	}

}

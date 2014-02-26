package com.orangegames.gasmilagejournal.car;

import java.util.ArrayList;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.orangegames.gasmilagejournal.fillup.FillUp;

@DatabaseTable(tableName = "Cars")
public class Car
{
	@DatabaseField(id = true)
	private int id;
	
	@DatabaseField(canBeNull = false)
	private int year = 0;
	
	@DatabaseField(canBeNull = false)
	private String name = "";
	
	@DatabaseField(canBeNull = false)
	private String make = "";
	
	@DatabaseField(canBeNull = false)
	private String model = "";
	
	@DatabaseField(canBeNull = false)
	private double engineSize = 0.0;
	
	@DatabaseField(canBeNull = false)
	private double milage = 0;
	
	@DatabaseField(canBeNull = false)
	ArrayList<FillUp> fillUps = new ArrayList<FillUp>();
	
	@DatabaseField(canBeNull = false)
	private boolean deleted;

	public Car()
	{
		
	}
	
	public Car(String name, int year, String make, String model, double milage) {
		this.year = year;
		this.make = make;
		this.model = model;
		this.milage = milage;
		this.name = name;
	}

	public Car(String name, int year, String make, String model, double milage, ArrayList<FillUp> fillups) {
		this.year = year;
		this.make = make;
		this.model = model;
		this.milage = milage;
		this.fillUps = fillups;
		this.name = name;
	}

	public Car(String[] changes) {
		name = changes[0];
		year = Integer.parseInt(changes[1]);
		make = changes[2];
		model = changes[3];
		milage = Double.parseDouble(changes[4]);
	}

	public String getName() { return name; }

	public void setMilage(double x) { milage = x; }

	public int getYear() { return year; }
	public void setYear(int value) { year = value; }

	public String getMake() { return make; }
	public void setMake(String value) { make = value; }

	public String getModel() { return model; }
	public void setModel(String value) { model = value; }

	public double getEngineSize() { return engineSize; }
	public void setEngineSize(double value) { engineSize = value; }

	public double getMilage() { return milage; }
	public void setMilage(int value) { milage = value; }

	public ArrayList<FillUp> getFillUps() { return fillUps; }
	public void setFillUps(ArrayList<FillUp> value) { fillUps = value; }
	public void addFillUp(FillUp value) { fillUps.add(value); }
	
	public boolean isDeleted() { return deleted; }
	public void setDeleted(boolean value) { this.deleted = value; }

	@Override
	public String toString()
	{
		return "Year: " + year + " make: " + make + " model: " + model + " mileage: " + milage + " name: " + name;
	}

	public String toOutputFileString()
	{
		return "" + name + " , " + year + " , " + make + " , " + model + " , " + milage;
	}

}

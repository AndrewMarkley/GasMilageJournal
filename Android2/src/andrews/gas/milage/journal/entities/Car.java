package andrews.gas.milage.journal.entities;

import java.io.Serializable;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "Cars")
public class Car implements Serializable
{
	private static final long serialVersionUID = 1L;

	@DatabaseField(generatedId = true)
	private int id = 1;
	
	@DatabaseField(canBeNull = false)
	private int year = 0;
	
	@DatabaseField(canBeNull = false)
	private String name = "";
	
	@DatabaseField(canBeNull = false)
	private String make = "";
	
	@DatabaseField(canBeNull = false)
	private String model = "";
	
	@DatabaseField(canBeNull = false)
	private double milage = 0;	

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
	
	public int getId() { return id; }
	public void setId(int value) { id = value; }

	public String getName() { return name; }
	public void setName(String value) { this.name = value; }

	public int getYear() { return year; }
	public void setYear(int value) { year = value; }

	public String getMake() { return make; }
	public void setMake(String value) { make = value; }

	public String getModel() { return model; }
	public void setModel(String value) { model = value; }
	
	public double getMilage() { return milage; }
	public void setMilage(double value) { milage = value; }

	@Override
	public String toString()
	{
		return id + " " + name + " \t\t" + year + " " + make + " " + model;
	}

}

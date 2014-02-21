package andrews.gas.milage.journal;

import java.util.ArrayList;

public class Car
{
	int year = 0;
	String name = "";
	String make = "";
	String model = "";
	double engineSize = 0.0;
	double milage = 0;
	ArrayList<FillUp> fillUps = new ArrayList<FillUp> ( );

	public Car(String name, int year, String make, String model, double milage) {
		this.year = year;
		this.make = make;
		this.model = model;
		this.milage = milage;
		this.name = name;
	}

	public Car(String name, int year, String make, String model, double milage,
			ArrayList<FillUp> fillups) {
		this.year = year;
		this.make = make;
		this.model = model;
		this.milage = milage;
		this.fillUps = fillups;
		this.name = name;
	}

	public Car(String[] changes) {
		for ( int ctr = 0; ctr < changes.length; ctr++ ) {
			System.out.println ( changes[ctr] );
		}

		name = changes[0];
		year = Integer.parseInt ( changes[1] );
		make = changes[2];
		model = changes[3];
		milage = Double.parseDouble ( changes[4] );
	}

	public String getName()
	{
		return name;
	}

	public void setMilage(double x)
	{
		milage = x;
	}

	public void setYear(int x)
	{
		year = x;
	}

	public void setMake(String x)
	{
		make = x;
	}

	public void setModel(String x)
	{
		model = x;
	}

	public void setEngineSize(double x)
	{
		engineSize = x;
	}

	public void setFillUps(ArrayList<FillUp> x)
	{
		fillUps = x;
	}

	public void addFillUp(FillUp x)
	{
		fillUps.add ( x );
	}

	public int getYear()
	{
		return year;
	}

	public String getMake()
	{
		return make;
	}

	public String getModel()
	{
		return model;
	}

	public double getEngineSize()
	{
		return engineSize;
	}

	public ArrayList<FillUp> getFillUps()
	{
		return fillUps;
	}

	public double getMilage()
	{
		return milage;
	}

	@Override
	public String toString()
	{
		return "Year: " + year + " make: " + make + " model: " + model
				+ " mileage: " + milage + " name: " + name;
	}

	public String toOutputFileString()
	{
		return "" + name + " , " + year + " , " + make + " , " + model + " , "
				+ milage;
	}

}

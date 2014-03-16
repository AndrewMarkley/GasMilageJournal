package andrews.gas.milage.journal;

import java.util.ArrayList;

public class Car {
	int year = 0;
	String make = "", model = "";
	double engineSize = 0.0;
	double milage = 0;
	int id = 0;
	ArrayList<FillUp> fillUps = new ArrayList<FillUp>();
	String name;
	public Car(int i, String nick, int y, String mak, String mod, double mile){
		id = i;
		year = y;
		make = mak;
		model = mod;
		milage = mile;
		name = nick;
	}
	public Car(int i, String nick, int y, String mak, String mod, double mile, ArrayList<FillUp> x){
		year = y;
		make = mak;
		model = mod;
		milage = mile;
		fillUps = x;
		id = i;
		name = nick;
	}
	public String getName(){
		return name;
	}
	public void setMilage(double x){
		milage = x;
	}
	public void setYear(int x){
		year = x;
	}
	public void setMake(String x){
		make = x;
	}
	public void setModel(String x){
		model = x;
	}
	public void setEngineSize(double x){
		engineSize = x;
	}
	public void setFillUps(ArrayList<FillUp> x){
		fillUps = x;
	}
	public void addFillUp(FillUp x){
		fillUps.add(x);
	}
//get methods
	
	public int getYear(){
		return year;
	}
	public int getID(){
		return id;
	}
	public String getMake(){
		return make;
	}
	public String getModel(){
		return model;
	}
	public double getEngineSize(){
		return engineSize;
	}
	public ArrayList<FillUp> getFillUps(){
		return fillUps;
	}
	public double getMilage(){
		return milage;
	}
	@Override
	public String toString(){
		return " name: "+name+ "Year: " +year +" make: "+ make + " model: "+model+" mileage: "+milage;
	}
	public String toOutputFileString(){
		return ""+name+" , " +year +" , "+ make +" , "+model+" , "+milage;
	}
	
}

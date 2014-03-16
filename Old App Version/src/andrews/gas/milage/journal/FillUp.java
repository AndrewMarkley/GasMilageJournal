package andrews.gas.milage.journal;

import java.util.Calendar;


public class FillUp {
	double distance,gas,price, totalCost, mpg;
	int id = 0;
	String comments = "empty";
	String date = "";
	Calendar cal = null;
	int car = 0;
	public FillUp(int carname, double d, double g, double p, String da, String c)
	{
		distance = d;
		gas = g;
		price = round(p, 2);
		comments = c;
		mpg = round(distance/gas, 3);
		totalCost = gas*price;
		date = da;
		cal = toCal(da);
		car = carname;
	}
	public FillUp(int carname, double d, double g, double p, double tc, double mp,  String da, String c)
	{
		distance = d;
		gas = g;
		price = round(p, 2);
		comments = c;
		mpg = round(mp, 3);
		totalCost = tc;
		date = da;
		cal = toCal(da);
		car = carname;
	}
	public FillUp(){
		
	}
	//get methods
	public int getCarID()
	{
		return car;
	}
	public String getDate()
	{
		return date;
	}
	public Calendar getCalendar()
	{
		return cal;
	}
	public double getDistance()
	{
		return distance;
	}
	public double getGas()
	{
		return gas;
	}
	public double getPrice()
	{
		return price;
	}
	public double getTotalCost()
	{
		return totalCost;
	}
	public String getComments()
	{
		return comments;
	}
	public double getMPG()
	{
		return mpg;
	}
	//set methods
	public void setDistance(double x)
	{
		distance = x;
	}
	public void setGas(double x)
	{
		gas = x;
	}
	public void setPrice(double x)
	{
		price = x;
	}
	public void setTotalCost(double x)
	{
		totalCost = x;
	}
	public void setComments(String x)
	{
		comments = x;
	}
	public void setID(int x)
	{
		car = x;
	}
	public void setMPG(double x)
	{
		mpg = x;
	}
	//truncate to x places
	public double round(double value, int places) {
	    if (places < 0)
	    	return value;
	    long factor = (long) Math.pow(10, places);
	    value = value * factor;
	    long tmp = Math.round(value);
	    return (double) tmp / factor;
	}
	//to string!
	@Override
	public String toString()
	{
		return "Car: "+car+", Distance: "+distance+", Gas: "+gas +", Price per Gallon: $"+price+", Total Cost of Fill Up: $"+totalCost+", MPG: "+mpg+", Date: "+date+", Comments: "+comments;
	}
	public String toFileOutputString()
	{
		return ""+car +" , "+distance+" , "+gas +" , "+price+" , "+totalCost+" , "+mpg+" , "+date + " , "+comments ;		
	}
	/*public void parse(String s)
	{
		String[] temp = s.split(" , ");
		distance = Double.parseDouble(temp[0]);
		gas = Double.parseDouble(temp[1]);
		price = Double.parseDouble(temp[2]);
		totalCost = Double.parseDouble(temp[3]);
		mpg = Double.parseDouble(temp[4]);
		comments = temp[5];
		date = temp[6];
		cal = toCal(temp[6]);
	}*/
	public Calendar toCal(String x)
	{
		String datee[] = new String[4];
		datee[0] = x.substring(0, x.indexOf("-"));
		datee[1] = x.substring(x.indexOf("-"), x.lastIndexOf("-"));
		datee[2] = x.substring(x.lastIndexOf("-")+1);
		Calendar temp = Calendar.getInstance();
		temp.set(Integer.parseInt(datee[2]), Integer.parseInt(datee[0]), Integer.parseInt(datee[1]));
		return temp;
	}
	
}

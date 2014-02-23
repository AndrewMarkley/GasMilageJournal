package andrews.gas.milage.journal;

import java.util.Calendar;

public class FillUp
{
	double distance;
	double gas;
	double price;
	double totalCost;
	double mpg;
	int id = 0;
	String comments = "empty";
	String date = "";
	Calendar cal = null;
	String car = "";

	public FillUp(String carname, double distance, double gas, double price, String date, String comments) {
		this.distance = distance;
		this.gas = gas;
		this.price = round(price, 2);
		this.comments = comments;
		this.mpg = round(distance / gas, 3);
		this.totalCost = gas * price;
		this.date = date;
		this.cal = toCal(date);
		this.car = carname;
	}

	public FillUp(String carname, double distance, double gas, double price, double totalCost, double mpg, String date, String comments) {
		this.distance = distance;
		this.gas = gas;
		this.price = round(price, 2);
		this.comments = comments;
		this.mpg = round(mpg, 3);
		this.totalCost = totalCost;
		this.date = date;
		this.cal = toCal(date);
		this.car = carname;
	}

	public FillUp() {

	}

	public String getCarName()
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

	public void setName(String x)
	{
		car = x;
	}

	public void setMPG(double x)
	{
		mpg = x;
	}

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
		return "Car: " + car + ", Distance: " + distance + ", Gas: " + gas + ", Price per Gallon: $" + price + ", Total Cost of Fill Up: $" + totalCost + ", MPG: " + mpg + ", Date: " + date
				+ ", Comments: " + comments;
	}

	public String toFileOutputString()
	{
		return "" + car + " , " + distance + " , " + gas + " , " + price + " , " + totalCost + " , " + mpg + " , " + date + " , " + comments;
	}

	public Calendar toCal(String x)
	{
		String datee[] = new String[4];
		datee[0] = x.substring(0, x.indexOf("-"));
		datee[1] = x.substring(x.indexOf("-"), x.lastIndexOf("-"));
		datee[2] = x.substring(x.lastIndexOf("-") + 1);
		Calendar temp = Calendar.getInstance();
		temp.set(Integer.parseInt(datee[2]), Integer.parseInt(datee[0]), Integer.parseInt(datee[1]));
		return temp;
	}

}

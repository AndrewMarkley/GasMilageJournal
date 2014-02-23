package andrews.gas.milage.journal;

public class ServiceLog
{
	int id = 0;
	String date = "";
	String service = "";
	double price = 0;
	double odometer = 0;
	String shop = "";
	String comments = "";

	public ServiceLog() {

	}

	public ServiceLog(int Id, String Date, double od, String Service,
			double Price, String Shop, String Comms) {
		id = Id;
		date = Date;
		service = Service;
		price = Price;
		shop = Shop;
		comments = Comms;
		odometer = od;
	}

	public double getMiles()
	{
		return odometer;
	}

	public int getId()
	{
		return id;
	}

	public String getDate()
	{
		return date;
	}

	public String getService()
	{
		return service;
	}

	public double getPrice()
	{
		return price;
	}

	public String getRepairShop()
	{
		return shop;
	}

	public String getComments()
	{
		return comments;
	}
}

package com.orangegames.gasmilagejournal.statistics;

public class Statistics
{
	private int carId;
	private double avgMPG = 0;
	private double avgFuelCosts = 0;
	private double avgMilesPerDollar = 0;
	private double avgTimeBetweenstatisticss = 0;
	private double avgMilesBetweenstatisticss = 0;
	private double avgDollarPerDay = 0;
	
	public Statistics(int carId, double avgMPG, double avgFuelCosts, double avgMilesPerDollar, double avgTimeBetweenstatisticss, double avgMilesBetweenstatisticss, double avgDollarPerDay)
	{
		this.carId = carId;
		this.avgMPG = avgMPG;
		this.avgFuelCosts = avgFuelCosts;
		this.avgMilesPerDollar = avgMilesPerDollar;
		this.avgTimeBetweenstatisticss = avgTimeBetweenstatisticss;
		this.avgMilesBetweenstatisticss = avgMilesBetweenstatisticss;
		this.avgDollarPerDay = avgDollarPerDay;
	}
	
	public int getCarId() { return carId; } 
	public void setCarId(int value) { this.carId = value; }
	
	public double getAvgMPG() { return avgMPG; } 
	public void setAvgMPG(double value) { this.avgMPG = value; }
	
	public double getAvgFuelCosts() { return avgFuelCosts; }
	public void setAvgFuelCosts(double value) { this.avgFuelCosts = value; }
	
	public double getAvgMilesPerDollar() { return avgMilesPerDollar; }
	public void setAvgMilesPerDollar(double value) { this.avgMilesPerDollar = value; }
	
	public double getAvgTimeBetweenstatisticss() { return avgTimeBetweenstatisticss; }
	public void setAvgTimeBetweenstatisticss(double value) { this.avgTimeBetweenstatisticss = value; }
	
	public double getAvgMilesBetweenstatisticss() { return avgMilesBetweenstatisticss; }
	public void setAvgMilesBetweenstatisticss(double value) { this.avgMilesBetweenstatisticss = value; }
	
	public double getAvgDollarPerDay() { return avgDollarPerDay; }
	public void setAvgDollarPerDay(double value) { this.avgDollarPerDay = value; }
	
}

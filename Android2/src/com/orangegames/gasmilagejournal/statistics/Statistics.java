package com.orangegames.gasmilagejournal.statistics;

public class Statistics
{
	private int carId;
	private double avgMPG = 0;
	private double avgFuelCosts = 0;
	private double avgMilesPerDollar = 0;
	private double avgTimeBetweenFillUps = 0;
	private double avgMilesBetweenFillUps = 0;
	private double avgDollarPerDay = 0;
	
	public Statistics(int carId, double avgMPG, double avgFuelCosts, double avgMilesPerDollar, double avgTimeBetweenFillUps, double avgMilesBetweenFillUps, double avgDollarPerDay)
	{
		this.carId = carId;
		this.avgMPG = avgMPG;
		this.avgFuelCosts = avgFuelCosts;
		this.avgMilesPerDollar = avgMilesPerDollar;
		this.avgTimeBetweenFillUps = avgTimeBetweenFillUps;
		this.avgMilesBetweenFillUps = avgMilesBetweenFillUps;
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
	
	public double getAvgTimeBetweenFillUps() { return avgTimeBetweenFillUps; }
	public void setAvgTimeBetweenFillUps(double value) { this.avgTimeBetweenFillUps = value; }
	
	public double getAvgMilesBetweenFillUps() { return avgMilesBetweenFillUps; }
	public void setAvgMilesBetweenFillUps(double value) { this.avgMilesBetweenFillUps = value; }
	
	public double getAvgDollarPerDay() { return avgDollarPerDay; }
	public void setAvgDollarPerDay(double value) { this.avgDollarPerDay = value; }
	
}

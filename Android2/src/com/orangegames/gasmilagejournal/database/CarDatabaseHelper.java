package com.orangegames.gasmilagejournal.database;

import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicInteger;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.orangegames.gasmilagejournal.car.Car;

public class CarDatabaseHelper extends OrmLiteSqliteOpenHelper
{

	private static final String DATABASE_NAME = "CarDatabaseHelper.db";

	private static final int DATABASE_VERSION = 21;

	private Dao<Car, Integer> complexDao = null;

	private static CarDatabaseHelper helper = null;
	private static final AtomicInteger usageCounter = new AtomicInteger(0);

	private CarDatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public synchronized static CarDatabaseHelper getHelper(Context context)
	{
		if ( helper == null ) {
			helper = new CarDatabaseHelper(context);
		}
		usageCounter.incrementAndGet();
		return helper;
	}

	@Override
	public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource)
	{
		try {
			Log.i(CarDatabaseHelper.class.getName(), "onCreate");
			TableUtils.createTable(connectionSource, Car.class);

			Dao<Car, Integer> dao = getCarDao();
			long millis = System.currentTimeMillis();
			// create some entries in the onCreate
			
			Car car = new Car("name", 1999, "make", "model", 75000);
			dao.create(car);
			car = new Car("name", 2000, "make1", "model1", 75001);
			dao.update(car);
			dao.delete(car);
			Log.i(CarDatabaseHelper.class.getName(), "created new Car entries in onCreate: " + millis);
		} catch (SQLException e) {
			Log.e(CarDatabaseHelper.class.getName(), "Can't create database", e);
			throw new RuntimeException(e);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion)
	{
		try {
			Log.i(CarDatabaseHelper.class.getName(), "onUpgrade");
			TableUtils.dropTable(connectionSource, Car.class, true);
			// after we drop the old databases, we create the new ones
			onCreate(db, connectionSource);
		} catch (SQLException e) {
			Log.e(CarDatabaseHelper.class.getName(), "Can't drop databases", e);
			throw new RuntimeException(e);
		}
	}

	public Dao<Car, Integer> getCarDao() throws SQLException
	{
		if ( complexDao == null ) {
			complexDao = getDao(Car.class);
		}
		return complexDao;
	}

	@Override
	public void close()
	{
		if ( usageCounter.decrementAndGet() == 0 ) {
			super.close();
			complexDao = null;
			helper = null;
		}
	}
}
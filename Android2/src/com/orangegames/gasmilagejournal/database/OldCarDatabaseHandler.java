package com.orangegames.gasmilagejournal.database;

import java.util.ArrayList;
import java.util.List;

import com.orangegames.gasmilagejournal.entities.Car;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class OldCarDatabaseHandler extends SQLiteOpenHelper
{

	// Database Version
	private static final int DATABASE_VERSION = 3;

	// Database Name
	private static final String DATABASE_NAME = "carProfiles";

	// Contacts table name
	private static final String TABLE_FILL_UPS = "cars";

	// Contacts Table Columns names
	private static final String KEY_YEAR = "year";
	private static final String KEY_MAKE = "make";
	private static final String KEY_MODEL = "model";
	private static final String KEY_NAME = "name";
	private static final String KEY_MILES = "miles";

	public OldCarDatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{

	}

	public List<Car> getAllCars()
	{
		List<Car> cars = new ArrayList<Car>();

		String selectQuery = "SELECT  * FROM " + TABLE_FILL_UPS;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		if ( cursor.moveToFirst() ) {
			do {
				Car car = new Car(cursor.getString(cursor.getColumnIndex(KEY_NAME)), 
								  cursor.getInt(cursor.getColumnIndex(KEY_YEAR)), 
								  cursor.getString(cursor.getColumnIndex(KEY_MAKE)), 
								  cursor.getString(cursor.getColumnIndex(KEY_MODEL)), 
								  cursor.getDouble(cursor.getColumnIndex(KEY_MILES)));
				cars.add(car);
			} while (cursor.moveToNext());
		}

		return cars;
	}
	
	public void dropTable()
	{
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_FILL_UPS);
	}
	
	public boolean tableExists()
	{
		boolean result = true;
		try {
			SQLiteDatabase db = this.getWritableDatabase();
			db.execSQL("SELECT * FROM " + TABLE_FILL_UPS);
		} catch (Exception e) {
			result = false;
		}

		return result;
	}
}
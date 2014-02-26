package andrews.gas.milage.journal;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CarDatabaseHandler extends SQLiteOpenHelper
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

	public CarDatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{
		String CREATE_FILL_UP_TABLE = "CREATE TABLE " + TABLE_FILL_UPS + "(" + KEY_NAME + " TEXT," + KEY_YEAR + " INTEGER," + KEY_MAKE + " TEXT," + KEY_MODEL + " TEXT," + KEY_MILES + " Double" + ")";
		db.execSQL(CREATE_FILL_UP_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_FILL_UPS);

		onCreate(db);
	}

	/**
	 * All CRUD(Create, Read, Update, Delete) Operations
	 */

	void addCar(Car car)
	{
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_NAME, car.getName());
		values.put(KEY_YEAR, car.getYear());
		values.put(KEY_MAKE, car.getMake());
		values.put(KEY_MODEL, car.getModel());
		values.put(KEY_MILES, car.getMilage());

		db.insert(TABLE_FILL_UPS, null, values);
		db.close();
	}

	Car getCar(int id)
	{
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_FILL_UPS, new String[] { KEY_NAME, KEY_YEAR, KEY_MAKE, KEY_MODEL, KEY_MILES }, KEY_NAME + "=?", new String[] { String.valueOf(id) }, null, null, null, null);
		if ( cursor != null )
			cursor.moveToFirst();

		Car car = new Car(cursor.getString(0), cursor.getInt(1), cursor.getString(2), cursor.getString(3), cursor.getDouble(4));

		return car;
	}

	public List<Car> getAllCars()
	{
		List<Car> cars = new ArrayList<Car>();

		String selectQuery = "SELECT  * FROM " + TABLE_FILL_UPS;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		if ( cursor.moveToFirst() ) {
			do {
				Car car = new Car(cursor.getString(0), cursor.getInt(1), cursor.getString(2), cursor.getString(3), cursor.getDouble(4));
				cars.add(car);
			} while (cursor.moveToNext());
		}

		return cars;
	}

	public int updateCar(Car car)
	{
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_NAME, car.getName());
		values.put(KEY_YEAR, car.getYear());
		values.put(KEY_MAKE, car.getMake());
		values.put(KEY_MODEL, car.getModel());
		values.put(KEY_MILES, car.getMilage());

		return db.update(TABLE_FILL_UPS, values, KEY_NAME + " = ?", new String[] { String.valueOf(car.getName()) });
	}

	public void deleteCar(Car car, int row)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_FILL_UPS, KEY_NAME + " = ?", new String[] { String.valueOf(car.name) });
		db.close();
	}

	public int getCarCount()
	{
		String countQuery = "SELECT  * FROM " + TABLE_FILL_UPS;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		cursor.close();

		return cursor.getCount();
	}
}
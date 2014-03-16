package andrews.gas.milage.journal.database;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import andrews.gas.milage.journal.MainActivity;
import andrews.gas.milage.journal.entities.Car;
import andrews.gas.milage.journal.entities.FillUp;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

public class OldFillUpDatabaseHandler extends SQLiteOpenHelper
{

	private static final int DATABASE_VERSION = 12;

	private static final String DATABASE_NAME = "FillUpLog";

	private static final String TABLE_FILL_UPS = "fillUps";

	private static final String KEY_CAR_ID = "carID";
	private static final String KEY_DISTANCE = "distance";
	private static final String KEY_GAS = "gas";
	private static final String KEY_PRICE = "price";
	private static final String KEY_TOTAL_COST = "totalCost";
	private static final String KEY_MPG = "mpg";
	private static final String KEY_COMMENTS = "comments";
	private static final String KEY_DATE = "date";

	public OldFillUpDatabaseHandler(Context context) {
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

	public List<FillUp> getAllFillUps(List<OldCar> oldCars, List<Car> newCars)
	{
		List<FillUp> FillUpList = new ArrayList<FillUp>();

		String selectQuery = "SELECT  * FROM " + TABLE_FILL_UPS;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		if ( cursor.moveToFirst() ) {
			do {
				int carId = 0;
				int oldCarId = cursor.getInt(0);
				double distance = cursor.getDouble(cursor.getColumnIndex(KEY_DISTANCE));
				double gas = cursor.getDouble(cursor.getColumnIndex(KEY_GAS));
				double price = cursor.getDouble(cursor.getColumnIndex(KEY_PRICE));
				String comments = cursor.getString(cursor.getColumnIndex(KEY_COMMENTS));
				String d = cursor.getString(cursor.getColumnIndex(KEY_DATE));
				String carName = "";

				for ( OldCar car : oldCars ) {
					if ( car.getID() == oldCarId ) {
						carName = car.getName();
					}
				}

				for ( Car car : newCars ) {
					if ( car.getName().equals(carName) ) {
						carId = car.getId();
					}
				}

				if ( carId == 0 ) {
					throw new IllegalArgumentException("There was a car name without an id!");
				}

				Date date = toCal(d).getTime();

				FillUp fu = new FillUp(carId, distance, gas, price, date, comments, null);

				FillUpList.add(fu);
			} while (cursor.moveToNext());
		}

		return FillUpList;
	}

	public void dropTable()
	{
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_FILL_UPS);
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
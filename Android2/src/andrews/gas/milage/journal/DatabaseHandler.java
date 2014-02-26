package andrews.gas.milage.journal;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper
{

	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 11;

	private static final String DATABASE_NAME = "FillUpLog";

	private static final String TABLE_FILL_UPS = "fillUps";

	private static final String KEY_CAR = "carName";
	private static final String KEY_DISTANCE = "distance";
	private static final String KEY_GAS = "gas";
	private static final String KEY_PRICE = "price";
	private static final String KEY_TOTAL_COST = "totalCost";
	private static final String KEY_MPG = "mpg";
	private static final String KEY_COMMENTS = "comments";
	private static final String KEY_DATE = "date";

	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{
		String CREATE_FILL_UP_TABLE = "CREATE TABLE " + TABLE_FILL_UPS + "(" + KEY_CAR + " TEXT," + KEY_DISTANCE + " DOUBLE," + KEY_GAS + " DOUBLE," + KEY_PRICE + " DOUBLE," + KEY_TOTAL_COST
				+ " DOUBLE," + KEY_MPG + " DOUBLE," + KEY_DATE + " TEXT," + KEY_COMMENTS + " TEXT" + ")";

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

	void addFillUp(FillUp fillUp)
	{
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_CAR, fillUp.getCarName()); // Distance
		values.put(KEY_DISTANCE, fillUp.getDistance()); // Distance
		values.put(KEY_GAS, fillUp.getGas()); // Gas
		values.put(KEY_PRICE, fillUp.getPrice()); // Price
		values.put(KEY_TOTAL_COST, fillUp.getTotalCost()); // Total Cost
		values.put(KEY_MPG, fillUp.getMPG()); // MPG
		values.put(KEY_DATE, fillUp.getDate()); // Date
		values.put(KEY_COMMENTS, fillUp.getComments()); // Comments

		db.insert(TABLE_FILL_UPS, null, values);
		db.close();
	}

	FillUp getFillup(int id)
	{
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_FILL_UPS, new String[] { KEY_CAR, KEY_DISTANCE, KEY_GAS, KEY_PRICE, KEY_TOTAL_COST, KEY_MPG, KEY_DATE, KEY_COMMENTS }, KEY_CAR + "=?",
				new String[] { String.valueOf(id) }, null, null, null, null);
		if ( cursor != null )
			cursor.moveToFirst();

		FillUp contact = new FillUp(cursor.getString(0), cursor.getDouble(1), cursor.getDouble(2), cursor.getDouble(3), cursor.getDouble(4), cursor.getDouble(5), cursor.getString(6),
				cursor.getString(7));

		return contact;
	}

	public List<FillUp> getAllFillUps()
	{
		List<FillUp> FillUpList = new ArrayList<FillUp>();

		String selectQuery = "SELECT  * FROM " + TABLE_FILL_UPS;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		if ( cursor.moveToFirst() ) {
			do {
				FillUp fu = new FillUp(cursor.getString(0), cursor.getDouble(1), cursor.getDouble(2), cursor.getDouble(3), cursor.getDouble(4), cursor.getDouble(5), cursor.getString(6),
						cursor.getString(7));
				FillUpList.add(fu);
			} while (cursor.moveToNext());
		}

		return FillUpList;
	}

	public int updateFillUp(FillUp fillUp)
	{
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_CAR, fillUp.getCarName());
		values.put(KEY_DISTANCE, fillUp.getDistance());
		values.put(KEY_GAS, fillUp.getGas());
		values.put(KEY_PRICE, fillUp.getPrice());
		values.put(KEY_TOTAL_COST, fillUp.getTotalCost());
		values.put(KEY_MPG, fillUp.getMPG());
		values.put(KEY_DATE, fillUp.getDate());
		values.put(KEY_COMMENTS, fillUp.getComments());

		return db.update(TABLE_FILL_UPS, values, KEY_CAR + " = ?", new String[] { String.valueOf(fillUp.getCarName()) });
	}

	public void deleteFillUp(FillUp fu, int row)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_FILL_UPS, KEY_DISTANCE + " = ?", new String[] { String.valueOf(fu.distance) });
		db.close();
	}

	public int getFillUpsCount()
	{
		String countQuery = "SELECT  * FROM " + TABLE_FILL_UPS;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		cursor.close();

		return cursor.getCount();
	}

}
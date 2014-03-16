package andrews.gas.milage.journal;
 
import java.util.ArrayList;
import java.util.List;
 
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
 
public class DatabaseHandler extends SQLiteOpenHelper {
 
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 12;
 
    // Database Name
    private static final String DATABASE_NAME = "FillUpLog";
 
    // Contacts table name
    private static final String TABLE_FILL_UPS = "fillUps";
 
    // Contacts Table Columns names
    private static final String KEY_CAR_ID = "carID";
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
 
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_FILL_UP_TABLE = "CREATE TABLE " + TABLE_FILL_UPS + "("
        + KEY_CAR_ID + " TEXT," + KEY_DISTANCE + " DOUBLE," + KEY_GAS + " DOUBLE," 
        		+ KEY_PRICE + " DOUBLE,"    + KEY_TOTAL_COST + " DOUBLE,"
                + KEY_MPG + " DOUBLE,"      + KEY_DATE + " TEXT,"   
                + KEY_COMMENTS + " TEXT"   + ")";
        //System.out.println(db.toString());
        db.execSQL(CREATE_FILL_UP_TABLE);
    }
 
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FILL_UPS);
        // Create tables again
        onCreate(db);
    }
 
    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */
 
    // Adding new fill up
    void addFillUp(FillUp fillUp) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(KEY_CAR_ID, fillUp.getCarID()); // Distance
        values.put(KEY_DISTANCE, fillUp.getDistance()); // Distance
        values.put(KEY_GAS, fillUp.getGas()); // Gas
        values.put(KEY_PRICE, fillUp.getPrice()); // Price
        values.put(KEY_TOTAL_COST, fillUp.getTotalCost()); // Total Cost
        values.put(KEY_MPG, fillUp.getMPG()); // MPG
        values.put(KEY_DATE, fillUp.getDate()); // Date
        values.put(KEY_COMMENTS, fillUp.getComments()); // Comments
        
        
 
        // Inserting Row
        db.insert(TABLE_FILL_UPS, null, values);
        db.close(); // Closing database connection
    }
 
    // Getting single fillUp
    FillUp getFillup(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
 
        Cursor cursor = db.query(TABLE_FILL_UPS, new String[] { KEY_CAR_ID, KEY_DISTANCE, KEY_GAS, KEY_PRICE, KEY_TOTAL_COST, KEY_MPG, KEY_DATE, KEY_COMMENTS }, KEY_CAR_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
 
        FillUp contact = new FillUp(cursor.getInt(0), cursor.getDouble(1), cursor.getDouble(2), cursor.getDouble(3), cursor.getDouble(4), cursor.getDouble(5), cursor.getString(6), cursor.getString(7));
        // return contact
        return contact;
    }
 
    // Getting All Contacts
    public List<FillUp> getAllFillUps() {
        List<FillUp> FillUpList = new ArrayList<FillUp>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_FILL_UPS;
 
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
 
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                FillUp fu = new FillUp(cursor.getInt(0), cursor.getDouble(1), cursor.getDouble(2), cursor.getDouble(3), cursor.getDouble(4), cursor.getDouble(5), cursor.getString(6), cursor.getString(7));
                // Adding contact to list
                FillUpList.add(fu);
            } while (cursor.moveToNext());
        }
 
        // return contact list
        return FillUpList;
    }
 
    // Updating single contact
    public int updateFillUp(FillUp fillUp) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(KEY_CAR_ID, fillUp.getCarID()); // Distance
        values.put(KEY_DISTANCE, fillUp.getDistance()); // Distance
        values.put(KEY_GAS, fillUp.getGas()); // Gas
        values.put(KEY_PRICE, fillUp.getPrice()); // Price
        values.put(KEY_TOTAL_COST, fillUp.getTotalCost()); // Total Cost
        values.put(KEY_MPG, fillUp.getMPG()); // MPG
        values.put(KEY_DATE, fillUp.getDate()); // Date
        values.put(KEY_COMMENTS, fillUp.getComments()); // Comments
 
        // updating row
        return db.update(TABLE_FILL_UPS, values, KEY_CAR_ID + " = ?", new String[] { String.valueOf(fillUp.getCarID()) });
    }
 
    // Deleting single contact
    public void deleteFillUp(FillUp fu, int row) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FILL_UPS, KEY_DISTANCE + " = ?", new String[] { String.valueOf(fu.distance) });
        db.close();
    }
 
    // Getting contacts Count
    public int getFillUpsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_FILL_UPS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
 
        // return count
        return cursor.getCount();
    }
 
}
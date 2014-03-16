package andrews.gas.milage.journal;
 
import java.util.ArrayList;
import java.util.List;
 
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
 
public class ServiceLogDataBaseHandler extends SQLiteOpenHelper {
 
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;
 
    // Database Name
    private static final String DATABASE_NAME = "ServiceLogs";
 
    // Contacts table name
    private static final String TABLE_FILL_UPS = "services";
 
    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_SERVICE = "service";
    private static final String KEY_ODOMETER = "odometer";
    private static final String KEY_PRICE = "price";
    private static final String KEY_DATE = "date";
    private static final String KEY_REPAIR_SHOP = "repairShop";
    private static final String KEY_COMMENTS = "comments";

 
    public ServiceLogDataBaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
 
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_FILL_UP_TABLE = "CREATE TABLE " + TABLE_FILL_UPS + "("
        		+ KEY_ID + " INTEGER,"       + KEY_DATE + " TEXT,"       + KEY_SERVICE + " TEXT," +KEY_ODOMETER+" DOUBLE,"
        		+ KEY_PRICE + " DOUBLE," 	+ KEY_REPAIR_SHOP + " TEXT,"   
                + KEY_COMMENTS + " TEXT"   + ")";
        System.out.println(db.toString());
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
    void addFillUp(ServiceLog Slog) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(KEY_ID, Slog.getId()); // Date
        values.put(KEY_DATE, Slog.getDate()); // Date
        values.put(KEY_DATE, Slog.getMiles()); // odometer
        values.put(KEY_SERVICE, Slog.getService()); // Type of service
        values.put(KEY_PRICE, Slog.getPrice()); // Price
        values.put(KEY_REPAIR_SHOP, Slog.getRepairShop()); // repair shop
        values.put(KEY_COMMENTS, Slog.getComments()); // comments
        
        
 
        // Inserting Row
        db.insert(TABLE_FILL_UPS, null, values);
        db.close(); // Closing database connection
    }
 
    // Getting single fillUp
    ServiceLog getServiceLog(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
 
        Cursor cursor = db.query(TABLE_FILL_UPS, new String[] { KEY_ID, KEY_SERVICE, KEY_ODOMETER, KEY_DATE, KEY_PRICE, KEY_REPAIR_SHOP, KEY_COMMENTS }, KEY_DATE + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        ServiceLog Slog= new ServiceLog(cursor.getInt(0), cursor.getString(1), cursor.getDouble(2), cursor.getString(3), cursor.getDouble(4), cursor.getString(5), cursor.getString(6));
        // return contact
        return Slog;
    }
 
    // Getting All Contacts
    public List<ServiceLog> getAllServiceLogs() {
        List<ServiceLog> servs = new ArrayList<ServiceLog>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_FILL_UPS;
 
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
 
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
            	ServiceLog Slog= new ServiceLog(cursor.getInt(0), cursor.getString(1), cursor.getDouble(2), cursor.getString(3), cursor.getDouble(4), cursor.getString(5), cursor.getString(6));
                // Adding contact to list
                servs.add(Slog);
            } while (cursor.moveToNext());
        }
 
        // return contact list
        return servs;
    }
 
    // Updating single contact
    public int updateServiceLog(ServiceLog Slog) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(KEY_ID, Slog.getId()); // Date
        values.put(KEY_DATE, Slog.getDate()); // Date
        values.put(KEY_DATE, Slog.getMiles()); // odometer
        values.put(KEY_SERVICE, Slog.getService()); // Type of service
        values.put(KEY_PRICE, Slog.getPrice()); // Price
        values.put(KEY_REPAIR_SHOP, Slog.getRepairShop()); // repair shop
        values.put(KEY_COMMENTS, Slog.getComments()); // comments
 
        // updating row
        return db.update(TABLE_FILL_UPS, values, KEY_DATE + " = ?",
                new String[] { String.valueOf(Slog.getDate()) });
    }
 
    // Deleting single contact
    public void deleteServiceLog(ServiceLog sl, int row) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FILL_UPS, KEY_SERVICE + " = ?", new String[] { String.valueOf(sl.id) });
        db.close();
    }
 
    // Getting contacts Count
    public int getServiceLogsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_FILL_UPS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
 
        // return count
        return cursor.getCount();
    }
    //returns the next id due to be assigned in the list
    public int getServiceLogNewId(){
    	ArrayList<ServiceLog> x = (ArrayList<ServiceLog>) getAllServiceLogs();
    	return x.get(x.size()-1).getId()+1;
    }
 
}
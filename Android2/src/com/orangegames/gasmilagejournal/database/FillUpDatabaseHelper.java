package com.orangegames.gasmilagejournal.database;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.orangegames.gasmilagejournal.fillup.FillUp;

/**
 * Database helper class used to manage the creation and upgrading of your
 * database. This class also usually provides the DAOs used by the other
 * classes.
 */
public class FillUpDatabaseHelper extends OrmLiteSqliteOpenHelper
{

	// name of the database file for your application -- change to something
	// appropriate for your app
	private static final String DATABASE_NAME = "FillUpDatabaseHelper.db";
	// any time you make changes to your database objects, you may have to
	// increase the database version
	private static final int DATABASE_VERSION = 15;

	// the DAO object we use to access the FillUp table
	private Dao<FillUp, Integer> complexDao = null;

	// we do this so there is only one helper
	private static FillUpDatabaseHelper helper = null;
	private static final AtomicInteger usageCounter = new AtomicInteger(0);

	private FillUpDatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	/**
	 * Get the helper, possibly constructing it if necessary. For each call to
	 * this method, there should be 1 and only 1 call to {@link #close()}.
	 */
	public synchronized static FillUpDatabaseHelper getHelper(Context context)
	{
		if ( helper == null ) {
			helper = new FillUpDatabaseHelper(context);
		}
		usageCounter.incrementAndGet();
		return helper;
	}

	/**
	 * This is called when the database is first created. Usually you should
	 * call createTable statements here to create the tables that will store
	 * your data.
	 */
	@Override
	public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource)
	{
		try {
			Log.i(FillUpDatabaseHelper.class.getName(), "onCreate");
			TableUtils.createTable(connectionSource, FillUp.class);

			// here we try inserting data in the on-create as a test
			Dao<FillUp, Integer> dao = getFillUpDao();
			long millis = System.currentTimeMillis();
			// create some entries in the onCreate

			FillUp fillUp = new FillUp(1, 300, 20.5, 2.99, Calendar.getInstance().getTime(), "comments");
			dao.create(fillUp);
			fillUp = new FillUp(1, 301, 20.6, 2.98, Calendar.getInstance().getTime(), "comments1");
			dao.update(fillUp);
			Log.i(FillUpDatabaseHelper.class.getName(), "created new FillUp entries in onCreate: " + millis);
		} catch (SQLException e) {
			Log.e(FillUpDatabaseHelper.class.getName(), "Can't create database", e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * This is called when your application is upgraded and it has a higher
	 * version number. This allows you to adjust the various data to match the
	 * new version number.
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion)
	{
		try {
			Log.i(FillUpDatabaseHelper.class.getName(), "onUpgrade");
			TableUtils.dropTable(connectionSource, FillUp.class, true);
			// after we drop the old databases, we create the new ones
			onCreate(db, connectionSource);
		} catch (SQLException e) {
			Log.e(FillUpDatabaseHelper.class.getName(), "Can't drop databases", e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * Returns the Database Access Object (DAO) for our FillUp class. It will
	 * create it or just give the cached value.
	 */
	public Dao<FillUp, Integer> getFillUpDao() throws SQLException
	{
		if ( complexDao == null ) {
			complexDao = getDao(FillUp.class);
		}
		return complexDao;
	}

	/**
	 * Close the database connections and clear any cached DAOs. For each call
	 * to {@link #getHelper(Context)}, there should be 1 and only 1 call to this
	 * method. If there were 3 calls to {@link #getHelper(Context)} then on the
	 * 3rd call to this method, the helper and the underlying database
	 * connections will be closed.
	 */
	@Override
	public void close()
	{
		if ( usageCounter.decrementAndGet() == 0 ) {
			super.close();
			complexDao = null;
			helper = null;
		}
	}
	
	public int getNextId() {
		int id = 0;
		try {
			Dao<FillUp, Integer> fillUpDAO = getFillUpDao();
			FillUp f = fillUpDAO.query(fillUpDAO.queryBuilder().orderBy("id", false).limit(1L).prepare()).get(0);
			id = f.getId() + 1;
		} catch (Exception e) {
			id = 1;
		}
		return id;
	}
}
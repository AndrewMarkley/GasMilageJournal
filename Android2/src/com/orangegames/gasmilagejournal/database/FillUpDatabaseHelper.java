package com.orangegames.gasmilagejournal.database;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.concurrent.atomic.AtomicInteger;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.orangegames.gasmilagejournal.entities.FillUp;

public class FillUpDatabaseHelper extends OrmLiteSqliteOpenHelper
{

	private static final String DATABASE_NAME = "FillUpDatabaseHelper.db";

	private static final int DATABASE_VERSION = 21;

	private Dao<FillUp, Integer> complexDao = null;

	private static FillUpDatabaseHelper helper = null;
	private static final AtomicInteger usageCounter = new AtomicInteger(0);

	private FillUpDatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public synchronized static FillUpDatabaseHelper getHelper(Context context)
	{
		if ( helper == null ) {
			helper = new FillUpDatabaseHelper(context);
		}
		usageCounter.incrementAndGet();
		return helper;
	}

	@Override
	public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource)
	{
		try {
			Log.i(FillUpDatabaseHelper.class.getName(), "onCreate");
			TableUtils.createTable(connectionSource, FillUp.class);

			Dao<FillUp, Integer> dao = getFillUpDao();
			long millis = System.currentTimeMillis();

			FillUp fillUp = new FillUp(1, 300, 20.5, 2.99, Calendar.getInstance().getTime(), "comments", null);
			dao.create(fillUp);
			fillUp = new FillUp(1, 301, 20.6, 2.98, Calendar.getInstance().getTime(), "comments1", null);
			dao.update(fillUp);
			dao.delete(fillUp);
			Log.i(FillUpDatabaseHelper.class.getName(), "tested CRUD on a new FillUp entry in onCreate: " + millis);
		} catch (SQLException e) {
			Log.e(FillUpDatabaseHelper.class.getName(), "Can't create database", e);
			throw new RuntimeException(e);
		}
	}

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

	public Dao<FillUp, Integer> getFillUpDao() throws SQLException
	{
		if ( complexDao == null ) {
			complexDao = getDao(FillUp.class);
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
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
import com.orangegames.gasmilagejournal.entities.MaintenanceLog;

public class MaintenanceLogDatabaseHelper extends OrmLiteSqliteOpenHelper
{

	private static final String DATABASE_NAME = "MaintenanceLogDatabaseHelper.db";

	private static final int DATABASE_VERSION = 21;

	private Dao<MaintenanceLog, Integer> complexDao = null;

	private static MaintenanceLogDatabaseHelper helper = null;
	private static final AtomicInteger usageCounter = new AtomicInteger(0);

	private MaintenanceLogDatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public synchronized static MaintenanceLogDatabaseHelper getHelper(Context context)
	{
		if ( helper == null ) {
			helper = new MaintenanceLogDatabaseHelper(context);
		}
		usageCounter.incrementAndGet();
		return helper;
	}

	@Override
	public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource)
	{
		try {
			Log.i(MaintenanceLogDatabaseHelper.class.getName(), "onCreate");
			TableUtils.createTable(connectionSource, MaintenanceLog.class);

			Dao<MaintenanceLog, Integer> dao = getMaintenanceLogDao();
			long millis = System.currentTimeMillis();

			MaintenanceLog MaintenanceLog = new MaintenanceLog(1, Calendar.getInstance().getTime(), 10, 1000, "Oil Change", "Got the oil changed!", "Oil Change center", null);
			dao.create(MaintenanceLog);
			MaintenanceLog = new MaintenanceLog(1, Calendar.getInstance().getTime(), 1000, 100000, "Oil Change2", "Got the oil changed!", "Oil Change center", null);
			dao.update(MaintenanceLog);
			dao.delete(MaintenanceLog);
			Log.i(MaintenanceLogDatabaseHelper.class.getName(), "tested CRUD on a new MaintenanceLog entry in onCreate: " + millis);
		} catch (SQLException e) {
			Log.e(MaintenanceLogDatabaseHelper.class.getName(), "Can't create database", e);
			throw new RuntimeException(e);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion)
	{
		try {
			Log.i(MaintenanceLogDatabaseHelper.class.getName(), "onUpgrade");
			TableUtils.dropTable(connectionSource, MaintenanceLog.class, true);
			// after we drop the old databases, we create the new ones
			onCreate(db, connectionSource);
		} catch (SQLException e) {
			Log.e(MaintenanceLogDatabaseHelper.class.getName(), "Can't drop databases", e);
			throw new RuntimeException(e);
		}
	}

	public Dao<MaintenanceLog, Integer> getMaintenanceLogDao() throws SQLException
	{
		if ( complexDao == null ) {
			complexDao = getDao(MaintenanceLog.class);
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
package com.orangegames.gasmilagejournal.database;

import java.io.IOException;

import android.database.SQLException;

import com.j256.ormlite.android.apptools.OrmLiteConfigUtil;

public class DatabaseConfigUtil extends OrmLiteConfigUtil {

	public static void main(String[] args) throws SQLException, IOException {
		try {
			writeConfigFile("ormlite_config.txt");
		} catch (java.sql.SQLException e) {
			e.printStackTrace();
		}
	}
}
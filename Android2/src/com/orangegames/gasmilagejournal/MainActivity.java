package com.orangegames.gasmilagejournal;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import au.com.bytecode.opencsv.CSVWriter;

import com.orangegames.gasmilagejournal.activities.AboutActivity;
import com.orangegames.gasmilagejournal.activities.SettingsActivity;
import com.orangegames.gasmilagejournal.database.CarDatabaseHelper;
import com.orangegames.gasmilagejournal.database.FillUpDatabaseHelper;
import com.orangegames.gasmilagejournal.entities.FillUp;
import com.orangegames.gasmilagejournal.fragments.CarsViewFragment;
import com.orangegames.gasmilagejournal.fragments.FillUpViewFragment;
import com.orangegames.gasmilagejournal.fragments.MaintenanceLogViewFragment;
import com.orangegames.gasmilagejournal.fragments.StatisticsViewFragment;

public class MainActivity extends FragmentActivity
{

	private SectionsPagerAdapter mSectionsPagerAdapter;
	private ViewPager mViewPager;
	private CarDatabaseHelper carDatabaseHelper = null;
	private FillUpDatabaseHelper fillUpDatabaseHelper = null;

	public static final String MEASUREMENT_KEY = "measurement";
	public static final String DATE_FORMAT_KEY = "date_format";
	public static final String CURRENCY_KEY = "currency";

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		mViewPager.setCurrentItem(1, false);

		// check shared preference keys
		Context context = getApplicationContext();
		SharedPreferences sharedPref = context.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();

		if ( ! sharedPref.contains(MEASUREMENT_KEY) ) {
			editor.putString(MEASUREMENT_KEY, "US");
		}
		if ( ! sharedPref.contains(DATE_FORMAT_KEY) ) {
			editor.putString(DATE_FORMAT_KEY, "mm/dd/yyyy");
		}
		if ( ! sharedPref.contains(CURRENCY_KEY) ) {
			editor.putString(CURRENCY_KEY, "$");
		}

		editor.commit();

		if ( fillUpDatabaseHelper == null ) {
			this.fillUpDatabaseHelper = FillUpDatabaseHelper.getHelper(this);
		}

		if ( carDatabaseHelper == null ) {
			carDatabaseHelper = CarDatabaseHelper.getHelper(this);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		v.vibrate(100);
		switch (item.getItemId()) {
			case R.id.action_export:
				displayExporterDialog();
				return true;
			case R.id.action_about:
				Intent intent = new Intent(this, AboutActivity.class);
				startActivity(intent);
				return true;
			case R.id.action_settings:
				intent = new Intent(this, SettingsActivity.class);
				startActivity(intent);
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	public class SectionsPagerAdapter extends FragmentPagerAdapter
	{

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position)
		{
			Fragment fragment = null;
			Bundle args = new Bundle();
			args.putInt("section_number", position + 1);
			switch (position) {
				case 0:
					fragment = new StatisticsViewFragment();
					break;
				case 1:
					fragment = new FillUpViewFragment();
					break;
				case 2:
					fragment = new CarsViewFragment();
					break;
				case 3:
					fragment = new MaintenanceLogViewFragment();
					break;
				default:
					break;
			}

			fragment.setArguments(args);
			return fragment;
		}

		@Override
		public int getCount()
		{
			return 4;
		}

		@Override
		public CharSequence getPageTitle(int position)
		{
			Locale l = Locale.getDefault();
			switch (position) {
				case 0:
					return getString(R.string.title_section1).toUpperCase(l);
				case 1:
					return getString(R.string.title_section2).toUpperCase(l);
				case 2:
					return getString(R.string.title_section3).toUpperCase(l);
				case 3:
					return getString(R.string.title_section4).toUpperCase(l);
			}
			return null;
		}
	}

	private void displayExporterDialog()
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Select what data to export");
		String[] items = { "Fill Ups as a CSV", "Receipt Images" };
		final boolean[] checkedItems = { true, true };

		builder.setTitle("Select an Option!");

		builder.setMultiChoiceItems(items, checkedItems, new OnMultiChoiceClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int pos, boolean selected)
			{
				checkedItems[pos] = selected;
			}
		}).setPositiveButton("Export", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int id)
			{
				boolean success = false;
				List<FillUp> fillups = null;
				try {
					fillups = fillUpDatabaseHelper.getFillUpDao().queryForAll();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				if ( checkedItems[0] ) {
					CSVWriter writer = null;

					File newFolder = new File(Environment.getExternalStorageDirectory(), "GasMilageJournal");
					if ( ! newFolder.exists() ) {
						newFolder.mkdir();
					}
					try {
						File file = new File(newFolder, "fillups.csv");
						file.createNewFile();
						writer = new CSVWriter(new FileWriter(file));
						Log.i("info", file.getAbsolutePath());
					} catch (Exception ex) {
					}
					Log.i("info", "" + ( writer == null ));
					SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
					List<String[]> data = new ArrayList<String[]>();
					String[] columns = { "CAR NAME", "MPG", "PRICE", "GALLONS", "DISTANCE", "DATE", "COMMENTS" };
					for ( FillUp fu : fillups ) {
						try {
							String[] values = new String[7];
							values[0] = carDatabaseHelper.getCarDao().queryForId(fu.getCarId()).getName();
							values[1] = "" + fu.getMPG();
							values[2] = "" + fu.getPrice();
							values[3] = "" + fu.getGas();
							values[4] = "" + fu.getDistance();
							values[5] = new SimpleDateFormat(sharedPref.getString(DATE_FORMAT_KEY, ""), Locale.US).format(fu.getDate());
							values[6] = "" + fu.getComments();
							data.add(values);
						} catch (SQLException e) {
							Log.i("erroe", e.toString());
						}
					}
					writer.writeNext(columns);
					writer.writeAll(data);

					try {
						writer.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
					success = true;
				}
				if ( checkedItems[1] ) {
					for ( FillUp fu : fillups ) {
						if ( fu.getReceipt() != null && fu.getReceipt().length > 0 ) {
							FileOutputStream fos = null;
							try {
								File newFolder = new File(Environment.getExternalStorageDirectory(), "GasMilageJournal");
								if ( ! newFolder.exists() ) {
									newFolder.mkdir();
								}
								try {
									File file = new File(newFolder, fu.getId() + "_" + fu.getDate() + ".jpg");
									file.createNewFile();
									 fos = new FileOutputStream(file);
									Log.i("info", file.getAbsolutePath());
								} catch (Exception ex) {
								}

								fos.write(fu.getReceipt());
								fos.close();
							} catch (IOException e) {

							}
						}
					}
					success = true;
				}
				if(success) {
					AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
					builder.setMessage("Files were successfully exported to GasMialgeJournal folder on your SD Card").setPositiveButton("Ok", null);
					builder.create().show();
				}
			}

		}).setNegativeButton("Cancel", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int id)
			{

			}
		});
		AlertDialog alert = builder.create();
		alert.show();
	}

	@Override
	public void onPause()
	{
		super.onPause();
	}

	@Override
	public void onResume()
	{
		super.onResume();
		if ( fillUpDatabaseHelper == null ) {
			this.fillUpDatabaseHelper = FillUpDatabaseHelper.getHelper(this);
		}

		if ( carDatabaseHelper == null ) {
			carDatabaseHelper = CarDatabaseHelper.getHelper(this);
		}
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
	}

}

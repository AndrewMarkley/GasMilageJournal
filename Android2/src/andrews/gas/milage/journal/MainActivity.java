package andrews.gas.milage.journal;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import andrews.gas.milage.journal.activities.SettingsActivity;
import andrews.gas.milage.journal.apprater.AppRater;
import andrews.gas.milage.journal.database.CarDatabaseHelper;
import andrews.gas.milage.journal.database.FillUpDatabaseHelper;
import andrews.gas.milage.journal.database.MaintenanceLogDatabaseHelper;
import andrews.gas.milage.journal.database.OldCar;
import andrews.gas.milage.journal.database.OldCarDatabaseHandler;
import andrews.gas.milage.journal.database.OldFillUpDatabaseHandler;
import andrews.gas.milage.journal.entities.Car;
import andrews.gas.milage.journal.entities.FillUp;
import andrews.gas.milage.journal.entities.MaintenanceLog;
import andrews.gas.milage.journal.fragments.CarsViewFragment;
import andrews.gas.milage.journal.fragments.FillUpViewFragment;
import andrews.gas.milage.journal.fragments.MaintenanceLogViewFragment;
import andrews.gas.milage.journal.fragments.StatisticsViewFragment;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
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
import android.widget.Toast;
import au.com.bytecode.opencsv.CSVWriter;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.MapBuilder;

public class MainActivity extends FragmentActivity
{

	private SectionsPagerAdapter mSectionsPagerAdapter;
	private ViewPager mViewPager;
	private CarDatabaseHelper carDatabaseHelper = null;
	private FillUpDatabaseHelper fillUpDatabaseHelper = null;
	private MaintenanceLogDatabaseHelper maintenanceLogDatabaseHelper = null;

	public static final String MEASUREMENT_KEY = "measurement";
	public static final String DATE_FORMAT_KEY = "date_format";
	public static final String CURRENCY_KEY = "currency";
	public static final String FILLUP_READING_KEY = "fillup_reading"; 

	public static EasyTracker tracker;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		PackageInfo pInfo = null;
		try {
			pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		String value = pInfo.versionName;
		tracker = EasyTracker.getInstance(this);
		tracker.set(Fields.APP_ID, value);
		tracker.set(Fields.LANGUAGE, "English");
		tracker.send(MapBuilder.createEvent("App Info", "App Started", Calendar.getInstance().getTime().toString(), null).build());

		AppRater.app_launched(this);

		mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		mViewPager.setCurrentItem(2, false);

		// check shared preference keys
		Context context = getApplicationContext();
		SharedPreferences sharedPref = context.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();

		if ( ! sharedPref.contains(MEASUREMENT_KEY) ) {
			editor.putString(MEASUREMENT_KEY, "US");
		}
		if ( ! sharedPref.contains(DATE_FORMAT_KEY) ) {
			editor.putString(DATE_FORMAT_KEY, "MM/dd/yyyy");
		}
		if ( ! sharedPref.contains(CURRENCY_KEY) ) {
			editor.putString(CURRENCY_KEY, "$");
		}
		if ( ! sharedPref.contains(FILLUP_READING_KEY) ) {
			editor.putString(FILLUP_READING_KEY, "trip");
		}
		editor.commit();

		// initialize databases
		if ( fillUpDatabaseHelper == null ) {
			this.fillUpDatabaseHelper = FillUpDatabaseHelper.getHelper(this);
		}

		if ( carDatabaseHelper == null ) {
			carDatabaseHelper = CarDatabaseHelper.getHelper(this);
		}

		if ( maintenanceLogDatabaseHelper == null ) {
			maintenanceLogDatabaseHelper = MaintenanceLogDatabaseHelper.getHelper(this);
		}

		// migrate old database
		try {
			OldCarDatabaseHandler oldCarDB = new OldCarDatabaseHandler(this);
			OldFillUpDatabaseHandler oldFillUpDB = new OldFillUpDatabaseHandler(this);

			List<Car> oldCars = oldCarDB.getAllCars();

			for ( Car car : oldCars ) {
				carDatabaseHelper.getCarDao().create(car);
			}

			List<OldCar> oldestCars = oldCarDB.getAllOldCars();
			List<Car> newCars = carDatabaseHelper.getCarDao().queryForAll();
			List<FillUp> fillUps = oldFillUpDB.getAllFillUps(oldestCars, newCars);

			for ( FillUp fu : fillUps ) {
				fillUpDatabaseHelper.getFillUpDao().create(fu);
			}

			oldFillUpDB.dropTable();
			oldCarDB.dropTable();
		} catch (Exception e) {

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
				displayAboutDialog();
				return true;
			case R.id.action_settings:
				Intent intent = new Intent(this, SettingsActivity.class);
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
		String[] items = { "Fill Ups as a CSV", "Maintenance Logs as a CSV", "Receipt Images" };
		final boolean[] checkedItems = { true, true, true };

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
				List<MaintenanceLog> logs = null;
				try {
					fillups = fillUpDatabaseHelper.getFillUpDao().queryForAll();
					logs = maintenanceLogDatabaseHelper.getMaintenanceLogDao().queryForAll();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				if ( checkedItems[0] ) {
					CSVWriter writer = null;

					File newFolder = new File(Environment.getExternalStorageDirectory(), "GasMileageJournal");
					if ( ! newFolder.exists() ) {
						newFolder.mkdir();
					}
					try {
						String date = new SimpleDateFormat("MM-dd-yyyy_HH:mm_", Locale.US).format(Calendar.getInstance().getTime());
						File file = new File(newFolder, date + "fill_ups.csv");
						file.createNewFile();
						writer = new CSVWriter(new FileWriter(file));
					} catch (Exception ex) {
					}

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
					CSVWriter writer = null;

					File newFolder = new File(Environment.getExternalStorageDirectory(), "GasMileageJournal");
					if ( ! newFolder.exists() ) {
						newFolder.mkdir();
					}
					try {
						String date = new SimpleDateFormat("MM-dd-yyyy_HH:mm_", Locale.US).format(Calendar.getInstance().getTime());
						File file = new File(newFolder, date + "maintenance_logs.csv");
						file.createNewFile();
						writer = new CSVWriter(new FileWriter(file));
					} catch (Exception ex) {
					}

					SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
					List<String[]> data = new ArrayList<String[]>();
					String[] columns = { "CAR NAME", "TITLE", "DESCRIPTION", "COST", "ODOMETER", "LOCATION", "DATE" };
					for ( MaintenanceLog log : logs ) {
						try {
							// public MaintenanceLog(int carId, Date date,
							// double cost, double odometer, String title,
							// String description, String location, byte[]
							// receipt)
							String[] values = new String[7];
							values[0] = carDatabaseHelper.getCarDao().queryForId(log.getCarId()).getName();
							values[1] = "" + log.getTitle();
							values[2] = "" + log.getDescription();
							values[3] = "" + log.getCost();
							values[4] = "" + log.getOdometer();
							values[5] = "" + log.getLocation();
							values[6] = new SimpleDateFormat(sharedPref.getString(DATE_FORMAT_KEY, ""), Locale.US).format(log.getDate());
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
				if ( checkedItems[2] ) {
					for ( FillUp fu : fillups ) {
						if ( fu.getReceipt() != null && fu.getReceipt().length > 0 ) {
							FileOutputStream fos = null;
							try {
								File newFolder = new File(Environment.getExternalStorageDirectory(), "GasMileageJournal");
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
				if ( success ) {
					AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
					builder.setMessage("Files were successfully exported to GasMileageJournal folder on your SD Card").setPositiveButton("Ok", null);
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

	public void displayAboutDialog()
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
		builder.setMessage("Gas Mileage Journal\nDeveloped by Orange Games\nVersion 2.0").setPositiveButton("Ok", null);
		builder.create().show();
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

		if ( maintenanceLogDatabaseHelper == null ) {
			maintenanceLogDatabaseHelper = MaintenanceLogDatabaseHelper.getHelper(this);
		}
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if ( resultCode == RESULT_OK ) {
			// launch tutorial here
		}

		if ( resultCode == Activity.RESULT_CANCELED ) {

		}
	}

	@Override
	public void onStart()
	{
		super.onStart();
		EasyTracker.getInstance(this).activityStart(this);
	}

	@Override
	public void onStop()
	{
		super.onStop();
		EasyTracker.getInstance(this).activityStop(this);
	}

}

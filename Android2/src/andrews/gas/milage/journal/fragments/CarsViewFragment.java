package andrews.gas.milage.journal.fragments;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import andrews.gas.milage.journal.MainActivity;
import andrews.gas.milage.journal.R;
import andrews.gas.milage.journal.database.CarDatabaseHelper;
import andrews.gas.milage.journal.database.FillUpDatabaseHelper;
import andrews.gas.milage.journal.database.MaintenanceLogDatabaseHelper;
import andrews.gas.milage.journal.dialogs.ShowCarDialog;
import andrews.gas.milage.journal.entities.Car;
import andrews.gas.milage.journal.entities.FillUp;
import andrews.gas.milage.journal.entities.MaintenanceLog;


public class CarsViewFragment extends Fragment
{
	private CarDatabaseHelper carDatabaseHelper = null;
	private FillUpDatabaseHelper fillUpDatabaseHelper = null;
	ListView carListView = null;
	View rootView = null;
	CarArrayAdapter carArrayAdapter = null;
	List<Car> cars = new ArrayList<Car>();
	Button newCarButton = null;
	private MaintenanceLogDatabaseHelper maintenanceLogDatabaseHelper = null;
	AlertDialog.Builder builder;

	public CarsViewFragment() {}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		rootView = inflater.inflate(R.layout.car_view_fragment, container, false);
		carListView = (ListView) rootView.findViewById(R.id.car_view_fragment_list);
		newCarButton = (Button) rootView.findViewById(R.id.car_view_fragment_new_car_button);

		AdView adView = (AdView) rootView.findViewById(R.id.adView);
		AdRequest adRequest = new AdRequest.Builder().addTestDevice("89C5255F42662D2FCFD03698061CF86D").build();
		adView.loadAd(adRequest);

		newCarButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent i = new Intent(getActivity().getApplicationContext(), ShowCarDialog.class);
				i.putExtra("new", true);
				startActivityForResult(i, 1);
			}
		});

		refreshCarList();
		carListView.setLongClickable(true);
		carListView.setOnItemLongClickListener(new OnItemLongClickListener()
		{

			@Override
			public boolean onItemLongClick(AdapterView<?> adapterView, View view, int pos, long arg3)
			{
				rootView.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
				final Car selectedCar = (Car) adapterView.getAdapter().getItem(pos);
				final int positionInList = pos;

				final CharSequence[] items = { "Edit", "Delete", "Cancel" };
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				builder.setTitle("Select an Option!");

				builder.setItems(items, new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface dialog, int item)
					{
						switch (item) {
							case 0:
								Intent intent = new Intent(getActivity(), ShowCarDialog.class);
								intent.putExtra("new", false);
								intent.putExtra("car", selectedCar);
								startActivityForResult(intent, 1);
								break;
							case 1:
								AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
								builder.setMessage("Are you sure you want to delete this car and all of it's associated records?").setPositiveButton("Ok", new DialogInterface.OnClickListener()
								{
									@Override
									public void onClick(DialogInterface dialog, int id)
									{
										try {
											List<FillUp> fillUps = fillUpDatabaseHelper.getFillUpDao().queryBuilder().where().eq(FillUp.COLUMN_CAR_ID, selectedCar.getId()).query();
											List<MaintenanceLog> logs = maintenanceLogDatabaseHelper.getMaintenanceLogDao().queryBuilder().where().eq(FillUp.COLUMN_CAR_ID, selectedCar.getId())
													.query();

											for ( FillUp fu : fillUps ) {
												fillUpDatabaseHelper.getFillUpDao().delete(fu);
											}

											for ( MaintenanceLog log : logs ) {
												maintenanceLogDatabaseHelper.getMaintenanceLogDao().delete(logs);
											}

											carDatabaseHelper.getCarDao().delete(selectedCar);
										} catch (SQLException e) {
											e.printStackTrace();
										}
										cars.remove(positionInList);
										refreshCarList();
									}
								});
								builder.create().show();
								break;
							case 2:
								break;
							default:
						}
					}
				});

				AlertDialog alert = builder.create();
				alert.show();

				return false;
			}

		});
		return rootView;
	}

	class CarArrayAdapter extends ArrayAdapter<Car>
	{
		private final Context context;
		private final Car[] values;

		public CarArrayAdapter(Context context, Car[] values) {
			super(context, R.layout.car_view_fragment_list_view, values);
			this.context = context;
			this.values = values;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View rowView = inflater.inflate(R.layout.car_view_fragment_list_view, parent, false);

			TextView carName = (TextView) rowView.findViewById(R.id.car_view_frament_list_view_car_name);
			TextView year = (TextView) rowView.findViewById(R.id.car_view_frament_list_view_car_year);
			TextView make = (TextView) rowView.findViewById(R.id.car_view_frament_list_view_car_make);
			TextView model = (TextView) rowView.findViewById(R.id.fillup_view_frament_list_view_car_units);
			TextView odometer = (TextView) rowView.findViewById(R.id.car_view_frament_list_view_car_odometer);

			if ( position % 2 == 0 ) {
				rowView.setBackgroundColor(Color.BLACK);
			} else {
				rowView.setBackgroundColor(Color.DKGRAY);
			}

			carName.setTextColor(Color.WHITE);
			year.setTextColor(Color.WHITE);
			make.setTextColor(Color.WHITE);
			model.setTextColor(Color.WHITE);
			odometer.setTextColor(Color.WHITE);

			SharedPreferences sharedPref = context.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
			String units = "";
			if ( sharedPref.getString(MainActivity.MEASUREMENT_KEY, "").equals("US") ) {
				units = " mi";
			} else {
				units = " km";
			}

			Car c = values[position];
			carName.setText(c.getName());
			year.setText("" + c.getYear());
			make.setText(c.getMake());
			model.setText(c.getModel());
			odometer.setText("Odometer: " + c.getMilage() + units);

			WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
			Display display = wm.getDefaultDisplay();
			int width = display.getWidth(); // deprecated

			carName.setMaxWidth(width);
			carName.setMaxLines(1);

			return rowView;
		}
	}

	@Override
	public void onAttach(Activity activity)
	{
		super.onAttach(activity);
		if ( fillUpDatabaseHelper == null ) {
			this.fillUpDatabaseHelper = FillUpDatabaseHelper.getHelper(activity);
		}

		if ( carDatabaseHelper == null ) {
			carDatabaseHelper = CarDatabaseHelper.getHelper(activity);
		}

		if ( maintenanceLogDatabaseHelper == null ) {
			maintenanceLogDatabaseHelper = MaintenanceLogDatabaseHelper.getHelper(activity);
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if ( requestCode == android.app.Activity.RESULT_OK ) {
			boolean success = false;
			success = data.getBooleanExtra("success", success);
		}
		refreshCarList();
	}

	public void refreshCarList()
	{
		try {
			cars = carDatabaseHelper.getCarDao().queryForAll();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		Car[] temp = new Car[cars.size()];
		cars.toArray(temp);

		carArrayAdapter = new CarArrayAdapter(getActivity().getBaseContext(), temp);
		carListView.setAdapter(carArrayAdapter);
		
		if ( cars == null || cars.isEmpty() && builder == null) {
			// Force creation of a new car
			builder = new AlertDialog.Builder(getActivity());
			builder.setMessage("You Must have at least 1 car entered to use Gas Mileage Journal!").setPositiveButton("OK", new DialogInterface.OnClickListener()
			{

				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					Intent i = new Intent(getActivity(), ShowCarDialog.class);
					startActivityForResult(i, 1);
					refreshCarList();
					builder = null;
				}
			}).setOnCancelListener(new DialogInterface.OnCancelListener()
			{
				
				@Override
				public void onCancel(DialogInterface dialog)
				{
					builder = null;
					refreshCarList();
				}
			});
			builder.show();

		}
	}

	@Override
	public void onDestroy()
	{
		if ( carDatabaseHelper != null ) {
			carDatabaseHelper.close();
			carDatabaseHelper = null;
		}
		if ( fillUpDatabaseHelper != null ) {
			fillUpDatabaseHelper.close();
			fillUpDatabaseHelper = null;
		}
		if ( maintenanceLogDatabaseHelper != null ) {
			maintenanceLogDatabaseHelper.close();
			maintenanceLogDatabaseHelper = null;
		}
		super.onDestroy();
	}

	public void onResume()
	{
		super.onResume();
		refreshCarList();
	}
}

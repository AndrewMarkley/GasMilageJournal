package com.orangegames.gasmilagejournal.fragments;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemLongClickListener;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.orangegames.gasmilagejournal.MainActivity;
import com.orangegames.gasmilagejournal.R;
import com.orangegames.gasmilagejournal.database.CarDatabaseHelper;
import com.orangegames.gasmilagejournal.database.MaintenanceLogDatabaseHelper;
import com.orangegames.gasmilagejournal.dialogs.ShowFillUpDialog;
import com.orangegames.gasmilagejournal.dialogs.ShowMaintenanceDialog;
import com.orangegames.gasmilagejournal.entities.FillUp;
import com.orangegames.gasmilagejournal.entities.MaintenanceLog;

public class MaintenanceLogViewFragment extends Fragment
{
	MaintenanceArrayAdapter maintenanceArrayAdapter = null;
	private List<MaintenanceLog> maintenanceLogs = null;
	private CarDatabaseHelper carDatabaseHelper = null;
	private MaintenanceLogDatabaseHelper maintenanceLogDatabaseHelper = null;
	private ListView listView = null;
	private View rootView = null;

	public MaintenanceLogViewFragment() {}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		rootView = inflater.inflate(R.layout.maintenance_log_view_fragment, container, false);
		listView = (ListView) rootView.findViewById(R.id.maintenance_log_view_fragment_list);
		Button newLogButton = (Button) rootView.findViewById(R.id.maintenance_log_view_fragment_add_button);
		
		newLogButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent i = new Intent(getActivity().getApplicationContext(), ShowMaintenanceDialog.class);
				i.putExtra("new", true);
				startActivityForResult(i, 1);
			}
		});
		
		listView.setLongClickable(true);
		listView.setOnItemLongClickListener(new OnItemLongClickListener()
		{

			@Override
			public boolean onItemLongClick(AdapterView<?> adapterView, View view, int pos, long arg3)
			{
				rootView.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
				final MaintenanceLog selectedLog = (MaintenanceLog) adapterView.getAdapter().getItem(pos);
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
								Intent intent = new Intent(getActivity(), ShowMaintenanceDialog.class);
								intent.putExtra("new", false);
								intent.putExtra("log", selectedLog);
								startActivityForResult(intent, 1);
								break;
							case 1:
								try {
									maintenanceLogDatabaseHelper.getMaintenanceLogDao().delete(selectedLog);
									maintenanceLogs.remove(positionInList);
									refreshMaintenanceList();
								} catch (SQLException e) {
									e.printStackTrace();
								}
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

		AdView adView = (AdView) rootView.findViewById(R.id.adView);
		AdRequest adRequest = new AdRequest.Builder().addTestDevice("89C5255F42662D2FCFD03698061CF86D").build();
		adView.loadAd(adRequest);
		
		return rootView;
	}

	class MaintenanceArrayAdapter extends ArrayAdapter<MaintenanceLog>
	{
		private final Context context;
		private final MaintenanceLog[] values;

		public MaintenanceArrayAdapter(Context context, MaintenanceLog[] values) {
			super(context, R.layout.maintenance_log_view_fragment_list_view, values);
			this.context = context;
			this.values = values;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View rowView = inflater.inflate(R.layout.maintenance_log_view_fragment_list_view, parent, false);

			TextView title = (TextView) rowView.findViewById(R.id.maintenance_log_view_frament_title);
			TextView description = (TextView) rowView.findViewById(R.id.maintenance_log_view_frament_description);
			TextView cost = (TextView) rowView.findViewById(R.id.maintenance_log_view_frament_list_view_cost);
			TextView date = (TextView) rowView.findViewById(R.id.maintenance_log_view_frament_list_view_date);
			TextView odometer = (TextView) rowView.findViewById(R.id.maintenance_log_view_frament_list_view_odometer);
			TextView carName = (TextView) rowView.findViewById(R.id.maintenance_log_view_frament_list_view_car_name);
			
			if ( position % 2 == 0 ) {
				rowView.setBackgroundColor(Color.BLACK);
			} else {
				rowView.setBackgroundColor(Color.DKGRAY);
			}
			
			title.setTextColor(Color.WHITE);
			description.setTextColor(Color.WHITE);
			cost.setTextColor(Color.WHITE);
			date.setTextColor(Color.WHITE);
			odometer.setTextColor(Color.WHITE);
			carName.setTextColor(Color.WHITE);
			
			
			SharedPreferences sharedPref = context.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
			String currencyUnits = sharedPref.getString(MainActivity.CURRENCY_KEY, "");
			String distanceUnits = "";
			
			if ( sharedPref.getString(MainActivity.MEASUREMENT_KEY, "").equals("US") ) {
				distanceUnits = " mi";
			} else {
				distanceUnits = " km";
			}			

			MaintenanceLog log = maintenanceLogs.get(position);
			String name = "";
			try {
				if ( carDatabaseHelper.getCarDao().queryForId(log.getCarId()) != null ) {
					name = carDatabaseHelper.getCarDao().queryForId(log.getCarId()).getName();
				}
			} catch (SQLException e) {

			}

			title.setText(log.getTitle());
			description.setText(log.getDescription());
			cost.setText(currencyUnits + round(log.getCost()));
			date.setText(new SimpleDateFormat(sharedPref.getString(MainActivity.DATE_FORMAT_KEY, ""), Locale.US).format(log.getDate()));
			odometer.setText("" + round(log.getOdometer()) + distanceUnits);
			carName.setText(name);
			return rowView;
		}
	}

	@Override
	public void onAttach(Activity activity)
	{
		super.onAttach(activity);
		if ( maintenanceLogDatabaseHelper == null ) {
			this.maintenanceLogDatabaseHelper = MaintenanceLogDatabaseHelper.getHelper(activity);
		}

		if ( carDatabaseHelper == null ) {
			carDatabaseHelper = CarDatabaseHelper.getHelper(activity);
		}
	}

	public void refreshMaintenanceList()
	{
		try {
			maintenanceLogs = maintenanceLogDatabaseHelper.getMaintenanceLogDao().queryForAll();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		MaintenanceLog[] temp = new MaintenanceLog[maintenanceLogs.size()];
		maintenanceLogs.toArray(temp);
		maintenanceArrayAdapter = new MaintenanceArrayAdapter(getActivity().getBaseContext(), (MaintenanceLog[]) temp);
		listView.setAdapter(maintenanceArrayAdapter);
	}

	private double round(double x)
	{
		return ( (int) ( x * 100 ) ) / 100.0;
	}

	@Override
	public void onResume() 
	{
		super.onResume();
		refreshMaintenanceList();
	}
}

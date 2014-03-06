package com.orangegames.gasmilagejournal.fragments;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.orangegames.gasmilagejournal.R;
import com.orangegames.gasmilagejournal.car.Car;
import com.orangegames.gasmilagejournal.database.CarDatabaseHelper;
import com.orangegames.gasmilagejournal.database.FillUpDatabaseHelper;
import com.orangegames.gasmilagejournal.dialogs.ShowCarDialog;

public class CarsViewFragment extends Fragment
{
	private CarDatabaseHelper carDatabaseHelper = null;
	private FillUpDatabaseHelper fillUpDatabaseHelper = null;
	ListView carListView = null;
	View rootView = null;
	CarArrayAdapter carArrayAdapter = null;
	List<Car> cars = new ArrayList<Car>();
	Button newCarButton = null;

	public CarsViewFragment() {}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		rootView = inflater.inflate(R.layout.car_view_fragment, container, false);
		carListView = (ListView) rootView.findViewById(R.id.car_view_fragment_list);
		newCarButton = (Button) rootView.findViewById(R.id.car_view_fragment_new_car_button);

		AdView adView = (AdView)rootView.findViewById(R.id.adView);
		AdRequest adRequest = new AdRequest.Builder().addTestDevice("89C5255F42662D2FCFD03698061CF86D").build();
		adView.loadAd(adRequest);
		
		newCarButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Log.i("info", "Launching ShowCarDialog as an intent inside CarsViewFragment");
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
								try {
									carDatabaseHelper.getCarDao().delete(selectedCar);
									cars.remove(positionInList);
									refreshCarList();
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

			TextView title = (TextView) rowView.findViewById(R.id.car_view_frament_list_view_car_name);
			TextView info = (TextView) rowView.findViewById(R.id.car_view_frament_list_view_car_info);
			TextView odometer = (TextView) rowView.findViewById(R.id.car_view_frament_list_view_odometer);

			Car c = values[position];
			title.setText(c.getName());
			info.setText(c.getYear() + " " + c.getMake() + " " + c.getModel());
			odometer.setText("" + c.getMilage());

			// if (s.equals("WindowsMobile")) {
			// imageView.setImageResource(R.drawable.windowsmobile_logo);
			// } else if (s.equals("iOS")) {
			// imageView.setImageResource(R.drawable.ios_logo);
			// } else if (s.equals("Blackberry")) {
			// imageView.setImageResource(R.drawable.blackberry_logo);
			// } else {
			// imageView.setImageResource(R.drawable.android_logo);
			// }

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

		if ( cars == null ) {
			// Force creation of a new car
			Intent i = new Intent(getActivity(), ShowCarDialog.class);
			startActivityForResult(i, 1);
		} else {
			Car[] temp = new Car[cars.size()];
			cars.toArray(temp);

			carArrayAdapter = new CarArrayAdapter(getActivity().getBaseContext(), temp);
			carListView.setAdapter(carArrayAdapter);
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
		super.onDestroy();
	}

}

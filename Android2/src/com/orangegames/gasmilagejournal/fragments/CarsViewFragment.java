package com.orangegames.gasmilagejournal.fragments;

import java.sql.SQLException;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.orangegames.gasmilagejournal.R;
import com.orangegames.gasmilagejournal.car.Car;
import com.orangegames.gasmilagejournal.database.CarDatabaseHelper;
import com.orangegames.gasmilagejournal.database.FillUpDatabaseHelper;
import com.orangegames.gasmilagejournal.dialogs.ShowCarDialog;

public class CarsViewFragment extends Fragment
{
	private CarDatabaseHelper carDatabaseHelper = null;
	private FillUpDatabaseHelper fillUpDatabaseHelper = null;
	ListView listView = null;

	public CarsViewFragment() {}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View rootView = inflater.inflate(R.layout.car_view_fragment, container, false);

		listView = (ListView) rootView.findViewById(R.id.car_view_fragment_list);
		Button newCarButton = (Button) rootView.findViewById(R.id.car_view_fragment_new_car_button);

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
		
		fillUpDatabaseHelper.getWritableDatabase();
		List<Car> cars = null;
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
			System.err.println(temp.length);
			CarArrayAdapter adapter = new CarArrayAdapter(getActivity(), temp);

			listView = (ListView) rootView.findViewById(R.id.car_view_fragment_list);
			listView.setAdapter(adapter);
		}

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
			TextView title = (TextView) rowView.findViewById(R.id.car_view_fragment_list_view_title);
			TextView description = (TextView) rowView.findViewById(R.id.car_view_fragment_list_view_description);
			
			System.err.println("position = " + position);
			
			if(values[position] == null) {
				title.setText("default");
				description.setText("default");
			} else {
				title.setText(values[position].getName());
				description.setText(values[position].getName());
			}

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
		if (fillUpDatabaseHelper == null) {
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
			if(success) {
				List<Car> cars = null;
				try {
					cars = carDatabaseHelper.getCarDao().queryForAll();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
				CarArrayAdapter adapter = new CarArrayAdapter(getActivity(), (Car[]) cars.toArray());
				listView.setAdapter(adapter);
			}
		}

	}
}

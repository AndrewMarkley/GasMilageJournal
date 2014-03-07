package com.orangegames.gasmilagejournal.fragments;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.orangegames.gasmilagejournal.R;
import com.orangegames.gasmilagejournal.car.Car;
import com.orangegames.gasmilagejournal.database.CarDatabaseHelper;
import com.orangegames.gasmilagejournal.database.FillUpDatabaseHelper;
import com.orangegames.gasmilagejournal.fillup.FillUp;
import com.orangegames.gasmilagejournal.statistics.Statistics;

public class StatisticsViewFragment extends Fragment
{
	public StatisticsViewFragment() {}

	private CarDatabaseHelper carDatabaseHelper = null;
	private FillUpDatabaseHelper fillUpDatabaseHelper = null;

	ListView listView = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View rootView = inflater.inflate(R.layout.statistics_view_fragment, container, false);

		listView = (ListView) rootView.findViewById(R.id.statistics_view_fragment_list);

		refreshStatisticsList();

		return rootView;
	}

	class statisticsArrayAdapter extends ArrayAdapter<Statistics>
	{
		private final Context context;
		private final Statistics[] values;

		public statisticsArrayAdapter(Context context, Statistics[] values) {
			super(context, R.layout.statistics_view_fragment_list_view, values);
			this.context = context;
			this.values = values;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			View rowView = inflater.inflate(R.layout.statistics_view_fragment_list_view, parent, false);
			TextView carName = (TextView) rowView.findViewById(R.id.statistics_view_frament_list_view_car_name);
			TextView avgMpg = (TextView) rowView.findViewById(R.id.statistics_view_frament_list_view_mpg);
			TextView avgMilesBetweenFillups = (TextView) rowView.findViewById(R.id.statistics_view_frament_list_view_miles_between_fillups);
			TextView avgDollarsPerDay = (TextView) rowView.findViewById(R.id.statistics_view_fragment_list_view_dolalrs_per_day);
			TextView avgMilesPerDollar = (TextView) rowView.findViewById(R.id.statistics_view_fragment_list_view_miles_per_dollar);
			TextView avgFuelCost = (TextView) rowView.findViewById(R.id.statistics_view_frament_list_view_fuel_cost);
			TextView avgTimeBetween = (TextView) rowView.findViewById(R.id.statistics_view_fragment_list_view_time_between_fillups);

			Statistics temp = values[position];
			
			try {
				carName.setText(carDatabaseHelper.getCarDao().queryForId(temp.getCarId()).getName());
			} catch (SQLException e) {
				carName.setText("Car");
			}
			avgMpg.setText("Avg MPG: " + temp.getAvgMPG());
			avgMilesBetweenFillups.setText("Avg Miles Between Fill Ups: " + temp.getAvgMilesBetweenFillUps());
			avgDollarsPerDay.setText("Avg Dollars Per Day: " + temp.getAvgDollarPerDay());
			avgMilesPerDollar.setText("Avg Miles Per Dollar: " + temp.getAvgMilesPerDollar());
			avgFuelCost.setText("Avg Fuel Cost: " + temp.getAvgFuelCosts());
			avgTimeBetween.setText("Avg Time Between Fill Ups: " + temp.getAvgTimeBetweenFillUps());
			
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

	public void refreshStatisticsList()
	{
		List<Car> cars = null;
		List<FillUp> fillUps = null;

		try {
			cars = carDatabaseHelper.getCarDao().queryForAll();
			fillUps = fillUpDatabaseHelper.getFillUpDao().queryForAll();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		Statistics[] statistics = new Statistics[cars.size()];
		List<Statistics> stats = new ArrayList<Statistics>();

		for ( Car car : cars ) {
			double gallons = 0;
			double distance = 0;
			double spent = 0;
			int numberOfFillUps = 0;

			Date earliestDate = new Date();

			for ( FillUp fu : fillUps ) {
				if ( fu.getCarId() == car.getId() ) {
					if ( earliestDate.after(fu.getDate()) )  {
						earliestDate = fu.getDate();
					}
					gallons += fu.getGas();
					distance += fu.getDistance();
					spent += fu.getPrice();
					numberOfFillUps++;
				}
			}
			if ( numberOfFillUps > 0 ) {
				int diffInDays = (int) ( ( Calendar.getInstance().getTime().getTime() - earliestDate.getTime() ) / ( 1000 * 60 * 60 * 24 ) );
				double avgMPG = round(gallons / distance);
				double avgFuelCosts = round(spent / (diffInDays + 1));
				double avgMilesPerDollar = round(distance / spent);
				double avgDollarPerDay = round(diffInDays / spent);
				double avgTimeBetweenFillUps = round(diffInDays / numberOfFillUps);
				double avgMilesBetweenFillUps = round(distance / numberOfFillUps);
				stats.add(new Statistics(car.getId(), avgMPG, avgFuelCosts, avgMilesPerDollar, avgTimeBetweenFillUps, avgMilesBetweenFillUps, avgDollarPerDay));
			} else {
				stats.add(new Statistics(car.getId(), 0, 0, 0, 0, 0, 0));
			}
		}

		stats.toArray(statistics);

		statisticsArrayAdapter adapter = new statisticsArrayAdapter(getActivity(), statistics);

		listView.setAdapter(adapter);
	}
	
	private double round(double x) {
		return ((int)(x * 100)) / 100.0;
	}
}

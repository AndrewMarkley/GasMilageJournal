package com.orangegames.gasmilagejournal.fragments;

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
import com.orangegames.gasmilagejournal.statistics.Statistics;

public class StatisticsViewFragment extends Fragment
{
	public StatisticsViewFragment() {}
	
	private double avgMPG = 0;
	private double avgFuelCosts = 0;
	private double avgMilesPerDollar = 0;
	private double avgTimeBetweenstatisticss = 0;
	private double avgMilesBetweenstatisticss = 0;
	private double avgDollarPerDay = 0;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View rootView = inflater.inflate(R.layout.statistics_view_fragment, container, false);

		ListView listView = (ListView) rootView.findViewById(R.id.statistics_view_fragment_list);
		
		Statistics[] statistics = {new Statistics(1, 1, 1, 1, 1, 1, 1), new Statistics(1, 1, 1, 1, 1, 1, 1)};
		statisticsArrayAdapter adapter = new statisticsArrayAdapter(getActivity(), statistics);

		listView = (ListView) rootView.findViewById(R.id.statistics_view_fragment_list);
		listView.setAdapter(adapter);

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
			TextView textView = (TextView) rowView.findViewById(R.id.statistics_view_fragment_list_view_title);
			textView.setText("car name");
			TextView tv = (TextView) rowView.findViewById(R.id.statistics_view_fragment_list_view_description);
			tv.setText("test stat");

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
}

package com.orangegames.gasmilagejournal.fragments;

import java.sql.SQLException;
import java.util.Calendar;
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

public class FillUpViewFragment extends Fragment
{
	private CarDatabaseHelper carDatabaseHelper = null;
	private FillUpDatabaseHelper fillUpDatabaseHelper = null;
	
	public FillUpViewFragment() {}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View rootView = inflater.inflate(R.layout.fillup_view_fragment, container, false);

		ListView listView = (ListView) rootView.findViewById(R.id.fill_view_fragment_list);
		
		FillUp[] fillups = {new FillUp(1, 1, 300, 20.5, 2.99, Calendar.getInstance().getTime(), "comments")};
		
		List<FillUp> temp = null;
		try {
			temp = fillUpDatabaseHelper.getFillUpDao().queryForAll();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		if(!temp.isEmpty()) {
			temp.toArray(fillups);
		}
		
		FillUpArrayAdapter adapter = new FillUpArrayAdapter(getActivity(), fillups);

		listView = (ListView) rootView.findViewById(R.id.fill_view_fragment_list);
		listView.setAdapter(adapter);

		return rootView;
	}

	class FillUpArrayAdapter extends ArrayAdapter<FillUp>
	{
		private final Context context;
		private final FillUp[] values;

		public FillUpArrayAdapter(Context context, FillUp[] values) {
			super(context, R.layout.fillup_view_fragment_list_view, values);
			this.context = context;
			this.values = values;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			View rowView = inflater.inflate(R.layout.fillup_view_fragment_list_view, parent, false);
			TextView textView = (TextView) rowView.findViewById(R.id.fillup_view_fragment_list_view_title);
			textView.setText("car name");
			TextView tv = (TextView) rowView.findViewById(R.id.fillup_view_fragment_list_view_description);
			tv.setText(values[position].getComments());

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
	
}

package com.orangegames.gasmilagejournal.fragments;

import java.util.Calendar;

import com.orangegames.gasmilagejournal.R;
import com.orangegames.gasmilagejournal.R.id;
import com.orangegames.gasmilagejournal.R.layout;
import com.orangegames.gasmilagejournal.fillup.FillUp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class FillUpViewFragment extends Fragment
{
	public FillUpViewFragment() {}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View rootView = inflater.inflate(R.layout.fillup_view_fragment, container, false);

		ListView listView = (ListView) rootView.findViewById(R.id.fill_view_fragment_list);
		
		FillUp[] fillups = {new FillUp(1, 300, 20.5, 2.99, Calendar.getInstance().getTime(), "comments")};
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
}

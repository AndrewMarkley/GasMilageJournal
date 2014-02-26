package com.orangegames.gasmilagejournal;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class CarsViewFragment extends Fragment
{
	public CarsViewFragment() {}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View rootView = inflater.inflate(R.layout.car_view_fragment, container, false);

		ListView listView = (ListView) rootView.findViewById(R.id.car_view_fragment_list);
		
		Car[] cars = {new Car("name", 1992, "make", "model", 100000)};
		CarArrayAdapter adapter = new CarArrayAdapter(getActivity(), cars);

		listView = (ListView) rootView.findViewById(R.id.car_view_fragment_list);
		listView.setAdapter(adapter);

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
			TextView textView = (TextView) rowView.findViewById(R.id.car_view_fragment_list_view_title);
			textView.setText(values[position].getName());
			TextView tv = (TextView) rowView.findViewById(R.id.car_view_fragment_list_view_description);
			tv.setText(values[position].getMake() + " " + values[position].getModel() + " " + values[position].getMilage());

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

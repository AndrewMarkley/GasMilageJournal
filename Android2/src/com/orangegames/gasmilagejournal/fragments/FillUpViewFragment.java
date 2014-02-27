package com.orangegames.gasmilagejournal.fragments;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.orangegames.gasmilagejournal.R;
import com.orangegames.gasmilagejournal.car.Car;
import com.orangegames.gasmilagejournal.database.CarDatabaseHelper;
import com.orangegames.gasmilagejournal.database.FillUpDatabaseHelper;
import com.orangegames.gasmilagejournal.dialogs.ShowCarDialog;
import com.orangegames.gasmilagejournal.dialogs.ShowFillUpDialog;
import com.orangegames.gasmilagejournal.fillup.FillUp;

public class FillUpViewFragment extends Fragment
{
	private CarDatabaseHelper carDatabaseHelper = null;
	private FillUpDatabaseHelper fillUpDatabaseHelper = null;
	
	ListView listView = null;
	List<FillUp> fillUps = new ArrayList<FillUp>();
	FillUpArrayAdapter fillUpArrayAdapter = null;
	Button newFillUp = null;
	
	public FillUpViewFragment() {}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View rootView = inflater.inflate(R.layout.fillup_view_fragment, container, false);
		listView = (ListView) rootView.findViewById(R.id.fill_view_fragment_list);
		newFillUp = (Button) rootView.findViewById(R.id.fillup_view_fragment_new_fillup_button);
		
		refreshFillUpsList();
		
		newFillUp.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent(getActivity(), ShowFillUpDialog.class);
				startActivityForResult(intent, 1);
			}
		});
		
		registerForContextMenu(listView);

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
			
			TextView title = (TextView) rowView.findViewById(R.id.fillup_view_fragment_list_view_title);
			TextView description = (TextView) rowView.findViewById(R.id.fillup_view_fragment_list_view_description);
			
			title.setText("car name");
			description.setText(values[position].getComments());

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
	
	public void refreshFillUpsList() {
		try {
			fillUps = fillUpDatabaseHelper.getFillUpDao().queryForAll();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		if ( fillUps == null ) {
			// Force creation of a new car
			Intent i = new Intent(getActivity(), ShowCarDialog.class);
			startActivityForResult(i, 1);
		} else {
			FillUp[] temp = new FillUp[fillUps.size()]; 
			fillUps.toArray(temp);

			fillUpArrayAdapter = new FillUpArrayAdapter(getActivity(), temp);
			listView.setAdapter(fillUpArrayAdapter);
		}
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if ( requestCode == android.app.Activity.RESULT_OK ) {
			boolean success = false;
			success = data.getBooleanExtra("success", success);
		}
		refreshFillUpsList();
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo)
	{
		if ( v.getId() == R.id.fill_view_fragment_list ) {
			menu.setHeaderTitle("Select an Option");

			String[] menuItems = { "Edit", "Delete", "Cancel" };
			for ( int i = 0; i < menuItems.length; i++ ) {
				menu.add(Menu.NONE, i, i, menuItems[i]);
			}
		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem item)
	{
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
		int menuItemIndex = item.getItemId();
		FillUp selectedFillUp = fillUps.get(info.position);
		
		//{ "Edit", "Delete", "Cancel" }
		switch(menuItemIndex) {
			case 0: Intent intent = new Intent(getActivity(), ShowFillUpDialog.class);
					intent.putExtra("newFillUp", false);
					intent.putExtra("fillUp", selectedFillUp);
					startActivityForResult(intent, 1);
				break;
			case 1: try {
					fillUpDatabaseHelper.getFillUpDao().delete(selectedFillUp);
					fillUps.remove(info.position);
					refreshFillUpsList();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				break;
			case 2:
				break;
			default:
		}
		return true;
	}
}

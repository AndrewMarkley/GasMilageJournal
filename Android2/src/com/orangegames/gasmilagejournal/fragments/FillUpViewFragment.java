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
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import com.orangegames.gasmilagejournal.R;
import com.orangegames.gasmilagejournal.database.CarDatabaseHelper;
import com.orangegames.gasmilagejournal.database.FillUpDatabaseHelper;
import com.orangegames.gasmilagejournal.dialogs.ShowFillUpDialog;
import com.orangegames.gasmilagejournal.fillup.FillUp;

public class FillUpViewFragment extends Fragment
{
	private CarDatabaseHelper carDatabaseHelper = null;
	private FillUpDatabaseHelper fillUpDatabaseHelper = null;

	ListView fillUpListView = null;
	List<FillUp> fillUps = new ArrayList<FillUp>();
	FillUpArrayAdapter fillUpArrayAdapter = null;
	Button newFillUp = null;
	View rootView = null;
	
	public FillUpViewFragment() {}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		rootView = inflater.inflate(R.layout.fillup_view_fragment, container, false);
		fillUpListView = (ListView) rootView.findViewById(R.id.fillup_view_fragment_list);
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
		
		fillUpListView.setLongClickable(true);
		fillUpListView.setOnItemLongClickListener(new OnItemLongClickListener()
		{

			@Override
			public boolean onItemLongClick(AdapterView<?> adapterView, View view, int pos, long arg3)
			{
				rootView.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
				final FillUp selectedFillUp = (FillUp) adapterView.getAdapter().getItem(pos);
				final int positionInList = pos;
				
				final CharSequence[] items = {"Edit", "Delete", "Cancel"};
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				builder.setTitle("Select an Option!");
				
				builder.setItems(items, new DialogInterface.OnClickListener() {
				    public void onClick(DialogInterface dialog, int item) {
				    	switch (item) {
							case 0:
								Intent intent = new Intent(getActivity(), ShowFillUpDialog.class);
								intent.putExtra("newFillUp", false);
								intent.putExtra("fillUp", selectedFillUp);
								startActivityForResult(intent, 1);
								break;
							case 1:
								try {
									fillUpDatabaseHelper.getFillUpDao().delete(selectedFillUp);
									fillUps.remove(positionInList);
									refreshFillUpsList();
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

			TextView date = (TextView) rowView.findViewById(R.id.fillup_view_frament_list_view_date);
			TextView fillUpInfo = (TextView) rowView.findViewById(R.id.fillup_view_frament_list_view_purchase_info);
			TextView mpg = (TextView) rowView.findViewById(R.id.fillup_view_frament_list_view_mpg);

			FillUp temp = values[position];
			date.setText(new SimpleDateFormat("MM/dd/yy", Locale.US).format(temp.getDate()));
			fillUpInfo.setText("Gallons: " + temp.getGas() + " Distance: " + temp.getDistance());
			mpg.setText("MPG: " + temp.getMPG()  + " Cost: " + (values[position].getPrice() * temp.getGas()));

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

	public void refreshFillUpsList()
	{
		try {
			fillUps = fillUpDatabaseHelper.getFillUpDao().queryForAll();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		if(fillUps.isEmpty()) {
			return;
		}
		FillUp[] temp = new FillUp[fillUps.size()];
		fillUps.toArray(temp);

		fillUpArrayAdapter = new FillUpArrayAdapter(getActivity().getBaseContext(), temp);
		fillUpListView.setAdapter(fillUpArrayAdapter);
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
	public void onDestroy() {
		super.onDestroy();

		if (carDatabaseHelper != null) {
			carDatabaseHelper.close();
			carDatabaseHelper = null;
		}
		if (fillUpDatabaseHelper != null) {
			fillUpDatabaseHelper.close();
			fillUpDatabaseHelper = null;
		}
	}

}

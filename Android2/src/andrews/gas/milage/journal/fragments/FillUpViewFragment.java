package andrews.gas.milage.journal.fragments;

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
import android.util.Log;
import android.view.Display;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
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

import andrews.gas.milage.journal.MainActivity;
import andrews.gas.milage.journal.R;
import andrews.gas.milage.journal.database.CarDatabaseHelper;
import andrews.gas.milage.journal.database.FillUpDatabaseHelper;
import andrews.gas.milage.journal.dialogs.ShowFillUpDialog;
import andrews.gas.milage.journal.entities.Car;
import andrews.gas.milage.journal.entities.FillUp;


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

		AdView adView = (AdView) rootView.findViewById(R.id.adView);
		AdRequest adRequest = new AdRequest.Builder().addTestDevice("89C5255F42662D2FCFD03698061CF86D").build();
		adView.loadAd(adRequest);

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

				final CharSequence[] items = { "Edit", "Delete", "Cancel" };
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				builder.setTitle("Select an Option!");

				builder.setItems(items, new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface dialog, int item)
					{
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
			TextView carName = (TextView) rowView.findViewById(R.id.fillup_view_frament_list_view_car_name);
			TextView mpg = (TextView) rowView.findViewById(R.id.fillup_view_frament_list_view_mpg);
			TextView cost = (TextView) rowView.findViewById(R.id.fillup_view_frament_list_view_cost);
			TextView distance = (TextView) rowView.findViewById(R.id.fillup_view_frament_list_view_purchase_distance);
			TextView units = (TextView) rowView.findViewById(R.id.fillup_view_frament_list_view_car_units);

			SharedPreferences sharedPref = getActivity().getApplicationContext().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
			String measurementSystem = sharedPref.getString(MainActivity.MEASUREMENT_KEY, "");
			String currencySymbol = sharedPref.getString(MainActivity.CURRENCY_KEY, "");

			if ( position % 2 == 0 ) {
				rowView.setBackgroundColor(Color.BLACK);
			} else {
				rowView.setBackgroundColor(Color.DKGRAY);
			}
			date.setTextColor(Color.WHITE);
			carName.setTextColor(Color.WHITE);
			mpg.setTextColor(Color.WHITE);
			cost.setTextColor(Color.WHITE);
			distance.setTextColor(Color.WHITE);

			FillUp temp = values[position];
			Car car = null;
			try {
				car = carDatabaseHelper.getCarDao().queryForId(temp.getCarId());
				if ( car != null ) {
					carName.setText(car.getName());
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}

			if ( measurementSystem.equals("US") ) {
				units.setText("MPG");
				distance.setText(round(temp.getDistance()) + "mi");
			} else {
				units.setText("Km\\L");
				distance.setText(round(temp.getDistance()) + "km");
			}
			
			date.setText(new SimpleDateFormat(sharedPref.getString(MainActivity.DATE_FORMAT_KEY, ""), Locale.US).format(temp.getDate()));
			mpg.setText("" + round(temp.getMPG()));
			cost.setText(currencySymbol + round(temp.getPrice() * temp.getGas()));

			WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
			Display display = wm.getDefaultDisplay();
			int width = display.getWidth();  // deprecated
			
			carName.setMaxWidth((int)(width * .52));
			carName.setMaxLines(1);
			
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
		
		FillUp[] temp = new FillUp[fillUps.size()];
		fillUps.toArray(temp);

		fillUpListView.invalidate();
		fillUpArrayAdapter = new FillUpArrayAdapter(getActivity().getBaseContext(), temp);
		fillUpListView.setAdapter(fillUpArrayAdapter);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if ( requestCode == android.app.Activity.RESULT_OK ) {

		}
		refreshFillUpsList();
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

	@Override
	public void onPause()
	{
		super.onPause();
	}

	@Override
	public void onResume()
	{
		super.onResume();
		refreshFillUpsList();
	}

	private double round(double x)
	{
		return ( (int) ( x * 100 ) ) / 100.0;
	}

}

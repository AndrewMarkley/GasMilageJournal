package com.orangegames.gasmilagejournal.dialogs;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.orangegames.gasmilagejournal.R;
import com.orangegames.gasmilagejournal.car.Car;
import com.orangegames.gasmilagejournal.database.CarDatabaseHelper;
import com.orangegames.gasmilagejournal.database.FillUpDatabaseHelper;
import com.orangegames.gasmilagejournal.fillup.FillUp;

public class ShowFillUpDialog extends Activity
{
	Intent in;
	boolean success = false;
	boolean newFillUp = true;
	TextView milesTraveled, gallonsPurchased, pricePerGallon, comments, title;
	Spinner carList;
	Button save, date;
	FillUp fillUp;

	private CarDatabaseHelper carDatabaseHelper = null;
	private FillUpDatabaseHelper fillUpDatabaseHelper = null;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detailed_fillup_form);
		in = getIntent();
		newFillUp = in.getBooleanExtra("newFillUp", newFillUp);

		// initialize all the fields
		title = (TextView) findViewById(R.id.detailed_fillup_form_title);
		date = (Button) findViewById(R.id.detailed_fillup_form_date_button);
		milesTraveled = (TextView) findViewById(R.id.detailed_fillup_form_miles_traveled);
		gallonsPurchased = (TextView) findViewById(R.id.detailed_fillup_form_gallons_purchased);
		pricePerGallon = (TextView) findViewById(R.id.detailed_fillup_form_price_of_gas);
		comments = (TextView) findViewById(R.id.detailed_fillup_form_comments);
		carList = (Spinner) findViewById(R.id.detailed_fillup_form_car);
		save = (Button) findViewById(R.id.detailed_fillup_form_save_button);

		date.setText(new SimpleDateFormat("MM/dd/yyyy", Locale.US).format(Calendar.getInstance().getTime()));

		List<Car> cars = new ArrayList<Car>();
		try {
			cars = getCarDatabaseHelper().getCarDao().queryForAll();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		Car[] carArray = new Car[cars.size()];
		cars.toArray(carArray);

		CarArrayAdapter carArrayAdapter = new CarArrayAdapter(this, carArray);

		carArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		carList.setAdapter(carArrayAdapter);

		// Adjust the title based on the action being done
		if ( newFillUp ) {
			title.setText("Add a Fill Up!");
		} else {
			title.setText("Modify a Fill Up!");
			save.setText("Save Car");
			fillUp = (FillUp) in.getSerializableExtra("fillUp");
			comments.setText(fillUp.getComments());
			pricePerGallon.setText("" + fillUp.getPrice());
			gallonsPurchased.setText("" + fillUp.getGas());
			milesTraveled.setText("" + fillUp.getDistance());
			date.setText(new SimpleDateFormat("MM/dd/yyyy", Locale.US).format(fillUp.getDate()));
			
		}
		
		Calendar c = Calendar.getInstance();
		int mYear = c.get(Calendar.YEAR);
		int mMonth = c.get(Calendar.MONTH);
		int mDay = c.get(Calendar.DAY_OF_MONTH);
		final DatePickerDialog dialog = new DatePickerDialog(this, mDateSetListener, mYear, mMonth, mDay);
		
		date.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				dialog.show();
			}
		});

		save.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				try {
					int carId = ( (Car) carList.getSelectedItem() ).getId();
					Double distance = Double.parseDouble(milesTraveled.getText().toString());
					Double price = Double.parseDouble(pricePerGallon.getText().toString());
					Double gallons = Double.parseDouble(gallonsPurchased.getText().toString());
					Date time = new SimpleDateFormat("MM/dd/yyyy", Locale.US).parse(date.getText().toString());

					if ( newFillUp ) {
						fillUp = new FillUp(carId, distance, gallons, price, time, comments.getText().toString());
						getFillUpDatabaseHelper().getFillUpDao().create(fillUp);
					} else {
						int fId = fillUp.getId();
						fillUp = new FillUp(carId, distance, gallons, price, time, comments.getText().toString());
						fillUp.setId(fId);
						getFillUpDatabaseHelper().getFillUpDao().update(fillUp);
					}

					Intent i = getIntent();
					i.putExtra("success", success);
					setResult(RESULT_OK, i);
					finish();

				} catch (Exception e) {
					e.printStackTrace();
					CharSequence text = "Please fill in all blank spaces!";
					Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG);
					toast.show();
				}
			}
		});

	}

	@Override
	public void onBackPressed()
	{
		Intent returnIntent = new Intent();
		returnIntent.putExtra("success", false);
		setResult(RESULT_OK, returnIntent);
		finish();
	}

	private CarDatabaseHelper getCarDatabaseHelper()
	{
		if ( carDatabaseHelper == null ) {
			this.carDatabaseHelper = CarDatabaseHelper.getHelper(getApplicationContext());
		}
		return carDatabaseHelper;
	}

	private FillUpDatabaseHelper getFillUpDatabaseHelper()
	{
		if ( fillUpDatabaseHelper == null ) {
			this.fillUpDatabaseHelper = FillUpDatabaseHelper.getHelper(getApplicationContext());
		}
		return fillUpDatabaseHelper;
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

			if ( values[position] == null ) {
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

	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener()
	{
		@Override
		public void onDateSet(DatePicker view, int year, int month, int day)
		{
			date.setText(month+1 + "/" + day + "/" + year);
		}
	};
}

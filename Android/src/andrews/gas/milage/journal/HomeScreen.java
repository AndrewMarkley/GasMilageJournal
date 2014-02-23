package andrews.gas.milage.journal;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.widget.DatePicker;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class HomeScreen extends Activity
{
	ArrayList<FillUp> fillUps = new ArrayList<FillUp>();
	FillUp x;
	Button clear, calculate, b4, b5, b6, save, date_picker, car = null;
	TextView milesPerGallon, gallonsPurchased, milesTraveled, pricePerGallon,
			totalCost, dateInput, dateString, comments = null;
	double gas, distance, cost, mpg, pricePG = 0;
	Date date;
	int mYear, mMonth, mDay;
	String Data = "";
	CarDatabaseHandler carDatabaseHandler = null;
	ArrayList<Car> cars;
	String[] carNames = null;
	Context ct = this;
	Intent intent = null;
	Boolean successfullyAddedFillUp = false;
	String[] values = new String[7];
	FillUp temp;
	int counter = 0;

	static final int DATE_DIALOG_ID = 1;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home_screen);
		intent = this.getIntent();

		// set buttons
		calculate = (Button) findViewById(R.id.calculate_button);
		save = (Button) findViewById(R.id.save_fill_up_button);
		clear = (Button) findViewById(R.id.clear_screen_button);
		clear.setText("cancel");
		date_picker = (Button) findViewById(R.id.pick_date_button);
		car = (Button) findViewById(R.id.choose_car_button);

		// set TextViews
		gallonsPurchased = (TextView) findViewById(R.id.gallons_purchased_input_field);
		milesPerGallon = (TextView) findViewById(R.id.miles_per_gallon_output);
		comments = (TextView) findViewById(R.id.comments_input_field);
		milesTraveled = (TextView) findViewById(R.id.miles_traveled_input_field);
		pricePerGallon = (TextView) findViewById(R.id.price_per_gallon_input_field);
		totalCost = (TextView) findViewById(R.id.total_cost_output);
		dateString = (TextView) findViewById(R.id.date_display);

		// Set text box values
		milesPerGallon.setText("");
		totalCost.setText("");

		successfullyAddedFillUp = intent.getBooleanExtra("edit",
				successfullyAddedFillUp);

		if ( successfullyAddedFillUp ) {
			values = intent.getStringArrayExtra("values");
			car.setText(values[0]);
			milesTraveled.setText(values[1]);
			gallonsPurchased.setText(values[2]);
			pricePerGallon.setText(values[3]);
			totalCost.setText(values[4]);
			milesPerGallon.setText(values[5]);
			dateString.setText(values[6]);

			temp = new FillUp(values[0], Double.parseDouble(values[1]),
					Double.parseDouble(values[2]),
					Double.parseDouble(values[3]),
					Double.parseDouble(values[4]),
					Double.parseDouble(values[5]), values[6], "");

			if ( values.length < 7 )
				comments.setText("");

		}

		try {

			carDatabaseHandler = new CarDatabaseHandler(ct);
			cars = (ArrayList<Car>) carDatabaseHandler.getAllCars();
			ArrayList<Car> listOfCars = new ArrayList<Car>();
			
			for ( int ctr = 0; ctr < cars.size(); ctr++ ) {
				boolean maybe = true;
				for ( int c = 0; c < listOfCars.size(); c++ ) {
					if ( listOfCars.get(c).getName().equals(cars.get(ctr).getName()) ) {
						maybe = false;
					}
				}
				if ( maybe ) {
					listOfCars.add(cars.get(ctr));
				}
			}
			
			carNames = new String[listOfCars.size()];
			for ( int ctr = 0; ctr < listOfCars.size(); ctr++ ) {
				carNames[ctr] = listOfCars.get(ctr).getName();
			}
			car.setText(carNames[0]);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// clear button listener, will clear all inputs if selected
		clear.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				finish();
			}
		});

		// calculate button listener
		// when clicked will calculate MPG and total cost, then display it to
		// the user
		calculate.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				try {
					// get user input
					distance = Double.parseDouble(milesTraveled.getText()
							.toString());
					gas = Double.parseDouble(gallonsPurchased.getText()
							.toString());
					pricePG = Double.parseDouble(pricePerGallon.getText()
							.toString());
					// calculate and round cost and mpg
					cost = round(gas * pricePG, 2);
					mpg = round(distance / gas, 3);
					// update text views with answers
					milesPerGallon.setText("" + mpg);
					totalCost.setText("" + cost);
				} catch (Exception e) {
					// if they try to calculate without filling in all info a
					// toast will popup and ask for
					// all fields to be filled
					CharSequence text = "Please fill in all blank spaces!";
					Toast toast = Toast.makeText(getApplicationContext(), text,
							Toast.LENGTH_SHORT);
					toast.show();
				}
			}
		});
		// save button listener
		// when clicker it will save entry into the sql database
		save.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// System.out.println("distance: " + distance +" gas: " +gas
				// +" mpg: "+mpg);
				try {
					// ensure all calculations were performed
					calculate.performClick();
					// get user input
					distance = Double.parseDouble(milesTraveled.getText()
							.toString());
					gas = Double.parseDouble(gallonsPurchased.getText()
							.toString());
					pricePG = Double.parseDouble(pricePerGallon.getText()
							.toString());
					String comms = "";
					comms += comments.getText().toString();
					// round cost and mpg to appropriate sig figs
					cost = round(gas * pricePG, 2);
					mpg = round(distance / gas, 3);
					// make sure rounded answes are reflected
					milesPerGallon.setText("" + mpg);
					totalCost.setText("" + cost);
					// create new FillUp object based on user input
					System.out.println("" + car.getText());

					x = new FillUp("" + car.getText(), distance, gas, pricePG,
							cost, mpg, dateString.getText().toString().trim(),
							comms);
					// add it to class arraylist
					fillUps.add(x);

					// create new instance of sql database to add fillup
					DatabaseHandler db = new DatabaseHandler(
							getApplicationContext());
					// add the fillup to database
					int val = - 1;
					val = intent.getIntExtra("originalPos", val);
					if ( successfullyAddedFillUp ) {
						db.deleteFillUp(temp, val);
						db.addFillUp(x);
					} else
						db.addFillUp(x);
					// set up intente result return
					Intent i = getIntent();
					// return 1 for success!
					i.putExtra("1", x.toString());
					setResult(RESULT_OK);
					finish();
				} catch (Exception e) {
					// makes sure user filled in all user input areas
					e.printStackTrace();
					CharSequence text = "Please fill in all blank spaces!";
					Toast toast = Toast.makeText(getApplicationContext(), text,
							Toast.LENGTH_LONG);
					toast.show();
				}
			}
		});
		car.setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				AlertDialog.Builder builder = new AlertDialog.Builder(ct);
				builder.setTitle("Choose which car");
				builder.setItems(carNames,
						new DialogInterface.OnClickListener()
						{
							@Override
							public void onClick(DialogInterface dialog, int p)
							{
								car.setText("" + carNames[p]);
							}
						});
				builder.create();
				builder.show();
			}
		});
		// date_picker listener launches new date pciker dialog if selected
		date_picker.setOnClickListener(new View.OnClickListener()
		{
			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View v)
			{
				showDialog(DATE_DIALOG_ID);
			}
		});

		final Calendar c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);
		updateDisplay();
	}

	@Override
	protected Dialog onCreateDialog(int id)
	{
		switch (id) {

			case DATE_DIALOG_ID:
				return new DatePickerDialog(this, mDateSetListener, mYear,
						mMonth, mDay);

		}
		return null;
	}

	@Override
	protected void onPrepareDialog(int id, Dialog dialog)
	{
		switch (id) {

			case DATE_DIALOG_ID:
				( (DatePickerDialog) dialog ).updateDate(mYear, mMonth, mDay);
				break;
		}
	}

	private void updateDisplay()
	{
		if ( ! successfullyAddedFillUp || counter > 0 )
			dateString.setText(new StringBuilder().append(mMonth + 1)
					.append("-").append(mDay).append("-").append(mYear)
					.append(" "));
		else {
			String x = values[6];
			String datee[] = new String[4];
			datee[0] = x.substring(0, x.indexOf("-"));
			datee[1] = x.substring(x.indexOf("-") + 1, x.lastIndexOf("-"));
			datee[2] = x.substring(x.lastIndexOf("-") + 1);
			dateString.setText(new StringBuilder().append(datee[0]).append("-")
					.append(datee[1]).append("-").append(datee[2]).append(" "));
		}
	}

	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener()
	{

		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth)
		{
			mYear = year;
			mMonth = monthOfYear;
			mDay = dayOfMonth;
			updateDisplay();
		}
	};

	// round value to x places right of the decimal
	public double round(double value, int places)
	{
		if ( places < 0 )
			return value;
		long factor = (long) Math.pow(10, places);
		value = value * factor;
		long tmp = Math.round(value);
		return (double) tmp / factor;
	}

	// this is necessary to set what the user inputs as the fields text so it
	// can be retrieved
	public void onClick(View v)
	{
		if ( v.getId() == gallonsPurchased.getId()
				&& gallonsPurchased.getText().toString().length() > 0 )
			gallonsPurchased.setText(gallonsPurchased.getText().toString());

		if ( v.getId() == milesTraveled.getId()
				&& milesTraveled.getText().toString().length() > 0 )
			milesTraveled.setText(milesTraveled.getText().toString());

		if ( v.getId() == pricePerGallon.getId()
				&& pricePerGallon.getText().toString().length() > 0 )
			pricePerGallon.setText(pricePerGallon.getText().toString());

		if ( v.getId() == clear.getId() ) {
			gallonsPurchased.setText("");
			milesPerGallon.setText("");
			milesTraveled.setText("");
			pricePerGallon.setText("");
			totalCost.setText("");
		}
		if ( v.getId() == date_picker.getId() ) {
			dateString.setText(dateString.getText().toString());
		}
	}

}

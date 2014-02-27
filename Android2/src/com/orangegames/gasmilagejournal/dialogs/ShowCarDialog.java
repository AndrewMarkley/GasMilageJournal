package com.orangegames.gasmilagejournal.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.orangegames.gasmilagejournal.R;
import com.orangegames.gasmilagejournal.car.Car;
import com.orangegames.gasmilagejournal.database.CarDatabaseHelper;

public class ShowCarDialog extends Activity
{
	Intent in;
	boolean newCar = true;
	boolean success = false;
	TextView nickname, make, model, odometer, title;
	Spinner year;
	Button save;
	Car car;

	private CarDatabaseHelper carDatabaseHelper = null;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detailed_car_form);
		in = getIntent();
		newCar = in.getBooleanExtra("new", newCar);

		// initialize all the fields
		title = (TextView) findViewById(R.id.detailed_car_form_title);
		nickname = (TextView) findViewById(R.id.detailed_car_form_nickname);
		make = (TextView) findViewById(R.id.detailed_car_form_make);
		model = (TextView) findViewById(R.id.detailed_car_form_model);
		odometer = (TextView) findViewById(R.id.detailed_car_form_odometer);
		year = (Spinner) findViewById(R.id.detailed_car_form_year);
		save = (Button) findViewById(R.id.detailed_car_form_save_button);

		String[] yearSpan = new String[74];

		for ( int ctr = 1960; ctr < 2014; ctr++ ) {
			yearSpan[ctr - 1960] = ""+ctr;
		}

		ArrayAdapter<String> aa = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, yearSpan);

		aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		year.setAdapter(aa);

		// Adjust the title based on the action being done
		if ( newCar ) {
			title.setText("Add a New Car!");
		} else {
			title.setText("Modify your Car!");
			save.setText("Save Car");
			car = (Car) in.getSerializableExtra("car");
		}

		save.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				try {
					CharSequence var = (String) year.getSelectedItem();
					int year = ( Integer.parseInt(var.toString()) );

					var = odometer.getText();
					double odometer = ( Double.parseDouble(var.toString()) );
					
					if ( newCar ) {
						car = new Car(nickname.getText().toString(), year, make.getText().toString(), model.getText().toString(), odometer);
						getCarDatabaseHelper().getCarDao().create(car);
					} else {
						car = new Car(nickname.getText().toString(), year, make.getText().toString(), model.getText().toString(), odometer);
						getCarDatabaseHelper().getCarDao().update(car);
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

	// public void onClick(View v)
	// {
	// if ( v.getId() == nickname.getId() &&
	// nickname.getText().toString().length() > 0 ) {
	// nickname.setText(nickname.getText().toString());
	// }
	//
	// if ( v.getId() == make.getId() && make.getText().toString().length() > 0
	// ) {
	// make.setText(make.getText().toString());
	// }
	//
	// if ( v.getId() == model.getId() && model.getText().toString().length() >
	// 0 ) {
	// model.setText(model.getText().toString());
	// }
	//
	// if ( v.getId() == odometer.getId() &&
	// odometer.getText().toString().length() > 0 ) {
	// System.out.println("odometer before: " + odometer.getText());
	// odometer.setText(odometer.getText().toString());
	// System.out.println("odometer after: " + odometer.getText());
	// }
	//
	// if ( v.getId() == year.getId() && year.getText().toString().length() > 0
	// )
	// year.setText(year.getText().toString());
	// }

	public void noCarExistsAlert()
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this.getApplicationContext());
		builder.setTitle("Enter a Car");
		builder.setMessage("You must have atleast 1 car profile before continuing.\n");
		builder.setPositiveButton("Ok", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int id)
			{
				// Action for 'Ok' Button
			}
		});
		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int id)
			{
				dialog.cancel();
			}
		});
		builder.create();
		builder.show();
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
			carDatabaseHelper = carDatabaseHelper.getHelper(this);
		}
		return carDatabaseHelper;
	}
}

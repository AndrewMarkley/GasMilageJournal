package andrews.gas.milage.journal;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class newCarProfile extends Activity
{
	TextView name, make, model, odometer, year;
	Button save, cancel, plus, minus;
	CarDatabaseHandler db = null;
	int mYear = 0;
	Context ct = this;
	boolean newCar = false;
	boolean editCar = false;
	String[] values = new String[5];
	Car temp = null;
	Intent in;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_car_form);
		in = getIntent();
		newCar = in.getBooleanExtra("new", newCar);
		editCar = in.getBooleanExtra("edit", editCar);

		if ( newCar ) {
			show();
			in.putExtra("new", false);
		}

		name = (TextView) findViewById(R.id.car_nick_name_input);
		make = (TextView) findViewById(R.id.make_input);
		model = (TextView) findViewById(R.id.model_input);
		odometer = (TextView) findViewById(R.id.odometer_input);
		year = (TextView) findViewById(R.id.year_input);

		save = (Button) findViewById(R.id.save);
		cancel = (Button) findViewById(R.id.cancel);
		plus = (Button) findViewById(R.id.plus);
		minus = (Button) findViewById(R.id.minus);

		db = new CarDatabaseHandler(this);

		year.setText("" + Calendar.getInstance().get(Calendar.YEAR));

		if ( editCar ) {
			save.setText("Save Car");
			values = in.getStringArrayExtra("values");
			name.setText(values[0]);
			year.setText(values[1]);
			make.setText(values[2]);
			model.setText(values[3]);
			odometer.setText(values[4]);
			temp = new Car(values[0], Integer.parseInt(values[1]), values[2], values[3], Double.parseDouble(values[4]));
		}

		cancel.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent i = getIntent();
				// return 0 for failure!
				if ( newCar ) {
					Intent returnIntent = new Intent();
					returnIntent.putExtra("added", false);
					setResult(RESULT_OK, returnIntent);
				}
				finish();
			}
		});
		save.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Car c = null;
				try {
					CharSequence var = year.getText();
					int year = ( Integer.parseInt(var.toString()) );

					var = odometer.getText();
					double odometer = ( Double.parseDouble(var.toString()) );

					c = new Car(name.getText().toString(), year, make.getText().toString(), model.getText().toString(), odometer);

					if ( ! editCar ) {
						db.addCar(c);
					} else {
						int val = in.getIntExtra("pos", 0);
						db.deleteCar(temp, val);
						db.addCar(c);
					}

					Intent i = getIntent();
					i.putExtra("change", c.toOutputFileString().split(" , "));
					i.putExtra("ogName", temp.getName());
					setResult(10);
					finish();

				} catch (Exception e) {

					e.printStackTrace();
					CharSequence text = "Please fill in all blank spaces!";
					Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG);
					toast.show();

				}
			}
		});
		plus.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				year.setText("" + ( Integer.parseInt(year.getText().toString()) + 1 ));
			}
		});
		minus.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				year.setText("" + ( Integer.parseInt(year.getText().toString()) - 1 ));
			}
		});

	}

	public void onClick(View v)
	{
		if ( v.getId() == name.getId() && name.getText().toString().length() > 0 )
			name.setText(name.getText().toString());

		if ( v.getId() == make.getId() && make.getText().toString().length() > 0 )
			make.setText(make.getText().toString());

		if ( v.getId() == model.getId() && model.getText().toString().length() > 0 )
			model.setText(model.getText().toString());

		if ( v.getId() == odometer.getId() && odometer.getText().toString().length() > 0 ) {
			System.out.println("odometer before: " + odometer.getText());
			odometer.setText(odometer.getText().toString());
			System.out.println("odometer after: " + odometer.getText());
		}

		if ( v.getId() == year.getId() && year.getText().toString().length() > 0 )
			year.setText(year.getText().toString());
	}

	public void show()
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(ct);
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
		if ( ! newCar )
			finish();
	}
}

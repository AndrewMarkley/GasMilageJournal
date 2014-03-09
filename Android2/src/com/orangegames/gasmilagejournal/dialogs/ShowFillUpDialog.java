package com.orangegames.gasmilagejournal.dialogs;

import java.io.ByteArrayOutputStream;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.internal.di;
import com.orangegames.gasmilagejournal.MainActivity;
import com.orangegames.gasmilagejournal.R;
import com.orangegames.gasmilagejournal.database.CarDatabaseHelper;
import com.orangegames.gasmilagejournal.database.FillUpDatabaseHelper;
import com.orangegames.gasmilagejournal.entities.Car;
import com.orangegames.gasmilagejournal.entities.FillUp;

public class ShowFillUpDialog extends Activity
{
	public static final int CAMERA_REQUEST = 1888;
	Intent in;
	boolean success = false;
	boolean newFillUp = true;
	TextView milesTraveled, gallonsPurchased, pricePerGallon, comments, title;
	Spinner carList;
	Button saveButton, dateButton, receiptButton;
	FillUp fillUp;
	ImageView receiptView;
	byte[] receiptImage = null;

	private CarDatabaseHelper carDatabaseHelper = null;
	private FillUpDatabaseHelper fillUpDatabaseHelper = null;
	private boolean useOdometerReading = false;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detailed_fillup_form);
		in = getIntent();
		newFillUp = in.getBooleanExtra("newFillUp", newFillUp);
		SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);

		// initialize all the fields
		title = (TextView) findViewById(R.id.detailed_fillup_form_title);
		dateButton = (Button) findViewById(R.id.detailed_fillup_form_date_button);
		milesTraveled = (TextView) findViewById(R.id.detailed_fillup_form_miles_traveled);
		gallonsPurchased = (TextView) findViewById(R.id.detailed_fillup_form_gallons_purchased);
		pricePerGallon = (TextView) findViewById(R.id.detailed_fillup_form_price_of_gas);
		comments = (TextView) findViewById(R.id.detailed_fillup_form_comments);
		carList = (Spinner) findViewById(R.id.detailed_fillup_form_car);
		saveButton = (Button) findViewById(R.id.detailed_fillup_form_save_button);
		receiptButton = (Button) findViewById(R.id.detailed_fill_up_receipt_button);
		receiptView = (ImageView) findViewById(R.id.detailed_fill_up_receipt_view);
		dateButton.setText(new SimpleDateFormat("MM/dd/yyyy", Locale.US).format(Calendar.getInstance().getTime()));

		if ( sharedPref.getString(MainActivity.FILLUP_READING_KEY, "trip").equals("trip") ) {
			milesTraveled.setHint("Miles Traveled");
			useOdometerReading = false;
		} else {
			milesTraveled.setHint("Odometer Reading");
			useOdometerReading = newFillUp ? true: false;
		}

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

		if ( newFillUp ) {
			title.setText("Add a Fill Up!");
		} else {
			title.setText("Modify a Fill Up!");
			saveButton.setText("Save Fill Up");
			fillUp = (FillUp) in.getSerializableExtra("fillUp");
			comments.setText(fillUp.getComments());
			pricePerGallon.setText("" + fillUp.getPrice());
			gallonsPurchased.setText("" + fillUp.getGas());
			milesTraveled.setText("" + fillUp.getDistance());
			dateButton.setText(new SimpleDateFormat("MM/dd/yyyy", Locale.US).format(fillUp.getDate()));

			if ( fillUp.getReceipt() != null ) {
				Bitmap photo = BitmapFactory.decodeByteArray(fillUp.getReceipt(), 0, fillUp.getReceipt().length);
				receiptView.setImageBitmap(photo);
			}
		}

		Calendar c = Calendar.getInstance();
		int mYear = c.get(Calendar.YEAR);
		int mMonth = c.get(Calendar.MONTH);
		int mDay = c.get(Calendar.DAY_OF_MONTH);
		final DatePickerDialog dialog = new DatePickerDialog(this, mDateSetListener, mYear, mMonth, mDay);

		receiptView.setOnLongClickListener(new OnLongClickListener()
		{

			@Override
			public boolean onLongClick(View v)
			{
				Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
				vibrator.vibrate(100);
				final CharSequence[] items = { "Delete Image", "Retake Image", "Cancel" };
				AlertDialog.Builder builder = new AlertDialog.Builder(ShowFillUpDialog.this);
				builder.setTitle("Select an Option!");

				builder.setItems(items, new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface dialog, int item)
					{
						switch (item) {
							case 0:
								receiptImage = null;
								receiptView.invalidate();
								receiptView.setImageBitmap(null);
								receiptView.setVisibility(View.GONE);
								break;
							case 1:
								receiptButton.performClick();
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

		receiptButton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(cameraIntent, CAMERA_REQUEST);
			}
		});

		dateButton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				dialog.show();
			}
		});

		saveButton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				try {
					Car car = ( (Car) carList.getSelectedItem() );
					int carId = car.getId();
					Double distance = Double.parseDouble(milesTraveled.getText().toString());
					Double price = Double.parseDouble(pricePerGallon.getText().toString());
					Double gallons = Double.parseDouble(gallonsPurchased.getText().toString());
					Date time = new SimpleDateFormat("MM/dd/yyyy", Locale.US).parse(dateButton.getText().toString());
					
					if(useOdometerReading) {
						distance -= car.getMilage();
					}

					if ( newFillUp ) {
						fillUp = new FillUp(carId, distance, gallons, price, time, comments.getText().toString(), receiptImage);
						getFillUpDatabaseHelper().getFillUpDao().create(fillUp);
						car.setMilage(car.getMilage() + fillUp.getDistance());
						getCarDatabaseHelper().getCarDao().update(car);
					} else {
						int fId = fillUp.getId();
						fillUp = new FillUp(carId, distance, gallons, price, time, comments.getText().toString(), receiptImage);
						fillUp.setId(fId);
						getFillUpDatabaseHelper().getFillUpDao().update(fillUp);
						car.setMilage(car.getMilage() + fillUp.getDistance());
						getCarDatabaseHelper().getCarDao().update(car);
					}

				} catch (Exception e) {
					e.printStackTrace();
					CharSequence text = "Please fill in all blank spaces!";
					Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG);
					toast.show();
				}
				
				AlertDialog.Builder builder = new AlertDialog.Builder(ShowFillUpDialog.this);
				builder.setMessage("Your MPG was " + round(fillUp.getMPG()) + "!").setPositiveButton("Ok", new OnClickListener()
				{
					
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						Intent i = getIntent();
						i.putExtra("success", success);
						setResult(RESULT_OK, i);
						finish();
					}
				});
				builder.create().show();
			}
		});

	}

	@Override
	public void onBackPressed()
	{
		Intent returnIntent = new Intent();
		returnIntent.putExtra("success", false);
		setResult(RESULT_CANCELED, returnIntent);
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
			View rowView = inflater.inflate(R.layout.spinner_view, parent, false);

			TextView title = (TextView) rowView.findViewById(R.id.spinner_title);
			title.setText(values[position].getName());

			return rowView;
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if ( resultCode == RESULT_OK ) {

			if ( requestCode == CAMERA_REQUEST ) {
				Bitmap photo = (Bitmap) data.getExtras().get("data");

				ByteArrayOutputStream bytes = new ByteArrayOutputStream();
				photo.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
				receiptImage = bytes.toByteArray();
				receiptView.setImageBitmap(photo);
				receiptView.setVisibility(View.VISIBLE);
			}

		}

		if ( resultCode == Activity.RESULT_CANCELED ) {

		}
	}

	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener()
	{
		@Override
		public void onDateSet(DatePicker view, int year, int month, int day)
		{
			dateButton.setText(month + 1 + "/" + day + "/" + year);
		}
	};
	
	public double round(double x)
	{
		return ((int)(x * 100))/100.0;
	}
}

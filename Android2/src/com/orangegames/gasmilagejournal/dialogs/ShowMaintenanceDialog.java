package com.orangegames.gasmilagejournal.dialogs;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
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
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.orangegames.gasmilagejournal.R;
import com.orangegames.gasmilagejournal.database.CarDatabaseHelper;
import com.orangegames.gasmilagejournal.database.MaintenanceLogDatabaseHelper;
import com.orangegames.gasmilagejournal.entities.Car;
import com.orangegames.gasmilagejournal.entities.FillUp;
import com.orangegames.gasmilagejournal.entities.MaintenanceLog;

public class ShowMaintenanceDialog extends Activity
{
	public static final int CAMERA_REQUEST = 1888;
	
	private boolean newLog = true;
	private MaintenanceLog log = null;
	private TextView title, description, cost, odometer, pageTitle, location = null;
	private Spinner carList;
	private Button dateButton, receiptButton, saveButton;
	private ImageView receiptView;
	private byte[] receiptImage = null;
	private CarDatabaseHelper carDatabaseHelper = null;
	private MaintenanceLogDatabaseHelper maintenanceLogDatabaseHelper = null;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detailed_maintenance_form);
		newLog = getIntent().getBooleanExtra("new", true);
		log = (MaintenanceLog) getIntent().getSerializableExtra("log");

		title = (TextView) findViewById(R.id.detailed_maintenance_log_form_title);
		description = (TextView) findViewById(R.id.detailed_maintenance_log_form_description);
		cost = (TextView) findViewById(R.id.detailed_maintenance_log_form_cost);
		odometer = (TextView) findViewById(R.id.detailed_maintenance_log_form_odometer);
		pageTitle = (TextView) findViewById(R.id.detailed_maintenance_log_form_page_title);
		location = (TextView) findViewById(R.id.detailed_maintenance_log_form_location);

		dateButton = (Button) findViewById(R.id.detailed_maintenance_log_form_date_button);
		receiptButton = (Button) findViewById(R.id.detailed_maintenance_log_receipt_button);
		saveButton = (Button) findViewById(R.id.detailed_maintenance_log_form_save_button);

		carList = (Spinner) findViewById(R.id.detailed_maintenance_log_form_car);
		receiptView = (ImageView) findViewById(R.id.detailed_maintenance_log_receipt_view);

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

		if ( newLog ) {
			pageTitle.setText("Add a Maintenance Event!");
		} else {
			title.setText("Modify a Maintenance Event!");
			saveButton.setText("Save Maintenance Event");
			
			title.setText(log.getTitle());
			description.setText(log.getDescription());
			odometer.setText("" + log.getOdometer());
			cost.setText("" +log.getCost());
			dateButton.setText(new SimpleDateFormat("MM/dd/yyyy", Locale.US).format(log.getDate()));
			location.setText(log.getLocation());

			if ( log.getReceipt() != null ) {
				Bitmap photo = BitmapFactory.decodeByteArray(log.getReceipt(), 0, log.getReceipt().length);
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
				AlertDialog.Builder builder = new AlertDialog.Builder(ShowMaintenanceDialog.this);
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
					int carId = ( (Car) carList.getSelectedItem() ).getId();
					Double odom = Double.parseDouble(odometer.getText().toString());
					Double price = Double.parseDouble(cost.getText().toString());
					String tit = title.getText().toString();
					String desc = description.getText().toString();
					Date time = new SimpleDateFormat("MM/dd/yyyy", Locale.US).parse(dateButton.getText().toString());
					String loc = location.getText().toString();

					if ( newLog ) {
						MaintenanceLog mlog = new MaintenanceLog(carId, time, price, odom, tit, desc, loc, receiptImage);
						getMaintenanceLogDatabaseHelper().getMaintenanceLogDao().create(mlog);
					} else {
						int lId = log.getId();
						MaintenanceLog mlog = new MaintenanceLog(carId, time, price, odom, tit, desc, loc, receiptImage);
						log.setId(lId);
						getMaintenanceLogDatabaseHelper().getMaintenanceLogDao().update(mlog);
					}

					Intent i = getIntent();
					i.putExtra("success", true);
					setResult(RESULT_OK, i);
					finish();

				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch(Exception e) {
					CharSequence text = "Please fill in all blank spaces!";
					Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG);
					toast.show();
				}
			}
		});
	}
	
	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener()
	{
		@Override
		public void onDateSet(DatePicker view, int year, int month, int day)
		{
			dateButton.setText(month + 1 + "/" + day + "/" + year);
		}
	};

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
	
	private MaintenanceLogDatabaseHelper getMaintenanceLogDatabaseHelper()
	{
		if ( maintenanceLogDatabaseHelper == null ) {
			this.maintenanceLogDatabaseHelper = MaintenanceLogDatabaseHelper.getHelper(getApplicationContext());
		}
		return maintenanceLogDatabaseHelper;
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

}

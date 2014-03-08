package com.orangegames.gasmilagejournal.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewDebug.FlagToString;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.internal.em;
import com.orangegames.gasmilagejournal.MainActivity;
import com.orangegames.gasmilagejournal.R;

public class SettingsActivity extends Activity
{
	ListViewArrayAdapter listViewArrayAdapter = null;
	private String[] titles = { "Measurement System", "Date Format", "Currency", "Contact Dev" };
	private String[] descriptions = { "Choose from English, Imperial, or Metric units", "Change the date format used", "Change the currency used", "Questions or comments? email us!" };

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.options_settings);
		ListView settingsList = (ListView) findViewById(R.id.options_settings_list);

		listViewArrayAdapter = new ListViewArrayAdapter(getApplicationContext(), titles, descriptions);
		settingsList.setAdapter(listViewArrayAdapter);
		settingsList.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int pos, long arg3)
			{
				Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
				v.vibrate(100);

				Context context = getApplicationContext();
				SharedPreferences sharedPref = context.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
				final SharedPreferences.Editor editor = sharedPref.edit();
				AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
				AlertDialog alert = null;

				switch (pos) {
					case 0: // Measurement System
						final int selectedPosition = ( sharedPref.getString(MainActivity.MEASUREMENT_KEY, "").equals("US") ? 0 : 1 );
						Log.i("info", sharedPref.getString(MainActivity.MEASUREMENT_KEY, ""));
						final CharSequence[] items = { "US (Miles, Gallons)", "Metric (Kilometer, Litre)" };

						builder.setTitle("Select an Option!");

						builder.setSingleChoiceItems(items, selectedPosition, new DialogInterface.OnClickListener()
						{
							public void onClick(DialogInterface dialog, int item)
							{
								switch (item) {
									case 0:
										editor.putString(MainActivity.MEASUREMENT_KEY, "US");
										editor.commit();
										break;
									case 1:
										editor.putString(MainActivity.MEASUREMENT_KEY, "metric");
										editor.commit();
										break;
									default:
								}
								dialog.dismiss();
							}
						});
						alert = builder.create();
						alert.show();
						break;
					case 1: // Date Format
						final int sp;
						if ( sharedPref.getString(MainActivity.DATE_FORMAT_KEY, "").equals("MM/dd/yy") ) {
							sp = 0;
						} else if ( sharedPref.getString(MainActivity.DATE_FORMAT_KEY, "").equals("dd/MM/yyyy") ) {
							sp = 1;
						} else if ( sharedPref.getString(MainActivity.DATE_FORMAT_KEY, "").equals("MM/dd") ) {
							sp = 2;
						} else {
							sp = 0;
						}
						final String[] i = { "MM/dd/yyyy", "dd/MM/yyyy", "MM/dd" };

						builder.setTitle("Select an Option!");

						builder.setSingleChoiceItems(i, sp, new DialogInterface.OnClickListener()
						{
							public void onClick(DialogInterface dialog, int item)
							{
								switch (item) {
									case 0:
										editor.putString(MainActivity.DATE_FORMAT_KEY, "MM/dd/yyyy");
										editor.commit();
										break;
									case 1:
										editor.putString(MainActivity.DATE_FORMAT_KEY, "dd/MM/yyyy");
										editor.commit();
										break;
									case 2:
										editor.putString(MainActivity.DATE_FORMAT_KEY, "MM/dd");
										editor.commit();
										break;
									default:
								}
								dialog.dismiss();
							}
						});
						alert = builder.create();
						alert.show();
						break;
					case 2: // Currency

						final int selectedPos;
						if ( sharedPref.getString(MainActivity.CURRENCY_KEY, "").equals("$") ) {
							selectedPos = 0;
						} else if ( sharedPref.getString(MainActivity.CURRENCY_KEY, "").equals("£") ) {
							selectedPos = 1;
						} else if ( sharedPref.getString(MainActivity.CURRENCY_KEY, "").equals("€") ) {
							selectedPos = 2;
						} else {
							selectedPos = 0;
						}
						final CharSequence[] item = { "$", "£", "€" };

						builder.setTitle("Select an Option!");

						builder.setSingleChoiceItems(item, selectedPos, new DialogInterface.OnClickListener()
						{
							public void onClick(DialogInterface dialog, int item)
							{
								switch (item) {
									case 0:
										editor.putString(MainActivity.CURRENCY_KEY, "$");
										editor.commit();
										break;
									case 1:
										editor.putString(MainActivity.CURRENCY_KEY, "£");
										editor.commit();
										break;
									case 2:
										editor.putString(MainActivity.CURRENCY_KEY, "€");
										editor.commit();
										break;
									default:
								}
								dialog.dismiss();
							}
						});
						alert = builder.create();
						alert.show();
						break;
					case 3: // Contact Dev
						final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);

						emailIntent.setType("plain/text");
						emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[] { "ajm501028@gmail.com" });
						emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Gas Milage Journal Feedback");
						emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "");
						emailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

						startActivity(Intent.createChooser(emailIntent, ""));
						break;
					default:
						break;
				}
			}
		});
	}

	class ListViewArrayAdapter extends ArrayAdapter<String>
	{
		private final Context context;
		private final String[] titles;
		private final String[] descriptions;

		public ListViewArrayAdapter(Context context, String[] titles, String[] descriptions) {
			super(context, R.layout.options_settings_list_view, titles);
			this.context = context;
			this.titles = titles;
			this.descriptions = descriptions;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View rowView = inflater.inflate(R.layout.options_settings_list_view, parent, false);

			TextView title = (TextView) rowView.findViewById(R.id.options_settings_list_view_title);
			TextView description = (TextView) rowView.findViewById(R.id.options_settings_list_view_description);

			title.setText(titles[position]);
			description.setText(descriptions[position]);

			Log.i("info", "addind settings title " + title.getText());

			return rowView;
		}
	}

}

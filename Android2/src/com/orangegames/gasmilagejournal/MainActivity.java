package com.orangegames.gasmilagejournal;

import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import com.orangegames.gasmilagejournal.activities.AboutActivity;
import com.orangegames.gasmilagejournal.activities.ExportActivity;
import com.orangegames.gasmilagejournal.activities.SettingsActivity;
import com.orangegames.gasmilagejournal.fragments.CarsViewFragment;
import com.orangegames.gasmilagejournal.fragments.FillUpViewFragment;
import com.orangegames.gasmilagejournal.fragments.StatisticsViewFragment2;

public class MainActivity extends FragmentActivity
{

	SectionsPagerAdapter mSectionsPagerAdapter;
	ViewPager mViewPager;
	
	public static final String MEASUREMENT_KEY = "measurement";
	public static final String DATE_FORMAT_KEY = "date_format";
	public static final String CURRENCY_KEY = "currency";

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		mViewPager.setCurrentItem(1, false);
		
		//check shared preference keys
		Context context = getApplicationContext();
		SharedPreferences sharedPref = context.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		
		if(!sharedPref.contains(MEASUREMENT_KEY)) {
			editor.putString(MEASUREMENT_KEY, "US");
		}
		if(!sharedPref.contains(DATE_FORMAT_KEY)) {
			editor.putString(DATE_FORMAT_KEY, "mm/dd/yyyy");
		}
		if(!sharedPref.contains(CURRENCY_KEY)) {
			editor.putString(CURRENCY_KEY, "$");
		}
		
		editor.commit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		v.vibrate(100);
	    switch (item.getItemId()) {
	    case R.id.action_export:
	    	Intent intent = new Intent(this, ExportActivity.class);
            startActivity(intent);
	        return true;
	    case R.id.action_about:
	    	intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
	        return true;
	    case R.id.action_settings:
	    	intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
	        return true;
	    default:
	        return super.onOptionsItemSelected(item);
	    }
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter
	{

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position)
		{
			Fragment fragment = null;
			Bundle args = new Bundle();
			args.putInt("section_number", position + 1);
			switch (position) {
				case 0:
					fragment = new StatisticsViewFragment2();
					break;
				case 1:
					fragment = new FillUpViewFragment();
					break;
				case 2:
					fragment = new CarsViewFragment();
					break;
				default:
					fragment = new Fragment();
					break;
			}

			fragment.setArguments(args);
			return fragment;
		}

		@Override
		public int getCount()
		{
			return 3;
		}

		@Override
		public CharSequence getPageTitle(int position)
		{
			Locale l = Locale.getDefault();
			switch (position) {
				case 0:
					return getString(R.string.title_section1).toUpperCase(l);
				case 1:
					return getString(R.string.title_section2).toUpperCase(l);
				case 2:
					return getString(R.string.title_section3).toUpperCase(l);
			}
			return null;
		}
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
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
	}
}

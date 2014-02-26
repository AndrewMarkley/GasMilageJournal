package com.orangegames.gasmilagejournal;

import java.util.Locale;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;

import com.orangegames.gasmilagejournal.database.CarDatabaseHelper;
import com.orangegames.gasmilagejournal.database.FillUpDatabaseHelper;
import com.orangegames.gasmilagejournal.fragments.CarsViewFragment;
import com.orangegames.gasmilagejournal.fragments.FillUpViewFragment;
import com.orangegames.gasmilagejournal.fragments.StatisticsViewFragment;

public class MainActivity extends FragmentActivity
{

	SectionsPagerAdapter mSectionsPagerAdapter;
	ViewPager mViewPager;
	
	private CarDatabaseHelper carDatabaseHelper = null;
	private FillUpDatabaseHelper fillUpDatabaseHelper = null;


	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
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
			Fragment fragment = new FillUpViewFragment();
			Bundle args = new Bundle();
			args.putInt("section_number", position + 1);
			switch (position) {
				case 1:
					fragment = new FillUpViewFragment();
					break;
				case 2:
					fragment = new CarsViewFragment();
					break;
				case 3:
					fragment = new StatisticsViewFragment();
					break;
				default:
					break;
			}
			
			fragment.setArguments(args);
			return fragment;
		}

		@Override
		public int getCount()
		{
			// Show 3 total pages.
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
	protected void onDestroy() {
		super.onDestroy();

		/*
		 * You'll need this in your class to release the helper when done.
		 */
		if (carDatabaseHelper != null) {
			carDatabaseHelper.close();
			carDatabaseHelper = null;
		}
		if (fillUpDatabaseHelper != null) {
			fillUpDatabaseHelper.close();
			fillUpDatabaseHelper = null;
		}
	}
	
	/**
	 * You'll need this in your class to get the helper from the manager once per class.
	 */
	private CarDatabaseHelper getCarDatabaseHelper() {
		if (carDatabaseHelper == null) {
			carDatabaseHelper = carDatabaseHelper.getHelper(this);
		}
		return carDatabaseHelper;
	}

	/**
	 * You'll need this in your class to get the helper from the manager once per class.
	 */
	private FillUpDatabaseHelper getFillUpDatabaseHelper() {
		if (fillUpDatabaseHelper == null) {
			fillUpDatabaseHelper = fillUpDatabaseHelper.getHelper(this);
		}
		return fillUpDatabaseHelper;
	}

//	/**
//	 * Do our sample database stuff as an example.
//	 */
//	private void doSampleDatabaseStuff(String action, TextView tv) {
//		try {
//			// our string builder for building the content-view
//			StringBuilder sb = new StringBuilder();
//			doSimpleDatabaseStuff(action, sb);
//			sb.append("------------------------------------------\n");
//			doComplexDatabaseStuff(action, sb);
//			tv.setText(sb.toString());
//			Log.i(LOG_TAG, "Done with page at " + System.currentTimeMillis());
//		} catch (SQLException e) {
//			Log.e(LOG_TAG, "Database exception", e);
//			tv.setText("Database exeption: " + e);
//			return;
//		}
//	}

}

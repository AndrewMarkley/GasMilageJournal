package com.orangegames.gasmilagejournal.fragments;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.orangegames.gasmilagejournal.R;
import com.orangegames.gasmilagejournal.car.Car;
import com.orangegames.gasmilagejournal.database.CarDatabaseHelper;
import com.orangegames.gasmilagejournal.database.FillUpDatabaseHelper;
import com.orangegames.gasmilagejournal.fillup.FillUp;

public class StatisticsViewFragment2 extends Fragment
{
	public StatisticsViewFragment2() {}

	private CarDatabaseHelper carDatabaseHelper = null;
	private FillUpDatabaseHelper fillUpDatabaseHelper = null;
	private Spinner carList = null;
	LinearLayout chartView = null;
	GraphicalView gview = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View rootView = inflater.inflate(R.layout.statistics_view_fragment2, container, false);
		carList = (Spinner) rootView.findViewById(R.id.statistics_view_fragment_car_spinner);
		chartView = (LinearLayout) rootView.findViewById(R.id.chart_container);
		
		List<Car> cars = new ArrayList<Car>();
		try {
			cars = getCarDatabaseHelper().getCarDao().queryForAll();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		Car[] carArray = new Car[cars.size()];
		cars.toArray(carArray);

		CarArrayAdapter carArrayAdapter = new CarArrayAdapter(getActivity(), carArray);

		carArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		carList.setAdapter(carArrayAdapter);

		chartView.addView(getGraphicalView());

		carList.setOnItemSelectedListener(new OnItemSelectedListener()
		{

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3)
			{
				chartView.removeAllViews();
				chartView.addView(getGraphicalView());
				Log.i("info", "Repainting");
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0)
			{
				// TODO Auto-generated method stub

			}

		});

		return rootView;
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

	private XYMultipleSeriesDataset getDemoDataset()
	{
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		TimeSeries series = new TimeSeries("Date");

		if ( carList.getSelectedItem() != null ) {
			Car car = (Car) carList.getSelectedItem();
			List<FillUp> fillUps = null;

			try {
				QueryBuilder<FillUp, Integer> queryBuilder = getFillUpDatabaseHelper().getFillUpDao().queryBuilder();
				queryBuilder.where().eq(FillUp.COLUMN_CAR_ID, car.getId());
				Log.i("info", "querying for car id: " + car.getId());
				PreparedQuery<FillUp> preparedQuery = queryBuilder.prepare();
				fillUps = getFillUpDatabaseHelper().getFillUpDao().query(preparedQuery);
			} catch (SQLException e) {
				e.printStackTrace();
			}

			int x = 0;
			for ( FillUp fu : fillUps ) {
				series.add(x++, fu.getMPG());
				Log.i("info", "adding point: (" + x + ", " + fu.getMPG() + ")");
			}
		}

		dataset.addSeries(series);
		return dataset;
	}

	@SuppressWarnings("deprecation")
	private GraphicalView getGraphicalView()
	{
		XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
		renderer.setAxisTitleTextSize(35);
		renderer.setChartTitleTextSize(50);
		renderer.setLabelsTextSize(35);
		renderer.setPointSize(5f);
		renderer.setZoomEnabled(false, false);
		// top, left, bottom, right
		renderer.setMargins(new int[] { 80, 80, 20, 0 });
		XYSeriesRenderer r = new XYSeriesRenderer();
		r.setColor(Color.YELLOW);
		r.setPointStyle(PointStyle.DIAMOND);
		r.setFillBelowLine(false);
		r.setFillPoints(true);
		renderer.addSeriesRenderer(r);

		XYMultipleSeriesDataset ds = getDemoDataset();
		setChartSettings(renderer, ds.getSeriesAt(0).getMaxX(), ds.getSeriesAt(0).getMaxY(), "Date Filled Up", "MPG");
		gview = ChartFactory.getLineChartView(getActivity(), ds, renderer);
		return gview;
	}

	private void setChartSettings(XYMultipleSeriesRenderer renderer, double xRange, double yRange, String xLabel, String yLabel)
	{
		renderer.setChartTitle("Chart demo");
		renderer.setXTitle(xLabel);
		renderer.setYTitle(yLabel);
		renderer.setApplyBackgroundColor(false);
		renderer.setRange(new double[] { 0, 6, 0, 40 });
		renderer.setFitLegend(false);
		renderer.setPanEnabled(false, false);
		renderer.setZoomEnabled(false, false);
		renderer.setAxesColor(Color.RED);
		renderer.setShowGrid(true);
		renderer.setZoomEnabled(false);
		renderer.setInScroll(true);

		renderer.setXAxisMin(0);
		renderer.setXAxisMax(xRange + 0.5);
		renderer.setYAxisMin(0);
		renderer.setYAxisMax(yRange + 0.5);
	}

	public void onResume()
	{
		super.onResume();
		gview.repaint();
	}

	private CarDatabaseHelper getCarDatabaseHelper()
	{
		if ( carDatabaseHelper == null ) {
			this.carDatabaseHelper = CarDatabaseHelper.getHelper(getActivity());
		}
		return carDatabaseHelper;
	}

	private FillUpDatabaseHelper getFillUpDatabaseHelper()
	{
		if ( fillUpDatabaseHelper == null ) {
			this.fillUpDatabaseHelper = FillUpDatabaseHelper.getHelper(getActivity());
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

			chartView.invalidate();
			chartView.addView(getGraphicalView());
			gview.repaint();
			Log.i("info", "Invalidating and repainting");

			return rowView;
		}
	}

}

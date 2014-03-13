package com.orangegames.gasmilagejournal.fragments;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.orangegames.gasmilagejournal.R;
import com.orangegames.gasmilagejournal.database.CarDatabaseHelper;
import com.orangegames.gasmilagejournal.database.FillUpDatabaseHelper;
import com.orangegames.gasmilagejournal.entities.Car;
import com.orangegames.gasmilagejournal.entities.FillUp;

public class StatisticsViewFragment extends Fragment
{
	public StatisticsViewFragment() {}

	private CarDatabaseHelper carDatabaseHelper = null;
	private FillUpDatabaseHelper fillUpDatabaseHelper = null;
	private Spinner carList = null;

	LinearLayout lMPG = null;
	LinearLayout lPPG = null;
	LinearLayout lMFC = null;
	LinearLayout lMM = null;

	GraphicalView gviewMPG = null;
	GraphicalView gviewPricePerGallon = null;
	GraphicalView gviewMonthlyFuelCosts = null;
	GraphicalView gviewMonthlyMilage = null;

	private View rootView = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		rootView = inflater.inflate(R.layout.statistics_view_fragment, container, false);

		carList = (Spinner) rootView.findViewById(R.id.statistics_view_fragment_car_spinner);
		lMPG = (LinearLayout) rootView.findViewById(R.id.statistics_view_fragment_mpg_graph);
		lPPG = (LinearLayout) rootView.findViewById(R.id.statistics_view_fragment_ppg_graph);
		lMFC = (LinearLayout) rootView.findViewById(R.id.statistics_view_fragment_mfc_graph);
		lMM = (LinearLayout) rootView.findViewById(R.id.statistics_view_fragment_mm_graph);

		gviewMPG = getMPGGraph();
		gviewPricePerGallon = getPPGGraph();
		gviewMonthlyFuelCosts = getMFCGraph();
		gviewMonthlyMilage = getMMGraph();

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

		repaintGraphs();
		addAllGraphViews();

		carList.setOnItemSelectedListener(new OnItemSelectedListener()
		{

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3)
			{
				removeAllGraphViews();
				addAllGraphViews();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0)
			{}

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

	public void onResume()
	{
		super.onResume();
		repaintGraphs();
		addAllGraphViews();
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

			repaintGraphs();

			return rowView;
		}
	}

	public void addAllGraphViews()
	{
		removeAllGraphViews();

		List<FillUp> fillups = null;
		try {
			fillups = getFillUpDatabaseHelper().getFillUpDao().queryForAll();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		View container = View.inflate(getActivity().getApplicationContext(), R.layout.statistics_view_high_container, (ViewGroup) rootView.findViewById(R.id.high));
		View container2 = View.inflate(getActivity().getApplicationContext(), R.layout.statistics_view_high_container, (ViewGroup) rootView.findViewById(R.id.high));
		View container3 = View.inflate(getActivity().getApplicationContext(), R.layout.statistics_view_high_container, (ViewGroup) rootView.findViewById(R.id.high));
		View container4 = View.inflate(getActivity().getApplicationContext(), R.layout.statistics_view_high_container, (ViewGroup) rootView.findViewById(R.id.high));
		View container5 = View.inflate(getActivity().getApplicationContext(), R.layout.statistics_view_high_container, (ViewGroup) rootView.findViewById(R.id.high));

		TextView h = (TextView) container.findViewById(R.id.statistics_view_high_text);
		TextView l = (TextView) container.findViewById(R.id.statistics_view_low_text);

		h.setText("Best: " + getBestMPGRecord(fillups));
		l.setText("Worst: " + getWorstMPGRecord(fillups));
		lMPG.addView(container);

		h = (TextView) container2.findViewById(R.id.statistics_view_high_text);
		l = (TextView) container2.findViewById(R.id.statistics_view_low_text);

		h.setText("Best: " + getWorstPPGRecord(fillups));
		l.setText("Worst: " + getBestPPGRecord(fillups));
		lPPG.addView(container2);

		h = (TextView) container3.findViewById(R.id.statistics_view_high_text);
		l = (TextView) container3.findViewById(R.id.statistics_view_low_text);

		h.setText("Best: " + getBestMFCRecord(fillups));
		l.setText("Worst: " + getWorstMFCRecord(fillups));
		lMFC.addView(container3);

		h = (TextView) container5.findViewById(R.id.statistics_view_high_text);
		l = (TextView) container5.findViewById(R.id.statistics_view_low_text);

		h.setText("Best: " + getBestMMRecord(fillups));
		l.setText("Worst: " + getWorstMMRecord(fillups));
		lMM.addView(container5);

		WindowManager wm = (WindowManager) getActivity().getBaseContext().getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		int yPx = (int) ( display.getHeight() * 0.7 );
		lMPG.addView(getMPGGraph(), new LayoutParams(LayoutParams.MATCH_PARENT, yPx));
		lPPG.addView(getPPGGraph(), new LayoutParams(LayoutParams.MATCH_PARENT, yPx));
		lMFC.addView(getMFCGraph(), new LayoutParams(LayoutParams.MATCH_PARENT, yPx));
		lMM.addView(getMMGraph(), new LayoutParams(LayoutParams.MATCH_PARENT, yPx));

	}

	public void removeAllGraphViews()
	{
		lMPG.removeAllViews();
		lPPG.removeAllViews();
		lMFC.removeAllViews();
		lMM.removeAllViews();
	}

	public void repaintGraphs()
	{
		gviewMPG.repaint();
		gviewPricePerGallon.repaint();
		gviewMonthlyFuelCosts.repaint();
		gviewMonthlyMilage.repaint();

	}

	public GraphicalView getMPGGraph()
	{
		GraphicalView gview = null;

		// Create the dataset
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		TimeSeries series = new TimeSeries("Date");
		ArrayList<DateValue> values = new ArrayList<StatisticsViewFragment.DateValue>();
		if ( carList.getSelectedItem() != null ) {
			Car car = (Car) carList.getSelectedItem();
			List<FillUp> fillUps = null;
			try {
				QueryBuilder<FillUp, Integer> queryBuilder = getFillUpDatabaseHelper().getFillUpDao().queryBuilder();
				queryBuilder.where().eq(FillUp.COLUMN_CAR_ID, car.getId());
				PreparedQuery<FillUp> preparedQuery = queryBuilder.prepare();
				fillUps = getFillUpDatabaseHelper().getFillUpDao().query(preparedQuery);

				for ( FillUp fu : fillUps ) {
					values.add(new DateValue(fu.getDate(), fu.getMPG()));
				}

				Collections.sort(values);

				for ( DateValue result : values ) {
					series.add(result.getDate(), result.getValue());
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}

		}

		dataset.addSeries(series);

		// Setup the renderers
		XYSeriesRenderer r = new XYSeriesRenderer();
		r.setColor(Color.parseColor("#33b5e5"));
		r.setLineWidth(10);
		r.setPointStyle(PointStyle.DIAMOND);
		r.setFillBelowLine(false);
		r.setFillPoints(true);

		XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
		renderer.setAxisTitleTextSize(35);
		renderer.setChartTitleTextSize(60);
		renderer.setLabelsTextSize(35);
		renderer.setPointSize(5f);
		renderer.setZoomEnabled(false, false);
		// top, left, bottom, right
		renderer.setMargins(new int[] { 80, 80, 20, 0 });
		renderer.setChartTitle("MPG Over Time");
		renderer.setXTitle("Date");
		renderer.setYTitle("MPG");
		renderer.setApplyBackgroundColor(false);
		renderer.setFitLegend(false);
		renderer.setPanEnabled(false, false);
		renderer.setZoomEnabled(false, false);
		renderer.setAxesColor(Color.WHITE);
		renderer.setShowGrid(true);
		renderer.setZoomEnabled(false);
		renderer.setInScroll(true);
		renderer.addSeriesRenderer(r);
		renderer.setXLabels(0);
		renderer.setYAxisMin(dataset.getSeriesAt(0).getMinY() - 3);
		renderer.setYAxisMax(dataset.getSeriesAt(0).getMaxY() + 3);
		renderer.setXAxisMin(dataset.getSeriesAt(0).getMinX() - ( 172800000 ));
		renderer.setXAxisMax(dataset.getSeriesAt(0).getMaxX() + ( 172800000 ));

		for ( DateValue result : values ) {
			renderer.addXTextLabel(result.getDate().getTime(), new SimpleDateFormat("MMM-dd", Locale.US).format(result.getDate()));
		}

		renderer.setXLabelsAlign(Align.CENTER);
		renderer.setXLabelsPadding(10);

		gview = ChartFactory.getLineChartView(getActivity(), dataset, renderer);
		return gview;
	}

	public GraphicalView getPPGGraph()
	{
		GraphicalView gview = null;

		// Create the dataset
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		TimeSeries series = new TimeSeries("Date");
		ArrayList<DateValue> values = new ArrayList<StatisticsViewFragment.DateValue>();
		if ( carList.getSelectedItem() != null ) {
			Car car = (Car) carList.getSelectedItem();
			List<FillUp> fillUps = null;
			try {
				QueryBuilder<FillUp, Integer> queryBuilder = getFillUpDatabaseHelper().getFillUpDao().queryBuilder();
				queryBuilder.where().eq(FillUp.COLUMN_CAR_ID, car.getId());
				PreparedQuery<FillUp> preparedQuery = queryBuilder.prepare();
				fillUps = getFillUpDatabaseHelper().getFillUpDao().query(preparedQuery);

				for ( FillUp fu : fillUps ) {
					values.add(new DateValue(fu.getDate(), fu.getPrice()));
				}

				Collections.sort(values);

				for ( DateValue result : values ) {
					series.add(result.getDate(), result.getValue());
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}

		}

		dataset.addSeries(series);

		// Setup the renderers
		XYSeriesRenderer r = new XYSeriesRenderer();
		r.setColor(Color.parseColor("#33b5e5"));
		r.setLineWidth(10);
		r.setPointStyle(PointStyle.DIAMOND);
		r.setFillBelowLine(false);
		r.setFillPoints(true);

		XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
		renderer.setAxisTitleTextSize(35);
		renderer.setChartTitleTextSize(60);
		renderer.setLabelsTextSize(35);
		renderer.setPointSize(5f);
		renderer.setZoomEnabled(false, false);
		// top, left, bottom, right
		renderer.setMargins(new int[] { 80, 80, 20, 0 });
		renderer.setChartTitle("Price Per Gallon");
		renderer.setXTitle("Date");
		renderer.setYTitle("Price Per Gallon");
		renderer.setApplyBackgroundColor(false);
		renderer.setFitLegend(false);
		renderer.setPanEnabled(false, false);
		renderer.setZoomEnabled(false, false);
		renderer.setAxesColor(Color.WHITE);
		renderer.setShowGrid(true);
		renderer.setZoomEnabled(false);
		renderer.setInScroll(true);
		renderer.addSeriesRenderer(r);
		renderer.setXLabels(0);
		renderer.setYAxisMin(dataset.getSeriesAt(0).getMinY() - 3);
		renderer.setYAxisMax(dataset.getSeriesAt(0).getMaxY() + 3);
		renderer.setXAxisMin(dataset.getSeriesAt(0).getMinX() - ( 172800000 ));
		renderer.setXAxisMax(dataset.getSeriesAt(0).getMaxX() + ( 172800000 ));

		for ( DateValue result : values ) {
			renderer.addXTextLabel(result.getDate().getTime(), new SimpleDateFormat("MMM-dd", Locale.US).format(result.getDate()));
		}

		renderer.setXLabelsAlign(Align.CENTER);
		renderer.setXLabelsPadding(10);

		gview = ChartFactory.getLineChartView(getActivity(), dataset, renderer);
		return gview;
	}

	public GraphicalView getMFCGraph()
	{
		GraphicalView gview = null;

		// Create the dataset
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		TimeSeries series = new TimeSeries("Date");
		Map<Date, Double> map = new HashMap<Date, Double>();
		ArrayList<DateValue> values = new ArrayList<StatisticsViewFragment.DateValue>();
		if ( carList.getSelectedItem() != null ) {
			Car car = (Car) carList.getSelectedItem();
			List<FillUp> fillUps = null;
			try {
				QueryBuilder<FillUp, Integer> queryBuilder = getFillUpDatabaseHelper().getFillUpDao().queryBuilder();
				queryBuilder.where().eq(FillUp.COLUMN_CAR_ID, car.getId());
				PreparedQuery<FillUp> preparedQuery = queryBuilder.prepare();
				fillUps = getFillUpDatabaseHelper().getFillUpDao().query(preparedQuery);

				for ( FillUp fu : fillUps ) {
					Date date = new Date(fu.getDate().getYear(), fu.getDate().getMonth(), 1);
					if ( map.containsKey(date) ) {
						map.put(date, map.get(date) + ( fu.getGas() * fu.getPrice() ));
					} else {
						map.put(date, ( fu.getGas() * fu.getPrice() ));
					}
				}

				Iterator it = map.entrySet().iterator();
				while (it.hasNext()) {
					Map.Entry pairs = (Map.Entry) it.next();
					values.add(new DateValue((Date) pairs.getKey(), (Double) pairs.getValue()));
					it.remove();
				}

				Collections.sort(values);
				Log.i("map", "" + values.size());

				for ( DateValue result : values ) {
					Log.i("map", result.toString());
					series.add(result.getDate(), result.getValue());
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}

		}

		dataset.addSeries(series);

		// Setup the renderers
		XYSeriesRenderer r = new XYSeriesRenderer();
		r.setColor(Color.parseColor("#33b5e5"));
		r.setLineWidth(10);
		r.setPointStyle(PointStyle.DIAMOND);
		r.setFillBelowLine(false);
		r.setFillPoints(true);

		XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
		renderer.setAxisTitleTextSize(35);
		renderer.setChartTitleTextSize(60);
		renderer.setLabelsTextSize(35);
		renderer.setPointSize(5f);
		renderer.setZoomEnabled(false, false);
		// top, left, bottom, right
		renderer.setMargins(new int[] { 80, 80, 20, 0 });
		renderer.setChartTitle("Monthly Fuel Costs");
		renderer.setXTitle("Month");
		renderer.setYTitle("Cost");
		renderer.setApplyBackgroundColor(false);
		renderer.setFitLegend(false);
		renderer.setPanEnabled(false, false);
		renderer.setZoomEnabled(false, false);
		renderer.setAxesColor(Color.WHITE);
		renderer.setShowGrid(true);
		renderer.setZoomEnabled(false);
		renderer.setInScroll(true);
		renderer.addSeriesRenderer(r);
		renderer.setXLabels(0);
		renderer.setYAxisMin(dataset.getSeriesAt(0).getMinY() - 10);
		renderer.setYAxisMax(dataset.getSeriesAt(0).getMaxY() + 10);
		renderer.setXAxisMin(dataset.getSeriesAt(0).getMinX() - ( 1999000000 ));
		renderer.setXAxisMax(dataset.getSeriesAt(0).getMaxX() + ( 1999000000 ));

		for ( DateValue result : values ) {
			renderer.addXTextLabel(result.getDate().getTime(), new SimpleDateFormat("MMM-yy", Locale.US).format(result.getDate()));
		}

		renderer.setXLabelsAlign(Align.CENTER);
		renderer.setXLabelsPadding(10);

		gview = ChartFactory.getBarChartView(getActivity(), dataset, renderer, Type.DEFAULT);
		return gview;
	}

	public GraphicalView getMMGraph()
	{
		GraphicalView gview = null;

		// Create the dataset
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		TimeSeries series = new TimeSeries("Date");
		Map<Date, Double> map = new HashMap<Date, Double>();
		ArrayList<DateValue> values = new ArrayList<StatisticsViewFragment.DateValue>();
		if ( carList.getSelectedItem() != null ) {
			Car car = (Car) carList.getSelectedItem();
			List<FillUp> fillUps = null;
			try {
				QueryBuilder<FillUp, Integer> queryBuilder = getFillUpDatabaseHelper().getFillUpDao().queryBuilder();
				queryBuilder.where().eq(FillUp.COLUMN_CAR_ID, car.getId());
				PreparedQuery<FillUp> preparedQuery = queryBuilder.prepare();
				fillUps = getFillUpDatabaseHelper().getFillUpDao().query(preparedQuery);

				for ( FillUp fu : fillUps ) {
					Date date = new Date(fu.getDate().getYear(), fu.getDate().getMonth(), 1);
					if ( map.containsKey(date) ) {
						map.put(date, map.get(date) + fu.getDistance());
					} else {
						map.put(date, fu.getDistance());
					}
				}

				Iterator it = map.entrySet().iterator();
				while (it.hasNext()) {
					Map.Entry pairs = (Map.Entry) it.next();
					values.add(new DateValue((Date) pairs.getKey(), (Double) pairs.getValue()));
					it.remove();
				}

				Collections.sort(values);

				for ( DateValue result : values ) {
					series.add(result.getDate(), result.getValue());
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}

		}

		dataset.addSeries(series);

		// Setup the renderers
		XYSeriesRenderer r = new XYSeriesRenderer();
		r.setColor(Color.parseColor("#33b5e5"));
		r.setLineWidth(10);
		r.setPointStyle(PointStyle.DIAMOND);
		r.setFillBelowLine(false);
		r.setFillPoints(true);

		XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
		renderer.setAxisTitleTextSize(35);
		renderer.setChartTitleTextSize(60);
		renderer.setLabelsTextSize(35);
		renderer.setPointSize(5f);
		renderer.setZoomEnabled(false, false);
		// top, left, bottom, right
		renderer.setMargins(new int[] { 80, 80, 20, 0 });
		renderer.setChartTitle("Monthly Mileage");
		renderer.setXTitle("Month");
		renderer.setYTitle("Miles");
		renderer.setApplyBackgroundColor(false);
		renderer.setFitLegend(false);
		renderer.setPanEnabled(false, false);
		renderer.setZoomEnabled(false, false);
		renderer.setAxesColor(Color.WHITE);
		renderer.setShowGrid(true);
		renderer.setZoomEnabled(false);
		renderer.setInScroll(true);
		renderer.addSeriesRenderer(r);
		renderer.setXLabels(0);
		renderer.setYAxisMin(dataset.getSeriesAt(0).getMinY() - 10);
		renderer.setYAxisMax(dataset.getSeriesAt(0).getMaxY() + 10);
		renderer.setXAxisMin(dataset.getSeriesAt(0).getMinX() - ( 1999000000 ));
		renderer.setXAxisMax(dataset.getSeriesAt(0).getMaxX() + ( 1999000000 ));

		for ( DateValue result : values ) {
			renderer.addXTextLabel(result.getDate().getTime(), new SimpleDateFormat("MMM-yy", Locale.US).format(result.getDate()));
		}

		renderer.setXLabelsAlign(Align.CENTER);
		renderer.setXLabelsPadding(10);

		gview = ChartFactory.getBarChartView(getActivity(), dataset, renderer, Type.DEFAULT);
		return gview;
	}

	public double getBestMPGRecord(List<FillUp> fillups)
	{
		double best = 0.0;
		for ( FillUp fu : fillups ) {
			if ( best < fu.getMPG() ) {
				best = fu.getMPG();
			}
		}
		return round(best);
	}

	public double getWorstMPGRecord(List<FillUp> fillups)
	{
		double best = Double.MAX_VALUE;
		for ( FillUp fu : fillups ) {
			if ( best > fu.getMPG() ) {
				best = fu.getMPG();
			}
		}
		return round(best);
	}

	public double getBestPPGRecord(List<FillUp> fillups)
	{
		double best = 0.0;
		for ( FillUp fu : fillups ) {
			if ( best < fu.getPrice() ) {
				best = fu.getPrice();
			}
		}
		return round(best);
	}

	public double getWorstPPGRecord(List<FillUp> fillups)
	{
		double best = Double.MAX_VALUE;
		for ( FillUp fu : fillups ) {
			if ( best > fu.getPrice() ) {
				best = fu.getPrice();
			}
		}
		return round(best);
	}

	public double getBestMFCRecord(List<FillUp> fillups)
	{
		Double[] values = new Double[12];
		Arrays.fill(values, 0.0);

		for ( FillUp fu : fillups ) {
			values[fu.getDate().getMonth()] += fu.getPrice() * fu.getGas();
		}

		double max = values[0];

		for ( int i = 1; i < values.length; i++ ) {
			if ( values[i] > max ) {
				max = values[i];
			}
		}

		return round(max);
	}

	public double getWorstMFCRecord(List<FillUp> fillups)
	{
		Double[] values = new Double[12];
		Arrays.fill(values, 0.0);

		for ( FillUp fu : fillups ) {
			values[fu.getDate().getMonth()] += fu.getPrice() * fu.getGas();
		}

		double max = Double.MAX_VALUE;

		for ( int i = 1; i < values.length; i++ ) {
			if ( values[i] < max && values[i] != 0) {
				max = values[i];
			}
		}

		return max;
	}

	public double getBestMMRecord(List<FillUp> fillups)
	{
		Double[] values = new Double[12];
		Arrays.fill(values, 0.0);

		for ( FillUp fu : fillups ) {
			values[fu.getDate().getMonth()] += fu.getDistance();
		}

		double max = values[0];

		for ( int i = 1; i < values.length; i++ ) {
			if ( values[i] > max ) {
				max = values[i];
			}
		}

		return round(max);
	}

	public double getWorstMMRecord(List<FillUp> fillups)
	{
		Double[] values = new Double[12];
		Arrays.fill(values, 0.0);

		for ( FillUp fu : fillups ) {
			values[fu.getDate().getMonth()] += fu.getDistance();
		}

		double max = Double.MAX_VALUE;

		for ( int i = 1; i < values.length; i++ ) {
			if ( values[i] < max && values[i] != 0) {
				max = values[i];
			}
		}

		return round(max);
	}

	private double round(double x)
	{
		return ( (int) ( x * 100 ) ) / 100.0;
	}

	private class DateValue implements Comparable<DateValue>
	{
		private Date date;
		private double value;

		public DateValue(Date date, double value) {
			this.date = date;
			this.value = value;
		}

		public Date getDate()
		{
			return date;
		}

		public double getValue()
		{
			return value;
		}

		@Override
		public String toString()
		{
			return new SimpleDateFormat("MMM-yyyy", Locale.US).format(date) + " " + value;
		}

		@Override
		public int compareTo(DateValue dv)
		{
			if ( date.before(dv.getDate()) ) {
				return - 1;
			} else if ( date.equals(dv.getDate()) ) {
				return 0;
			} else {
				return 1;
			}
		}
	}
}

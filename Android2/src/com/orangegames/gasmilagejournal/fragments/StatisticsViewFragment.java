package com.orangegames.gasmilagejournal.fragments;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import android.widget.TableLayout;
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
	LinearLayout lCPM = null;
	LinearLayout lMM = null;

	GraphicalView gviewMPG = null;
	GraphicalView gviewPricePerGallon = null;
	GraphicalView gviewMonthlyFuelCosts = null;
	GraphicalView gviewCostPerMile = null;
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
		lCPM = (LinearLayout) rootView.findViewById(R.id.statistics_view_fragment_cpm_graph);
		lMM = (LinearLayout) rootView.findViewById(R.id.statistics_view_fragment_mm_graph);

		gviewMPG = getMPGGraph();
		gviewPricePerGallon = getPPGGraph();
		gviewMonthlyFuelCosts = getMFCGraph();
		gviewCostPerMile = getCPMGraph();
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
		
		h.setText("Best: " + getBestPPGRecord(fillups));
		l.setText("Worst: " + getWorstPPGRecord(fillups));
		lPPG.addView(container2);
		
		h = (TextView) container3.findViewById(R.id.statistics_view_high_text);
		l = (TextView) container3.findViewById(R.id.statistics_view_low_text);
		
		h.setText("Best: " + getBestMFCRecord(fillups));
		l.setText("Worst: " + getWorstMFCRecord(fillups));
		lMFC.addView(container3);
		
		h = (TextView) container4.findViewById(R.id.statistics_view_high_text);
		l = (TextView) container4.findViewById(R.id.statistics_view_low_text);
		
		h.setText("Best: " + getBestCPMRecord(fillups));
		l.setText("Worst: " + getWorstMFCRecord(fillups));
		lCPM.addView(container4);
		
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
		lCPM.addView(getCPMGraph(), new LayoutParams(LayoutParams.MATCH_PARENT, yPx));
		lMM.addView(getMMGraph(), new LayoutParams(LayoutParams.MATCH_PARENT, yPx));

	}

	public void removeAllGraphViews()
	{
		lMPG.removeAllViews();
		lPPG.removeAllViews();
		lMFC.removeAllViews();
		lCPM.removeAllViews();
		lMM.removeAllViews();
	}

	public void repaintGraphs()
	{
		gviewMPG.repaint();
		gviewPricePerGallon.repaint();
		gviewMonthlyFuelCosts.repaint();
		gviewCostPerMile.repaint();
		gviewMonthlyMilage.repaint();

	}

	// LineChart
	public GraphicalView getMPGGraph()
	{
		GraphicalView gview = null;

		// Create the dataset
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		TimeSeries series = new TimeSeries("Date");

		if ( carList.getSelectedItem() != null ) {
			Car car = (Car) carList.getSelectedItem();
			List<FillUp> fillUps = null;

			try {
				QueryBuilder<FillUp, Integer> queryBuilder = getFillUpDatabaseHelper().getFillUpDao().queryBuilder();
				queryBuilder.where().eq(FillUp.COLUMN_CAR_ID, car.getId());
				PreparedQuery<FillUp> preparedQuery = queryBuilder.prepare();
				fillUps = getFillUpDatabaseHelper().getFillUpDao().query(preparedQuery);
			} catch (SQLException e) {
				e.printStackTrace();
			}

			int x = 0;
			for ( FillUp fu : fillUps ) {
				series.add(x++, fu.getMPG());
			}
		}

		dataset.addSeries(series);

		// Setup the renderers
		XYSeriesRenderer r = new XYSeriesRenderer();
		r.setColor(Color.YELLOW);
		r.setPointStyle(PointStyle.DIAMOND);
		r.setFillBelowLine(false);
		r.setFillPoints(true);

		XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
		renderer.setAxisTitleTextSize(35);
		renderer.setChartTitleTextSize(50);
		renderer.setLabelsTextSize(35);
		renderer.setPointSize(5f);
		renderer.setZoomEnabled(false, false);
		// top, left, bottom, right
		renderer.setMargins(new int[] { 80, 80, 20, 0 });
		renderer.setChartTitle("MPG");
		renderer.setXTitle("Time");
		renderer.setYTitle("MPG");
		renderer.setApplyBackgroundColor(false);
		renderer.setRange(new double[] { 0, 6, 0, 40 });
		renderer.setFitLegend(false);
		renderer.setPanEnabled(false, false);
		renderer.setZoomEnabled(false, false);
		renderer.setAxesColor(Color.RED);
		renderer.setShowGrid(true);
		renderer.setZoomEnabled(false);
		renderer.setInScroll(true);
		renderer.addSeriesRenderer(r);
		renderer.setXAxisMin(0);
		renderer.setXAxisMax(dataset.getSeriesAt(0).getMaxX() + 0.5);
		renderer.setYAxisMin(0);
		renderer.setYAxisMax(dataset.getSeriesAt(0).getMaxY() + 0.5);

		gview = ChartFactory.getLineChartView(getActivity(), dataset, renderer);
		return gview;
	}

	// Scatter Plot
	public GraphicalView getPPGGraph()
	{
		GraphicalView gview = null;

		// Create the dataset
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		TimeSeries series = new TimeSeries("Date");

		if ( carList.getSelectedItem() != null ) {
			Car car = (Car) carList.getSelectedItem();
			List<FillUp> fillUps = null;

			try {
				QueryBuilder<FillUp, Integer> queryBuilder = getFillUpDatabaseHelper().getFillUpDao().queryBuilder();
				queryBuilder.where().eq(FillUp.COLUMN_CAR_ID, car.getId());

				PreparedQuery<FillUp> preparedQuery = queryBuilder.prepare();
				fillUps = getFillUpDatabaseHelper().getFillUpDao().query(preparedQuery);
			} catch (SQLException e) {
				e.printStackTrace();
			}

			for ( FillUp fu : fillUps ) {
				series.add(fu.getPrice(), fu.getGas());

			}
		}

		dataset.addSeries(series);

		// Setup the renderers
		XYSeriesRenderer r = new XYSeriesRenderer();
		r.setColor(Color.YELLOW);
		r.setPointStyle(PointStyle.DIAMOND);
		r.setFillBelowLine(false);
		r.setFillPoints(true);

		XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
		renderer.setAxisTitleTextSize(35);
		renderer.setChartTitleTextSize(50);
		renderer.setLabelsTextSize(35);
		renderer.setPointSize(5f);
		renderer.setZoomEnabled(false, false);
		// top, left, bottom, right
		renderer.setMargins(new int[] { 80, 80, 20, 0 });
		renderer.setChartTitle("Price Per Gallon");
		renderer.setXTitle("Time");
		renderer.setYTitle("Price");
		renderer.setApplyBackgroundColor(false);
		renderer.setRange(new double[] { 0, 6, 0, 40 });
		renderer.setFitLegend(false);
		renderer.setPanEnabled(false, false);
		renderer.setZoomEnabled(false, false);
		renderer.setAxesColor(Color.RED);
		renderer.setShowGrid(true);
		renderer.setZoomEnabled(false);
		renderer.setInScroll(true);
		renderer.addSeriesRenderer(r);
		renderer.setXAxisMin(0);
		renderer.setXAxisMax(dataset.getSeriesAt(0).getMaxX() + 0.5);
		renderer.setYAxisMin(0);
		renderer.setYAxisMax(dataset.getSeriesAt(0).getMaxY() + 0.5);

		gview = ChartFactory.getLineChartView(getActivity(), dataset, renderer);
		return gview;
	}

	// Bar Graph
	public GraphicalView getMFCGraph()
	{
		GraphicalView gview = null;

		// Create the dataset
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		TimeSeries series = new TimeSeries("Date");

		if ( carList.getSelectedItem() != null ) {
			Car car = (Car) carList.getSelectedItem();
			List<FillUp> fillUps = null;

			try {
				QueryBuilder<FillUp, Integer> queryBuilder = getFillUpDatabaseHelper().getFillUpDao().queryBuilder();
				queryBuilder.where().eq(FillUp.COLUMN_CAR_ID, car.getId());
				PreparedQuery<FillUp> preparedQuery = queryBuilder.prepare();
				fillUps = getFillUpDatabaseHelper().getFillUpDao().query(preparedQuery);
			} catch (SQLException e) {
				e.printStackTrace();
			}

			for ( FillUp fu : fillUps ) {
				double pos = Double.parseDouble(fu.getDate().getMonth() + "." + fu.getDate().getYear());
				series.add(pos, fu.getTotalCost());
			}
		}

		dataset.addSeries(series);

		// Setup the renderers
		XYSeriesRenderer r = new XYSeriesRenderer();
		r.setColor(Color.YELLOW);
		r.setPointStyle(PointStyle.DIAMOND);
		r.setFillBelowLine(false);
		r.setFillPoints(true);

		XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
		renderer.setAxisTitleTextSize(35);
		renderer.setChartTitleTextSize(50);
		renderer.setLabelsTextSize(35);
		renderer.setPointSize(5f);
		renderer.setZoomEnabled(false, false);
		// top, left, bottom, right
		renderer.setMargins(new int[] { 80, 80, 20, 0 });
		renderer.setChartTitle("Monthly Fuel Costs");
		renderer.setXTitle("Month");
		renderer.setYTitle("Cost");
		renderer.setApplyBackgroundColor(false);
		renderer.setRange(new double[] { 0, 6, 0, 40 });
		renderer.setFitLegend(false);
		renderer.setPanEnabled(false, false);
		renderer.setZoomEnabled(false, false);
		renderer.setAxesColor(Color.RED);
		renderer.setShowGrid(true);
		renderer.setZoomEnabled(false);
		renderer.setInScroll(true);
		renderer.addSeriesRenderer(r);
		renderer.setXAxisMin(0);
		renderer.setXAxisMax(dataset.getSeriesAt(0).getMaxX() + 0.5);
		renderer.setYAxisMin(0);
		renderer.setYAxisMax(dataset.getSeriesAt(0).getMaxY() + 0.5);

		gview = ChartFactory.getBarChartView(getActivity(), dataset, renderer, Type.DEFAULT);
		return gview;
	}

	// Scatter Plot
	public GraphicalView getCPMGraph()
	{
		GraphicalView gview = null;

		// Create the dataset
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		TimeSeries series = new TimeSeries("Date");

		if ( carList.getSelectedItem() != null ) {
			Car car = (Car) carList.getSelectedItem();
			List<FillUp> fillUps = null;

			try {
				QueryBuilder<FillUp, Integer> queryBuilder = getFillUpDatabaseHelper().getFillUpDao().queryBuilder();
				queryBuilder.where().eq(FillUp.COLUMN_CAR_ID, car.getId());

				PreparedQuery<FillUp> preparedQuery = queryBuilder.prepare();
				fillUps = getFillUpDatabaseHelper().getFillUpDao().query(preparedQuery);
			} catch (SQLException e) {
				e.printStackTrace();
			}

			for ( FillUp fu : fillUps ) {
				series.add(fu.getPrice(), fu.getMPG());

			}
		}

		dataset.addSeries(series);

		// Setup the renderers
		XYSeriesRenderer r = new XYSeriesRenderer();
		r.setColor(Color.YELLOW);
		r.setPointStyle(PointStyle.DIAMOND);
		r.setFillBelowLine(false);
		r.setFillPoints(true);

		XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
		renderer.setAxisTitleTextSize(35);
		renderer.setChartTitleTextSize(50);
		renderer.setLabelsTextSize(35);
		renderer.setPointSize(5f);
		renderer.setZoomEnabled(false, false);
		// top, left, bottom, right
		renderer.setMargins(new int[] { 80, 80, 20, 0 });
		renderer.setChartTitle("Cost Per Mile");
		renderer.setXTitle("Mile");
		renderer.setYTitle("Cost");
		renderer.setApplyBackgroundColor(false);
		renderer.setRange(new double[] { 0, 6, 0, 40 });
		renderer.setFitLegend(false);
		renderer.setPanEnabled(false, false);
		renderer.setZoomEnabled(false, false);
		renderer.setAxesColor(Color.RED);
		renderer.setShowGrid(true);
		renderer.setZoomEnabled(false);
		renderer.setInScroll(true);
		renderer.addSeriesRenderer(r);
		renderer.setXAxisMin(0);
		renderer.setXAxisMax(dataset.getSeriesAt(0).getMaxX() + 0.5);
		renderer.setYAxisMin(0);
		renderer.setYAxisMax(dataset.getSeriesAt(0).getMaxY() + 0.5);

		gview = ChartFactory.getScatterChartView(getActivity(), dataset, renderer);
		return gview;
	}

	// BarGraph
	public GraphicalView getMMGraph()
	{
		GraphicalView gview = null;

		// Create the dataset
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		TimeSeries series = new TimeSeries("Date");

		if ( carList.getSelectedItem() != null ) {
			Car car = (Car) carList.getSelectedItem();
			List<FillUp> fillUps = null;

			try {
				QueryBuilder<FillUp, Integer> queryBuilder = getFillUpDatabaseHelper().getFillUpDao().queryBuilder();
				queryBuilder.where().eq(FillUp.COLUMN_CAR_ID, car.getId());

				PreparedQuery<FillUp> preparedQuery = queryBuilder.prepare();
				fillUps = getFillUpDatabaseHelper().getFillUpDao().query(preparedQuery);
			} catch (SQLException e) {
				e.printStackTrace();
			}

			for ( FillUp fu : fillUps ) {
				series.add(fu.getDate().getMonth(), fu.getDistance());
			}
		}

		dataset.addSeries(series);

		// Setup the renderers
		XYSeriesRenderer r = new XYSeriesRenderer();
		r.setColor(Color.YELLOW);
		r.setPointStyle(PointStyle.DIAMOND);
		r.setFillBelowLine(false);
		r.setFillPoints(true);

		XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
		renderer.setAxisTitleTextSize(35);
		renderer.setChartTitleTextSize(50);
		renderer.setLabelsTextSize(35);
		renderer.setPointSize(5f);
		renderer.setZoomEnabled(false, false);
		// top, left, bottom, right
		renderer.setMargins(new int[] { 80, 80, 20, 0 });
		renderer.setChartTitle("Monthly Milage");
		renderer.setXTitle("Month");
		renderer.setYTitle("Miles");
		renderer.setApplyBackgroundColor(false);
		renderer.setRange(new double[] { 0, 6, 0, 40 });
		renderer.setFitLegend(false);
		renderer.setPanEnabled(false, false);
		renderer.setZoomEnabled(false, false);
		renderer.setAxesColor(Color.RED);
		renderer.setShowGrid(true);
		renderer.setZoomEnabled(false);
		renderer.setInScroll(true);
		renderer.addSeriesRenderer(r);
		renderer.setXAxisMin(0);
		renderer.setXAxisMax(dataset.getSeriesAt(0).getMaxX() + 0.5);
		renderer.setYAxisMin(0);
		renderer.setYAxisMax(dataset.getSeriesAt(0).getMaxY() + 0.5);

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

		double max = values[0];

		for ( int i = 1; i < values.length; i++ ) {
			if ( values[i] < max ) {
				max = values[i];
			}
		}

		return max;
	}

	public double getBestCPMRecord(List<FillUp> fillups)
	{
		double best = 0.0;
		for ( FillUp fu : fillups ) {
			if ( best < ( fu.getMPG() / fu.getPrice() ) ) {
				best = ( fu.getMPG() / fu.getPrice() );
			}
		}
		return round(best);
	}

	public double getWorstCPMRecord(List<FillUp> fillups)
	{
		double best = Double.MAX_VALUE;
		for ( FillUp fu : fillups ) {
			if ( best > ( fu.getMPG() / fu.getPrice() ) ) {
				best = ( fu.getMPG() / fu.getPrice() );
			}
		}
		return round(best);
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

		double max = values[0];

		for ( int i = 1; i < values.length; i++ ) {
			if ( values[i] < max ) {
				max = values[i];
			}
		}

		return round(max);
	}

	private double round(double x) 
	{
		return ((int)((x * 100) / 100));
	}
}

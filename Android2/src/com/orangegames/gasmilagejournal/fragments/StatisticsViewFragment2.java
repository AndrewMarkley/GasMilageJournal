package com.orangegames.gasmilagejournal.fragments;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.orangegames.gasmilagejournal.R;
import com.orangegames.gasmilagejournal.database.CarDatabaseHelper;
import com.orangegames.gasmilagejournal.database.FillUpDatabaseHelper;

public class StatisticsViewFragment2 extends Fragment
{
	public StatisticsViewFragment2() {}

	private CarDatabaseHelper carDatabaseHelper = null;
	private FillUpDatabaseHelper fillUpDatabaseHelper = null;

	private GraphicalView mChartView;
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View rootView = inflater.inflate(R.layout.statistics_view_fragment2, container, false);
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

		XYSeries series = new XYSeries("Performance");
		series.add(1, 2);
		series.add(2, 3);
		series.add(3, 2);
		series.add(4, 5);
		series.add(5, 4);

		dataset.addSeries(series);
		return dataset;
	}

	@SuppressWarnings("deprecation")
	private XYMultipleSeriesRenderer getDemoRenderer()
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
		r.setPointStyle(PointStyle.CIRCLE);
		r.setFillBelowLine(false);
		r.setFillPoints(true);
		renderer.addSeriesRenderer(r);
		setChartSettings(renderer);
		return renderer;
	}

	private void setChartSettings(XYMultipleSeriesRenderer renderer)
	{
		renderer.setChartTitle("Chart demo");
		renderer.setXTitle("x values");
		renderer.setYTitle("y values");
		renderer.setApplyBackgroundColor(false);
		renderer.setRange(new double[] { 0, 6, 0, 40 });
		renderer.setFitLegend(false);
		renderer.setPanEnabled(false, false);
		renderer.setZoomEnabled(false, false);
		renderer.setAxesColor(Color.RED);
		renderer.setShowGrid(true);
		renderer.setXAxisMin(0.5);
		renderer.setXAxisMax(10.5);
		renderer.setYAxisMin(0);
		renderer.setZoomEnabled(false);
		renderer.setYAxisMax(30);
	}

	public void onResume()
	{
		super.onResume();
		mChartView = ChartFactory.getLineChartView(getActivity(), getDemoDataset(), getDemoRenderer());
		LinearLayout chartView = (LinearLayout) getView().findViewById(R.id.chart_container);
		chartView.addView(mChartView);
	}
	
	
}

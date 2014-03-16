package andrews.gas.milage.journal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import com.google.ads.AdRequest;
import com.google.ads.AdView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemSelectedListener;

public class Stats extends Activity {
    ArrayList<FillUp> fillUps = new ArrayList<FillUp>();
    ListView listView = null;
    DatabaseHandler db = null;
    CarDatabaseHandler cd = null;
    ArrayAdapter<String> adapter = null;
    String Data = null;
    int tempPos, z=0;
    String[] values = null;
    Context ct = null;
    List<Car> cars;
    //Button service_log;
    double avg_mpg = 0, avg_fuel_cost = 0, avg_miles_per_dollar = 0, avg_time_between_fill_ups = 0; 
    double avg_miles_between_fill_ups = 0, avg_dollar_per_day = 0;
    Button filter;
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_stats);
        //ct = getApplicationContext();
        AdView ad = (AdView) findViewById(R.id.ad);
        AdRequest adRequest = new AdRequest();
        ad.loadAd(adRequest);
        filter = (Button) findViewById(R.id.filter_button);
        listView = (ListView) findViewById(R.id.statlist);
        listView.setCacheColorHint(Color.parseColor("#00000000"));
        db = new DatabaseHandler(this);
        cd = new CarDatabaseHandler(this);
        refreshStats();
        filter.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
        		AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
	            builder.setTitle("Pick a Filter!");
	            cars = cd.getAllCars();
	            String[] options = new String[cars.size()+1];
	            for(int ctr = 0; ctr<options.length-1; ctr++)
	            	options[ctr] = cars.get(ctr).getName();
	            options[options.length-1] = "All Cars";
	            builder.setItems(options, new DialogInterface.OnClickListener() {
	                    @Override
						public void onClick(DialogInterface dialog, int p) {
	                    	if(p<cars.size())
	                    		showStats(cars.get(p));
	                    	else
	                    		refreshStats();
	                    }
        	});
	        builder.create();
	        builder.show();				
			}
		});
	}
	public void onResume(){
		super.onResume();
		db = new DatabaseHandler(this);
		avg_time_between_fill_ups = 0;
        avg_miles_between_fill_ups = 0;
        avg_miles_per_dollar = 0; 
        avg_dollar_per_day = 0;
        avg_mpg = 0;
        avg_fuel_cost = 0;
        refreshStats();
	}
	//truncate to x places
	public double round(double value, int places) {
	    if (places < 0)
	    	return value;
	    long factor = (long) Math.pow(10, places);
	    value = value * factor;
	    long tmp = Math.round(value);
	    return (double) tmp / factor;
	}
    public ArrayList<FillUp> sort(ArrayList<FillUp> x){
    	fillUpComparator comparator = new fillUpComparator();
        Collections.sort(x, comparator);
        return x;
    }
    public void refreshStats(){
    	try{
        	fillUps = (ArrayList<FillUp>) db.getAllFillUps();
        	if(fillUps.size()>0)
        	{
        		fillUps = sort(fillUps);
	            Calendar earliest = fillUps.get(0).getCalendar();
	            Calendar latest = fillUps.get(fillUps.size()-1).getCalendar();
	            long day1 = earliest.getTimeInMillis(); // in milliseconds.
	            long day2 = latest.getTimeInMillis(); // in milliseconds.
	            long days = (day2 - day1) / 86400000;
	            int time = (int) days;            
	            
	            for(int ctr =0; ctr<fillUps.size(); ctr++){
	            	avg_mpg += fillUps.get(ctr).getMPG();
	            	avg_fuel_cost += fillUps.get(ctr).getPrice();
	            	avg_miles_per_dollar += fillUps.get(ctr).getDistance();
	            	//avg_time_between_fill_ups += fillUps.get(ctr).get; 
	                avg_miles_between_fill_ups += fillUps.get(ctr).getDistance(); 
	                avg_dollar_per_day += fillUps.get(ctr).getTotalCost();
	            }
	            avg_time_between_fill_ups = (double)time/fillUps.size();
	            avg_miles_between_fill_ups /= fillUps.size();
	            avg_miles_per_dollar /= avg_dollar_per_day; 
	            if(time!=0)
	            	avg_dollar_per_day /= (time);            	
	            avg_mpg /= fillUps.size();
	            avg_fuel_cost /= fillUps.size();
	            
	            avg_time_between_fill_ups = round( avg_time_between_fill_ups,3);
	            avg_miles_between_fill_ups = round( avg_miles_between_fill_ups,3);
	            avg_miles_per_dollar = round( avg_miles_per_dollar,3); 
	            avg_dollar_per_day = round(avg_dollar_per_day, 2);
	            avg_mpg = round( avg_mpg,3);
	            avg_fuel_cost = round( avg_fuel_cost,3);
        	}
        	else
        	{
        		avg_time_between_fill_ups = 0;
	            avg_miles_between_fill_ups = 0;
	            avg_miles_per_dollar = 0; 
	            avg_dollar_per_day = 0;
	            avg_mpg = 0;
	            avg_fuel_cost = 0;
        	}
            values = new String[6];
            values[0] = "Avg. mpg: ";
            values[1] = "Avg. miles between fill ups: ";
            values[2] = "Avg. miles per dollar: ";
            values[3] = "Avg. dollar per day: $";
            values[4] = "Avg. fuel cost: $";
            values[5] = "Avg. time between fill ups:";
            
            String[] ans = {""+avg_mpg, ""+avg_miles_between_fill_ups, ""+avg_miles_per_dollar, ""+avg_dollar_per_day, ""+avg_fuel_cost, ""+avg_time_between_fill_ups+" days"};
                List<HashMap<String,String>> aList = new ArrayList<HashMap<String,String>>();
         
                for(int i=0;i<6;i++){
                    HashMap<String, String> hm = new HashMap<String,String>();
                    hm.put("string", "" + values[i]);
                    hm.put("ans","" + ans[i]);
                    aList.add(hm);
                }
               String[] from = { "string","ans" }; 
               int[] to = { R.id.stats_text, R.id.stats_val};
               SimpleAdapter adapter = new SimpleAdapter(getBaseContext(), aList, R.layout.stats_list_view, from, to);
               listView.setAdapter(adapter);
        } catch(Exception e){
        	e.printStackTrace();
        }
    }
    public void showStats(Car c){
    	avg_time_between_fill_ups = 0;
        avg_miles_between_fill_ups = 0;
        avg_miles_per_dollar = 0; 
        avg_dollar_per_day = 0;
        avg_mpg = 0;
        avg_fuel_cost = 0;
    	try{
        	fillUps = (ArrayList<FillUp>) db.getAllFillUps();
        	if(fillUps.size()>0)
        	{
        		fillUps = sort(fillUps);
        		for(int ctr = 0; ctr<fillUps.size(); ctr++)
        		{
        			if(fillUps.get(ctr).getCarID()==c.getID())
        				{fillUps.remove(ctr);ctr--;}
        			
        		}
	            Calendar earliest = fillUps.get(0).getCalendar();
	            Calendar latest = fillUps.get(fillUps.size()-1).getCalendar();
	            long day1 = earliest.getTimeInMillis(); // in milliseconds.
	            long day2 = latest.getTimeInMillis(); // in milliseconds.
	            long days = (day2 - day1) / 86400000;
	            int time = (int) days;            
	            
	            for(int ctr =0; ctr<fillUps.size(); ctr++){
	            	avg_mpg += fillUps.get(ctr).getMPG();
	            	avg_fuel_cost += fillUps.get(ctr).getPrice();
	            	avg_miles_per_dollar += fillUps.get(ctr).getDistance();
	            	//avg_time_between_fill_ups += fillUps.get(ctr).get; 
	                avg_miles_between_fill_ups += fillUps.get(ctr).getDistance(); 
	                avg_dollar_per_day += fillUps.get(ctr).getTotalCost();
	            }
	            avg_time_between_fill_ups = (double)time/fillUps.size();
	            avg_miles_between_fill_ups /= fillUps.size();
	            avg_miles_per_dollar /= avg_dollar_per_day; 
	            if(time!=0)
	            	avg_dollar_per_day /= (time);            	
	            avg_mpg /= fillUps.size();
	            avg_fuel_cost /= fillUps.size();
	            
	            avg_time_between_fill_ups = round( avg_time_between_fill_ups,3);
	            avg_miles_between_fill_ups = round( avg_miles_between_fill_ups,3);
	            avg_miles_per_dollar = round( avg_miles_per_dollar,3); 
	            avg_dollar_per_day = round(avg_dollar_per_day, 2);
	            avg_mpg = round( avg_mpg,3);
	            avg_fuel_cost = round( avg_fuel_cost,3);
        	}
        	else
        	{
        		avg_time_between_fill_ups = 0;
	            avg_miles_between_fill_ups = 0;
	            avg_miles_per_dollar = 0; 
	            avg_dollar_per_day = 0;
	            avg_mpg = 0;
	            avg_fuel_cost = 0;
        	}
            values = new String[6];
            values[0] = "Avg. mpg: ";
            values[1] = "Avg. miles between fill ups: ";
            values[2] = "Avg. miles per dollar: ";
            values[3] = "Avg. dollar per day: $";
            values[4] = "Avg. fuel cost: $";
            values[5] = "Avg. time between fill ups:";
            
            String[] ans = {""+avg_mpg, ""+avg_miles_between_fill_ups, ""+avg_miles_per_dollar, ""+avg_dollar_per_day, ""+avg_fuel_cost, ""+avg_time_between_fill_ups+" days"};
                List<HashMap<String,String>> aList = new ArrayList<HashMap<String,String>>();
         
                for(int i=0;i<6;i++){
                    HashMap<String, String> hm = new HashMap<String,String>();
                    hm.put("string", "" + values[i]);
                    hm.put("ans","" + ans[i]);
                    aList.add(hm);
                }
               String[] from = { "string","ans" }; 
               int[] to = { R.id.stats_text, R.id.stats_val};
               SimpleAdapter adapter = new SimpleAdapter(getBaseContext(), aList, R.layout.stats_list_view, from, to);
               listView.setAdapter(adapter);
        } catch(Exception e){
        	e.printStackTrace();
        }
    }
}

package andrews.gas.milage.journal;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import com.google.ads.AdRequest;
import com.google.ads.AdView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
    ArrayAdapter<String> adapter = null;
    String Data = null;
    int tempPos, z=0;
    String[] values = null;
    Button b5, b6, carP = null;
    //Button service_log;
    double avg_mpg = 0, avg_fuel_cost = 0, avg_miles_per_dollar = 0, avg_time_between_fill_ups = 0; 
    double avg_miles_between_fill_ups = 0, avg_dollar_per_day = 0;
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_stats);
        
        AdView ad = (AdView) findViewById(R.id.ad);
        AdRequest adRequest = new AdRequest();
        adRequest.setTesting(true);
        ad.loadAd(adRequest);
        
        b5 = (Button) findViewById(R.id.button8); //all logs
        b6 = (Button) findViewById(R.id.button9); //Stats!
        carP = (Button) findViewById(R.id.carP); //car profile
        //service_log = (Button) findViewById(R.id.serviceS); //service
        
        b5.setBackgroundResource(R.drawable.book_6);
        b6.setBackgroundResource(R.drawable.stats_2);
        //service_log.setBackgroundResource(R.drawable.service);
        carP.setBackgroundResource(R.drawable.car_2);

        
        listView = (ListView) findViewById(R.id.statlist);

        db = new DatabaseHandler(this);
        try{
        	fillUps = (ArrayList<FillUp>) db.getAllFillUps();
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
            
            values = new String[6];
            values[0] = "Avg. mpg: ";
            values[1] = "Avg. miles between fill ups: ";
            values[2] = "Avg. miles per dollar: ";
            values[3] = "Avg. dollar per day: $";
            values[4] = "Avg. fuel cost: $";
            values[5] = "Avg. time between fill ups:";
            
            String[] ans = {""+avg_mpg, ""+avg_miles_between_fill_ups, ""+avg_miles_per_dollar, ""+avg_dollar_per_day, ""+avg_fuel_cost, ""+avg_time_between_fill_ups+" days"};
            
            //listView = (ListView) findViewById(R.id.statlist);

                List<HashMap<String,String>> aList = new ArrayList<HashMap<String,String>>();
         
                for(int i=0;i<6;i++){
                    HashMap<String, String> hm = new HashMap<String,String>();
                    hm.put("string", "" + values[i]);
                    hm.put("ans","" + ans[i]);
                    aList.add(hm);
                }
         
                // Keys used in Hashmap
               String[] from = { "string","ans" };
                  
               // Setting the adapter to the listViewview_layout
               int[] to = { R.id.stats_text, R.id.stats_val};
         
               // Instantiating an adapter to store each items
               // R.layout.listview_layout defines the layout of each item
               SimpleAdapter adapter = new SimpleAdapter(getBaseContext(), aList, R.layout.stats_list_view, from, to);
               
         
               // Getting a reference to listview of main.xml layout file
               //ListView listView = ( ListView ) findViewById(R.id.list);
         
               // Setting the adapter to the listView
               listView.setAdapter(adapter);
               
        } catch(Exception e){
        	e.printStackTrace();
        }
        listView.setOnItemSelectedListener(new OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
        	
        });

        //adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, values);

        //listView.setAdapter(adapter);        
        
        /*service_log.setOnClickListener(new View.OnClickListener() {
    		@Override
    		public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), showServiceLog.class);
                finish();
            	startActivity(i);
    		}
    	});*/
        
        b5.setOnClickListener(new View.OnClickListener() {
    		@Override
    		public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), showFillUps.class);
                finish();
            	startActivity(i);
    		}
    	});
        
        carP.setOnClickListener(new View.OnClickListener() {
    		@Override
    		public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), showCarProfile.class);
                finish();
            	startActivity(i);
    		}
    	});
	}
	@Override
	public void onBackPressed() {
	    // Do Here what ever you want do on back press;
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
}

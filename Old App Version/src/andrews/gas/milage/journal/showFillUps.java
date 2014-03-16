package andrews.gas.milage.journal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnDragListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.google.ads.AdRequest;
import com.google.ads.AdView;

public class showFillUps extends Activity {
    ArrayList<FillUp> fillUps = new ArrayList<FillUp>();
    ArrayList<FillUp> fillUpsUnsorted = new ArrayList<FillUp>();
    ListView listView = null;
    DatabaseHandler db = null;
    ArrayAdapter<String> adapter = null;
    String Data = null;
    int tempPos;
    String[] values = null;
    //Button service_log;
    ImageButton b4;
    String[] dates, costs, mpgs = null;
    CarDatabaseHandler car = new CarDatabaseHandler(this);
    Context ct;
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_fill_ups);
        ct = this;
        AdView ad = (AdView) findViewById(R.id.ad);
        AdRequest adRequest = new AdRequest();
        ad.loadAd(adRequest);
        //AdRequest r = new AdRequest();
        //r.addTestDevice("k2d307d321b5c7af0308ee51eed3e0ae");
        //adRequest.addTestDevice(AdRequest.TEST_EMULATOR);               // Emulator
        //adRequest.addTestDevice("TEST_DEVICE_ID");                      // Test Android Device
        
        //instantiate all the buttons
        b4 = (ImageButton) findViewById(R.id.fill_up_button2);//add fill up button  

       	forceNewCar();
        
        listView = (ListView) findViewById(R.id.list); // fillup log scrolling list
        listView.setCacheColorHint(Color.parseColor("#00000000"));
        
        db = new DatabaseHandler(this); //SQLLite database of fill up logs
        try{
        	//if there are any fillups in the database get them
        	//the try/catch statement prevents errors being thrown if there isn't
        	fillUps = sort((ArrayList<FillUp>) db.getAllFillUps());
        	fillUpsUnsorted = fillUps;
        	
        	fillUps = sort(fillUps);
        	values = new String[fillUps.size()];
        	for(int ctr = 0; ctr<fillUps.size(); ctr++)
        		System.out.println(fillUps.get(ctr).toString());
        } catch(Exception e){

        	//this will only be evaluated when there is no entries.
        	//to prevent errors being thrown farther down the list we give values a single value
        	values = new String[]{"No entries yet"};
        }  
        
        //the scrollable list view
        listView = (ListView) findViewById(R.id.list);
        listView.setCacheColorHint(Color.parseColor("#00000000"));
        dates = new String[fillUps.size()];
        mpgs = new String[fillUps.size()];
        costs = new String[fillUps.size()];
        for(int ctr = 0; ctr<fillUps.size(); ctr++)
        {
        	ArrayList<Car> cars = (ArrayList<Car>) car.getAllCars();
        	String name = "";
        	for(int c = 0; c<cars.size(); c++)
        		if(cars.get(c).getID()==fillUps.get(ctr).getCarID())
        			name = cars.get(c).getName();
        	dates[ctr] = name+"\t"+fillUps.get(ctr).getDate();
        	mpgs[ctr] = " "+fillUps.get(ctr).getMPG();
        	costs[ctr] = " "+fillUps.get(ctr).getTotalCost()+"\nComments: "+fillUps.get(ctr).getComments();
        }

        // Each row in the list stores country name, currency and flag
            List<HashMap<String,String>> aList = new ArrayList<HashMap<String,String>>();
     
            for(int i=0;i<fillUps.size();i++){
                HashMap<String, String> hm = new HashMap<String,String>();
                hm.put("date", "" + dates[i]);
                hm.put("mpg","MPG : " + mpgs[i]);
                hm.put("cost","Cost: $" + costs[i]);
                aList.add(hm);
            }
     
            // Keys used in Hashmap
           String[] from = { "date","mpg","cost" };
     
           // Ids of views in listview_layout
           int[] to = { R.id.txt,R.id.cur, R.id.cos};
     
           // Instantiating an adapter to store each items
           // R.layout.listview_layout defines the layout of each item
           SimpleAdapter adapter = new SimpleAdapter(getBaseContext(), aList, R.layout.listview_layout, from, to);
     
           // Getting a reference to listview of main.xml layout file
           ListView listView = ( ListView ) findViewById(R.id.list);
     
           // Setting the adapter to the listView
           listView.setAdapter(adapter);
           
        /*
        //this lets us add the contents of values to the listview
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, values);
        //set the listview adapter to the new one which has our values in it
        listView.setAdapter(adapter);
        listView.setTextFilterEnabled(true);
        */
        //this is used to pop up a dialog box if the user long clicks on an item
        //their options are going to be edit, stats, delete
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
        listView.setOnItemLongClickListener(new OnItemLongClickListener(){

			@Override
			//pos is the position of the item in the list that was selected
			public boolean onItemLongClick(AdapterView<?> a, View view, int pos, long arg3) {
	    		//sets the class variable tempPos to the position selected
				tempPos = pos;
	    		//we then create a dialog interface/pop up menu for our options
				AlertDialog.Builder builder = new AlertDialog.Builder(a.getContext());
	            builder.setTitle("Options");
	            String[] options = new String[] {"Edit", "Statistics","Delete"};
	            builder.setItems(options, new DialogInterface.OnClickListener() {
	                    @SuppressWarnings("deprecation")
						@Override
						public void onClick(DialogInterface dialog, int p) {
	                    	   //user selected delete
	                    	   if(p==2)
	                    	   {
	                    		   //get the fillup to be removed
	                    		   FillUp c = fillUps.remove(tempPos);
	                    		   //remove it from database
	                    		   db.deleteFillUp(c, tempPos);
	                    		   fillUpsUnsorted = (ArrayList<FillUp>) db.getAllFillUps();
	                    		   //update list in this class
	                    		   fillUps = sort((ArrayList<FillUp>) db.getAllFillUps());
	                    		   //update values in this class
	                    	        
	                    	        //the scrollable list view
	                    	        dates = new String[fillUps.size()];
	                    	        mpgs = new String[fillUps.size()];
	                    	        costs = new String[fillUps.size()];
	                    	        for(int ctr = 0; ctr<fillUps.size(); ctr++)
	                    	        {
	                    	        	ArrayList<Car> cars = (ArrayList<Car>) car.getAllCars();
	                    	        	String name = "";
	                    	        	for(int ca = 0; ca<cars.size(); ca++)
	                    	        		if(cars.get(ca).getID()==fillUps.get(ctr).getCarID())
	                    	        			name = cars.get(ca).getName();
	                    	        	dates[ctr] = name+"\t"+fillUps.get(ctr).getDate();
	                    	        	mpgs[ctr] = " "+fillUps.get(ctr).getMPG();
	                    	        	costs[ctr] = " "+fillUps.get(ctr).getTotalCost()+"\nComments: "+fillUps.get(ctr).getComments();
	                    	        }

	                    	        // Each row in the list stores country name, currency and flag
	                    	            List<HashMap<String,String>> aList = new ArrayList<HashMap<String,String>>();
	                    	     
	                    	            for(int i=0;i<fillUps.size();i++){
	                    	                HashMap<String, String> hm = new HashMap<String,String>();
	                    	                hm.put("date", "" + dates[i]);
	                    	                hm.put("mpg","MPG: " + mpgs[i]);
	                    	                hm.put("cost","Cost: $" + costs[i]);
	                    	                aList.add(hm);
	                    	            }
	                    	     
	                    	            // Keys used in Hashmap
	                    	           String[] from = { "date","mpg","cost" };
	                    	     
	                    	           // Ids of views in listview_layout
	                    	           int[] to = { R.id.txt,R.id.cur, R.id.cos};
	                    	     
	                    	           // Instantiating an adapter to store each items
	                    	           // R.layout.listview_layout defines the layout of each item
	                    	           SimpleAdapter adapter = new SimpleAdapter(getBaseContext(), aList, R.layout.listview_layout, from, to);
	                    	     
	                    	           // Getting a reference to listview of main.xml layout file
	                    	           ListView listView = ( ListView ) findViewById(R.id.list);
	                    	     
	                    	           // Setting the adapter to the listView
	                    	           listView.setAdapter(adapter);
	                    	           adapter.notifyDataSetChanged();
		                    	        listView.setAdapter(adapter);
		                    	        listView.refreshDrawableState();

	                    	   }
	                    	   //user selected options or stats
	                    	   if(p==1)
	                    	   {
		               	    		/*Context cont = getApplicationContext();
		            	    		CharSequence text = "This feature has yet to be implemented, sorry for the inconvenience";
		            	    		Toast toast = Toast.makeText(cont, text, Toast.LENGTH_LONG);
		            	    		toast.show();
		            	    		Intent i = new Intent(cont, Stats.class);
		            	    		i.putExtra("compare_fill_up", true);
		            	    		i.putExtra("fillUp", fillUps.get(tempPos).toFileOutputString());*/
	                    		    
	                    		    ((TabActivity) getParent()).getTabHost().setCurrentTab(1);
		            	    		
	                    	   }
	                    	   if(p==0)
	                    	   {
	                               Intent i = new Intent(getApplicationContext(), newFillUp.class);
	                               i.putExtra("edit", true);
	                               i.putExtra("values", fillUps.get(tempPos).toFileOutputString().split(" , "));
	                               for(int ctr = 0; ctr<fillUps.size(); ctr++)
	                            	   if(fillUps.get(tempPos).getCarID()== (fillUpsUnsorted.get(ctr).getCarID()))
	                            		   if(fillUps.get(tempPos).getDistance()== (fillUpsUnsorted.get(ctr).getDistance()))
	                            			   if(fillUps.get(tempPos).getDate().equals((fillUpsUnsorted.get(ctr).getDate())))
	                            				   i.putExtra("originalPos", ctr);
	                               System.out.println("hi here 1 in fillups");
	                               showFillUps.this.startActivityForResult(i, 1);
	                    	   }
	                   }
	            });
	            builder.create();
	            builder.show();
	    		return true;
			}
        	
        });

        listView.setOnItemClickListener(new OnItemClickListener(){
        	@Override
			public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
        		AlertDialog.Builder builder = new AlertDialog.Builder(ct);
	            builder.setTitle("Specific Fill Up Info");
	            String[] options = fillUps.get(pos).toString().split(", ");
	            builder.setItems(options, new DialogInterface.OnClickListener() {
	                    @Override
						public void onClick(DialogInterface dialog, int p) {
	                    	
	                    }
        	});
	        builder.create();
	        builder.show();
        }
        	
        });        
        //new fill up button listener
        b4.setOnClickListener(new View.OnClickListener() {
    		@Override
    		public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), newFillUp.class);
                //this intent is started differently from others. when the user is done and saves
                //the new fill up we need to know so the listview can be repopulated to reflect changes
            	showFillUps.this.startActivityForResult(i, 1);
    		}
    	});
        //car service log button listener
        /*service_log.setOnClickListener(new View.OnClickListener() {
    		@Override
    		public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), showServiceLog.class);
                finish();
                startActivity(i);
    		}
    	});*/
	}
	private void forceNewCar() {
		if(car.getAllCars().size()==0){
			Intent i = new Intent(getApplicationContext(), newCarProfile.class);
			i.putExtra("new", true);
			startActivityForResult(i, 2);
			boolean success = true;
			success = i.getBooleanExtra("added", success);
			//System.out.println(success);
			while(!success){
				forceNewCar();
			}
		}
	}
	//when user is done adding new fill up log this will repopulate list so changes are visible
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
    	if(resultCode == RESULT_OK)
    	{
    		forceNewCar();
    	}    		
    	try{
    		fillUpsUnsorted = (ArrayList<FillUp>) db.getAllFillUps();
 		   fillUps = sort((ArrayList<FillUp>) db.getAllFillUps());
		   //update values in this class
 		   
	        
	        //the scrollable list view
	        dates = new String[fillUps.size()];
	        mpgs = new String[fillUps.size()];
	        costs = new String[fillUps.size()];
	        for(int ctr = 0; ctr<fillUps.size(); ctr++)
	        {
	        	ArrayList<Car> cars = (ArrayList<Car>) car.getAllCars();
	        	String name = "";
	        	for(int c = 0; c<cars.size(); c++)
	        		if(cars.get(c).getID()==fillUps.get(ctr).getCarID())
	        			name = cars.get(c).getName();
	        	dates[ctr] = name+"\t"+fillUps.get(ctr).getDate();
	        	mpgs[ctr] = " "+fillUps.get(ctr).getMPG();
	        	costs[ctr] = " "+fillUps.get(ctr).getTotalCost()+"\nComments: "+fillUps.get(ctr).getComments();
	        }

	        // Each row in the list stores country name, currency and flag
	            List<HashMap<String,String>> aList = new ArrayList<HashMap<String,String>>();
	     
	            for(int i=0;i<fillUps.size();i++){
	                HashMap<String, String> hm = new HashMap<String,String>();
	                hm.put("date", "" + dates[i]);
	                hm.put("mpg","MPG: " + mpgs[i]);
	                hm.put("cost","Cost: $" + costs[i]);
	                aList.add(hm);
	            }
	     
	            // Keys used in Hashmap
	           String[] from = { "date","mpg","cost" };
	     
	           // Ids of views in listview_layout
	           int[] to = { R.id.txt,R.id.cur, R.id.cos};
	     
	           // Instantiating an adapter to store each items
	           // R.layout.listview_layout defines the layout of each item
	           SimpleAdapter adapter = new SimpleAdapter(getBaseContext(), aList, R.layout.listview_layout, from, to);
	     
	           // Getting a reference to listview of main.xml layout file
	           ListView listView = ( ListView ) findViewById(R.id.list);
	     
	           // Setting the adapter to the listView
	           listView.setAdapter(adapter);
	           adapter.notifyDataSetChanged();
    	        listView.setAdapter(adapter);
    	        listView.refreshDrawableState();
	        } catch(Exception e){
	        	
	        }
    }
    /*@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ( keyCode == KeyEvent.KEYCODE_MENU ) {
        	Log.d("showFillUps!", "MENU pressed");
            return true;
        }
        return super.onKeyDown(keyCode, event);        
    }*/
    public void onResume(){
    	super.onResume();
    	db = new DatabaseHandler(this); //SQLLite database of fill up logs
        try{
        	//if there are any fillups in the database get them
        	//the try/catch statement prevents errors being thrown if there isn't
        	fillUps = sort((ArrayList<FillUp>) db.getAllFillUps());
        	fillUpsUnsorted = fillUps;
        	
        	fillUps = sort(fillUps);
        	values = new String[fillUps.size()];
        	for(int ctr = 0; ctr<fillUps.size(); ctr++)
        		System.out.println(fillUps.get(ctr).toString());
        } catch(Exception e){

        	//this will only be evaluated when there is no entries.
        	//to prevent errors being thrown farther down the list we give values a single value
        	values = new String[]{"No entries yet"};
        }  
        
        //the scrollable list view
        listView = (ListView) findViewById(R.id.list);
        listView.setCacheColorHint(Color.parseColor("#00000000"));
        dates = new String[fillUps.size()];
        mpgs = new String[fillUps.size()];
        costs = new String[fillUps.size()];
        for(int ctr = 0; ctr<fillUps.size(); ctr++)
        {
        	ArrayList<Car> cars = (ArrayList<Car>) car.getAllCars();
        	String name = "";
        	for(int c = 0; c<cars.size(); c++)
        		if(cars.get(c).getID()==fillUps.get(ctr).getCarID())
        			name = cars.get(c).getName();
        	dates[ctr] = name+"\t"+fillUps.get(ctr).getDate();
        	mpgs[ctr] = " "+fillUps.get(ctr).getMPG();
        	costs[ctr] = " "+fillUps.get(ctr).getTotalCost()+"\nComments: "+fillUps.get(ctr).getComments();
        }

        // Each row in the list stores country name, currency and flag
            List<HashMap<String,String>> aList = new ArrayList<HashMap<String,String>>();
     
            for(int i=0;i<fillUps.size();i++){
                HashMap<String, String> hm = new HashMap<String,String>();
                hm.put("date", "" + dates[i]);
                hm.put("mpg","MPG : " + mpgs[i]);
                hm.put("cost","Cost: $" + costs[i]);
                aList.add(hm);
            }
     
            // Keys used in Hashmap
           String[] from = { "date","mpg","cost" };
     
           // Ids of views in listview_layout
           int[] to = { R.id.txt,R.id.cur, R.id.cos};
     
           // Instantiating an adapter to store each items
           // R.layout.listview_layout defines the layout of each item
           SimpleAdapter adapter = new SimpleAdapter(getBaseContext(), aList, R.layout.listview_layout, from, to);
     
           // Getting a reference to listview of main.xml layout file
           ListView listView = ( ListView ) findViewById(R.id.list);
     
           // Setting the adapter to the listView
           listView.setAdapter(adapter);
    }
    public ArrayList<FillUp> sort(ArrayList<FillUp> x){
    	fillUpComparator comparator = new fillUpComparator();
        Collections.sort(x, comparator);
        return x;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.activity_home_screen, menu); 
    	return true;
    }
}
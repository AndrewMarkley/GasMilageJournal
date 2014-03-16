package andrews.gas.milage.journal;

import java.util.ArrayList;
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
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class showCarProfile extends Activity {
    ArrayList<Car> cars = new ArrayList<Car>();
    ListView listView = null;
    CarDatabaseHandler db = null;
    DatabaseHandler fu = null;
    ArrayAdapter<String> adapter = null;
    String Data = null;
    int tempPos;
    String[] values = null;
    //Button service_log;
    ImageButton b4;
    String[] name, make, mile = null;
    Intent in;
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_car_profiles);
        b4 = (ImageButton) findViewById(R.id.new_car_button);//add fill up button  
        AdView ad = (AdView) findViewById(R.id.ad);
        AdRequest adRequest = new AdRequest();
        ad.loadAd(adRequest);

        listView = (ListView) findViewById(R.id.car_list); // fillup log scrolling list
        listView.setCacheColorHint(Color.parseColor("#00000000"));
        db = new CarDatabaseHandler(this); //SQLLite database of fill up logs
        fu = new DatabaseHandler(this); //SQLLite database of fill up logs
        try{
        	//if there are any fillups in the database get them
        	//the try/catch statement prevents errors being thrown if there isn't
        	cars = (ArrayList<Car>) db.getAllCars();
        	//the scrollable list view        
            name = new String[cars.size()];
            make = new String[cars.size()];
            mile = new String[cars.size()];
            for(int ctr = 0; ctr<cars.size(); ctr++)
            {
            	name[ctr] = cars.get(ctr).getName();
            	make[ctr] = cars.get(ctr).getYear()+" " + cars.get(ctr).getMake()+" " + cars.get(ctr).getModel();
            	mile[ctr] = "Has "+cars.get(ctr).getMilage()+" miles on it";
            }
        	
        } catch(Exception e){

        	//this will only be evaluated when there is no entries.
        	//to prevent errors being thrown farther down the list we give values a single value
        	name = new String[]{"No cars entered yet!"};
            make = new String[]{" "};
            mile = new String[]{" "};
        }  
        

        // Each row in the list stores country name, currency and flag
            List<HashMap<String,String>> aList = new ArrayList<HashMap<String,String>>();
     
            for(int i=0;i<name.length;i++){
                HashMap<String, String> hm = new HashMap<String,String>();
                hm.put("name", "" + name[i]);
                hm.put("make", "" + make[i]);
                hm.put("mile", "" + mile[i]);
                aList.add(hm);
            }
            
            // Keys used in Hashmap
           String[] from = { "name","make","mile" };
     
           // Ids of views in listview_layout
           int[] to = { R.id.carName,R.id.carMake, R.id.carMile};
     
           // Instantiating an adapter to store each items
           // R.layout.listview_layout defines the layout of each item
           SimpleAdapter adapter = new SimpleAdapter(getBaseContext(), aList, R.layout.custom_car_layout, from, to);
           //System.out.println(adapter);
           
           // Getting a reference to listview of main.xml layout file
           listView = ( ListView ) findViewById(R.id.car_list);
           //System.out.println("tada!");
           // Setting the adapter to the listView
           listView.setAdapter(adapter);
           //System.out.println("tada!");
           
        /*
        //this lets us add the contents of values to the listview
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, values);
        //set the listview adapter to the new one which has our values in it
        listView.setAdapter(adapter);
        listView.setTextFilterEnabled(true);
        */
        //this is used to pop up a dialog box if the user long clicks on an item
        //their options are going to be edit, stats, delete
           
           b4.setOnClickListener(new View.OnClickListener() {
       		@Override
       		public void onClick(View v) {
                   Intent i = new Intent(getApplicationContext(), newCarProfile.class);
                   //this intent is started differently from others. when the user is done and saves
                   //the new fill up we need to know so the listview can be repopulated to reflect changes
               	showCarProfile.this.startActivityForResult(i, 1);
       		}
       	});
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
	            String[] options = new String[] {"Edit","Delete"};
	            builder.setItems(options, new DialogInterface.OnClickListener() {
	                    @Override
						public void onClick(DialogInterface dialog, int p) {
	                    	   //user selected delete
	                    	   if(p==1)
	                    	   {
	                    		   //get the fillup to be removed
	                    		   for(int ctr = 0; ctr<cars.size(); ctr++)
	                    		   System.out.println(cars.get(ctr).toString());
	                    		   Car c = cars.remove(tempPos);
	                    		   ArrayList<FillUp> f = (ArrayList<FillUp>) fu.getAllFillUps();
	                    		   for(int ctr = 0; ctr<f.size(); ctr++){
	                    			   if(f.get(ctr).getCarID()==c.getID())
	                    			   {
	                    				   FillUp fa = f.get(ctr);
	                    				   System.out.println(fa.toString());
	                    				   fu.deleteFillUp(fa, ctr);
	                    				   ctr = 0;
	                    				   f = (ArrayList<FillUp>) fu.getAllFillUps();
	                    			   }
	                    		   }
	                    		   fu.close();
	                    		   System.out.println("Car to be deleted: " +c.toString());
	                    		   //remove it from database
	                    		   db.deleteCar(c.getID());
	                    		   //update list in this class
	                    		   cars = (ArrayList<Car>) db.getAllCars();
	                    		   //update values in this class
	                    	        
	                    	        //the scrollable list view
	                    		   name = new String[cars.size()];
	                    	        make = new String[cars.size()];
	                    	        mile = new String[cars.size()];
	                    	        for(int ctr = 0; ctr<cars.size(); ctr++)
	                    	        {
	                    	        	name[ctr] = cars.get(ctr).getName();
	                    	        	make[ctr] = cars.get(ctr).getYear()+" " + cars.get(ctr).getMake()+" " + cars.get(ctr).getModel();
	                                	mile[ctr] = "Has "+cars.get(ctr).getMilage()+" miles on it";
	                    	        }

	                    	        // Each row in the list stores country name, currency and flag
	                    	            List<HashMap<String,String>> aList = new ArrayList<HashMap<String,String>>();
	                    	     
	                    	            for(int i=0;i<cars.size();i++){
	                    	                HashMap<String, String> hm = new HashMap<String,String>();
	                    	                hm.put("name", "" + name[i]);
	                    	                hm.put("make", "" + make[i]);
	                    	                hm.put("mile", "" + mile[i]);
	                    	                aList.add(hm);
	                    	            }
	                    	     
	                    	            // Keys used in Hashmap
	                    	           String[] from = { "name","make","mile" };
	                    	     
	                    	           // Ids of views in listview_layout
	                    	           int[] to = { R.id.carName,R.id.carMake, R.id.carMile};
	                    	     
	                    	           // Instantiating an adapter to store each items
	                    	           // R.layout.listview_layout defines the layout of each item
	                    	           SimpleAdapter adapter = new SimpleAdapter(getBaseContext(), aList, R.layout.custom_car_layout, from, to);
	                    	     
	                    	     
	                    	           // Setting the adapter to the listView
	                    	           listView.setAdapter(adapter);
	                    	     
	                    	           // Setting the adapter to the listView
	                    	           adapter.notifyDataSetChanged();
		                    	        listView.setAdapter(adapter);
		                    	        listView.refreshDrawableState();

	                    	   }
	                    	   //user selected options or stats
	                    	   if(p==0)
	                    	   {
	                    		   if(p==0)
		                    	   {
		                               in = new Intent(getApplicationContext(), newCarProfile.class);
		                               in.putExtra("edit", true);
		                               in.putExtra("pos", tempPos);
		                               in.putExtra("values", cars.get(tempPos).toOutputFileString().split(" , "));
		                               startActivityForResult(in, 1);
		                    	   }
	                    	   }
	                   }
	            });
	            builder.create();
	            builder.show();
	    		return true;
			}
        	
        });
        //new fill up button listener
        b4.setOnClickListener(new View.OnClickListener() {
    		@Override
    		public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), newCarProfile.class);
                //this intent is started differently from others. when the user is done and saves
                //the new fill up we need to know so the listview can be repopulated to reflect changes
            	showCarProfile.this.startActivityForResult(i, 1);
    		}
    	});
	}
	//when user is done adding new fill up log this will repopulate list so changes are visible
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
    	/*if(resultCode==10){
    		String[] changes = in.getStringArrayExtra("change");
    		System.out.println(changes.length);
    		Car ch = new Car(changes);
    		DatabaseHandler base = new DatabaseHandler(this);
    		ArrayList<FillUp> fu = (ArrayList<FillUp>) base.getAllFillUps();
    		for(int ctr = 0; ctr<fu.size(); ctr++){
    			if(fu.get(ctr).getCarName().equals(data.getStringExtra("ogName"))){
    				FillUp fill = fu.get(ctr);
    				base.deleteFillUp(fill, ctr);
    				fill.setName(ch.getName());
    				base.addFillUp(fill);
    			}
    		}
    	}*/
    	db = new CarDatabaseHandler(this); //SQLLite database of fill up logs
        try{
        	//if there are any fillups in the database get them
        	//the try/catch statement prevents errors being thrown if there isn't
        	cars = (ArrayList<Car>) db.getAllCars();
        	values = new String[cars.size()];
        	
        } catch(Exception e){

        	//this will only be evaluated when there is no entries.
        	//to prevent errors being thrown farther down the list we give values a single value
        	values = new String[]{"No entries yet"};
        }  
        
        //the scrollable list view        
        name = new String[cars.size()];
        make = new String[cars.size()];
        mile = new String[cars.size()];
        for(int ctr = 0; ctr<cars.size(); ctr++)
        {
        	name[ctr] = cars.get(ctr).getName();
        	make[ctr] = cars.get(ctr).getYear()+" " + cars.get(ctr).getMake()+" " + cars.get(ctr).getModel();
        	mile[ctr] = "Has "+cars.get(ctr).getMilage()+" miles on it";
        }

        // Each row in the list stores country name, currency and flag
            List<HashMap<String,String>> aList = new ArrayList<HashMap<String,String>>();
     
            for(int i=0;i<cars.size();i++){
                HashMap<String, String> hm = new HashMap<String,String>();
                hm.put("name", "" + name[i]);
                hm.put("make", "" + make[i]);
                hm.put("mile", "" + mile[i]);
                aList.add(hm);
            }
     
            // Keys used in Hashmap
           String[] from = { "name","make","mile" };
     
           // Ids of views in listview_layout
           int[] to = { R.id.carName,R.id.carMake, R.id.carMile};
     
           // Instantiating an adapter to store each items
           // R.layout.listview_layout defines the layout of each item
           SimpleAdapter adapter = new SimpleAdapter(getBaseContext(), aList, R.layout.custom_car_layout, from, to);
     
           // Setting the adapter to the listView
           listView.setAdapter(adapter);	     
	           // Setting the adapter to the listView
	       adapter.notifyDataSetChanged();
    	   listView.setAdapter(adapter);
    	   listView.refreshDrawableState();
    }
    /*@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ( keyCode == KeyEvent.KEYCODE_MENU ) {
        	Log.d("showFillUps!", "MENU pressed");
            return true;
        }
        return super.onKeyDown(keyCode, event);        
    }*/
    public ArrayList<Car> sort(ArrayList<Car> x){
    	carComparator comparator = new carComparator();
        Collections.sort(x, comparator);
        return x;
    }
}
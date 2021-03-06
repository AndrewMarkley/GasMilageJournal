package andrews.gas.milage.journal;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class newCarProfile extends Activity {
    TextView naam, mak, mod, odom, jaar;
    Button save, cancel, plus, minus;
    CarDatabaseHandler db = null;
    int mYear = 0;
    Context ct = this;
    boolean ans = false;
    boolean edit = false;
    int id = 0;
    String[] values = new String[5];
    Car temp = null;    
    Intent in;
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_car_form);
        CarDatabaseHandler cd = new CarDatabaseHandler(getApplicationContext());
        ArrayList<Car> c = (ArrayList<Car>) cd.getAllCars();
        if(c.size()==0)
        	id = 0;
        else
        	id = c.get(c.size()-1).getID();
        in = getIntent();
        ans = in.getBooleanExtra("new", ans);
        edit = in.getBooleanExtra("edit", edit);
        if(!edit)
        	id++;
        else
        	id = Integer.parseInt(in.getStringArrayExtra("values")[0]);
        if(ans){
        	show();
        	in.putExtra("new", false);
        }        
        naam = (TextView) findViewById(R.id.car_nick_name_input); //name field
        mak = (TextView) findViewById(R.id.make_input); //make field
        mod = (TextView) findViewById(R.id.model_input); //model field
        odom = (TextView) findViewById(R.id.odometer_input); //odometer field
        jaar = (TextView) findViewById(R.id.year_input); //year button
        
        save = (Button) findViewById(R.id.save); //save button
        cancel = (Button) findViewById(R.id.cancel); //cancel button
        plus = (Button) findViewById(R.id.plus); //save button
        minus = (Button) findViewById(R.id.minus); //cancel button
        
        db = new CarDatabaseHandler(this); //SQLLite database of fill up logs
        
        jaar.setText(""+Calendar.getInstance().get(Calendar.YEAR));
        
        if(edit){
        	save.setText("Save Car");
        	values = in.getStringArrayExtra("values");
        	id = Integer.parseInt(values[0]);
        	naam.setText(values[1]);
        	jaar.setText(values[2]);
        	mak.setText(values[3]);
        	mod.setText(values[4]);
        	odom.setText(values[5]);
        	temp = new Car(Integer.parseInt(values[0]), values[1], Integer.parseInt(values[2]), values[3], values[4], Double.parseDouble(values[5]));
        }
		
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
			public void onClick(View v) {
	    		//return 0 for failure!
            	if(ans)
            	{
            		Intent returnIntent = new Intent();
            	    returnIntent.putExtra("added",false);
            	    setResult(RESULT_OK,returnIntent);  
            	}
	    		finish();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
			public void onClick(View v) {
            	Car c = null;
            	//System.out.println("made it here");
            	try{
            		CharSequence x = jaar.getText();
            		int y = (Integer.parseInt(x.toString()));
            		x = odom.getText();
            		double z = (Double.parseDouble(x.toString()));
            		//System.out.println("made it here 2");
            		//System.out.println(id+" " +naam.getText().toString()+" " +y+" " +mak.getText().toString()+" " +mod.getText().toString()+" "+ z);
            		System.out.println(id);
            		c = new Car(id, naam.getText().toString(), y, mak.getText().toString(), mod.getText().toString(), z);
            		if(!edit){
            			db.addCar(c);
            		}
            		else{
            			int val = 0;
            			val = in.getIntExtra("pos", val);
            			db.deleteCar(val);
            			db.addCar(c);
            		}
            		db.close();
                	Intent i = getIntent();
    	    		//return 0 for failure!
                	System.out.println("not here 2");
    	    		i.putExtra("change", c.toOutputFileString().split(" , "));
    	    		if(edit)
    	    			i.putExtra("ogName", temp.getName());
    	    		System.out.println("not here 3");
    	    		setResult(10);
    	    		finish();
            	} catch(Exception e){
            		e.printStackTrace();
            		CharSequence text = "Please fill in all blank spaces!";
            		Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG);
            		toast.show();
            	}
            }
        });
        plus.setOnClickListener(new View.OnClickListener() {
    		@Override
    		public void onClick(View v) {
    			jaar.setText(""+(Integer.parseInt(jaar.getText().toString())+1));
            }
        });
        minus.setOnClickListener(new View.OnClickListener() {
    		@Override
    		public void onClick(View v) {
    			jaar.setText(""+(Integer.parseInt(jaar.getText().toString())-1));
            }
        });
        
	}  
	public void onClick(View v) {
        if(v.getId()==naam.getId() && naam.getText().toString().length()>0)
        	naam.setText(naam.getText().toString());
        
        if(v.getId()==mak.getId() && mak.getText().toString().length()>0)
        	mak.setText(mak.getText().toString());
        
        if(v.getId()==mod.getId() && mod.getText().toString().length()>0)
        	mod.setText(mod.getText().toString());
        
        if(v.getId()==odom.getId() && odom.getText().toString().length()>0){
        	//System.out.println("odometer before: "+odom.getText());
        	odom.setText(odom.getText().toString());
        	//System.out.println("odometer after: "+odom.getText());
        }
        
        if(v.getId()==jaar.getId() && jaar.getText().toString().length()>0)
        	jaar.setText(jaar.getText().toString());
     } 
	public void show(){
		AlertDialog.Builder builder = new AlertDialog.Builder(ct);
        builder.setTitle("Enter a Car");
        builder.setMessage("You must have atleast 1 car profile before continuing.\n");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int id)
			{
				//Action for 'Ok' Button
			}
		});
		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int id)
		    {
				dialog.cancel();
		    }
		});
        builder.create();
        builder.show();
	}
	
	@Override
	public void onBackPressed() {
	    if(!ans)
	    	finish();
	}
}









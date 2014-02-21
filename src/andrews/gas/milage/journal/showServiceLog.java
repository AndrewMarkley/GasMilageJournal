package andrews.gas.milage.journal;

import java.util.ArrayList;
import andrews.gas.milage.journal.R;
import andrews.gas.milage.journal.ServiceLog;
import andrews.gas.milage.journal.ServiceLogDataBaseHandler;
import andrews.gas.milage.journal.Stats;
import andrews.gas.milage.journal.showFillUps;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class showServiceLog extends Activity
{
	ArrayList<ServiceLog> services = new ArrayList<ServiceLog>( );
	ListView listView = null;
	ServiceLogDataBaseHandler db = null;
	ArrayAdapter<CharSequence> adapter = null;
	String Data = null;
	int tempPos;
	String[] values = null;
	Button b5 = null;
	Button b6 = null;
	Button b4 = null;
	Button service_log = null;
	Button carP = null;
	Context cx = null;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate( savedInstanceState );
		setContentView( R.layout.show_service_log );
		cx = getApplicationContext( );
		// b4 = (Button) findViewById(R.id.car_service_log_button); //Fill up
		// screen
		b5 = (Button) findViewById( R.id.main_screen_button ); // all logs
		b6 = (Button) findViewById( R.id.stats_button ); // Stats!
		service_log = (Button) findViewById( R.id.car_p );
		b4.setBackgroundResource( R.drawable.service );
		b5.setBackgroundResource( R.drawable.book );
		b6.setBackgroundResource( R.drawable.stats );
		service_log.setBackgroundResource( R.drawable.car );
		carP = (Button) findViewById( R.id.car_p );

		TextView shop = (TextView) findViewById( R.id.service_shop_input );
		TextView odom = (TextView) findViewById( R.id.odometer_input );
		TextView work_done_other = (TextView) findViewById( R.id.work_done_other_input );
		TextView cost = (TextView) findViewById( R.id.cost_input );
		TextView comments = (TextView) findViewById( R.id.comments_input_field );
		TextView date_string = (TextView) findViewById( R.id.date_string_input );

		Button save = (Button) findViewById( R.id.save_button );
		Button cancel = (Button) findViewById( R.id.save_button );
		Button change_date = (Button) findViewById( R.id.pick_date_button );
		// Spinner spinner = (Spinner) findViewById(R.id.car_profiles_spinner);
		Button spin = (Button) findViewById( R.id.spin );
		spin.setText( "All Cars" );
		// need to implement this as a car profile object instead of services,
		// that goes in service log form
		/*
		 * String servicesArray[] = new String[]{"Oil Change", "New Tires",
		 * "Rotate Tires", "Brakes" }; adapter = new ArrayAdapter <CharSequence>
		 * (this, android.R.layout.simple_spinner_item, servicesArray);
		 * 
		 * spinner.setAdapter(adapter); listView = (ListView)
		 * findViewById(R.id.car_service_list); db = new
		 * ServiceLogDataBaseHandler(this); try{ services =
		 * (ArrayList<ServiceLog>) db.getAllServiceLogs(); } catch(Exception e){
		 * }
		 */
		/*
		 * //adapter = new ArrayAdapter<String>(this,
		 * android.R.layout.simple_list_item_1, android.R.id.text1, values);
		 * listView.setAdapter(adapter); listView.setTextFilterEnabled(true);
		 * 
		 * listView.setOnItemLongClickListener(new OnItemLongClickListener(){
		 * 
		 * @Override public boolean onItemLongClick(AdapterView<?> a, View view,
		 * int pos, long arg3) { tempPos = pos;
		 * 
		 * AlertDialog.Builder builder = new
		 * AlertDialog.Builder(a.getContext()); builder.setTitle("Options");
		 * String[] options = new String[] {"Edit","Delete"};
		 * 
		 * builder.setItems(options, new DialogInterface.OnClickListener() {
		 * 
		 * @Override public void onClick(DialogInterface dialog, int p) {
		 * if(p==0) { Context cont = getApplicationContext(); CharSequence text
		 * =
		 * "This feature has yet to be implemented, sorry for the inconvenience"
		 * ; Toast toast = Toast.makeText(cont, text, Toast.LENGTH_LONG);
		 * toast.show(); } else if(p==1) { ServiceLog c =
		 * services.remove(tempPos);
		 * System.out.println("REMOVING: "+c.toString()); db.deleteServiceLog(c,
		 * tempPos); services = (ArrayList<ServiceLog>) db.getAllServiceLogs();
		 * 
		 * //adapter = new ArrayAdapter<String>(adapter.getContext(),
		 * android.R.layout.simple_list_item_1, android.R.id.text1, values);
		 * adapter.notifyDataSetChanged(); listView.setAdapter(adapter);
		 * listView.refreshDrawableState(); }
		 * 
		 * } }); builder.create(); builder.show(); return true; } });
		 */
		b5.setOnClickListener( new View.OnClickListener( )
		{
			@Override
			public void onClick(View v)
			{
				Intent i = new Intent( getApplicationContext( ),
						showFillUps.class );
				finish( );
				startActivity( i );
			}
		} );
		/*
		 * spin.setOnClickListener(new View.OnClickListener() {
		 * 
		 * @Override public void onClick(View v) {
		 * 
		 * String[] items = {"All Cars","Car 1", "Car 2", "Car 3"};
		 * Log.i("here", "hello"); AlertDialog.Builder build = new
		 * AlertDialog.Builder(cx); build.setTitle("Choose a car");
		 * build.setItems(items, new DialogInterface.OnClickListener() {
		 * 
		 * @Override public void onClick(DialogInterface dialog, int p) {
		 * CharSequence text =
		 * "This feature has yet to be implemented, sorry for the inconvenience"
		 * ; Toast toast = Toast.makeText(cx, text, Toast.LENGTH_LONG);
		 * toast.show(); } }); build.create(); build.show(); } });
		 */

		b6.setOnClickListener( new View.OnClickListener( )
		{
			@Override
			public void onClick(View v)
			{
				Intent i = new Intent( getApplicationContext( ), Stats.class );
				finish( );
				startActivity( i );
			}
		} );
		carP.setOnClickListener( new View.OnClickListener( )
		{
			@Override
			public void onClick(View v)
			{
				Intent i = new Intent( getApplicationContext( ),
						showCarProfile.class );
				finish( );
				startActivity( i );
			}
		} );

	}

	@Override
	public void onBackPressed()
	{
		// Do Here what ever you want do on back press;
	}
}
package andrews.gas.milage.journal;
import android.app.TabActivity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TabHost;
import android.widget.TextView;

@SuppressWarnings("deprecation")
public class Controller extends TabActivity {
	TabHost tabHost;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);
        tabHost = getTabHost();
        TabHost.TabSpec spec = null;
        Intent intent;
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        intent=new Intent().setClass(Controller.this, showFillUps.class);
		spec = tabHost.newTabSpec("FillUp").setIndicator("Fill Up").setContent(intent);
        tabHost.addTab(spec);
        
        intent=new Intent().setClass(Controller.this, Stats.class);
		spec = tabHost.newTabSpec("Stats").setIndicator("Stats").setContent(intent);
        tabHost.addTab(spec);
        
        intent=new Intent().setClass(Controller.this, showCarProfile.class);
		spec = tabHost.newTabSpec("carP").setIndicator("Cars").setContent(intent);
        tabHost.addTab(spec);
        
        tabHost.setCurrentTab(0);
        getTabHost().getTabWidget().getChildAt(0).setBackgroundResource(R.drawable.book_6);
        getTabHost().getTabWidget().getChildAt(1).setBackgroundResource(R.drawable.stats_2);
        getTabHost().getTabWidget().getChildAt(2).setBackgroundResource(R.drawable.car_2);
        for(int i=0;i<tabHost.getTabWidget().getChildCount();i++) 
        { 
        	TextView tv = (TextView) tabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title); //Unselected Tabs
            tv.setTextColor(Color.parseColor("#ffffff"));
        } 
        //TextView tv = (TextView) tabHost.getCurrentTabView().findViewById(android.R.id.title); //for Selected Tab
        //tv.setTextColor(Color.parseColor("#000000"));
        
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.contact:
                contact();
                System.out.println("Contact me!");
                return true;
            case R.id.menu_settings:
                rate();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void contact() {
		Intent it = new Intent(Intent.ACTION_SEND);
		String[] tos = {"ajm501018@gmail.com"};
		it.putExtra(Intent.EXTRA_EMAIL, tos);
		it.putExtra(Intent.EXTRA_SUBJECT, "Gas Journal App Feedback");
		it.setType("text/plain");
		startActivity(it);
	}
    public void rate(){
    	//Intent intent = new Intent(Intent.ACTION_VIEW);
    	//intent.setData(Uri.parse("market://details?id=andrews.gas.milage.journal"));
    	try {
    	    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=andrews.gas.milage.journal")));
    	} catch (android.content.ActivityNotFoundException anfe) {
    	    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=andrews.gas.milage.journal")));
    	}
    }
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.activity_home_screen, menu); 
    	System.out.println("tada!");
    	return true;
    }
}

package andrews.gas.milage.journal;

import android.app.Activity;
import android.os.Bundle;

import com.google.analytics.tracking.android.EasyTracker;

public class GoogleAnalytics extends Activity
{
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	  }

	  @Override
	  public void onStart() {
	    super.onStart();
	    
	    EasyTracker.getInstance(this).activityStart(this);
	  }

	  @Override
	  public void onStop() {
	    super.onStop();
	    
	    EasyTracker.getInstance(this).activityStop(this);
	  }
}
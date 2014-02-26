package andrews.gas.milage.journal;

import com.google.ads.AdRequest;
import com.google.ads.AdView;

import android.app.Activity;
import android.view.View;

public class AdmobActivity extends Activity {

	AdView ad = null;
		
	protected void showAds(View root) {
	    AdRequest request = new AdRequest();
	    request.addTestDevice(AdRequest.TEST_EMULATOR);
	    ad.loadAd(request); 
	  }
    
	public void adView(int id){
    	ad = (AdView) findViewById(id);
	}
}

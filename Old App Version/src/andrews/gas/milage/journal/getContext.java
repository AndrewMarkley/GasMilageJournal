package andrews.gas.milage.journal;

import android.app.Application;
import android.content.Context;

public class getContext extends Application{

    private static Context context;

    public void onCreate(){
        super.onCreate();
        getContext.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return getContext.context;
    }
}
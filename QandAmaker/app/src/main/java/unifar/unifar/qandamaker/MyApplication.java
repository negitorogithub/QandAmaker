package unifar.unifar.qandamaker;

import android.app.Application;
import android.content.Context;

import com.squareup.leakcanary.LeakCanary;


public class MyApplication extends Application {
    public static Context context;

    public void onCreate(){
        super.onCreate();
        MyApplication.context = getApplicationContext();
        LeakCanary.install(this);
    }

    public static Context getAppContext() {
        return MyApplication.context;
    }
}

package unifar.unifar.qandamaker;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import com.squareup.leakcanary.LeakCanary;


public class MyApplication extends Application {
    public static Context context;
    public static Bundle bundle;
    public void onCreate(){
        super.onCreate();
        MyApplication.context = getApplicationContext();
        LeakCanary.install(this);
        bundle = new Bundle();
        bundle.putString("str_tag_name", "");
        bundle.putBoolean("isRecreated", false);
        bundle.putBoolean("isEditMode", false);
    }

    public static Context getAppContext() {
        return MyApplication.context;
    }
}

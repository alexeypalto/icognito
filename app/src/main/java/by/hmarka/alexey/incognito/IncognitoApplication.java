package by.hmarka.alexey.incognito;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.squareup.otto.Bus;

public class  IncognitoApplication extends Application {

    public static Bus bus = new Bus();

    private static IncognitoApplication instance;

    public static IncognitoApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

}

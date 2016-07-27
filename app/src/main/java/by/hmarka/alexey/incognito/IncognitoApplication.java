package by.hmarka.alexey.incognito;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.squareup.otto.Bus;

public class  IncognitoApplication extends android.support.multidex.MultiDexApplication{

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


}

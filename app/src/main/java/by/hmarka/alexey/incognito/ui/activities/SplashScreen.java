package by.hmarka.alexey.incognito.ui.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.squareup.otto.Subscribe;

import java.util.Locale;

import by.hmarka.alexey.incognito.IncognitoApplication;
import by.hmarka.alexey.incognito.R;
import by.hmarka.alexey.incognito.entities.requests.RegisterDeviceRequest;
import by.hmarka.alexey.incognito.events.LocationReadyEvent;
import by.hmarka.alexey.incognito.rest.RestClient;
import by.hmarka.alexey.incognito.utils.Constants;
import by.hmarka.alexey.incognito.utils.SharedPreferenceHelper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Alexey on 22.06.2016.
 */
public class SplashScreen extends BaseAppCompatActivity {

    private final int SPLASH_DISPLAY_LENGTH = 500;
    private static String access;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.splash_screen);

        String language = Locale.getDefault().getLanguage() + "_" + Locale.getDefault().getCountry();
        Log.i("Language", language);
        SharedPreferenceHelper.setLanguage("ru_RU");
        SharedPreferenceHelper.setAccessType(getNetworkType());
        //sendRequest();
        //   SharedPreferenceHelper.setRadius("100000000");
//        TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
//        SharedPreferenceHelper.setImei(String.valueOf(tm.getDeviceId()));
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);

    }

    @Override
    public void onStart() {
        super.onStart();
        IncognitoApplication.bus.register(this);
        if (ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.READ_PHONE_STATE ) != PackageManager.PERMISSION_GRANTED
                ||ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        }
        else
        {
            TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
            SharedPreferenceHelper.setImei(String.valueOf(tm.getDeviceId()));
            getLocation(Constants.REQUEST_CODE_SPLASH_ACTIVITY_GET_LOCATION);
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        IncognitoApplication.bus.unregister(this);
    }

    private void sendRequest() {
        Call<ResponseBody> call = RestClient.getServiceInstance().registerDevice(getDeviceRegisterRequest());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent mainIntent = new Intent(SplashScreen.this, MainActivity.class);
                            SplashScreen.this.startActivity(mainIntent);
                            SplashScreen.this.finish();
                        }
                    }, SPLASH_DISPLAY_LENGTH);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                {
                    Toast.makeText(getApplicationContext(),"fail to register",Toast.LENGTH_LONG).show();
                    //startResolutionForResult(this, 1000);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent mainIntent = new Intent(SplashScreen.this, MainActivity.class);
                            SplashScreen.this.startActivity(mainIntent);
                            SplashScreen.this.finish();
                        }
                    }, SPLASH_DISPLAY_LENGTH);
                }
            }
        });
    }

    private RegisterDeviceRequest getDeviceRegisterRequest() {
        RegisterDeviceRequest registerDeviceRequest = new RegisterDeviceRequest();
        registerDeviceRequest.setImei(SharedPreferenceHelper.getImei());
        registerDeviceRequest.setLanguage("ru_RU");
        registerDeviceRequest.setLocation_lat(SharedPreferenceHelper.getLocationLattitude());
        registerDeviceRequest.setLocation_long(SharedPreferenceHelper.getLocationLongitude());
        registerDeviceRequest.setAccess_type("mobile");
        return registerDeviceRequest;
    }

    @Subscribe
    public void onLocationReady(LocationReadyEvent event) {
        for (Integer code : event.getRequestCodes()) {
            switch (code) {
                case Constants.REQUEST_CODE_SPLASH_ACTIVITY_GET_LOCATION:
                    SharedPreferenceHelper.setLocationLattitude(String.valueOf(event.getLocation().getLatitude()));
                    SharedPreferenceHelper.setLocationLongitude(String.valueOf(event.getLocation().getLongitude()));
                    sendRequest();
                    break;
            }
        }
    }

    public String getNetworkType() {
        Activity act = this;
        String network_type = "UNKNOWN";//maybe usb reverse tethering
        NetworkInfo active_network = ((ConnectivityManager) act.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (active_network != null && active_network.isConnectedOrConnecting()) {
            if (active_network.getType() == ConnectivityManager.TYPE_WIFI) {
                network_type = "wifi";
            } else if (active_network.getType() == ConnectivityManager.TYPE_MOBILE) {
                network_type = "mobile";
               // network_type = ((ConnectivityManager) act.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo().getSubtypeName();
            }
        }
        return network_type;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
                SharedPreferenceHelper.setImei(String.valueOf(tm.getDeviceId()));

                getLocation(Constants.REQUEST_CODE_SPLASH_ACTIVITY_GET_LOCATION);
            }
            else{
                Toast.makeText(this,"permission not granted",Toast.LENGTH_LONG).show();
            }
        }
    }

}
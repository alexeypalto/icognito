package by.hmarka.alexey.incognito.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.view.View;

import com.squareup.otto.Subscribe;

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
public class SplashScreen extends BaseAppCompatActivity{

    private final int SPLASH_DISPLAY_LENGTH = 500;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.splash_screen);
     //   sendRequest();
        SharedPreferenceHelper.setRadius("100000000");
        TelephonyManager tm =(TelephonyManager)getSystemService(TELEPHONY_SERVICE);
        SharedPreferenceHelper.setImei(String.valueOf(tm.getDeviceId()));
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
        getLocation(Constants.REQUEST_CODE_SPLASH_ACTIVITY_GET_LOCATION);
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

            }
        });
    }

    private RegisterDeviceRequest getDeviceRegisterRequest() {
        RegisterDeviceRequest registerDeviceRequest = new RegisterDeviceRequest();
        registerDeviceRequest.setImei("32131");
        registerDeviceRequest.setLanguage("ru_RU");
        registerDeviceRequest.setLocation_lat("2");
        registerDeviceRequest.setLocation_long("32");
        registerDeviceRequest.setAccess_type("mobile");
        return registerDeviceRequest;
    }

    @Subscribe
    public void onLocationReady(LocationReadyEvent event) {
        for (Integer code : event.getRequestCodes()) {
            switch (code) {
                case Constants.REQUEST_CODE_SPLASH_ACTIVITY_GET_LOCATION:
                    sendRequest();
                    break;
            }
        }
    }

}
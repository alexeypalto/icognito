package by.hmarka.alexey.incognito.ui.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.HashSet;

import by.hmarka.alexey.incognito.utils.LocationHelper;

/**
 * Created by lashket on 7.7.16.
 */
public class BaseAppCompatActivity extends AppCompatActivity {


    private static final String TAG = BaseAppCompatActivity.class.getName();

    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1301;
    private static final int REQUEST_CODE_LOCATION_PROVIDERS = 1302;

    private LocationHelper locationHelper;
    private HashSet<Integer> requestCodes = new HashSet<>();

    public void getLocation(int requestCode) {
        Log.i(TAG, "location check: getLocation request code = " + requestCode + " request codes = " + requestCodes);
        if (requestCode != -1) {
            requestCodes.add(requestCode);
        }
        if (locationHelper == null) {
            locationHelper = new LocationHelper(this);
        }
        try {
            locationHelper.checkLocationConditions();
            locationHelper.requestLocation(requestCodes);
            requestCodes.clear();
        } catch (LocationHelper.LocationProvidersNotAvailableException e) {
        } catch (LocationHelper.LocationPPermissionsNotGrantedException e) {
            Log.i(TAG, "location permission not granted. starting LocationPermissionNotGrantedActivity");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG, "onActivityResult requestCode = " + requestCode + " resultCode = " + resultCode);
        switch (requestCode) {
            case REQUEST_CODE_LOCATION_PROVIDERS:
            case REQUEST_CODE_LOCATION_PERMISSION:
                // Because by this time we have already put request code to collection, we are passing -1.
                getLocation(-1);
                break;
        }
    }
}

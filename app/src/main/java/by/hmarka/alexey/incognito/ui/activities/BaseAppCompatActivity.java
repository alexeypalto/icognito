package by.hmarka.alexey.incognito.ui.activities;

import android.content.Intent;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.util.HashSet;

import by.hmarka.alexey.incognito.utils.LocationHelper;

/**
 * Created by lashket on 7.7.16.
 */
public class BaseAppCompatActivity extends AppCompatActivity {

    private MaterialDialog materialDialog;

    private static final String TAG = BaseAppCompatActivity.class.getName();

    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1301;
    private static final int REQUEST_CODE_LOCATION_PROVIDERS = 1302;
    private static final int REQUEST_CODE_TURN_ON_LOCATION_SETTINGS = 5000;
    private int MAIN_REQUEST_CODE;

    private MaterialDialog.Builder builder;

    private LocationHelper locationHelper;
    private HashSet<Integer> requestCodes = new HashSet<>();

    public void getLocation(int requestCode) {
        MAIN_REQUEST_CODE = requestCode;
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
            builder = new MaterialDialog.Builder(this)
                    .title("Ошибка")
                    .content("Для дальнейшей работы с приложением включите определение местоположения в настройках устройства")
                    .cancelable(false)
                    .positiveText("OK")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivityForResult(intent, REQUEST_CODE_TURN_ON_LOCATION_SETTINGS);
                        }
                    });
            materialDialog = builder.build();
            materialDialog.show();
        } catch (LocationHelper.LocationPPermissionsNotGrantedException e) {
            Log.i(TAG, "location permission not granted.");
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
            case REQUEST_CODE_TURN_ON_LOCATION_SETTINGS:
                if (LocationHelper.isLocationProvidersAvailable(this)) {
                    materialDialog.dismiss();
                    getLocation(-1);
                } else {
                    materialDialog.show();
                }
                break;
        }
    }
}

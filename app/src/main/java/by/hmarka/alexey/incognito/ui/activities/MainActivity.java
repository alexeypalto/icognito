package by.hmarka.alexey.incognito.ui.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import by.hmarka.alexey.incognito.R;
import by.hmarka.alexey.incognito.services.RegistrationIntentService;
import by.hmarka.alexey.incognito.ui.adapters.CustomPagerAdapter;
import by.hmarka.alexey.incognito.ui.fragments.AddFragment;
import by.hmarka.alexey.incognito.ui.fragments.FavoritesFragment;
import by.hmarka.alexey.incognito.ui.fragments.HomeFragment;
import by.hmarka.alexey.incognito.ui.fragments.NewsFragment;
import by.hmarka.alexey.incognito.ui.fragments.SettingsFragment;
import by.hmarka.alexey.incognito.utils.Constants;

/**
 * Created by Alexey on 22.06.2016.
 */
public class MainActivity extends AppCompatActivity {
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private CustomPagerAdapter adapter;

    private BroadcastReceiver mRegistrationBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();

        registrGCMRessiver();
    }

    private void findViews() {
        tabLayout = (TabLayout) findViewById(R.id.mainTabLayout);
        viewPager = (ViewPager) findViewById(R.id.mainViewPager);
        setupViewPager();
    }

    private void setupViewPager() {
        adapter = new CustomPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new HomeFragment(), getString(R.string.fragment_home));
        adapter.addFragment(new NewsFragment(), getString(R.string.fragment_news));
        adapter.addFragment(new AddFragment(), getString(R.string.fragment_add));
        adapter.addFragment(new FavoritesFragment(), getString(R.string.fragment_favorite));
        adapter.addFragment(new SettingsFragment(), getString(R.string.fragment_settings));
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setOffscreenPageLimit(5);
        setupTabLayout();
    }

    private void setupTabLayout() {
        tabLayout.getTabAt(0).setIcon(R.drawable.selector_tab_home);
        tabLayout.getTabAt(1).setIcon(R.drawable.selector_tab_categories);
        tabLayout.getTabAt(2).setCustomView(R.layout.tab_add);
        tabLayout.getTabAt(3).setIcon(R.drawable.selector_tab_favorite);
        tabLayout.getTabAt(4).setIcon(R.drawable.selector_tab_settings);
//        tabLayout.getTabAt(0).getCustomView().setSelected(true);
    }

    private void registrGCMRessiver() {
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //mRegistrationProgressBar.setVisibility(ProgressBar.GONE);
                Log.d("MainActivity", "registered.");
                SharedPreferences sharedPreferences =
                        PreferenceManager.getDefaultSharedPreferences(context);
                boolean sentToken = sharedPreferences
                        .getBoolean(Constants.SENT_TOKEN_TO_SERVER, false);
                if (sentToken) {
                    // mInformationTextView.setText(getString(R.string.gcm_send_message));
                } else {
                    //mInformationTextView.setText(getString(R.string.token_error_message));
                }
            }
        };

        registerReceiver();

        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }
    }
    private boolean isReceiverRegistered;
    private void registerReceiver(){
        if(!isReceiverRegistered) {
            LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                    new IntentFilter(Constants.REGISTRATION_COMPLETE));
            isReceiverRegistered = true;
        }
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                //Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }


}

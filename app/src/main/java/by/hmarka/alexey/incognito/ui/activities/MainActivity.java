package by.hmarka.alexey.incognito.ui.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.squareup.otto.Subscribe;

import by.hmarka.alexey.incognito.IncognitoApplication;
import by.hmarka.alexey.incognito.R;
import by.hmarka.alexey.incognito.customcontrols.CustomTabLayout;
import by.hmarka.alexey.incognito.events.ShowCommentsInFavoriteFragment;
import by.hmarka.alexey.incognito.services.RegistrationIntentService;
import by.hmarka.alexey.incognito.ui.adapters.CustomPagerAdapter;
import by.hmarka.alexey.incognito.ui.fragments.FavoritesFragment;
import by.hmarka.alexey.incognito.ui.fragments.HomeFragment;
import by.hmarka.alexey.incognito.ui.fragments.NewsFragment;
import by.hmarka.alexey.incognito.ui.fragments.SettingsFragment;
import by.hmarka.alexey.incognito.utils.Constants;

/**
 * Created by Alexey on 22.06.2016.
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener, GestureDetector.OnGestureListener {
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    private CustomTabLayout tabLayout;
    //private TabLayout tabLayout;
    private ViewPager viewPager;
    private CustomPagerAdapter adapter;
    private RelativeLayout commentsLayout;
    private LinearLayout commentsList;
    private ImageView arrowUpComments;
    private boolean isShowingFullList = false;

    private BroadcastReceiver mRegistrationBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();

        registrGCMRessiver();
    }

    private void findViews() {
        tabLayout = (CustomTabLayout) findViewById(R.id.mainTabLayout);
        //tabLayout = (TabLayout) findViewById(R.id.mainTabLayout);
        viewPager = (ViewPager) findViewById(R.id.mainViewPager);
        commentsLayout = (RelativeLayout) findViewById(R.id.comments_layout);
        commentsList = (LinearLayout) findViewById(R.id.comments_list_layout);
        arrowUpComments = (ImageView) findViewById(R.id.arrow);
        arrowUpComments.setOnClickListener(this);
        setupViewPager();
    }

    private void setupViewPager() {
        adapter = new CustomPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new HomeFragment(), "home");
        adapter.addFragment(new NewsFragment(), "news");
        //adapter.addFragment(new AddFragment(), "add");
        adapter.addFragment(new FavoritesFragment(), "favorite");
        adapter.addFragment(new SettingsFragment(), "settings");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        viewPager.setOffscreenPageLimit(4);
        setupTabLayout();

        ImageButton but = new ImageButton(this);
        but.setImageResource(R.drawable.selector_tab_add);
        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addActivityIntent = new Intent(getApplicationContext(),AddPostActivity.class);
                startActivity(addActivityIntent);
            }
        });
        tabLayout.addCentralTab(but);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.arrow:
                if (!isShowingFullList) {
                    commentsList.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                    isShowingFullList = true;
                } else {
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 350);
                    params.setMarginStart(0);
                    commentsList.setLayoutParams(params);
                    isShowingFullList = false;
                }
                break;
        }
    }

    private void setupTabLayout() {
        tabLayout.getTabAt(0).setIcon(R.drawable.selector_tab_home);
        tabLayout.getTabAt(1).setIcon(R.drawable.selector_tab_categories);
        //tabLayout.getTabAt(2).setCustomView(R.layout.tab_add);
        tabLayout.getTabAt(2).setIcon(R.drawable.selector_tab_favorite);
        tabLayout.getTabAt(3).setIcon(R.drawable.selector_tab_settings);
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


    @Override
    public void onStart() {
        super.onStart();
        IncognitoApplication.bus.register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        IncognitoApplication.bus.unregister(this);
    }

    @Subscribe
    public void showComments(ShowCommentsInFavoriteFragment event) {
        commentsLayout.setVisibility(View.VISIBLE);
        commentsList.animate()
                .translationY(0)
                .alpha(1.0f)
                .setDuration(3000)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        super.onAnimationStart(animation);
                        commentsList.setVisibility(View.VISIBLE);
                    }
                });
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        switch (getSlope(e1.getX(), e1.getY(), e2.getX(), e2.getY())) {
            case 1:
                return true;
            case 2:
                return true;
            case 3:
                return true;
            case 4:
                return true;
        }
        return false;
    }

    private int getSlope(float x1, float y1, float x2, float y2) {
        Double angle = Math.toDegrees(Math.atan2(y1 - y2, x2 - x1));
        if (angle > 45 && angle <= 135)
            // top
            return 1;
        if (angle >= 135 && angle < 180 || angle < -135 && angle > -180)
            // left
            return 2;
        if (angle < -45 && angle>= -135)
            // down
            return 3;
        if (angle > -45 && angle <= 45)
            // right
            return 4;
        return 0;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onBackPressed() {
        if (isShowingFullList) {
            commentsLayout.setVisibility(View.GONE);
            commentsList.setVisibility(View.GONE);
        }
    }
}

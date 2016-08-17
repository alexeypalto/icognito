package by.hmarka.alexey.incognito.ui.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.server.converter.StringToIntConverter;
import com.google.gson.Gson;
import com.squareup.otto.Subscribe;

import java.io.IOException;
import java.util.ArrayList;

import by.hmarka.alexey.incognito.IncognitoApplication;
import by.hmarka.alexey.incognito.R;
import by.hmarka.alexey.incognito.customcontrols.CustomTabLayout;
import by.hmarka.alexey.incognito.entities.Comment;
import by.hmarka.alexey.incognito.entities.CommentsWrapper;
import by.hmarka.alexey.incognito.events.ShowCommentsInFavoriteFragment;
import by.hmarka.alexey.incognito.rest.RestClient;
import by.hmarka.alexey.incognito.services.RegistrationIntentService;
import by.hmarka.alexey.incognito.ui.adapters.CommentsAdapter;
import by.hmarka.alexey.incognito.ui.adapters.CustomPagerAdapter;
import by.hmarka.alexey.incognito.ui.fragments.FavoritesFragment;
import by.hmarka.alexey.incognito.ui.fragments.HomeFragment;
import by.hmarka.alexey.incognito.ui.fragments.NewsFragment;
import by.hmarka.alexey.incognito.ui.fragments.SettingsFragment;
import by.hmarka.alexey.incognito.utils.Constants;
import by.hmarka.alexey.incognito.utils.Helpers;
import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    private boolean isShowingComments = false;
    private EmojiconEditText comment;
    private ImageView sendCommentButton;
    private String postCommentId;
    private Helpers helpers = new Helpers();
    private RecyclerView commentsRecyclerView;
    private CommentsAdapter commentsAdapter;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private boolean isShowingFullScreenComments= false;
    private ImageView smile;
    private EmojIconActions  emojIcon;
    private static final String DEBUG_TAG = MainActivity.class.getSimpleName();

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
        commentsLayout.setOnClickListener(this);
        commentsList = (LinearLayout) findViewById(R.id.comments_list_layout);
        comment = (EmojiconEditText) findViewById(R.id.comment_editor);
        sendCommentButton = (ImageView) findViewById(R.id.send_button);
        commentsRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        arrowUpComments = (ImageView) findViewById(R.id.arrow);
        sendCommentButton.setOnClickListener(this);
        arrowUpComments.setOnClickListener(this);
        smile = (ImageView) findViewById(R.id.smile_button) ;
        smile.setOnClickListener(this);
        setupViewPager();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        commentsRecyclerView.setLayoutManager(linearLayoutManager);
        commentsAdapter = new CommentsAdapter(MainActivity.this,new ArrayList<Comment>());
        commentsRecyclerView.setAdapter(commentsAdapter);
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
        emojIcon = new EmojIconActions(this,commentsList,comment,smile,"#495C66","#DCE1E2","#E6EBEF");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.smile_button:
                emojIcon.ShowEmojIcon();
                break;
            case R.id.arrow:
                if (!isShowingFullScreenComments) {
//                commentsLayout.setVisibility(View.GONE);
//                commentsList.setVisibility(View.GONE);
//                isShowingComments = false;
                  //  commentsList.setY(0);
                    ValueAnimator arrowRotationAnimator = ValueAnimator.ofFloat(0f, -180f);
                    arrowRotationAnimator.setDuration(300);
                    arrowRotationAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator valueAnimator) {
                            arrowUpComments.setRotation((Float) valueAnimator.getAnimatedValue());
                        }
                    });
                    arrowRotationAnimator.setInterpolator(new AccelerateInterpolator());
                    arrowRotationAnimator.start();
//                    commentsList.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                    isShowingFullScreenComments = true;
                } else {
//                    commentsList.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Math.round(helpers.convertDpToPixel(350, this))));
//                    commentsList.setY(commentsLayout.getHeight() - helpers.convertDpToPixel(350, this));
                    ValueAnimator arrowRotationAnimator = ValueAnimator.ofFloat(-180f, 0f);
                    arrowRotationAnimator.setDuration(300);
                    arrowRotationAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator valueAnimator) {
                            arrowUpComments.setRotation((Float) valueAnimator.getAnimatedValue());
                        }
                    });
                    arrowRotationAnimator.setInterpolator(new AccelerateInterpolator());
                    arrowRotationAnimator.start();
//                    commentsList.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Math.round(helpers.convertDpToPixel(350, this))));
//                    commentsList.setY(commentsLayout.getHeight() - helpers.convertDpToPixel(350, this));
//                   RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//                    commentsLayout.setLayoutParams(layoutParams);
                    isShowingFullScreenComments = false;
                }
                break;
            case R.id.send_button:
                sendComment();
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
                .alpha(1.0f)
                .setDuration(3000)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        super.onAnimationStart(animation);
                        commentsList.setVisibility(View.VISIBLE);
                    }
                });
        postCommentId = event.getPostId();
        getCommentsList();
        isShowingComments = true;
    }

    private void getCommentsList() {
        Call<ResponseBody> call = RestClient.getServiceInstance().getListComments(helpers.getCommentsListRequest(postCommentId));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    String responseString = "";
                    try {
                        responseString = response.body().string();
                        CommentsWrapper commentsWrapper = new CommentsWrapper();
                        commentsWrapper = new Gson().fromJson(responseString, CommentsWrapper.class);
                        commentsAdapter = new CommentsAdapter(MainActivity.this,(ArrayList<Comment>) commentsWrapper.getComments());
                        commentsRecyclerView.setAdapter(commentsAdapter);
                    } catch (IOException e) {

                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

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
        isShowingFullScreenComments = false;
        if (isShowingComments) {
            commentsLayout.setVisibility(View.GONE);
            commentsList.setVisibility(View.GONE);
        }
    }

    private void sendComment() {
        Call<ResponseBody> call = RestClient.getServiceInstance().sendComment(helpers.getLeaveReviewRequest(postCommentId, comment.getText().toString()));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Comment commentResponse = new Comment();
                    commentResponse.setComment_text(comment.getText().toString());
                    commentResponse.setComment_timestamp(getString(R.string.just_now));
                    commentsAdapter.add(commentResponse);
                    comment.setText("");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

}

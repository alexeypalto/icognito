package by.hmarka.alexey.incognito.ui.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import by.hmarka.alexey.incognito.IncognitoApplication;
import by.hmarka.alexey.incognito.R;
import by.hmarka.alexey.incognito.entities.Comment;
import by.hmarka.alexey.incognito.entities.CommentsWrapper;
import by.hmarka.alexey.incognito.entities.Post;
import by.hmarka.alexey.incognito.events.ShowCommentsInFavoriteFragment;
import by.hmarka.alexey.incognito.rest.RestClient;
import by.hmarka.alexey.incognito.ui.adapters.CommentsAdapter;
import by.hmarka.alexey.incognito.utils.Constants;
import by.hmarka.alexey.incognito.utils.Helpers;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by lashket on 5.7.16.
 */
public class PostActivity extends AppCompatActivity implements View.OnClickListener,GestureDetector.OnGestureListener {

    private Toolbar toolbar;
    private String postId;
    private TextView postText;
    private ImageView postImageView;
    private ImageView iconPlay;
    private TextView countLike;
    private TextView countShare;
    private TextView commentCount;
    private Post post;
    private Menu menu;
    private ImageView share;
    private ScrollView mainLayout;
    private LinearLayout likesLayout;
    private LinearLayout addLike;
    private LinearLayout removeLike;

    private RelativeLayout commentsLayout;
    private LinearLayout commentsList;
    private ImageView arrowUpComments;
    private ImageView iconComments, iconShare;
    private boolean isShowingComments = false;
    private EditText comment;
    private ImageView sendCommentButton;
    private String postCommentId;
    private RecyclerView commentsRecyclerView;
    private CommentsAdapter commentsAdapter;
    private GestureDetector mGestureDetector;


    private List<Media> visualContent;
    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 100;

    private int position;

    private Helpers helpers = new Helpers();
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        postId = getIntent().getStringExtra("POSTID");
        findViews();
        request();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        mGestureDetector = new GestureDetector(this, this);
    }


    private void findViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        postText = (TextView) findViewById(R.id.postText);
        postImageView = (ImageView) findViewById(R.id.post_image);
        iconPlay = (ImageView) findViewById(R.id.play_img);
        countLike = (TextView) findViewById(R.id.postLikeCount);
        countShare = (TextView) findViewById(R.id.postShareCount);
        commentCount = (TextView) findViewById(R.id.postCountComments);
        iconComments = (ImageView) findViewById(R.id.icon_comments);
        likesLayout = (LinearLayout) findViewById(R.id.likeLayout);
        addLike = (LinearLayout) findViewById(R.id.plus);
        removeLike = (LinearLayout) findViewById(R.id.minus);
        mainLayout = (ScrollView) findViewById(R.id.mainLayout);

        mainLayout.setOnClickListener(this);
        removeLike.setOnClickListener(this);
        addLike.setOnClickListener(this);
        likesLayout.setOnClickListener(this);
        countLike.setOnClickListener(this);


        iconComments.setOnClickListener(this);
        iconShare = (ImageView) findViewById(R.id.icon_share);
        iconShare.setOnClickListener(this);

        toolbarSettings(toolbar);

        commentsLayout = (RelativeLayout) findViewById(R.id.comments_layout);
        commentsLayout.setOnClickListener(this);

        commentsList = (LinearLayout) findViewById(R.id.comments_list_layout);
        comment = (EditText) findViewById(R.id.comment_editor);
        sendCommentButton = (ImageView) findViewById(R.id.send_button);
        commentsRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        arrowUpComments = (ImageView) findViewById(R.id.arrow);
        sendCommentButton.setOnClickListener(this);
        arrowUpComments.setOnClickListener(this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        commentsRecyclerView.setLayoutManager(linearLayoutManager);

        postImageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return mGestureDetector.onTouchEvent(motionEvent);
            }
        } );
    }

    private void setUi() {
        postText.setText(post.getPost_text());
        countLike.setText(post.getLike_count());
        if (post.getComments() != null) {
            commentCount.setText(String.valueOf(post.getComments().size()));
        } else {
            commentCount.setText("0");
        }
        countShare.setText(post.getShares_count());
        if (post.getIsFavorite().equals("1")) {
            menu.findItem(R.id.add_post_to_favorites).setIcon(R.drawable.favorit_active);
        }

        visualContent = new ArrayList<>();
        if(post.getVideoIds()!=null) {
            for (String videoId : post.getVideoIds()) {
                String imgUrl = "http://img.youtube.com/vi/"+ videoId + "/0.jpg";
                Uri uri = Uri.parse(imgUrl);
                visualContent.add(new Media( uri, videoId,Media.VIDEO));
            }
        }
        if(post.getPostImages()!=null) {
            for (String path : post.getPostImages()) {
                Uri uri = Uri.parse(Constants.BASE_URL + path.substring(1));
                visualContent.add(new Media( uri,Media.IMAGE));
            }
        }

        if(visualContent.size()>0){
            setPostImage(visualContent.get(0));
        }
    }

    private void toolbarSettings(Toolbar toolbar) {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.add_post_to_favorites:
                if (post.getIsFavorite().equals("1")) {
                    removePostFromFavorites(postId);
                } else {
                    addPostToFavorites(postId);
                }
                return true;
            case R.id.send_button:
                sendComment();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.full_post_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        this.menu = menu;
        return super.onPrepareOptionsMenu(menu);
    }

    public static void show(Context context, String postId) {
        Intent intent = new Intent(context, PostActivity.class);
        intent.putExtra("POSTID", postId);
        context.startActivity(intent);
    }

    private void request() {
        Call<ResponseBody> call = RestClient.getServiceInstance().getFullPost(helpers.getPostRequest(postId));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    String responseString = "";
                    try {
                        responseString = response.body().string();
                        post = new Gson().fromJson(responseString, Post.class);
                        setUi();
                    } catch (IOException e) {

                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }


    private void addPostToFavorites(String postId) {
        Call<ResponseBody> call = RestClient.getServiceInstance().addPostToFavorite(helpers.getAddPostToFavoriteRequest(postId));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    menu.findItem(R.id.add_post_to_favorites).setIcon(R.drawable.favorit_active);
                    post.setIsFavorite("1");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private void removePostFromFavorites(String postId) {
        Call<ResponseBody> call = RestClient.getServiceInstance().addPostToFavorite(helpers.getRemovePostToFavoriteRequest(postId));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    menu.findItem(R.id.add_post_to_favorites).setIcon(R.drawable.favorite_bot);
                    post.setIsFavorite("-1");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private void addShare() {
        Call<ResponseBody> call = RestClient.getServiceInstance().addShareToPost(helpers.getShareRequest(post.getPost_id(), post.getPost_text()));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                int count = Integer.parseInt(countShare.getText().toString());
                count = count + 1;
                countShare.setText(String.valueOf(count));
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.icon_comments:
                showComments();
//                IncognitoApplication.bus.post(new ShowCommentsInFavoriteFragment(post.getPost_id()));
                break;
            case R.id.icon_share:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, post.getPost_text());
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                addShare();
            case R.id.arrow:
                commentsLayout.setVisibility(View.GONE);
                commentsList.setVisibility(View.GONE);
                isShowingComments = false;
                break;
            case R.id.send_button:
                sendComment();
                break;
            case R.id.plus:
                addLike(countLike, true, postId);
                likesLayout.setVisibility(View.GONE);
                break;
            case R.id.minus:
                addLike(countLike, false, postId);
                likesLayout.setVisibility(View.GONE);
                break;
            case R.id.postLikeCount:
                if (likesLayout.getVisibility() == View.VISIBLE) {
                    likesLayout.setVisibility(View.GONE);
                } else {
                    likesLayout.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    private void showComments() {
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
//        getCommentsList();
        commentsAdapter = new CommentsAdapter(PostActivity.this, (ArrayList<Comment>) post.getComments());
        commentsRecyclerView.setAdapter(commentsAdapter);
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
                        commentsAdapter = new CommentsAdapter(PostActivity.this, (ArrayList<Comment>) commentsWrapper.getComments());
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
    public void onBackPressed() {
        if (isShowingComments) {
            commentsLayout.setVisibility(View.GONE);
            commentsList.setVisibility(View.GONE);
            isShowingComments = false;
        }
        else
            super.onBackPressed();
    }

    private void sendComment() {
        Call<ResponseBody> call = RestClient.getServiceInstance().sendComment(helpers.getLeaveReviewRequest(post.getPost_id(), comment.getText().toString()));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Comment commentResponse = new Comment();
                    commentResponse.setComment_text(comment.getText().toString());
                    commentsAdapter.add(commentResponse);
                    comment.setText("");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
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

    public void setPostImage(final Media media) {
        Picasso.with(this)
                .load(media.path)
                .resize(postImageView.getWidth(), postImageView.getHeight())
                .centerCrop()
                .into(postImageView, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        if(media.type == Media.VIDEO) {
                            iconPlay.setVisibility(View.VISIBLE);
                        }else {
                            iconPlay.setVisibility(View.INVISIBLE);
                        }
                    }

                    @Override
                    public void onError() {

                    }
                });
    }

    public void setPositionNext() {
        position++;
        if (position > visualContent.size() - 1) {
            position = 0;
        }
    }

    public void setPositionPrev() {
        position--;
        if (position < 0) {
            position = visualContent.size() - 1;
        }
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        try {
            if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
                return false;
            // shift left
            if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
                    && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                setPositionNext();
                setPostImage(visualContent.get(position));
                //shift right
            } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
                    && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                setPositionPrev();
                setPostImage(visualContent.get(position));
            }
        } catch (Exception e) {
            // nothing
            return true;
        }
        return true;
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return true;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        Media currentContent = visualContent.get(position);
        if(currentContent.type==Media.VIDEO) {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + currentContent.videoId));
                startActivity(intent);
            } catch (ActivityNotFoundException ex) {
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://www.youtube.com/watch?v=" + currentContent.videoId));
                startActivity(intent);
            }
        }else{
            //show full screen image
        }
        return true;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {
    }

    private void addLike(final TextView textView, final boolean isLike, String postId) {
        Call<ResponseBody> call = RestClient.getServiceInstance().addLike(helpers.getAddLikeRequest(isLike, postId));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    if (isLike) {
                        textView.setText(String.valueOf(Integer.parseInt(textView.getText().toString()) + 1));
                    } else {
                        textView.setText(String.valueOf(Integer.parseInt(textView.getText().toString()) - 1));
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }
    class Media{
        public Media(Uri path, int type){
            this.path = path;
            this.type = type;

        }
        public Media(Uri path,String videoId, int type){
            this.path = path;
            this.type = type;
            this.videoId= videoId;
        }

        public static final int IMAGE = 0;
        public static final int VIDEO = 1;
        public Uri path;
        public int type = 0;
        public String videoId;
    }
}

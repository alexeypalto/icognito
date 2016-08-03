package by.hmarka.alexey.incognito.ui.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;
import com.squareup.otto.Subscribe;

import java.io.IOException;
import java.util.ArrayList;

import by.hmarka.alexey.incognito.IncognitoApplication;
import by.hmarka.alexey.incognito.R;
import by.hmarka.alexey.incognito.entities.Comment;
import by.hmarka.alexey.incognito.entities.CommentsWrapper;
import by.hmarka.alexey.incognito.entities.Post;
import by.hmarka.alexey.incognito.events.ShowCommentsInFavoriteFragment;
import by.hmarka.alexey.incognito.rest.RestClient;
import by.hmarka.alexey.incognito.ui.adapters.CommentsAdapter;
import by.hmarka.alexey.incognito.utils.Helpers;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by lashket on 5.7.16.
 */
public class PostActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private String postId;
    private TextView postText;
    private TextView countLike;
    private TextView countShare;
    private TextView commentCount;
    private Post post;
    private Menu menu;
    private ImageView share;

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

    }

    private void findViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        postText = (TextView) findViewById(R.id.postText);
        countLike = (TextView) findViewById(R.id.postLikeCount);
        countShare = (TextView) findViewById(R.id.postShareCount);
        commentCount = (TextView) findViewById(R.id.postCountComments);
        iconComments = (ImageView) findViewById(R.id.icon_comments);
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

}

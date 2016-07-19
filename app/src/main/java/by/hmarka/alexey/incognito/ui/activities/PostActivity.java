package by.hmarka.alexey.incognito.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.IOException;

import by.hmarka.alexey.incognito.entities.Post;
import by.hmarka.alexey.incognito.rest.RestClient;
import by.hmarka.alexey.incognito.utils.Helpers;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import by.hmarka.alexey.incognito.R;

/**
 * Created by lashket on 5.7.16.
 */
public class PostActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private String postId;
    private TextView  postText;
    private TextView countLike;
    private TextView countShare;
    private TextView commentCount;
    private Post post;
    private Menu menu;

    private Helpers helpers = new Helpers();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        postId = getIntent().getStringExtra("POSTID");
        findViews();
        request();
    }

    private void findViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        postText = (TextView) findViewById(R.id.postText);
        countLike = (TextView) findViewById(R.id.postLikeCount);
        countShare = (TextView) findViewById(R.id.postShareCount);
        commentCount = (TextView) findViewById(R.id.postCountComments);
        toolbarSettings(toolbar);
    }

    private void setUi() {
        postText.setText(post.getPost_text());
        countLike.setText(post.getLike_count());
        commentCount.setText(post.getComment_count());
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

}

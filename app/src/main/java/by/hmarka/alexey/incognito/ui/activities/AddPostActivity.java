package by.hmarka.alexey.incognito.ui.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.ActionMenuItemView;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import by.hmarka.alexey.incognito.R;
import by.hmarka.alexey.incognito.entities.AddPostResponse;
import by.hmarka.alexey.incognito.entities.requests.AddImagesRequest;
import by.hmarka.alexey.incognito.entities.requests.AddPostRequest;
import by.hmarka.alexey.incognito.rest.RestClient;
import by.hmarka.alexey.incognito.ui.fragments.AddFragment;
import by.hmarka.alexey.incognito.utils.Helpers;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Andrey on 19.07.2016.
 */
public class AddPostActivity extends AppCompatActivity implements AddFragment.AddActivityInterface {
    private Toolbar toolbar;
    private Menu menu;
    private TextView titleView;
    private MaterialDialog materialDialog;
    Helpers helper = new Helpers();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        titleView = (TextView) findViewById(R.id.custom_toolbar_title);
        materialDialog = helper.getMaterialDialog(this);
        titleView.setVisibility(View.VISIBLE);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

//        Intent intent = getIntent();
//        String action = intent.getAction();
//        String dataType = intent.getType();
//        Bundle extras = intent.getExtras();
//        if (Intent.ACTION_SEND.equals(action)) {
//            if (extras.containsKey(Intent.EXTRA_TEXT)) {
//                String imgUri =  intent.getParcelableExtra(Intent.EXTRA_TEXT);
//                getYoutubeThumbnailUrlFromVideoUrl(imgUri);
//                // Do job here
//            }
//        }
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
    public void setTitle(String title) {
        titleView.setText(title);
    }
    @Override
    public void sendPost(String title) {
        sendPost(title,null, null);
    }

    private boolean sendPostRequestInProgress = false;
    @Override
    public void sendPost(String title,List<String> videoIds, List<Bitmap> images) {
        AddPostRequest request = helper.getAddPostRequest(title);

        if(!sendPostRequestInProgress) {
            materialDialog.show();
            sendPostRequestInProgress = true;
            Call<ResponseBody> call = RestClient.serviceInstance.addNewPost(request);
            call.enqueue(new AddPostCallback(images, videoIds));
        }
    }

    public class AddPostCallback implements Callback<ResponseBody>{

        List<Bitmap> mImages;
        List<String> mVideo;

        public AddPostCallback(List<Bitmap> images, List<String> video){
            super();
            mImages = images;
            mVideo = video;
        }

        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            if(response.isSuccessful()) {
                AddPostResponse postResponse = null;
                String responseString = "";
                try {
                    responseString = response.body().string();
                    postResponse = new Gson().fromJson(responseString, AddPostResponse.class);
                } catch (IOException e) {
                    // TODO handling error
                }
                if (postResponse != null && !postResponse.getPostId().equals(null)){
                    if (mImages != null && mImages.size() > 0 || mVideo != null && mVideo.size() > 0) {
                        sendImages(postResponse.getPostId(), mImages, mVideo);
                    } else {
                        materialDialog.dismiss();
                        Toast.makeText(getBaseContext(), "ok", Toast.LENGTH_SHORT).show();
                        if(isRuning)
                            onBackPressed();
                        sendPostRequestInProgress = false;
                    }
                }else {
                    materialDialog.dismiss();
                    sendPostRequestInProgress = false;
                }
            }else{
                Toast.makeText(getBaseContext(), "add post error", Toast.LENGTH_SHORT).show();
                sendPostRequestInProgress =false;
            }
        }

        @Override
        public void onFailure(Call<okhttp3.ResponseBody> call, Throwable t) {
            Toast.makeText(getBaseContext(), "error", Toast.LENGTH_SHORT).show();
            sendPostRequestInProgress =false;
        }
    }

    private void sendImages(String postId, List<Bitmap> images, List<String> video){
        AddImagesRequest request = helper.getAddImagesRequest(postId, images, video);

        //for debug
        String r = new Gson().toJson(request);

        Call<ResponseBody> call = RestClient.serviceInstance.addImages( request);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()) {
                    materialDialog.dismiss();
                    Toast.makeText(getBaseContext(), "ok", Toast.LENGTH_SHORT).show();
                    if(isRuning)
                        onBackPressed();
                }
                else{
                    materialDialog.dismiss();
                    Toast.makeText(getBaseContext(), "add image error", Toast.LENGTH_SHORT).show();
                }
                sendPostRequestInProgress =false;
            }

            @Override
            public void onFailure(Call<okhttp3.ResponseBody> call, Throwable t) {
                Toast.makeText(getBaseContext(), "error", Toast.LENGTH_SHORT).show();
                sendPostRequestInProgress =false;
            }
        });
    }

    private boolean isRuning;
    @Override
    protected void onResume() {
        super.onResume();
        isRuning = true;
    }
    @Override
    protected void onPause() {
        super.onPause();
        isRuning = false;
    }

}

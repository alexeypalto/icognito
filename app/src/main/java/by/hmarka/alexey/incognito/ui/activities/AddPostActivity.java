package by.hmarka.alexey.incognito.ui.activities;

import android.graphics.Bitmap;
import android.os.Bundle;
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

    Helpers helper = new Helpers();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        titleView = (TextView) findViewById(R.id.custom_toolbar_title);
        titleView.setVisibility(View.VISIBLE);

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
    public void setTitle(String title) {
        titleView.setText(title);
    }
    @Override
    public void sendPost(String title) {
        sendPost(title, null);
    }

    @Override
    public void sendPost(String title, List<Bitmap> images) {

        //sendImages("b4fdd198-6f10-4150-9cde-1a1c3ec538e9",images);

        AddPostRequest request = helper.getAddPostRequest(title);
        Call<ResponseBody> call = RestClient.serviceInstance.addNewPost(request);
        call.enqueue(new AddPostCallback(images));
    }


    public class AddPostCallback implements Callback<ResponseBody>{

        List<Bitmap> mImages;

        public AddPostCallback(List<Bitmap> images){
            super();
            mImages = images;
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
                if(postResponse !=null && !postResponse.getPostId().equals(null) && mImages!=null && mImages.size()>0){
                    sendImages(postResponse.getPostId(),mImages);
                }else{
                    Toast.makeText(getBaseContext(), "ok", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }
            }
            else{
                Toast.makeText(getBaseContext(), "add post error", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onFailure(Call<okhttp3.ResponseBody> call, Throwable t) {
            Toast.makeText(getBaseContext(), "error", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendImages(String postId, List<Bitmap> images){
        AddImagesRequest request = helper.getAddImagesRequest(postId, images);

        //for debug
        String r = new Gson().toJson(request);

        Call<ResponseBody> call = RestClient.serviceInstance.addImages( request);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()) {
                    Toast.makeText(getBaseContext(), "ok", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }
                else{
                    Toast.makeText(getBaseContext(), "add image error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<okhttp3.ResponseBody> call, Throwable t) {
                Toast.makeText(getBaseContext(), "error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

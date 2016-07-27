package by.hmarka.alexey.incognito.ui.activities;

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

import by.hmarka.alexey.incognito.R;
import by.hmarka.alexey.incognito.entities.requests.AddPostRequest;
import by.hmarka.alexey.incognito.rest.RestClient;
import by.hmarka.alexey.incognito.ui.fragments.AddFragment;
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
        AddPostRequest request = new AddPostRequest();
        request.setPostText(title);
        Call<ResponseBody> call = RestClient.serviceInstance.addNewPost(request);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if(response.isSuccessful()) {
                    Toast.makeText(getBaseContext(), "ok", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }
                else{
                    Toast.makeText(getBaseContext(), "error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<okhttp3.ResponseBody> call, Throwable t) {
                Toast.makeText(getBaseContext(), "error", Toast.LENGTH_SHORT).show();
            }
        });

    }
}

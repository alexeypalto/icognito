package by.hmarka.alexey.incognito.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.squareup.otto.Subscribe;

import java.io.IOException;
import java.util.ArrayList;

import by.hmarka.alexey.incognito.entities.Thread;
import by.hmarka.alexey.incognito.entities.ThreadsWrapper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import by.hmarka.alexey.incognito.IncognitoApplication;
import by.hmarka.alexey.incognito.R;
import by.hmarka.alexey.incognito.events.ShowPostsInCategoriesFragmentEvent;
import by.hmarka.alexey.incognito.rest.RestClient;
import by.hmarka.alexey.incognito.ui.adapters.PostsAdapter;
import by.hmarka.alexey.incognito.ui.adapters.ThemesListAdapter;
import by.hmarka.alexey.incognito.utils.Helpers;

/**
 * Created by lashket on 28.6.16.
 */
public class NewsFragment extends Fragment {

    private Toolbar toolbar;
    private Menu menu;
    private RecyclerView recyclerView;
    private RecyclerView recyclerViewPosts;
    private Helpers helpers = new Helpers();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        setToolbarWithoutButton();
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerViewPosts = (RecyclerView) view.findViewById(R.id.recyclerViewPosts);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        getListOfThreads();
       // recyclerView.setAdapter(new ThemesListAdapter());
        return view;
    }

    private void setToolbarWithoutButton() {
        if (menu != null) {
            try {
                menu.findItem(R.id.action_search).setVisible(true);
            } catch (NullPointerException e) {

            }
        }
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Тема");
    }

    private void setToolbarWithBackButton() {
        try {
            if (menu != null) {
                try {
                    menu.findItem(R.id.action_search).setVisible(false);
                } catch (NullPointerException e) {

                }
            }
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Тема");
        } catch (NullPointerException e) {

        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        this.menu = menu;
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.categories_menu, menu);
        final MenuItem item = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
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
    public void showPosts(ShowPostsInCategoriesFragmentEvent event) {
        recyclerView.setVisibility(View.GONE);
        recyclerViewPosts.setVisibility(View.VISIBLE);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerViewPosts.setLayoutManager(linearLayoutManager);
     //   recyclerViewPosts.setAdapter(new PostsAdapter());
        setToolbarWithBackButton();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                recyclerView.setVisibility(View.VISIBLE);
                recyclerViewPosts.setVisibility(View.GONE);
                setToolbarWithoutButton();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getListOfThreads() {

        Call<ResponseBody> call = RestClient.getServiceInstance().getThreadList(helpers.getThreadListRequest());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    String responseString = "";
                    try {
                        responseString = response.body().string();
                        ThreadsWrapper threadsWrapper = new Gson().fromJson(responseString, ThreadsWrapper.class);
                        recyclerView.setAdapter(new ThemesListAdapter(getContext(), (ArrayList<Thread>) threadsWrapper.getThreads()));
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
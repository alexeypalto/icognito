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

import com.squareup.otto.Subscribe;

import by.hmarka.alexey.incognito.IncognitoApplication;
import by.hmarka.alexey.incognito.R;
import by.hmarka.alexey.incognito.events.ShowPostsInCategoriesFragmentEvent;
import by.hmarka.alexey.incognito.ui.adapters.PostsAdapter;
import by.hmarka.alexey.incognito.ui.adapters.ThemesListAdapter;

/**
 * Created by lashket on 28.6.16.
 */
public class NewsFragment extends Fragment {

    private Toolbar toolbar;
    private Menu menu;
    private RecyclerView recyclerView;
    private RecyclerView recyclerViewPosts;

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
        recyclerView.setAdapter(new ThemesListAdapter());
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
}
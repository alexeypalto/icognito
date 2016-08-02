package by.hmarka.alexey.incognito.ui.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.squareup.otto.Subscribe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import by.hmarka.alexey.incognito.IncognitoApplication;
import by.hmarka.alexey.incognito.R;
import by.hmarka.alexey.incognito.entities.Post;
import by.hmarka.alexey.incognito.entities.PostsWrapper;
import by.hmarka.alexey.incognito.entities.requests.PostsListRequest;
import by.hmarka.alexey.incognito.events.ShowCommentsInFavoriteFragment;
import by.hmarka.alexey.incognito.rest.RestClient;
import by.hmarka.alexey.incognito.ui.adapters.PostsAdapter;
import by.hmarka.alexey.incognito.utils.Helpers;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by lashket on 28.6.16.
 */
public class FavoritesFragment extends Fragment {

    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private Helpers helpers = new Helpers();
    private RelativeLayout commentsLayout;
    private LinearLayout commentsList;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        if (((AppCompatActivity) getActivity()).getSupportActionBar() != null) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.title_favorite);
        }
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        commentsLayout = (RelativeLayout) view.findViewById(R.id.comments_layout);
        commentsList = (LinearLayout) view.findViewById(R.id.comments_list_layout);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
       // recyclerView.setAdapter(new PostsAdapter());

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                sendRequest();
            }
        });
        sendRequest();
        return view;
    }

    private void sendRequest() {
        PostsListRequest postsListRequest = helpers.getNewPostsListRequest();
        postsListRequest.setSorting("favorite");
        Call<ResponseBody> call = RestClient.getServiceInstance().getFavoritesList(postsListRequest);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    String responseString = "";
                    try {
                        responseString = response.body().string();
                        PostsWrapper postsWrapper = new Gson().fromJson(responseString, PostsWrapper.class);
                        List<Post> posts = postsWrapper.getPosts();
                        recyclerView.setAdapter(new PostsAdapter((ArrayList<Post>) posts, getContext()));
                    } catch (IOException e) {
                        // TODO handling error
                    }
                }
                if(mSwipeRefreshLayout!=null)
                    mSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if(mSwipeRefreshLayout!=null)
                    mSwipeRefreshLayout.setRefreshing(false);
            }
        });

    }

}
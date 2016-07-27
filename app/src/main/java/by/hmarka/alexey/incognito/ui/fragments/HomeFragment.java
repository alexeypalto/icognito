package by.hmarka.alexey.incognito.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import by.hmarka.alexey.incognito.R;
import by.hmarka.alexey.incognito.entities.Post;
import by.hmarka.alexey.incognito.entities.PostsWrapper;
import by.hmarka.alexey.incognito.entities.requests.PostsListRequest;
import by.hmarka.alexey.incognito.rest.RestClient;
import by.hmarka.alexey.incognito.ui.adapters.CustomPagerAdapter;
import by.hmarka.alexey.incognito.ui.adapters.HomeFragmentPagerAdapter;
import by.hmarka.alexey.incognito.utils.Helpers;
import by.hmarka.alexey.incognito.utils.SharedPreferenceHelper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by lashket on 28.6.16.
 */
public class HomeFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private HomeFragmentPagerAdapter adapter;
    private NewPostsFragment postsFragment = new NewPostsFragment();
    private PopularPostsFragment popularPostsFragment = new PopularPostsFragment();
    private Helpers helpers = new Helpers();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        findViews(view);
        return view;
    }

    private void findViews(View v) {
        tabLayout = (TabLayout) v.findViewById(R.id.tabs);
        viewPager = (ViewPager) v.findViewById(R.id.viewpager);
        adapter = new HomeFragmentPagerAdapter(getActivity().getSupportFragmentManager());
        adapter.addFragment(postsFragment, "Новое");
        adapter.addFragment(new PopularPostsFragment(), "Популярное");
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(2);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.novoe_active);
        tabLayout.getTabAt(1).setIcon(R.drawable.popular_active);
        getNewPostsList();
        getPopularPostsList();
    }

    private void getNewPostsList() {
        PostsListRequest postsListRequest = new PostsListRequest();
        postsListRequest.setImei("12345");
        postsListRequest.setRadius("100000000");
        postsListRequest.setAccess_type("mobile");
        postsListRequest.setLanguage("ru_RU");
        postsListRequest.setLocation_lat("34");
        postsListRequest.setLocation_long("52");
        postsListRequest.setSorting("date");
        postsListRequest.setLastPostId("10");
        postsListRequest.setPostOnPage("10");
        Call<ResponseBody> call = RestClient.getServiceInstance().getPostsList(helpers.getNewPostsListRequest());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    String responseString = "";
                    try {
                        responseString = response.body().string();
                        PostsWrapper postsWrapper = new Gson().fromJson(responseString, PostsWrapper.class);
                        List<Post> posts = postsWrapper.getPosts();
                        postsFragment.addList((ArrayList<Post>) posts);
                    } catch (IOException e) {
                        // TODO handling error
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }
    private void getPopularPostsList() {
        PostsListRequest postsListRequest = new PostsListRequest();
        postsListRequest.setImei("12345");
        postsListRequest.setRadius("100000000");
        postsListRequest.setAccess_type("mobile");
        postsListRequest.setLanguage("ru_RU");
        postsListRequest.setLocation_lat("34");
        postsListRequest.setLocation_long("52");
        postsListRequest.setSorting("like");
        postsListRequest.setLastPostId("10");
        postsListRequest.setPostOnPage("10");
        Call<ResponseBody> call = RestClient.getServiceInstance().getPostsList(helpers.getNewPostsListRequest());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    String responseString = "";
                    try {
                        responseString = response.body().string();
                        PostsWrapper postsWrapper = new Gson().fromJson(responseString, PostsWrapper.class);
                        List<Post> posts = postsWrapper.getPosts();
                        popularPostsFragment.addList((ArrayList<Post>) posts);
                    } catch (IOException e) {
                        // TODO handling error
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

}

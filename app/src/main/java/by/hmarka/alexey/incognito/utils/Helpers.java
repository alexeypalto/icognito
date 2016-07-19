package by.hmarka.alexey.incognito.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import by.hmarka.alexey.incognito.entities.requests.AddPostToFavoriteRequest;
import by.hmarka.alexey.incognito.entities.requests.GetFullPostRequest;
import by.hmarka.alexey.incognito.entities.requests.PostsListRequest;
import by.hmarka.alexey.incognito.entities.requests.RegisterDeviceRequest;
import by.hmarka.alexey.incognito.entities.requests.ThreadsListRequest;

/**
 * Created by lashket on 12.7.16.
 */
public class Helpers {

    public RegisterDeviceRequest getRegisterDiveRequest() {
        RegisterDeviceRequest registerDeviceRequest = new RegisterDeviceRequest();
        registerDeviceRequest.setImei("12345");
        registerDeviceRequest.setLanguage("ru_RU");
        registerDeviceRequest.setLocation_lat("2");
        registerDeviceRequest.setLocation_long("32");
        registerDeviceRequest.setAccess_type("mobile");
        return registerDeviceRequest;
    }

    public PostsListRequest getNewPostsListRequest() {
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
        return postsListRequest;
    }

    public PostsListRequest getPopularPostsRequest() {
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
        return postsListRequest;
    }

    public ThreadsListRequest getThreadListRequest() {
        ThreadsListRequest threadsListRequest = new ThreadsListRequest();
        threadsListRequest.setImei("12345");
        threadsListRequest.setAccess_type("mobile");
        threadsListRequest.setLanguage("ru_RU");
        threadsListRequest.setLocation_lat("34");
        threadsListRequest.setLocation_long("52");
        return threadsListRequest;
    }

    public GetFullPostRequest getPostRequest(String postId) {
        GetFullPostRequest getFullPostRequest = new GetFullPostRequest();
        getFullPostRequest.setLanguage("ru_RU");
        getFullPostRequest.setAccess_type("mobile");
        getFullPostRequest.setPostId(postId);
        getFullPostRequest.setLocation_lat("34");
        getFullPostRequest.setLocation_long("52");
        getFullPostRequest.setImei("12345");
        return getFullPostRequest;
    }

    public AddPostToFavoriteRequest getAddPostToFavoriteRequest(String postId) {
        AddPostToFavoriteRequest addPostToFavoriteRequest = new AddPostToFavoriteRequest();
        addPostToFavoriteRequest.setAccess_type("mobile");
        addPostToFavoriteRequest.setImei("12345");
        addPostToFavoriteRequest.setLanguage("ru_RU");
        addPostToFavoriteRequest.setLocation_lat("34");
        addPostToFavoriteRequest.setLocation_long("52");
        addPostToFavoriteRequest.setPostId(postId);
        return addPostToFavoriteRequest;
    }

    public float convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }

}

package by.hmarka.alexey.incognito.utils;

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

}

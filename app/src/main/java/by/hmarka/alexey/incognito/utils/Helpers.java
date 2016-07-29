package by.hmarka.alexey.incognito.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import by.hmarka.alexey.incognito.entities.requests.AddPostRequest;
import by.hmarka.alexey.incognito.entities.requests.AddPostToFavoriteRequest;
import by.hmarka.alexey.incognito.entities.requests.GetCommentListRequest;
import by.hmarka.alexey.incognito.entities.requests.GetFullPostRequest;
import by.hmarka.alexey.incognito.entities.requests.LeaveCommentRequest;
import by.hmarka.alexey.incognito.entities.requests.PostsListRequest;
import by.hmarka.alexey.incognito.entities.requests.RegisterDeviceRequest;
import by.hmarka.alexey.incognito.entities.requests.ThreadsListRequest;

/**
 * Created by lashket on 12.7.16.
 */
public class Helpers {

    public RegisterDeviceRequest getRegisterDiveRequest() {
        RegisterDeviceRequest registerDeviceRequest = new RegisterDeviceRequest();
        registerDeviceRequest.setImei(SharedPreferenceHelper.getImei());
        registerDeviceRequest.setLanguage(SharedPreferenceHelper.getLanguage());
        registerDeviceRequest.setLocation_lat(SharedPreferenceHelper.getLocationLattitude());
        registerDeviceRequest.setLocation_long(SharedPreferenceHelper.getLocationLongitude());
        registerDeviceRequest.setAccess_type(SharedPreferenceHelper.getAccessType());
        return registerDeviceRequest;
    }

    public PostsListRequest getNewPostsListRequest() {
        PostsListRequest postsListRequest = new PostsListRequest();
        postsListRequest.setImei(SharedPreferenceHelper.getImei());
        postsListRequest.setRadius("100000000");
        postsListRequest.setAccess_type(SharedPreferenceHelper.getAccessType());
        postsListRequest.setLanguage(SharedPreferenceHelper.getLanguage());
        postsListRequest.setLocation_lat(SharedPreferenceHelper.getLocationLattitude());
        postsListRequest.setLocation_long(SharedPreferenceHelper.getLocationLongitude());
        postsListRequest.setSorting("date");
        postsListRequest.setLastPostId("");
        postsListRequest.setPostOnPage("100");
        postsListRequest.setPage("0");
        return postsListRequest;
    }

    public PostsListRequest getPopularPostsRequest() {
        PostsListRequest postsListRequest = new PostsListRequest();
        postsListRequest.setImei(SharedPreferenceHelper.getImei());
        postsListRequest.setRadius("100000000");
        postsListRequest.setAccess_type(SharedPreferenceHelper.getAccessType());
        postsListRequest.setLanguage(SharedPreferenceHelper.getLanguage());
        postsListRequest.setLocation_lat(SharedPreferenceHelper.getLocationLattitude());
        postsListRequest.setLocation_long(SharedPreferenceHelper.getLocationLongitude());
        postsListRequest.setSorting("like");
        postsListRequest.setLastPostId("");
        postsListRequest.setPostOnPage("100");
        postsListRequest.setPage("0");
        return postsListRequest;
    }

    public ThreadsListRequest getThreadListRequest() {
        ThreadsListRequest threadsListRequest = new ThreadsListRequest();
        threadsListRequest.setImei(SharedPreferenceHelper.getImei());
        threadsListRequest.setAccess_type(SharedPreferenceHelper.getAccessType());
        threadsListRequest.setLanguage(SharedPreferenceHelper.getLanguage());
        threadsListRequest.setLocation_lat(SharedPreferenceHelper.getLocationLattitude());
        threadsListRequest.setLocation_long(SharedPreferenceHelper.getLocationLongitude());
        return threadsListRequest;
    }

    public GetFullPostRequest getPostRequest(String postId) {
        GetFullPostRequest getFullPostRequest = new GetFullPostRequest();
        getFullPostRequest.setLanguage(SharedPreferenceHelper.getLanguage());
        getFullPostRequest.setAccess_type(SharedPreferenceHelper.getAccessType());
        getFullPostRequest.setPostId(postId);
        getFullPostRequest.setLocation_lat(SharedPreferenceHelper.getLocationLattitude());
        getFullPostRequest.setLocation_long(SharedPreferenceHelper.getLocationLongitude());
        getFullPostRequest.setImei(SharedPreferenceHelper.getImei());
        return getFullPostRequest;
    }

    public AddPostToFavoriteRequest getAddPostToFavoriteRequest(String postId) {
        AddPostToFavoriteRequest addPostToFavoriteRequest = new AddPostToFavoriteRequest();
        addPostToFavoriteRequest.setAccess_type(SharedPreferenceHelper.getAccessType());
        addPostToFavoriteRequest.setImei(SharedPreferenceHelper.getImei());
        addPostToFavoriteRequest.setLanguage(SharedPreferenceHelper.getLanguage());
        addPostToFavoriteRequest.setLocation_lat(SharedPreferenceHelper.getLocationLattitude());
        addPostToFavoriteRequest.setLocation_long(SharedPreferenceHelper.getLocationLongitude());
        addPostToFavoriteRequest.setAdd("1");
        addPostToFavoriteRequest.setPostId(postId);
        return addPostToFavoriteRequest;
    }

    public AddPostToFavoriteRequest getRemovePostToFavoriteRequest(String postId) {
        AddPostToFavoriteRequest addPostToFavoriteRequest = new AddPostToFavoriteRequest();
        addPostToFavoriteRequest.setAccess_type(SharedPreferenceHelper.getAccessType());
        addPostToFavoriteRequest.setImei(SharedPreferenceHelper.getImei());
        addPostToFavoriteRequest.setLanguage(SharedPreferenceHelper.getLanguage());
        addPostToFavoriteRequest.setLocation_lat(SharedPreferenceHelper.getLocationLattitude());
        addPostToFavoriteRequest.setLocation_long(SharedPreferenceHelper.getLocationLongitude());
        addPostToFavoriteRequest.setAdd("-1");
        addPostToFavoriteRequest.setPostId(postId);
        return addPostToFavoriteRequest;
    }

    public LeaveCommentRequest getLeaveReviewRequest(String postId, String commentText) {
        LeaveCommentRequest leaveCommentRequest = new LeaveCommentRequest();
        leaveCommentRequest.setAccess_type(SharedPreferenceHelper.getAccessType());
        leaveCommentRequest.setImei(SharedPreferenceHelper.getImei());
        leaveCommentRequest.setLocation_lat(SharedPreferenceHelper.getLocationLattitude());
        leaveCommentRequest.setLocation_long(SharedPreferenceHelper.getLocationLongitude());
        leaveCommentRequest.setLanguage(SharedPreferenceHelper.getLanguage());
        leaveCommentRequest.setCommentText(commentText);
        leaveCommentRequest.setPostId(postId);
        return leaveCommentRequest;
    }

    public GetCommentListRequest getCommentsListRequest(String postId) {
        GetCommentListRequest leaveCommentRequest = new GetCommentListRequest();
        leaveCommentRequest.setAccess_type(SharedPreferenceHelper.getAccessType());
        leaveCommentRequest.setImei(SharedPreferenceHelper.getImei());
        leaveCommentRequest.setLocation_lat(SharedPreferenceHelper.getLocationLattitude());
        leaveCommentRequest.setLocation_long(SharedPreferenceHelper.getLocationLongitude());
        leaveCommentRequest.setLanguage(SharedPreferenceHelper.getLanguage());
        leaveCommentRequest.setPostId(postId);
        leaveCommentRequest.setPage("1");
        return leaveCommentRequest;
    }

    public AddPostRequest getAddPostRequest(String text){
        return getAddPostRequest(text,null);
    }
    public AddPostRequest getAddPostRequest(String text, String threadId) {
        AddPostRequest addPostRequest = new AddPostRequest();
        addPostRequest.setAccess_type(SharedPreferenceHelper.getAccessType());
        addPostRequest.setImei(SharedPreferenceHelper.getImei());
        addPostRequest.setLanguage(SharedPreferenceHelper.getLanguage());
        addPostRequest.setLocation_lat(SharedPreferenceHelper.getLocationLattitude());
        addPostRequest.setLocation_long(SharedPreferenceHelper.getLocationLongitude());
        addPostRequest.setPostText(text);
        addPostRequest.setThreadId(threadId);
        return addPostRequest;
    }


    public float convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }

}

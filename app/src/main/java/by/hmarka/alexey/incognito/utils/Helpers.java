package by.hmarka.alexey.incognito.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.util.Base64;
import android.util.DisplayMetrics;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import by.hmarka.alexey.incognito.entities.requests.AddLikeRequest;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import by.hmarka.alexey.incognito.entities.requests.AddImagesRequest;
import by.hmarka.alexey.incognito.entities.requests.AddPostRequest;
import by.hmarka.alexey.incognito.entities.requests.AddPostToFavoriteRequest;
import by.hmarka.alexey.incognito.entities.requests.GetCommentListRequest;
import by.hmarka.alexey.incognito.entities.requests.GetFullPostRequest;
import by.hmarka.alexey.incognito.entities.requests.LeaveCommentRequest;
import by.hmarka.alexey.incognito.entities.requests.PostsListRequest;
import by.hmarka.alexey.incognito.entities.requests.RegisterDeviceRequest;
import by.hmarka.alexey.incognito.entities.requests.ShareRequest;
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


    public ShareRequest getShareRequest(String postId, String note) {
        ShareRequest leaveCommentRequest = new ShareRequest();
        leaveCommentRequest.setAccess_type(SharedPreferenceHelper.getAccessType());
        leaveCommentRequest.setImei(SharedPreferenceHelper.getImei());
        leaveCommentRequest.setLocation_lat(SharedPreferenceHelper.getLocationLattitude());
        leaveCommentRequest.setLocation_long(SharedPreferenceHelper.getLocationLongitude());
        leaveCommentRequest.setLanguage(SharedPreferenceHelper.getLanguage());
        leaveCommentRequest.setPostId(postId);
        leaveCommentRequest.setShareType("null");
        leaveCommentRequest.setShareUrl("null");
        leaveCommentRequest.setShareNote(note);
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

    public AddLikeRequest getAddLikeRequest(Boolean isLike, String postId) {
        AddLikeRequest addPostToFavoriteRequest = new AddLikeRequest();
        addPostToFavoriteRequest.setAccess_type(SharedPreferenceHelper.getAccessType());
        addPostToFavoriteRequest.setImei(SharedPreferenceHelper.getImei());
        addPostToFavoriteRequest.setLanguage(SharedPreferenceHelper.getLanguage());
        addPostToFavoriteRequest.setLocation_lat(SharedPreferenceHelper.getLocationLattitude());
        addPostToFavoriteRequest.setLocation_long(SharedPreferenceHelper.getLocationLongitude());
        if (isLike) {
            addPostToFavoriteRequest.setLikeValue("1");
        } else {
            addPostToFavoriteRequest.setLikeValue("-1");
        }
        addPostToFavoriteRequest.setPostId(postId);
        return addPostToFavoriteRequest;
    }
    public AddImagesRequest getAddImagesRequest(String id, List<Bitmap> images) {
        AddImagesRequest addPostRequest = new AddImagesRequest ();
        addPostRequest.setAccess_type(SharedPreferenceHelper.getAccessType());
        addPostRequest.setImei(SharedPreferenceHelper.getImei());
        addPostRequest.setLanguage(SharedPreferenceHelper.getLanguage());
        addPostRequest.setLocation_lat(SharedPreferenceHelper.getLocationLattitude());
        addPostRequest.setLocation_long(SharedPreferenceHelper.getLocationLongitude());
        addPostRequest.setPostId(id);

        List<String> imagesStringsArray = new ArrayList<>();
        for (Bitmap bm : images) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
            byte[] byteArrayImage = baos.toByteArray();
            String encodedImage = Base64.encodeToString(byteArrayImage, Base64.DEFAULT);
            imagesStringsArray.add(encodedImage);
        }
        addPostRequest.setImages(imagesStringsArray);
        return addPostRequest;

    }

    public float convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }

    public String getDate(String date) {
        Date date1 = new Date();
        long currentDateInMillis = date1.getTime();
        long differenceOfTime = 0;
        long incomingDateInMillis = 0;
        int yearsSeconds = 3600 * 24 * 365;
        int monthSeconds = 3600 * 24 * 30;
        int weeksSeconds = 3600 * 24 * 7;
        int daysSeconds = 3600 * 24;
        int hourSeconds = 3600;
        int minuteSeconds = 60;
        String returnString = "";
        SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
        try {
            format.setTimeZone(TimeZone.getTimeZone("GMT+0"));
            Date incomingDate = format.parse(date);
            incomingDateInMillis = incomingDate.getTime();
            differenceOfTime = currentDateInMillis - incomingDateInMillis;
            differenceOfTime = differenceOfTime / 1000;
            if ((differenceOfTime / yearsSeconds) > 0) {
                return String.valueOf(differenceOfTime / yearsSeconds) + "г.";
            }
            if ((differenceOfTime / monthSeconds) > 0) {
                return String.valueOf(differenceOfTime / monthSeconds) + "м.";
            }
            if ((differenceOfTime / weeksSeconds) > 0) {
                return String.valueOf(differenceOfTime / weeksSeconds) + "н.";
            }
            if ((differenceOfTime / daysSeconds) > 0) {
                return String.valueOf(differenceOfTime / daysSeconds) + "д.";
            }
            if ((differenceOfTime / hourSeconds) > 0) {
                return String.valueOf(differenceOfTime / hourSeconds) + "ч.";
            }
            if ((differenceOfTime / minuteSeconds) > 0) {
                return String.valueOf(differenceOfTime / minuteSeconds) + "мин.";
            }
            if (differenceOfTime > 0 && differenceOfTime < 60) {
                return String.valueOf(differenceOfTime) + "с.";
            }
        } catch (ParseException e) {
            return "";
        }
        return returnString;
    }

}

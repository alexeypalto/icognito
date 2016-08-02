package by.hmarka.alexey.incognito.entities;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Alex on 01-Aug-16.
 */
public class AddPostResponse {

    @SerializedName("postId")
    private  String postId;

    @SerializedName("errorTxt")
    private  String errorMessage;

    public String getPostId() {
        return postId;
    }

    public void setPostId(String post_id) {
        this.postId = post_id;
    }

    public String getMessage() {
        return errorMessage;
    }

    public void setMessage(String post_id) {
        this.errorMessage = post_id;
    }
}

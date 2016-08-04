package by.hmarka.alexey.incognito.entities.requests;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.annotations.SerializedName;


import by.hmarka.alexey.incognito.BuildConfig;

/**
 * Created by lashket on 7.7.16.
 */
public class AddPostRequest extends RegisterDeviceRequest {

    @SerializedName("post_text")
    private String postText;

    @SerializedName("threadId")
    private String threadId;

    public String getPostText() {
        return postText;
    }

    public void setPostText(String postText) {
        this.postText = postText;
    }

    public String getThreadId() {
        return threadId;
    }

    public void setThreadId(String threadId) {
        this.threadId = threadId;
    }
}

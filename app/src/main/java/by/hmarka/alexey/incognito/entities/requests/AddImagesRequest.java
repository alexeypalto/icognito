package by.hmarka.alexey.incognito.entities.requests;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Alex on 01-Aug-16.
 */
public class AddImagesRequest extends RegisterDeviceRequest {
    @SerializedName("postId")
    private String postId;

    @SerializedName("images")
    private List<String>  images;

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postText) {
        this.postId = postText;
    }

    public List<String>  getImages() {
        return images;
    }

    public void setImages(List<String>  threadId) {
        this.images = threadId;
    }
}

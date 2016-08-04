package by.hmarka.alexey.incognito.entities;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by lashket on 25.6.16.
 */
public class Post {

    private String post_id;
    private String post_text;
    private String post_timestamp;
    @SerializedName("post_picture")
    private List<String> post_images;
    @SerializedName("post_youtube")
    private List<String> videoIds;
    private String shares_count;
    private String like_count;
    private String comment_count;
    private String favorite_count;
    private String isFavorite;
    private List<Comment> comments;

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public String getIsFavorite() {
        return isFavorite;
    }

    public void setIsFavorite(String isFavorite) {
        this.isFavorite = isFavorite;
    }

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public String getPost_text() {
        return post_text;
    }

    public void setPost_text(String post_text) {
        this.post_text = post_text;
    }

    public String getPost_timestamp() {
        return post_timestamp;
    }

    public void setPost_timestamp(String post_timestamp) {
        this.post_timestamp = post_timestamp;
    }

    public List<String>  getPostImages() {
        return post_images;
    }

    public void setPostImages(List<String>  post_images) {
        this.post_images = post_images;
    }

    public String getLike_count() {
        return like_count;
    }

    public void setLike_count(String like_count) {
        this.like_count = like_count;
    }

    public String getComment_count() {
        return comment_count;
    }

    public void setComment_count(String comment_count) {
        this.comment_count = comment_count;
    }

    public String getFavorite_count() {
        return favorite_count;
    }

    public void setFavorite_count(String favorite_count) {
        this.favorite_count = favorite_count;
    }

    public String getShares_count() {
        return shares_count;
    }

    public void setShares_count(String shares_count) {
        this.shares_count = shares_count;
    }

    public List<String> getVideoIds() {
        return videoIds;
    }

    public void setVideoIds(List<String> videoIds) {
        this.videoIds = videoIds;
    }
}

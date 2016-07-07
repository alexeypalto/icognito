package by.hmarka.alexey.incognito.entities;

import java.util.List;

/**
 * Created by lashket on 7.7.16.
 */
public class Comment {

    private String comment_text;
    private String comment_timestamp;
    private List<String> comment_picture;

    public String getComment_text() {
        return comment_text;
    }

    public void setComment_text(String comment_text) {
        this.comment_text = comment_text;
    }

    public String getComment_timestamp() {
        return comment_timestamp;
    }

    public void setComment_timestamp(String comment_timestamp) {
        this.comment_timestamp = comment_timestamp;
    }

    public List<String> getComment_picture() {
        return comment_picture;
    }

    public void setComment_picture(List<String> comment_picture) {
        this.comment_picture = comment_picture;
    }
}

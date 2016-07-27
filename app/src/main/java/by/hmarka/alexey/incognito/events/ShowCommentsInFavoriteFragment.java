package by.hmarka.alexey.incognito.events;

/**
 * Created by Andrey on 26.07.2016.
 */
public class ShowCommentsInFavoriteFragment {

    private String postId;

    public ShowCommentsInFavoriteFragment(String postId) {
        this.postId = postId;
    }

    public String getPostId() {
        return this.postId;
    }
}

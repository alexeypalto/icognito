package by.hmarka.alexey.incognito.events;

/**
 * Created by lashket on 5.7.16.
 */
public class ShowPostsInCategoriesFragmentEvent {

    private String categoryId;

    public ShowPostsInCategoriesFragmentEvent(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryId() {
        return this.categoryId;
    }
}

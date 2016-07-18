package by.hmarka.alexey.incognito.entities;

/**
 * Created by Alexey on 18.07.2016.
 */
public class Notify {
    private String title, preview, type, time;

    public Notify() {
    }

    public Notify(String title, String preview, String type, String time) {
        this.title = title;
        this.preview = preview;
        this.type = type;
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String name) {
        this.title = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPreview() {
        return preview;
    }

    public void setPreview(String preview) {
        this.preview = preview;
    }

    public void setTime(String time) {
        this.title = time;
    }

    public String getTime() {
        return time;
    }
}

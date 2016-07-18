package by.hmarka.alexey.incognito.entities;

/**
 * Created by Andrey on 18.07.2016.
 */
public class Thread {

    private String threadId;
    private String threadPicture;
    private String threadName;

    public String getThreadId() {
        return threadId;
    }

    public void setThreadId(String threadId) {
        this.threadId = threadId;
    }

    public String getThreadImage() {
        return threadPicture;
    }

    public void setThreadImage(String threadImage) {
        this.threadPicture = threadImage;
    }

    public String getThreadName() {
        return threadName;
    }

    public void setThreadName(String threadName) {
        this.threadName = threadName;
    }
}

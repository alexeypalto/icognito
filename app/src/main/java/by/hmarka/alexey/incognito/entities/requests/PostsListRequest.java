package by.hmarka.alexey.incognito.entities.requests;

public class PostsListRequest {

    private String imei;
    private String location_lat;
    private String location_long;
    private String radius;
    private String sorting;
    private String page;
    private String postOnPage;
    private String lastPostId;
    private String language;
    private String access_type;

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getLocation_lat() {
        return location_lat;
    }

    public void setLocation_lat(String location_lat) {
        this.location_lat = location_lat;
    }

    public String getLocation_long() {
        return location_long;
    }

    public void setLocation_long(String location_long) {
        this.location_long = location_long;
    }

    public String getRadius() {
        return radius;
    }

    public void setRadius(String radius) {
        this.radius = radius;
    }

    public String getSorting() {
        return sorting;
    }

    public void setSorting(String sorting) {
        this.sorting = sorting;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getPostOnPage() {
        return postOnPage;
    }

    public void setPostOnPage(String postOnPage) {
        this.postOnPage = postOnPage;
    }

    public String getLastPostId() {
        return lastPostId;
    }

    public void setLastPostId(String lastPostId) {
        this.lastPostId = lastPostId;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getAccess_type() {
        return access_type;
    }

    public void setAccess_type(String access_type) {
        this.access_type = access_type;
    }
}

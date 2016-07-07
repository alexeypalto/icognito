package by.hmarka.alexey.incognito.entities.requests;

public class RegisterDeviceRequest {

    private String imei;
    private String location_lat;
    private String location_long;
    private String access_type;
    private String language;

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

    public String getAccess_type() {
        return access_type;
    }

    public void setAccess_type(String access_type) {
        this.access_type = access_type;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}

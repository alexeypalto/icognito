package by.hmarka.alexey.incognito.entities;

/**
 * Created by Alexey on 01.08.2016.
 */
public class Properties {
    private final int FIRST_TYPE = 0, SECOND_TYPE = 1;
    public int type = 0;
    private String simbols_text, switchCompat, simbols_count;

    public Properties() {
    }

    public Properties(String simbols_text, String simbols_count) {
        this.simbols_text = simbols_text;
        this.simbols_count = simbols_count;
    }

    public Properties(String switchCompat) {
        this.switchCompat = switchCompat;
    }

    public String getSimbols_text() {
        return simbols_text;
    }

    public void setSimbols_text(String simbols_text) {
        this.simbols_text = simbols_text;
    }

    public String getSimbols_count() {
        return simbols_count;
    }

    public void setSimbols_count(String simbols_count) {
        this.simbols_count = simbols_count;
    }

    public String getSwitchCompat() {
        return switchCompat;
    }

    public void setSwitchCompat(String switchCompat) {
        this.switchCompat = switchCompat;
    }
}

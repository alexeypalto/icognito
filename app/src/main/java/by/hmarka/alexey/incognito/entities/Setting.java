package by.hmarka.alexey.incognito.entities;

import android.app.Activity;
import android.view.View;

/**
 * Created by vitalyorlov on 06.07.16.
 */
public class Setting {

    private String name;
    private int image;

    public String getName() {
        return name;
    }

    public int getImage() {
        return image;
    }

    public Setting (int image, String name, Class<?> intent) {
        this(image,name);
        classToStart= intent;
    }
    public Setting (int image, String name) {
        this.name = name;
        this.image = image;
    }

    public Class<?> classToStart;
}

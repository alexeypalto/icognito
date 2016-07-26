package by.hmarka.alexey.incognito.entities;

import android.app.Fragment;

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

    public Setting (int image, String name, Fragment intent){
        this(image, name);
        fragmentToStart= intent;
    }

    public Class<?> classToStart;

    public Fragment fragmentToStart;
}

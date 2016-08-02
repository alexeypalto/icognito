package by.hmarka.alexey.incognito.entities;

import android.app.Fragment;

/**
 * Created by vitalyorlov on 06.07.16.
 */
public class Setting {

    private String name;
    private int image;
    private int id;

    public int getId(){
        return id;
    }

    public String getName() {
        return name;
    }

    public int getImage() {
        return image;
    }

    public Setting (int id, int image, String name, Class<?> intent) {
        this(id, image,name);
        this.id = id;
        classToStart= intent;

    }
    public Setting (int id, int image, String name) {
        this.id = id;
        this.name = name;
        this.image = image;
    }

    public Setting (int id, int image, String name, Fragment intent){
        this(id, image, name);
        fragmentToStart= intent;
    }

    public Class<?> classToStart;

    public Fragment fragmentToStart;
}

package by.hmarka.alexey.incognito.entities.requests;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.annotations.SerializedName;

import by.hmarka.alexey.incognito.BuildConfig;

/**
 * Created by lashket on 7.7.16.
 */
public class AddPostRequest extends RegisterDeviceRequest {

    @SerializedName("post_text")
    private String postText;

    public AddPostRequest(){
        if(BuildConfig.DEBUG) {
            setImei("12345");
            setLanguage("ru_RU");
            setLocation_lat("53.8916095");
            setLocation_long("27.5528496");
            setAccess_type("mobile");
        }
        else
            throw new MaterialDialog.NotImplementedException("AddPostRequest parametrs error");
    }

    public String getPostText() {
        return postText;
    }

    public void setPostText(String postText) {
        this.postText = postText;
    }
}

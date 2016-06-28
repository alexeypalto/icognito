package by.hmarka.alexey.incognito.ui.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
//import com.facebook.FacebookSdk;
import by.hmarka.alexey.incognito.R;

/**
 * Created by Alexey on 28.06.2016.
 */
public class ShareActivity extends AppCompatActivity {

    private Button share_facebook;
    private Button share_vk;
    private Button share_twitter;
    private Button share_whatsapp;
    private Button share_email;
    private Button share_sms;
    private Button properties;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_share_layout);
        share_facebook = (Button)findViewById(R.id.share_button_facebook);
        share_vk = (Button)findViewById(R.id.share_button_vk);
        share_twitter = (Button)findViewById(R.id.share_button_twitter);
        share_whatsapp = (Button)findViewById(R.id.share_button_whatsapp);
        share_email = (Button)findViewById(R.id.share_button_email);
        share_sms = (Button)findViewById(R.id.share_button_sms);
        properties =(Button)findViewById(R.id.share_button_properties);

    }

    public void onClick(View v)
    {
        switch(v.getId()) {
            case R.id.share_button_facebook:
                openFacebook();
                break;
            case R.id.share_button_vk:
                openVk();
                break;
            case R.id.share_button_twitter:
                openTwitter();
                break;
            case R.id.share_button_whatsapp:
                openWhatsapp();
                break;
            case R.id.share_button_email:
                openEmail();
                break;
            case R.id.share_button_sms:
                openSms();
                break;
            case R.id.share_button_properties:
                onepProperties();
                break;
        }
        }

    }
}

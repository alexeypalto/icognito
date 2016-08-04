package by.hmarka.alexey.incognito.ui.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import by.hmarka.alexey.incognito.R;

/**
 * Created by Alexey on 04.08.2016.
 */
public class PropActivity extends AppCompatActivity implements View.OnClickListener {
    TextView symbols_text, symbols_count;
    SwitchCompat switchCompat1, switchCompat2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prop);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Настройки");

        findViews();
        symbols_text.setOnClickListener(this);
        switchCompat1.setOnClickListener(this);
        switchCompat2.setOnClickListener(this);
    }

    private void findViews(){
        symbols_text = (TextView)findViewById(R.id.symbols_text);
        symbols_count = (TextView)findViewById(R.id.symbols_count);
        switchCompat1 = (SwitchCompat) findViewById(R.id.switchCompat);
        switchCompat2 = (SwitchCompat)findViewById(R.id.switchCompat2);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.symbols_text:
                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                final EditText edittext = new EditText(this);
                alert.setMessage("Enter Your Count");
                alert.setTitle("Count of symbols");

                alert.setView(edittext);

                alert.setPositiveButton("Set count", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String symbolsCount = edittext.getText().toString();
                        symbols_count.setText(symbolsCount);

                    }
                });
                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                });

                alert.show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

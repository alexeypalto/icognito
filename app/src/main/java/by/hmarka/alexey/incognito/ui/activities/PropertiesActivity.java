package by.hmarka.alexey.incognito.ui.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import by.hmarka.alexey.incognito.R;
import by.hmarka.alexey.incognito.entities.Properties;
import by.hmarka.alexey.incognito.ui.adapters.PropertiesAdapter;

/**
 * Created by Alexey on 01.08.2016.
 */
public class PropertiesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PropertiesAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private List<Properties> propertiesList = new ArrayList<>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_properties);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Настройки");

        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewProperties);

        mAdapter = new PropertiesAdapter(propertiesList);
        mLayoutManager = new LinearLayoutManager(PropertiesActivity.this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        getSampleArrayList();
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

    private void getSampleArrayList() {
        Properties propertie = new Properties("symbol text", "500");
        propertiesList.add(propertie);

        propertie = new Properties("on");
        propertiesList.add(propertie);

        propertie = new Properties("off");
        propertiesList.add(propertie);

        mAdapter.notifyDataSetChanged();
    }
}

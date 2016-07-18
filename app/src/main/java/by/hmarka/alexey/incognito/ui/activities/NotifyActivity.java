package by.hmarka.alexey.incognito.ui.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

import by.hmarka.alexey.incognito.R;
import by.hmarka.alexey.incognito.entities.Notify;
import by.hmarka.alexey.incognito.ui.adapters.NotifyAdapter;

/**
 * Created by Alexey on 18.07.2016.
 */
public class NotifyActivity extends AppCompatActivity {
    private List<Notify> notifyList = new ArrayList<>();
    private RecyclerView recyclerView;
    private NotifyAdapter mAdapter;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify);

        //toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setTitle("");


        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewNotify);

        mAdapter = new NotifyAdapter(notifyList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        prepareNotifyData();
    }

    private void prepareNotifyData() {
        Notify notify = new Notify("Description", "Preview", "Type", "Time");
        notifyList.add(notify);

        notify = new Notify("Inside Out", "Animation, Kids & Family", "333", "15:30");
        notifyList.add(notify);

        notify = new Notify("Inside", "Animation", "2015", "3hrs");
        notifyList.add(notify);

        notify = new Notify("Out", "Kids & Family", "Back", "34hrs");
        notifyList.add(notify);

        mAdapter.notifyDataSetChanged();
    }
}
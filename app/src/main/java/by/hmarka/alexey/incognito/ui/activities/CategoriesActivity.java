package by.hmarka.alexey.incognito.ui.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import by.hmarka.alexey.incognito.R;

/**
 * Created by Alexey on 01.07.2016.
 */
public class CategoriesActivity extends AppCompatActivity {
    private CardView mCardView;
    private CardView.Adapter mAdapter;
    private CardView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.categories_layout);
        mCardView = (CardView) findViewById(R.id.card_view);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new MyAdapter(myDataset);
        mRecyclerView.setAdapter(mAdapter);
    }
}
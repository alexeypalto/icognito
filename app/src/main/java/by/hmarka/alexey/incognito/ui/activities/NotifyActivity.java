package by.hmarka.alexey.incognito.ui.activities;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.MenuItem;
import android.view.View;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Уведомления");

        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewNotify);

        mAdapter = new NotifyAdapter(notifyList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        setUpItemTouchHelper();

        prepareNotifyData();
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

    private void prepareNotifyData() {
        Notify notify = new Notify("Description", "Preview", "Type", "Time");
        notifyList.add(notify);

        notify = new Notify("Inside Out", "Animation, Kids & Family", "333", "15:30");
        notifyList.add(notify);

        notify = new Notify("Inside", "Animation", "2015", "3hrs");
        notifyList.add(notify);

        notify = new Notify("Out", "Kids & Family", "Back", "34hrs");
        notifyList.add(notify);

        notify = new Notify("Inside Out", "Animation, Kids & Family", "333", "15:30");
        notifyList.add(notify);

        notify = new Notify("Inside", "Animation", "2015", "3hrs");
        notifyList.add(notify);

        notify = new Notify("Out", "Kids & Family", "Back", "34hrs");
        notifyList.add(notify);

        notify = new Notify("Inside Out", "Animation, Kids & Family", "333", "15:30");
        notifyList.add(notify);

        notify = new Notify("Inside", "Animation", "2015", "3hrs");
        notifyList.add(notify);

        notify = new Notify("Out", "Kids & Family", "Back", "34hrs");
        notifyList.add(notify);

        notify = new Notify("Inside Out", "Animation, Kids & Family", "333", "15:30");
        notifyList.add(notify);

        notify = new Notify("Inside", "Animation", "2015", "3hrs");
        notifyList.add(notify);

        notify = new Notify("Out", "Kids & Family", "Back", "34hrs");
        notifyList.add(notify);


        mAdapter.notifyDataSetChanged();
    }

    private void setUpItemTouchHelper() {

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            Drawable background;
            Drawable xMark;
            int xMarkMargin;
            boolean initiated;

            private void init() {
                background = new ColorDrawable(Color.RED);
                xMark = ContextCompat.getDrawable(NotifyActivity.this, R.drawable.smile);
                xMark.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
                xMarkMargin = (int) NotifyActivity.this.getResources().getDimension(R.dimen.materialize_avatar);
                initiated = true;
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public int getSwipeDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                int position = viewHolder.getAdapterPosition();
                NotifyAdapter notifyAdapter = (NotifyAdapter) recyclerView.getAdapter();
                if (notifyAdapter.isUndoOn() && notifyAdapter.isPendingRemoval(position)) {
                    return 0;
                }
                return super.getSwipeDirs(recyclerView, viewHolder);
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                int swipedPosition = viewHolder.getAdapterPosition();
                NotifyAdapter adapter = (NotifyAdapter) recyclerView.getAdapter();
                boolean undoOn = adapter.isUndoOn();
                if (undoOn) {
                    adapter.pendingRemoval(swipedPosition);
                } else {
                    adapter.remove(swipedPosition);
                }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                View itemView = viewHolder.itemView;

                if (viewHolder.getAdapterPosition() == -1) {
                    return;
                }

                if (!initiated) {
                    init();
                }

                // draw red background
                background.setBounds(itemView.getRight() + (int) dX, itemView.getTop(), itemView.getRight(), itemView.getBottom());
                background.draw(c);

                // draw picture under the button mark
                int itemHeight = itemView.getBottom() - itemView.getTop();
                int intrinsicWidth = xMark.getIntrinsicWidth();
                int intrinsicHeight = xMark.getIntrinsicWidth();

                int xMarkLeft = itemView.getRight() - xMarkMargin - intrinsicWidth;
                int xMarkRight = itemView.getRight() - xMarkMargin;
                int xMarkTop = itemView.getTop() + (itemHeight - intrinsicHeight)/2;
                int xMarkBottom = xMarkTop + intrinsicHeight;
                xMark.setBounds(xMarkLeft, xMarkTop, xMarkRight, xMarkBottom);

                xMark.draw(c);

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }

        };
        ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        mItemTouchHelper.attachToRecyclerView(recyclerView);
    }
}
package by.hmarka.alexey.incognito.ui.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

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

        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Уведомления");*/


        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewNotify);

        mAdapter = new NotifyAdapter(notifyList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Notify notify = notifyList.get(position);
                Toast.makeText(getApplicationContext(), notify.getTitle() + " is selected!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

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

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private NotifyActivity.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final NotifyActivity.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }
}
package by.hmarka.alexey.incognito.ui.adapters;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import by.hmarka.alexey.incognito.R;
import by.hmarka.alexey.incognito.entities.Notify;

/**
 * Created by Alexey on 11.07.2016.
 */
public class NotifyAdapter extends RecyclerView.Adapter<NotifyAdapter.MyViewHolder> {

    private List<Notify> notifyList;
    private List<Notify> itemsPendingRemoval;


    private static final int PENDING_REMOVAL_TIMEOUT = 3000;
    int lastInsertedIndex; // so we can add some more items for testing purposes
    boolean undoOn; // is undo on, you can turn it on from the toolbar menu

    HashMap<String, Runnable> pendingRunnables = new HashMap<>(); // map of items to pending runnables, so we can cancel a removal if need be


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView description, preview, type, time;
        Button undoButton;


        public MyViewHolder(View view) {
            super(view);
            description = (TextView) view.findViewById(R.id.notify_description);
            preview = (TextView) view.findViewById(R.id.notify_preview);
            type = (TextView) view.findViewById(R.id.notify_pic);
            time = (TextView) view.findViewById(R.id.notify_time);


            undoButton = (Button) itemView.findViewById(R.id.undo_button);
        }
    }


    public NotifyAdapter(List<Notify> notifyList) {
        this.notifyList = notifyList;
        itemsPendingRemoval = new ArrayList<>();
        // let's generate some items
        lastInsertedIndex = 15;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notify, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        final Notify notify = notifyList.get(position);
        holder.description.setText(notify.getTitle());
        holder.preview.setText(notify.getPreview());
        holder.type.setText(notify.getType());
        holder.time.setText(notify.getTime());


        if (itemsPendingRemoval.contains(notify)) {
            // we need to show the "undo" state of the row
        holder.itemView.setBackgroundColor(Color.RED);
        holder.description.setVisibility(View.GONE);
        holder.undoButton.setVisibility(View.VISIBLE);
        holder.undoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // user wants to undo the removal, let's cancel the pending task
                Runnable pendingRemovalRunnable = pendingRunnables.get(notify);
                pendingRunnables.remove(notify);
                if (pendingRemovalRunnable != null)
                  //  handler.removeCallbacks(pendingRemovalRunnable);
                itemsPendingRemoval.remove(notify);
                // this will rebind the row in "normal" state
                notifyItemChanged(notifyList.indexOf(notify));
            }
        });
    } else {
        // we need to show the "normal" state
        holder.itemView.setBackgroundColor(Color.WHITE);
        holder.description.setVisibility(View.VISIBLE);
        holder.description.setText(notify.getTitle());
        holder.undoButton.setVisibility(View.GONE);
        holder.undoButton.setOnClickListener(null);
    }
    }

    public void setUndoOn(boolean undoOn) {
        this.undoOn = undoOn;
    }

    public boolean isUndoOn() {
        return undoOn;
    }


    @Override
    public int getItemCount() {
        return notifyList.size();
    }

    public void pendingRemoval(int position) {
        final Notify notify = notifyList.get(position);
        if (!itemsPendingRemoval.contains(notify)) {
            itemsPendingRemoval.add(notify);
            // this will redraw row in "undo" state
            notifyItemChanged(position);
            // let's create, store and post a runnable to remove the item
            Runnable pendingRemovalRunnable = new Runnable() {
                @Override
                public void run() {
                    remove(notifyList.indexOf(notify));
                }
            };
            //handler.postDelayed(pendingRemovalRunnable, PENDING_REMOVAL_TIMEOUT);
            //pendingRunnables.put(notify, pendingRemovalRunnable);
        }
    }

    public void remove(int position) {
        Notify notify = notifyList.get(position);
        if (itemsPendingRemoval.contains(notify)) {
            itemsPendingRemoval.remove(notify);
        }
        if (notifyList.contains(notify)) {
            notifyList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public boolean isPendingRemoval(int position) {
        Notify notify = notifyList.get(position);
        return itemsPendingRemoval.contains(notify);
    }
}
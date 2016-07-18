package by.hmarka.alexey.incognito.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import by.hmarka.alexey.incognito.R;
import by.hmarka.alexey.incognito.entities.Notify;

/**
 * Created by Alexey on 11.07.2016.
 */
public class NotifyAdapter extends RecyclerView.Adapter<NotifyAdapter.MyViewHolder> {

    private List<Notify> notifyList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView description, preview, type, time;

        public MyViewHolder(View view) {
            super(view);
            description = (TextView) view.findViewById(R.id.notify_description);
            preview = (TextView) view.findViewById(R.id.notify_preview);
            type = (TextView) view.findViewById(R.id.notify_pic);
            time = (TextView) view.findViewById(R.id.notify_time);
        }
    }


    public NotifyAdapter(List<Notify> notifyList) {
        this.notifyList = notifyList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notify, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Notify notify = notifyList.get(position);
        holder.description.setText(notify.getDescription());
        holder.preview.setText(notify.getPreview());
        holder.type.setText(notify.getType());
        holder.time.setText(notify.getTime());
    }

    @Override
    public int getItemCount() {
        return notifyList.size();
    }
}
/*
public class NotifyAdapter extends RecyclerView.Adapter<NotifyAdapter.ViewHolder> {
    private String[] mDescription;
    private String[] mPreview;
    private String[] mTime;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTextView;
        public ViewHolder(TextView v) {
            super(v);
            mTextView = v;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public NotifyAdapter(String[] myDataset) {
        mDescription = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public NotifyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.item_notify, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder((TextView) v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.mTextView.setText(mDescription[position]);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDescription.length;
    }
}
*/

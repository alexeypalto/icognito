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
        holder.description.setText(notify.getTitle());
        holder.preview.setText(notify.getPreview());
        holder.type.setText(notify.getType());
        holder.time.setText(notify.getTime());
    }

    @Override
    public int getItemCount() {
        return notifyList.size();
    }
}
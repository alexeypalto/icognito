package by.hmarka.alexey.incognito.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import by.hmarka.alexey.incognito.IncognitoApplication;
import by.hmarka.alexey.incognito.R;
import by.hmarka.alexey.incognito.entities.Thread;
import by.hmarka.alexey.incognito.events.ShowPostsInCategoriesFragmentEvent;
import by.hmarka.alexey.incognito.utils.Constants;
import by.hmarka.alexey.incognito.utils.Helpers;

/**
 * Created by lashket on 4.7.16.
 */
public class ThemesListAdapter extends RecyclerView.Adapter<ThemesListAdapter.ThemesListViewHolder> {

    private ArrayList<Thread> threads;
    private Context context;
    private Helpers helpers = new Helpers();

    public ThemesListAdapter(Context context, ArrayList<Thread> threads) {
        this.threads = threads;
        this.context = context;
    }

    @Override
    public ThemesListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.item_theme, parent, false);
        return new ThemesListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ThemesListViewHolder holder, int position) {
        holder.textView.setText(threads.get(position).getThreadName());
        holder.postsCount.setText(threads.get(position).getPostsInThread());
        String url = Constants.BASE_URL + threads.get(position).getThreadImage();
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels;
        Picasso.with(context)
                .load(url)
                .resize(Math.round(dpWidth), Math.round(helpers.convertDpToPixel(100, context)))
                .centerCrop()
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return threads.size();
    }

    public class ThemesListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView imageView;
        public TextView textView;
        public TextView postsCount;

        public ThemesListViewHolder(View v) {
            super(v);
            v.setOnClickListener(this);
            imageView = (ImageView) v.findViewById(R.id.threadImage);
            textView = (TextView) v.findViewById(R.id.threadName);
            postsCount = (TextView) v.findViewById(R.id.threadCount);
        }

        @Override
        public void onClick(View v) {
            IncognitoApplication.bus.post(new ShowPostsInCategoriesFragmentEvent(threads.get(getAdapterPosition()).getThreadId()));
        }
    }

}

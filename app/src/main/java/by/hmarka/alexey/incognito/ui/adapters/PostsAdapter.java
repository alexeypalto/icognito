package by.hmarka.alexey.incognito.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import by.hmarka.alexey.incognito.R;
import by.hmarka.alexey.incognito.ui.activities.PostActivity;

/**
 * Created by lashket on 4.7.16.
 */
public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.PostsViewHolder> {

    @Override
    public PostsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.item_post, parent, false);
        return new PostsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PostsViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public class PostsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Context context;

        public PostsViewHolder(View v) {
            super(v);
            context = v.getContext();
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            PostActivity.show(context);
        }
    }

}

package by.hmarka.alexey.incognito.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import by.hmarka.alexey.incognito.R;
import by.hmarka.alexey.incognito.entities.Post;
import by.hmarka.alexey.incognito.ui.activities.PostActivity;

/**
 * Created by lashket on 4.7.16.
 */
public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.PostsViewHolder> {

    private ArrayList<Post> posts;
    private Context context;

    public PostsAdapter(ArrayList<Post> posts, Context context) {
        if (posts != null) {
            this.posts = posts;
        }
        this.context = context;
    }

    @Override
    public PostsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.item_post, parent, false);
        return new PostsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PostsViewHolder holder, int position) {
        holder.postText.setText(posts.get(position).getPost_text());
        holder.postTime.setText(posts.get(position).getPost_timestamp());
    }

    @Override
    public int getItemCount() {
        if (posts == null) {
            return 0;
        }
        return posts.size();
    }

    public class PostsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Context context;
        private TextView postText;
        private TextView postTime;

        public PostsViewHolder(View v) {
            super(v);
            context = v.getContext();
            postText = (TextView) v.findViewById(R.id.postText);
            postTime = (TextView) v.findViewById(R.id.post_date);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            PostActivity.show(context, posts.get(getAdapterPosition()).getPost_id());
        }
    }

}

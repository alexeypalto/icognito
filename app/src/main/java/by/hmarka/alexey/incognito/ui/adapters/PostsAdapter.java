package by.hmarka.alexey.incognito.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;

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
        holder.mediaCountText.setText(posts.get(position).getPost_image_link());
        holder.ratingCountText.setText(posts.get(position).getLike_count());
        holder.commentsCountText.setText(posts.get(position).getComment_count());
        holder.shareCountText.setText("?");

        Calendar rightNow = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy");
        long dt = 0;
        Date date = null;
        try {
            date = simpleDateFormat.parse(posts.get(position).getPost_timestamp());
            dt = rightNow.getTime().getTime() - date.getTime();

        } catch (ParseException e) {
            e.printStackTrace();
            //throw new IllegalAccessException("Error in parsing date");
        }

        StringBuilder builder = new StringBuilder();
        long dTime = dt /(60 * 60 * 1000);
        if(dTime<=24) {
            builder.append(String.valueOf( dTime));
            builder.append(context.getString(R.string.hours_short));
        }else {
            if (dTime <= 30 * 24) {
                builder.append(String.valueOf(dTime / 24));
                builder.append(context.getString(R.string.days_short));
            } else {
                builder.append(String.valueOf(dTime / (24 *30)));
                builder.append(context.getString(R.string.month_short));
            }
        }
        holder.postDateText.setText(builder.toString());
        holder.favoritImage.setSelected(false);
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
        private TextView mediaCountText;
        private TextView ratingCountText;
        private TextView postDateText;
        private TextView commentsCountText;
        private TextView shareCountText ;
        private ImageView favoritImage ;
        private ViewGroup headerLayout;

        //private GestureDetector mGestureDetector;

        public PostsViewHolder(View v) {
            super(v);
            context = v.getContext();
            headerLayout = (ViewGroup) v.findViewById(R.id.header);
            postText = (TextView) v.findViewById(R.id.postText);
            mediaCountText = (TextView) v.findViewById(R.id.photo_count);
            ratingCountText = (TextView) v.findViewById(R.id.ratingCount);
            postDateText = (TextView) v.findViewById(R.id.post_date);
            commentsCountText = (TextView) v.findViewById(R.id.post_comments_count);
            shareCountText = (TextView) v.findViewById(R.id.post_share_count);
            favoritImage = (ImageView) v.findViewById(R.id.post_favorite);


            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            PostActivity.show(context, posts.get(getAdapterPosition()).getPost_id());
        }
    }

}

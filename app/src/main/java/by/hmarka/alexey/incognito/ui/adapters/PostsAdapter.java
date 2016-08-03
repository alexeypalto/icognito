package by.hmarka.alexey.incognito.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import by.hmarka.alexey.incognito.IncognitoApplication;
import by.hmarka.alexey.incognito.R;
import by.hmarka.alexey.incognito.entities.Post;
import by.hmarka.alexey.incognito.events.ShowCommentsInFavoriteFragment;
import by.hmarka.alexey.incognito.rest.RestClient;
import by.hmarka.alexey.incognito.ui.activities.PostActivity;
import by.hmarka.alexey.incognito.utils.Helpers;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by lashket on 4.7.16.
 */
public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.PostsViewHolder> {

    private ArrayList<Post> posts;
    private Context context;
    private Helpers helpers = new Helpers();

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
    public void onBindViewHolder(final PostsViewHolder holder, final int position) {
        final Post post;
        post = posts.get(position);
        String timesatmap = helpers.getDate(post.getPost_timestamp());
        holder.postDate.setText(timesatmap);
        holder.postText.setText(post.getPost_text());
        holder.likeCount.setText(post.getLike_count());
        holder.shareCount.setText(post.getLike_count());
        holder.commentsCount.setText(post.getComment_count());
        holder.shares.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, post.getPost_text());
                sendIntent.setType("text/plain");
                context.startActivity(sendIntent);
                Call<ResponseBody> call = RestClient.getServiceInstance().addShareToPost(helpers.getShareRequest(post.getPost_id(), post.getPost_text()));
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            int count = Integer.parseInt(holder.shareCount.getText().toString());
                            count = count + 1;
                            holder.shareCount.setText(String.valueOf(count));
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
            }
        });
        if (post.getIsFavorite().equals("1")) {
            holder.addToFavorites.setImageResource(R.drawable.favorit_active);
        }
        holder.addToFavorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (post.getIsFavorite().equals("1")) {
                    holder.addToFavorites.setImageResource(R.drawable.favorit);
                    posts.get(position).setIsFavorite("0");
                    Call<ResponseBody> call = RestClient.getServiceInstance().addPostToFavorite(helpers.getRemovePostToFavoriteRequest(posts.get(position).getPost_id()));
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (!response.isSuccessful()) {
                                holder.addToFavorites.setImageResource(R.drawable.favorit_active);
                                posts.get(position).setIsFavorite("1");
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {

                        }
                    });
                    return;
                }
                if (post.getIsFavorite().equals("0")) {
                    holder.addToFavorites.setImageResource(R.drawable.favorit_active);
                    posts.get(position).setIsFavorite("1");
                    Call<ResponseBody> call = RestClient.getServiceInstance().addPostToFavorite(helpers.getAddPostToFavoriteRequest(posts.get(position).getPost_id()));
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (!response.isSuccessful()) {
                                holder.addToFavorites.setImageResource(R.drawable.favorit);
                                posts.get(position).setIsFavorite("0");
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {

                        }
                    });
                }
            }
        });
        holder.comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               IncognitoApplication.bus.post(new ShowCommentsInFavoriteFragment(post.getPost_id()));
            }
        });
    }

    private void addLike(final TextView textView, final boolean isLike, String postId) {
        Call<ResponseBody> call = RestClient.getServiceInstance().addLike(helpers.getAddLikeRequest(isLike, postId));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    if (isLike) {
                        textView.setText(String.valueOf(Integer.parseInt(textView.getText().toString()) + 1));
                    } else {
                        textView.setText(String.valueOf(Integer.parseInt(textView.getText().toString()) - 1));
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
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
        private TextView commentsCount;
        private TextView shareCount;
        private TextView likeCount;
        private ImageView addToFavorites;
        private LinearLayout comments;
        private LinearLayout shares;
        private TextView postDate;

        public PostsViewHolder(View v) {
            super(v);
            context = v.getContext();
            shares = (LinearLayout) v.findViewById(R.id.post_share);
            comments = (LinearLayout) v.findViewById(R.id.comments_post_layout);
            postText = (TextView) v.findViewById(R.id.postText);
            commentsCount = (TextView) v.findViewById(R.id.post_comments_count);
            shareCount  = (TextView) v.findViewById(R.id.post_share_count);
            likeCount  = (TextView) v.findViewById(R.id.ratingCount);
            addToFavorites = (ImageView) v.findViewById(R.id.post_favorite);
            postDate = (TextView) v.findViewById(R.id.post_date);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            PostActivity.show(context, posts.get(getAdapterPosition()).getPost_id());
        }
    }

}

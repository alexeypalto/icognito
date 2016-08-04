package by.hmarka.alexey.incognito.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import by.hmarka.alexey.incognito.R;
import by.hmarka.alexey.incognito.entities.Comment;
import by.hmarka.alexey.incognito.utils.Helpers;

/**
 * Created by lashket on 27.7.16.
 */
public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentsViewHolder>{

    private Context context;
    private ArrayList<Comment> comments = new ArrayList<>();
    private Helpers helpers = new Helpers();

    public CommentsAdapter(Context context, ArrayList<Comment> comments) {
        this.context = context;
        this.comments = comments;
    }

    @Override
    public void onBindViewHolder(CommentsViewHolder holder, int position) {
        Comment comment = comments.get(position);
        holder.commentText.setText(comment.getComment_text());
        holder.date.setText(helpers.getDate(comment.getComment_timestamp()));
    }

    @Override
    public CommentsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.item_comment, parent, false);
        return new CommentsViewHolder(itemView);
    }

    public void add(Comment comment) {
        if (comment == null) {
            comments = new ArrayList<>();
        }
        comments.add(comment);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (comments == null) {
            return 0;
        }
        return comments.size();
    }

    public class CommentsViewHolder extends RecyclerView.ViewHolder{

        protected TextView commentText;
        protected TextView date;

        public CommentsViewHolder(View v) {
            super(v);
            commentText = (TextView) v.findViewById(R.id.text);
            date = (TextView) v.findViewById(R.id.date);
        }

    }

}

package by.hmarka.alexey.incognito.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import by.hmarka.alexey.incognito.R;
import by.hmarka.alexey.incognito.entities.Post;
import by.hmarka.alexey.incognito.ui.adapters.PostsAdapter;

/**
 * Created by lashket on 4.7.16.
 */
public class PopularPostsFragment extends Fragment {

    private RecyclerView recyclerView;
    private ArrayList<Post> posts;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_popular, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
      //  recyclerView.setAdapter(new PostsAdapter());
        if (this.posts != null) {
            addList(posts);
        }
        return view;
    }

    public void addList(ArrayList<Post> posts) {
        if (recyclerView == null) {
            this.posts = posts;
            return;
        }
        recyclerView.setAdapter(new PostsAdapter(posts, getContext()));
    }

    @Override
    public boolean getUserVisibleHint() {
        if (isVisible() && recyclerView != null && posts != null) {
            recyclerView.setAdapter(new PostsAdapter(posts, getContext()));
        }
        return super.getUserVisibleHint();
    }
}

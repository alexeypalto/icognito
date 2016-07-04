package by.hmarka.alexey.incognito.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import by.hmarka.alexey.incognito.R;

/**
 * Created by lashket on 4.7.16.
 */
public class ThemesListAdapter extends RecyclerView.Adapter<ThemesListAdapter.ThemesListViewHolder> {

    @Override
    public ThemesListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.item_theme, parent, false);
        return new ThemesListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ThemesListViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public class ThemesListViewHolder extends RecyclerView.ViewHolder {

        public ThemesListViewHolder(View v) {
            super(v);
        }

    }

}

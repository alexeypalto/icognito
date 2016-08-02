package by.hmarka.alexey.incognito.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import by.hmarka.alexey.incognito.R;
import by.hmarka.alexey.incognito.entities.Properties;

/**
 * Created by Alexey on 01.08.2016.
 */
public class PropertiesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int FIRST_TYPE = 0, SECOND_TYPE = 1;
    private List<Properties> propertiesList;

    public PropertiesAdapter(List<Properties> propertiesList) {
        this.propertiesList = propertiesList;
    }


    class ViewHolder1 extends RecyclerView.ViewHolder {
        public SwitchCompat switchCompat;

        public ViewHolder1(View view) {
            super(view);
            switchCompat = (SwitchCompat)view.findViewById(R.id.switchCompat);
        }
    }

    class ViewHolder2 extends RecyclerView.ViewHolder {
        public TextView simbols_text, simbols_count;

        public ViewHolder2(View view) {
            super(view);
            simbols_text = (TextView) view.findViewById(R.id.simbols_text);
            simbols_count = (TextView) view.findViewById(R.id.simbols_count);
        }
    }

    @Override
    public int getItemCount() {
        return propertiesList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (propertiesList.get(position) == propertiesList.get(0)) {
            return FIRST_TYPE;
        } else if (propertiesList.get(position) == propertiesList.get(1)| propertiesList.get(position) == propertiesList.get(2)) {
            return SECOND_TYPE;
        }
        return -1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder holder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case FIRST_TYPE:
                View view1 = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_settings_first, parent, false);
                holder = new ViewHolder1(view1);
                break;

            case SECOND_TYPE:
                View view2 = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_settings_second, parent, false);
                holder = new ViewHolder2(view2);
                break;

        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        switch (viewHolder.getItemViewType()) {
            case FIRST_TYPE:
                ViewHolder1 vh1 = (ViewHolder1) viewHolder;
                Properties propertie = propertiesList.get(position);
                vh1.switchCompat.setText(propertie.getSwitchCompat());
                break;
            case SECOND_TYPE:
                ViewHolder2 vh2 = (ViewHolder2) viewHolder;
                Properties propertie2 = propertiesList.get(position);
                vh2.simbols_count.setText(propertie2.getSimbols_count());
                vh2.simbols_text.setText(propertie2.getSimbols_text());
                break;
            }
    }
}

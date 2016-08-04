package by.hmarka.alexey.incognito.ui.adapters;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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


    static class ViewHolder1 extends RecyclerView.ViewHolder implements View.OnClickListener {
        public SwitchCompat switchCompat;
        public ClickListener clickListener;

        public ViewHolder1(View view, ClickListener listener) {
            super(view);
            clickListener = listener;
            switchCompat = (SwitchCompat)view.findViewById(R.id.switchCompat);
            switchCompat.setOnClickListener(this);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }

        public static interface ClickListener {
            public void onSwitch(View caller);
        }
    }

    static class ViewHolder2 extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView simbols_text, simbols_count;
        public ClickListener clickListener;

        public ViewHolder2(View view, ClickListener listener) {
            super(view);
            clickListener = listener;
            simbols_text = (TextView) view.findViewById(R.id.simbols_text);
            simbols_count = (TextView) view.findViewById(R.id.simbols_count);
            simbols_count.setOnClickListener(this);
            view.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {

        }

        public static interface ClickListener {
            public void onCountChange(View caller);
        }
    }

    @Override
    public int getItemCount() {
        return propertiesList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return propertiesList.get(position).type;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder holder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case FIRST_TYPE:
                View view1 = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_settings_first, parent, false);
                holder = new ViewHolder1(view1, new ViewHolder1.ClickListener() {
                    @Override
                    public void onSwitch(View caller) {

                    }
                });
                break;

            case SECOND_TYPE:
                View view2 = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_settings_second, parent, false);

                holder = new ViewHolder2(view2, new ViewHolder2.ClickListener() {
                    @Override
                    public void onCountChange(View caller) {
                        AlertDialog.Builder alert = new AlertDialog.Builder(parent.getContext());
                        final EditText edittext = new EditText(parent.getContext());
                        alert.setMessage("Enter Your Count");
                        alert.setTitle("Count of symbols");

                        alert.setView(edittext);

                        alert.setPositiveButton("Set count", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                              String symbolsCount = edittext.getText().toString();
                                // blyaaaaad'

                            }
                        });

                        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                            }
                        });

                        alert.show();
                    }
                });
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

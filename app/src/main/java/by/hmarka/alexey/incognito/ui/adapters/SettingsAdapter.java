package by.hmarka.alexey.incognito.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import by.hmarka.alexey.incognito.R;
import by.hmarka.alexey.incognito.entities.Setting;

/**
 * Created by vitalyorlov on 06.07.16.
 */
public class SettingsAdapter extends BaseAdapter {

    Context ctx;
    LayoutInflater lInflater;
    ArrayList<Setting> objects;

    public SettingsAdapter(Context context, ArrayList<Setting> settings) {
        ctx = context;
        objects = settings;
        lInflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Object getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return objects.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        final Setting setting = getSetting(position);

        if (view == null) {
            if (!setting.getName().equals("")) {
                view = lInflater.inflate(R.layout.fragment_setting, parent, false);
                view.setTag("1");
            } else {
                view = lInflater.inflate(R.layout.fragment_setting_indent, parent, false);
                view.setTag("2");
            }
        }
        else{
            if(view.getTag().equals("2")&&!setting.getName().equals("")){
                view = lInflater.inflate(R.layout.fragment_setting, parent, false);
                view.setTag("1");
            }
            if(view.getTag().equals("1")&&setting.getName().equals("")){
                view = lInflater.inflate(R.layout.fragment_setting_indent, parent, false);
                view.setTag("2");
            }
        }

        if (!setting.getName().equals(""))
        {
            try {
                ((TextView) view.findViewById(R.id.setting_name)).setText(setting.getName());
                ((ImageView) view.findViewById(R.id.setting_icon)).setImageResource(setting.getImage());
            } catch (NullPointerException e) {

            }
        }

            return view;
    }

    Setting getSetting(int position) {
        return ((Setting) getItem(position));
    }

}

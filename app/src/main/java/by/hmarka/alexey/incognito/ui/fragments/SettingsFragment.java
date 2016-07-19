package by.hmarka.alexey.incognito.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import by.hmarka.alexey.incognito.R;
import by.hmarka.alexey.incognito.entities.Setting;
import by.hmarka.alexey.incognito.ui.activities.NotifyActivity;
import by.hmarka.alexey.incognito.ui.adapters.SettingsAdapter;

/**
 * Created by lashket on 28.6.16.
 */
public class SettingsFragment extends Fragment {

    private ArrayList<Setting> settingsList = new ArrayList<Setting>();

    private SettingsAdapter settingsAdapter;
    private Toolbar toolbar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        if (((AppCompatActivity) getActivity()).getSupportActionBar() != null) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Настройки");
        }

        fillSettingsList();

        settingsAdapter = new SettingsAdapter(this.getContext(), settingsList);
        ListView lvMain = (ListView) view.findViewById(R.id.settingsList);
        lvMain.setAdapter(settingsAdapter);

        lvMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Setting item = (Setting)settingsAdapter.getItem(position);
                switch (position) {
                    case 0:
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                    case 4:
                        break;
                    case 7:
                        break;
                }
            }
        });

        return view;


    }

    private void fillSettingsList() {
        settingsList.add(new Setting(R.drawable.settings_share, "Расскажи друзьям о приложении"));

        settingsList.add(new Setting(0, ""));

        settingsList.add(new Setting(R.drawable.settings_notifications, "Уведомления", NotifyActivity.class));
        settingsList.add(new Setting(R.drawable.settings_shop, "Магазин"));
        settingsList.add(new Setting(R.drawable.settings_nastroyki, "Настройки"));

        settingsList.add(new Setting(0, ""));

        settingsList.add(new Setting(R.drawable.settings_guide, "Правила"));
        settingsList.add(new Setting(R.drawable.settings_confidenc, "Конфиденциальность"));
        settingsList.add(new Setting(R.drawable.settings_help, "Помощь"));

        settingsList.add(new Setting(0, ""));

        settingsList.add(new Setting(R.drawable.settings_about, "О нас"));
    }

    private void findViews() {


    }



}
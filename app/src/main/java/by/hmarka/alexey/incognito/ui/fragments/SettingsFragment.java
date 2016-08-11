package by.hmarka.alexey.incognito.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
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
import by.hmarka.alexey.incognito.ui.activities.AboutActivity;
import by.hmarka.alexey.incognito.ui.activities.HelpActivity;
import by.hmarka.alexey.incognito.ui.activities.NotifyActivity;
import by.hmarka.alexey.incognito.ui.activities.PropActivity;
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

                switch ((int)id) {

//                    case 4:
//                        Setting item4 = (Setting)settingsAdapter.getItem(position);
//                        Intent t4 =  new Intent(getContext(), PropertiesActivity.class);
//                        startActivity(t4);
//                        break;
                    case 0:
                        Intent sendIntent = new Intent();
                        sendIntent.setAction(Intent.ACTION_SEND);
                        sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
                        sendIntent.setType("text/plain");
                        startActivity(Intent.createChooser(sendIntent, "Отправить в..."));
                        break;
                    case 1:
                        Setting item = (Setting)settingsAdapter.getItem(position);
                        Intent t =  new Intent(getContext(),NotifyActivity.class);
                        startActivity(t);
                        break;
                    case 2:
                        AlertDialog.Builder shop = new AlertDialog.Builder(getActivity());
                        shop.setView(R.layout.fragment_shop);
                        shop.show();
                        break;
                    case 3:
                        Setting properties = (Setting)settingsAdapter.getItem(position);
                        Intent prop =  new Intent(getContext(), PropActivity.class);
                        startActivity(prop);
                        break;
                    case 4:
                        AlertDialog.Builder rules = new AlertDialog.Builder(getActivity());
                        rules.setView(R.layout.fragment_rules);
                        rules.show();
                        break;
                    case 5:
                        AlertDialog.Builder conf = new AlertDialog.Builder(getActivity());
                        conf.setView(R.layout.fragment_rules);
                        conf.show();
                        break;
                    case 6:
                        Setting item3 = (Setting)settingsAdapter.getItem(position);
                        Intent t3 =  new Intent(getContext(),HelpActivity.class);
                        startActivity(t3);
                        break;
                    case 7:
                        Setting item2 = (Setting)settingsAdapter.getItem(position);
                        Intent t2 =  new Intent(getContext(),AboutActivity.class);
                        startActivity(t2);
                        break;
                }

            }
        });

        return view;


    }

    private void fillSettingsList() {
        settingsList.add(new Setting(0, R.drawable.settings_share, "Расскажи друзьям о приложении"));

        settingsList.add(new Setting(-1, 0, ""));

        //settingsList.add(new Setting(1, R.drawable.settings_notifications, "Уведомления"));
        settingsList.add(new Setting(2, R.drawable.settings_shop, "Магазин"));
        settingsList.add(new Setting(3, R.drawable.settings_nastroyki, "Настройки"));

        settingsList.add(new Setting(-1, 0, ""));

        settingsList.add(new Setting(4, R.drawable.settings_guide, "Правила"));
        settingsList.add(new Setting(5, R.drawable.settings_confidenc, "Конфиденциальность"));
        settingsList.add(new Setting(6, R.drawable.settings_help, "Помощь"));

        settingsList.add(new Setting(-1, 0, ""));

        settingsList.add(new Setting(7, R.drawable.settings_about, "О нас"));
        settingsList.add(new Setting(-1, 0, ""));
    }
}
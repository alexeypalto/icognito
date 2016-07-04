package by.hmarka.alexey.incognito.ui.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import by.hmarka.alexey.incognito.R;
import by.hmarka.alexey.incognito.ui.adapters.CustomPagerAdapter;
import by.hmarka.alexey.incognito.ui.fragments.AddFragment;
import by.hmarka.alexey.incognito.ui.fragments.FavoritesFragment;
import by.hmarka.alexey.incognito.ui.fragments.HomeFragment;
import by.hmarka.alexey.incognito.ui.fragments.NewsFragment;
import by.hmarka.alexey.incognito.ui.fragments.SettingsFragment;

/**
 * Created by Alexey on 22.06.2016.
 */
public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private CustomPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
    }

    private void findViews() {
        tabLayout = (TabLayout) findViewById(R.id.mainTabLayout);
        viewPager = (ViewPager) findViewById(R.id.mainViewPager);
        setupViewPager();
    }

    private void setupViewPager() {
        adapter = new CustomPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new HomeFragment(), "home");
        adapter.addFragment(new NewsFragment(), "news");
        adapter.addFragment(new AddFragment(), "add");
        adapter.addFragment(new FavoritesFragment(), "favorite");
        adapter.addFragment(new SettingsFragment(), "settings");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setOffscreenPageLimit(5);
        setupTabLayout();
    }

    private void setupTabLayout() {
        tabLayout.getTabAt(0).setIcon(R.drawable.selector_tab_home);
        tabLayout.getTabAt(1).setIcon(R.drawable.selector_tab_categories);
        tabLayout.getTabAt(2).setCustomView(R.layout.tab_add);
        tabLayout.getTabAt(3).setIcon(R.drawable.selector_tab_favorite);
        tabLayout.getTabAt(4).setIcon(R.drawable.selector_tab_settings);
//        tabLayout.getTabAt(0).getCustomView().setSelected(true);
    }

}

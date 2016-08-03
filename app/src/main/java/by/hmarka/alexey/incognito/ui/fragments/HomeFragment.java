package by.hmarka.alexey.incognito.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import by.hmarka.alexey.incognito.R;
import by.hmarka.alexey.incognito.ui.adapters.HomeFragmentPagerAdapter;
import by.hmarka.alexey.incognito.utils.Helpers;

/**
 * Created by lashket on 28.6.16.
 */
public class HomeFragment extends Fragment {

    public  interface ChildFragment{
        void setToFirstPosition();
    }
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private HomeFragmentPagerAdapter adapter;
    private NewPostsFragment postsFragment = new NewPostsFragment();
    private PopularPostsFragment popularPostsFragment = new PopularPostsFragment();
    private Helpers helpers = new Helpers();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        findViews(view);
        return view;
    }

    private void findViews(View v) {
        tabLayout = (TabLayout) v.findViewById(R.id.tabs);
        viewPager = (ViewPager) v.findViewById(R.id.viewpager);
        adapter = new HomeFragmentPagerAdapter(getActivity().getSupportFragmentManager());
        adapter.addFragment(postsFragment, "Новое");
        adapter.addFragment(popularPostsFragment, "Популярное");
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(2);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                ChildFragment childFragment = (ChildFragment)adapter.getItem(tab.getPosition());
                childFragment.setToFirstPosition();
            }
        });
        tabLayout.getTabAt(0).setIcon(R.drawable.novoe_active);
        tabLayout.getTabAt(1).setIcon(R.drawable.popular_active);
        //getNewPostsList();
        //getPopularPostsList();
    }
}

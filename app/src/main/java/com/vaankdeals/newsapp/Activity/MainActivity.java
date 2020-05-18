package com.vaankdeals.newsapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;
import cn.jzvd.JzvdStd;

import android.os.Bundle;
import com.vaankdeals.newsapp.Adapter.NewsAdapter;
import com.vaankdeals.newsapp.Fragment.MenuFragment;
import com.vaankdeals.newsapp.Fragment.NewsFragment;
import com.vaankdeals.newsapp.R;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    ViewPager viewPager;
    MenuFragment menuFragment;
    NewsFragment newsFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = findViewById(R.id.viewTab);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        menuFragment = new MenuFragment();
        newsFragment = new NewsFragment();
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), 0);
        viewPagerAdapter.addFragment(menuFragment,"Discover");
        viewPagerAdapter.addFragment(newsFragment,"News");
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setCurrentItem(1);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                ViewPager2 newsViewpager = findViewById(R.id.news_swipe);
                JzvdStd.releaseAllVideos();
                ((NewsAdapter) Objects.requireNonNull(newsViewpager.getAdapter())).pauseYtVid();

            }

            @Override
            public void onPageSelected(int position) {
                JzvdStd.releaseAllVideos();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }


    public void swipeoptions(){
        viewPager.setCurrentItem(0,true);
    }
    public void swipeoptionstwo(){
        viewPager.setCurrentItem(1,true);
    }
    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private List<Fragment> fragments = new ArrayList<>();
        private List<String> fragmentsTitle = new ArrayList<>();
        private ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        private void addFragment(Fragment fragment,String title){
            fragments.add(fragment);
            fragmentsTitle.add(title);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }


        public CharSequence getPageTitle(int position) {
            return fragmentsTitle.get(position);
        }
        @Override
        public int getCount() {
            return fragments.size();
        }
    }
}

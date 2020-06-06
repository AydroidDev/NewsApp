package com.vaankdeals.newsapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.tabs.TabLayout;
import com.vaankdeals.newsapp.Fragment.MenuFragment;
import com.vaankdeals.newsapp.Fragment.NewsFragment;
import com.vaankdeals.newsapp.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity{

    public  static ViewPager viewPagerMain;
    MenuFragment menuFragment;
    NewsFragment newsFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPagerMain = findViewById(R.id.viewTab);

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        menuFragment = new MenuFragment();
        newsFragment = new NewsFragment();
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), 0);
        viewPagerAdapter.addFragment(menuFragment,"Discover");
        viewPagerAdapter.addFragment(newsFragment,"Feed");
        viewPagerMain.setAdapter(viewPagerAdapter);
        viewPagerMain.setCurrentItem(1);
        tabLayout.setupWithViewPager(viewPagerMain);
        findViewById(R.id.mainAppbar).bringToFront();
        viewPagerMain.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
    }
    private void hideSystemUI(){
        View decorView =getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        );
    }

    public void swipeoptions(){
        viewPagerMain.setCurrentItem(0,true);
    }
    public void swipeoptionstwo(){
        viewPagerMain.setCurrentItem(1,true);
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

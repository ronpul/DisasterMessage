package com.disaster.message;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import utils.BackClickEventHandler;

public class MainActivity extends AppCompatActivity {
    private ViewPager2 viewPager;
    private PagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewpager);
        pagerAdapter = new PagerAdapter(this);
        viewPager.setAdapter(pagerAdapter);

        addTabItem(tabLayout, viewPager,"HOME");
        addTabItem(tabLayout, viewPager,"SERVICE");
        addTabItem(tabLayout, viewPager,"NEWS");
        addTabItem(tabLayout, viewPager,"SETTING");

        pagerAdapter.notifyDataSetChanged();
    }

    private void addTabItem(TabLayout tabLayout, ViewPager2 viewPager, String tabText) {
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> tab.setText(tabText)).attach();
    }

    @Override
    public void onBackPressed() {
        BackClickEventHandler.onBackPressed(this);
    }
}
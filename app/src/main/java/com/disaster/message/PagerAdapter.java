package com.disaster.message;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.disaster.message.fragment.HomeFragment;
import com.disaster.message.fragment.NewsFragment;
import com.disaster.message.fragment.ServiceFragment;
import com.disaster.message.fragment.SettingFragment;

public class PagerAdapter extends FragmentStateAdapter {
    int mNumOfTabs = 4;
    public PagerAdapter(FragmentActivity fa) {
        super(fa);
    }

    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0: return new HomeFragment();
            case 1: return new ServiceFragment();
            case 2: return new NewsFragment();
            case 3: return new SettingFragment();
            default: return null;
        }
    }

    @Override
    public int getItemCount() {
        return mNumOfTabs;
    }
}

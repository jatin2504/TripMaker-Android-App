package com.example.tripmaker.adapters;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;


import com.example.tripmaker.fragments.AllTripsFragment;
import com.example.tripmaker.fragments.MyTripsFragment;

import java.util.ArrayList;
import java.util.List;

public class PageAdapter extends FragmentStatePagerAdapter {

    List<Fragment> fragmentList = new ArrayList<>();
    int numberOfTabs;

    public PageAdapter(@NonNull FragmentManager fm, int numberOfTabs) {
        super(fm, numberOfTabs);
        this.numberOfTabs = numberOfTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return numberOfTabs;
    }

    public void addFragment(Fragment f) {
        fragmentList.add(f);
    }

    public void refreshPages() {
        fragmentList.remove(0);
        fragmentList.remove(0);
        fragmentList.add(0, new MyTripsFragment());
        fragmentList.add(0, new AllTripsFragment());
    }
}

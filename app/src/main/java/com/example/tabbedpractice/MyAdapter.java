package com.example.tabbedpractice;

import android.content.Context;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.*;

public class MyAdapter extends FragmentPagerAdapter {

    Context context;
    int totalTabs;

    public MyAdapter(Context c, FragmentManager fm, int totalTabs){
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        context=c;
        this.totalTabs = totalTabs;
    }

    @Override
    public Fragment getItem(int position){
        switch(position) {
            case 0:
                return new all_envelopes();
            case 1:
                return new Tab2();
            case 2:
                return new Tab3();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return totalTabs;
    }
}
